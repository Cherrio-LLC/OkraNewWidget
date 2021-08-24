package com.cherrio.okra

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import com.cherrio.okra.databinding.OkraWidgetBinding
import com.cherrio.okra.handlers.OkraHandler
import com.cherrio.okra.utils.WebInterface
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.json.JSONObject


class OkraWidget(private val action: (OkraHandler) -> Unit) : BottomSheetDialogFragment() {

    private var _binding: OkraWidgetBinding? = null
    private val binding get() = requireNotNull(_binding)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = OkraWidgetBinding.inflate(inflater, container, false)
        return requireNotNull(_binding).root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomsheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val bottomSheetBehavior = bottomsheet.behavior
        bottomsheet.setOnShowListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        return bottomsheet
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val okraWeb = binding.okraWebview
        val webSettings = okraWeb.settings
        val okraHandler = OkraHandler()
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        okraWeb.addJavascriptInterface(WebInterface(okraHandler), "Android")
        okraWeb.loadUrl("https://v2-mobile.okra.ng/");

        okraWeb.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                val uri = Uri.parse(url)
                val linkData = parseLinkUriData(uri)
                val shouldClose = linkData["shouldClose"].toBoolean()
                if (shouldClose) {
                    action.invoke(okraHandler)
                    dismissAllowingStateLoss()
                } else {
                    return false
                }
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                val convertedJson = JSONObject(createOkraOptions()).toString().replace("'", "\\'")
                println("ConvertedJson: $convertedJson")
                binding.okraLoading.isVisible = false
                val toload = """openOkraWidget('$convertedJson');"""
                okraWeb.evaluateJavascript(toload, null)
            }
        }
    }


    private fun createOkraOptions(): MutableMap<*, *> {
        val products = arrayListOf("auth", "balance", "identity", "income")

        val deviceInfo = mapOf(
            "deviceName" to Build.BRAND,
            "deviceModel" to Build.MODEL,
            "longitude" to 0.0,
            "latitude" to 0.0,
            "platform" to "android"
        )
        return mutableMapOf(
            "products" to products,
            "key" to "40e47c77-5480-5b50-b866-90bcf41323d5",
            "token" to "611e6fbbd9b42445bc5dd210",
            "env" to "production-sandbox",
            "clientName" to "CashPally",
            "limit" to "24",
            "connectMessage" to "Select your Salary Account",
            "redirect_url" to "",
            "widget_success" to "Your account was successfully linked to Okra, Inc",
            "widget_failed" to "Which account do you want to connect with?",
            "currency" to "NGN",
            "isWebview" to true,
            "source" to "android",
            "uuid" to Settings.Secure.getString(
                requireActivity().contentResolver,
                Settings.Secure.ANDROID_ID
            ),
            "deviceInfo" to deviceInfo
        )
    }

    private fun parseLinkUriData(linkUri: Uri): HashMap<String, String?> {
        val linkData = HashMap<String, String?>()
        for (key in linkUri.queryParameterNames) {
            linkData[key] = linkUri.getQueryParameter(key)
        }
        return linkData
    }
}