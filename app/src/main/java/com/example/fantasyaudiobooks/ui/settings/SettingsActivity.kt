package com.example.fantasyaudiobooks.ui.settings

import android.os.Bundle
import com.example.fantasyaudiobooks.ui.common.BaseActivity

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBaseContent { paddingValues ->
            SettingScreen(paddingValues = paddingValues)
        }
    }
}