package com.example.fantasyaudiobooks.ui.mediaplayer

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fantasyaudiobooks.data.model.Chapter
import com.example.fantasyaudiobooks.utils.SharedPreferencesUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.util.Timer
import java.util.TimerTask
import java.net.URL

class MediaPlayerViewModel(application: Application) : AndroidViewModel(application) {
    private var mediaPlayer: MediaPlayer? = null
    private val _isPlaying = MutableLiveData<Boolean>(false)
    val isPlaying: LiveData<Boolean> get() = _isPlaying

    private val _currentChapterIndex = MutableLiveData<Int>(0)
    val currentChapterIndex: LiveData<Int> get() = _currentChapterIndex

    private val _currentPosition = MutableLiveData<Int>(0)
    val currentPosition: LiveData<Int> get() = _currentPosition

    private val _duration = MutableLiveData<Int>(0)
    val duration: LiveData<Int> get() = _duration

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var timer: Timer? = null

    private val _playbackSpeed = MutableLiveData<Float>(1.0f)
    val playbackSpeed: LiveData<Float> get() = _playbackSpeed

    private val _bookProgressFraction = MutableLiveData<Float>(0f)
    val bookProgressFraction: LiveData<Float> get() = _bookProgressFraction

    private var totalChaptersCount = 1

    private var sleepTimer: CountDownTimer? = null

    private var chapters: List<Chapter> = emptyList()

    var currentChapterTitle: String = ""

    private var isProgressLoaded = false  // Flag to control saving
    private var bookTitle: String = ""

    private val sharedPreferencesUtils = SharedPreferencesUtils(application.applicationContext)

    init {
        _currentChapterIndex.observeForever { chapterIndex ->
            if (isProgressLoaded) {  // Only save if progress has been loaded
                if (chapterIndex >= 0 && chapterIndex < chapters.size) {
                    currentChapterTitle = chapters[chapterIndex].name
                    sharedPreferencesUtils.saveProgress(bookTitle, chapterIndex, _currentPosition.value ?: 0)
                }
            }
        }
        _currentPosition.observeForever { position ->
            if (isProgressLoaded) {  // Only save if progress has been loaded
                if (_currentChapterIndex.value != null) {
                    sharedPreferencesUtils.saveProgress(bookTitle, _currentChapterIndex.value!!, position)
                }
            }
        }
    }

    //fun loadProgress(bookTitle: String): Pair<Int, Int> { return sharedPreferencesUtils.loadProgress(bookTitle) }

