package com.maha.builder.editor

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.maha.builder.R

class CanvasActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas)
        val webView = findViewById<WebView>(R.id.webViewCanvas)
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(this, "Android")
        webView.loadUrl("file:///android_asset/editor.html")
    }

    @JavascriptInterface
    fun executeTool(toolName: String) {
        runOnUiThread {
            Toast.makeText(this, "Executing: $toolName", Toast.LENGTH_SHORT).show()
        }
    }
}
