package fr.asaddour.elrondkotlinsdk

import android.app.Application
import android.content.Context
import com.elrond.erdkotlin.ErdSdk
import dagger.hilt.android.HiltAndroidApp
import okhttp3.logging.HttpLoggingInterceptor

@HiltAndroidApp
class SampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        // configure the OkHttpClient
        ErdSdk.elrondHttpClientBuilder.apply {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
    }

    companion object {
        private lateinit var instance: SampleApp
        val applicationContext: Context by lazy { instance.applicationContext }
    }

}