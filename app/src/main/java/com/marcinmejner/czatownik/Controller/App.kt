package com.marcinmejner.czatownik.Controller

import android.app.Application
import com.marcinmejner.czatownik.Utils.SharedPrefs

class App : Application() {

    companion object {
        lateinit var prefs : SharedPrefs
    }

    override fun onCreate() {
        prefs = SharedPrefs(applicationContext)
        super.onCreate()
    }
}