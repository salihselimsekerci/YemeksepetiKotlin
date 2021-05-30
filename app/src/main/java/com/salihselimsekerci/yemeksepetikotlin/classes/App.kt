package com.salihselimsekerci.yemeksepetikotlin.classes

import android.app.Application
import android.content.Context

class App : Application() {
    companion object {
        var ctx: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        ctx = applicationContext
    }
}