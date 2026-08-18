package com.guru.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.guru.app.data.datastore.UserPreferencesRepository
import com.guru.app.ui.navigation.GuruNavGraph
import com.guru.app.ui.theme.GURUTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeMode by userPreferencesRepository.themeMode.collectAsState(initial = "AMOLED")
            val navController = rememberNavController()

            GURUTheme(themeMode = themeMode) {
                GuruNavGraph(navController = navController)
            }
        }
    }
}
