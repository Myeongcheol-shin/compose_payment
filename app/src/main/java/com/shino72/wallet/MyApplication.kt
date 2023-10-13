package com.shino72.wallet

import android.app.Application
import com.shino72.wallet.db.pref.AlarmPreference
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    companion object {
        lateinit var prefs : AlarmPreference
    }
    override fun onCreate() {
        prefs = AlarmPreference(applicationContext)
        super.onCreate()
    }
}