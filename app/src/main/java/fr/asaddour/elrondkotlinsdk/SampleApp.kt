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
        // configure the OkHttpClient
        ErdSdk.elrondHttpClientBuilder.apply {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
    }

}