package com.example.virginmoney

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    companion object {
        lateinit var instance: App
        lateinit var analytics: FirebaseAnalytics
        lateinit var auth: FirebaseAuth
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        analytics = Firebase.analytics
        auth = FirebaseAuth.getInstance()

        analytics.logEvent(
            FirebaseAnalytics.Event.APP_OPEN,
            bundleOf()
        )

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}