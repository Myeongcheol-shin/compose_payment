package com.shino72.wallet.db.pref

import android.content.Context
import android.content.SharedPreferences

class AlarmPreference (context: Context){
    private val prefsName = "Alarm"
    private val prefsStatus = "Status";
    private val prefs : SharedPreferences = context.getSharedPreferences(prefsName,0)
    var EditStatus : Boolean
        get() = prefs.getBoolean(prefsStatus, false)
        set(value) = prefs.edit().putBoolean(prefsStatus,value).apply()
}