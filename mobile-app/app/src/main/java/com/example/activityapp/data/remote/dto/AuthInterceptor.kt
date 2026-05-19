package com.example.activityapp.data.remote

import android.content.Context
import android.content.Intent
import com.example.activityapp.MainActivity
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val prefs = context.getSharedPreferences("app", Context.MODE_PRIVATE)

        val token = prefs.getString("token", null)

        val request = chain.request().newBuilder()

        if (token != null) {
            request.addHeader("Authorization", "Bearer $token")
        }

        val response = chain.proceed(request.build())

        if (response.code == 401) {

            prefs.edit().remove("token").apply()

            val intent = Intent(context, MainActivity::class.java)

            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK

            context.startActivity(intent)
        }

        return response
    }
}