    fun startSleepTimer(minutes: Int) {
        sleepTimer?.cancel()
        val millis = minutes * 60 * 1000L
        sleepTimer = object : CountDownTimer(millis, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                // Optional: Update LiveData for remaining time if you want to display it
            }

            override fun onFinish() {
                pause()
                // Optional: Notify the user
            }
        }.start()
    }

    fun cancelSleepTimer() {
        sleepTimer?.cancel()
        sleepTimer = null
    }

    init {
        _currentChapterIndex.observeForever { updateBookProgress() }
        _currentPosition.observeForever { updateBookProgress() }
        _duration.observeForever { updateBookProgress() }
    }

    private fun updateBookProgress() {
        val totalChapters = totalChaptersCount
        val chapterIndex = _currentChapterIndex.value ?: 0
        val currentPosition = _currentPosition.value ?: 0
        val duration = _duration.value ?: 1 // Avoid division by zero

        val chapterProgress = (currentPosition.toFloat() / duration).takeIf { !it.isNaN() } ?: 0f
        val bookProgress = ((chapterIndex + chapterProgress) / totalChapters).takeIf { !it.isNaN() } ?: 0f
        _bookProgressFraction.postValue(bookProgress)
    }

    fun setPlaybackSpeed(speed: Float) {
        mediaPlayer?.let { player ->
            player.playbackParams = player.playbackParams.setSpeed(speed)
            _playbackSpeed.postValue(speed)
        }
    }

    fun playChapter(bookTitle: String, chapters: List<Chapter>, index: Int, startPosition: Int = 0) {
        this.bookTitle = bookTitle  // Set the bookTitle here
        totalChaptersCount = chapters.size
        this.chapters = chapters
        val chapter = chapters.getOrNull(index) ?: return

        currentChapterTitle = chapter.name

        releaseMediaPlayer()
        _isLoading.postValue(true)

        // Download the audio file
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val file = downloadAudioFile(chapter)
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                    initializeMediaPlayer(file, chapters, index, startPosition)
                    isProgressLoaded = true  // Allow saving after loading is complete
                }
            } catch (e: Exception) {
                Log.e("MediaPlayerError", "Error downloading chapter: ${e.message}")
                e.printStackTrace()
                _isLoading.postValue(false)
                // Handle the error, e.g., show a Toast or update the UI
            }
        }
    }


    private fun initializeMediaPlayer(
        file: File,
        chapters: List<Chapter>,
        index: Int,
        startPosition: Int
    ) {
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(file.absolutePath)
                prepareAsync()
                setOnPreparedListener {
                    it.seekTo(startPosition)
                    it.start()
                    _duration.postValue(it.duration)
                    _isPlaying.postValue(true)
                    startTimer()
                    playbackSpeed.value?.let { speed ->
                        it.playbackParams = it.playbackParams.setSpeed(speed)
                    }
                }

                setOnCompletionListener {
                    _isPlaying.postValue(false)
                    stopTimer()
                    // Automatically play the next chapter if available
                    if (index + 1 < chapters.size) {
                        playChapter(bookTitle, chapters, index + 1)
                    } else {
                        // End of book
                    }
                }
            } catch (e: Exception) {
                Log.e("MediaPlayerError", "Error initializing MediaPlayer: ${e.message}")
                e.printStackTrace()
            }
        }
        _currentChapterIndex.postValue(index)
        _currentPosition.postValue(startPosition)
    }

    private suspend fun downloadAudioFile(chapter: Chapter): File {
        val context = getApplication<Application>().applicationContext
        val fileName = "chapter_${chapter.id}.mp3"
        val file = File(context.cacheDir, fileName)

        if (file.exists()) {
            return file
        }

        val url = chapter.url

        // Using HttpURLConnection
        withContext(Dispatchers.IO) {
            try {
                val connection = URL(url).openConnection()
                connection.connect()

                connection.getInputStream().use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            } catch (e: Exception) {
                throw IOException("Failed to download file: ${e.message}")
            }
        }

        return file
    }

    fun play() {
        mediaPlayer?.start()
        _isPlaying.postValue(true)
        startTimer()
    }

    fun pause() {
        mediaPlayer?.pause()
        _isPlaying.postValue(false)
        stopTimer()
    }


    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
        _currentPosition.postValue(position)
    }

    private fun startTimer() {
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                mediaPlayer?.let {
                    _currentPosition.postValue(it.currentPosition)
                }
            }
        }, 0, 1000)
    }

    private fun stopTimer() {
        timer?.cancel()
        timer = null
    }

    override fun onCleared() {
        super.onCleared()
        releaseMediaPlayer()
        cleanUpCache()
        cancelSleepTimer()
    }

    private fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
        stopTimer()
        _isPlaying.postValue(false)
        _currentPosition.postValue(0)
    }

    fun skipForward(seconds: Int = 10) {
        mediaPlayer?.let {
            val newPosition = (it.currentPosition + seconds * 1000).coerceAtMost(it.duration)
            it.seekTo(newPosition)
            _currentPosition.postValue(newPosition)
        }
    }

    fun skipBackward(seconds: Int = 10) {
        mediaPlayer?.let {
            val newPosition = (it.currentPosition - seconds * 1000).coerceAtLeast(0)
            it.seekTo(newPosition)
            _currentPosition.postValue(newPosition)
        }
    }

    private fun cleanUpCache() {
        val cacheDir = getApplication<Application>().cacheDir
        val files = cacheDir.listFiles()
        files?.forEach { file ->
            if (file.isFile && file.name.startsWith("chapter_")) {
                file.delete()
            }
        }
    }
}