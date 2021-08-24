package com.cherrio.okra.utils

import android.util.Log
import android.webkit.JavascriptInterface
import com.cherrio.okra.handlers.OkraHandler


internal class WebInterface(private val okraHandler: OkraHandler) {
    private val TAG = "WebInterface"

    @JavascriptInterface
    fun onSuccess(json: String) {
        Log.d(TAG, "onSuccess: $json")
        okraHandler.data = json
        formatJsonString(json)
        okraHandler.isSuccessful = true
        okraHandler.isDone = true
        okraHandler.hasError = false
    }

    @JavascriptInterface
    fun onError(json: String) {
        Log.d(TAG, "onError: $json")
        formatJsonString(json)
        okraHandler.data = json
        okraHandler.hasError = true
        okraHandler.isDone = true
        okraHandler.isSuccessful = false
    }

    @JavascriptInterface
    fun onClose(json: String?) {
    }

    private fun formatJsonString(json: String) {
        try {
            okraHandler.response = FormatJson.formatJson(json)
        } catch (exception: Exception) {

        }
    }
}