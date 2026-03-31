package com.example.activityapp.util

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

fun createImagePartFromUri(
    context: Context,
    uri: Uri
): MultipartBody.Part {
    val inputStream = context.contentResolver.openInputStream(uri)
        ?: throw IllegalArgumentException("Cannot open image input stream")

    val bytes = inputStream.readBytes()

    val requestBody = bytes.toRequestBody("image/*".toMediaTypeOrNull())

    return MultipartBody.Part.createFormData(
        name = "file",
        filename = "profile_image.jpg",
        body = requestBody
    )
}