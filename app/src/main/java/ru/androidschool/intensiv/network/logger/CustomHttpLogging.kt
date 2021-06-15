package ru.androidschool.intensiv.network.logger

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

class CustomHttpLogging : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        if (!message.startsWith("{")) {
            Timber.d(message)
            return
        }
        try {
            val prettyPrintJson = GsonBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(JsonParser().parse(message))
            Timber.d(prettyPrintJson)
        } catch (m: JsonSyntaxException) {
            Timber.d(message)
        }
    }
}
