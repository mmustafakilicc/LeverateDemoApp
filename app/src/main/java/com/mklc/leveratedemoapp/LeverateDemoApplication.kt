package com.mklc.leveratedemoapp

import android.app.Application
import timber.log.Timber

class LeverateDemoApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }
}