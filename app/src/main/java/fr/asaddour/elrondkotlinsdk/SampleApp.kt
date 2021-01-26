package fr.asaddour.elrondkotlinsdk

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private lateinit var instance: SampleApp
        val applicationContext: Context by lazy { instance.applicationContext }
    }

}