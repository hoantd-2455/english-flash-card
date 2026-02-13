package com.englishflashcard

import android.app.Application
import com.englishflashcard.di.AppContainer

/**
 * Application class for dependency injection
 */
class FlashcardApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}

