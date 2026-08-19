package com.maha.builder

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView = findViewById<WebView>(R.id.webViewCanvas)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        
        // Load the HTML engine containing all nested tools
        webView.loadUrl("file:///android_asset/editor.html")

        findViewById<Button>(R.id.btnExport).setOnClickListener {
            Toast.makeText(this, "Site Compiled Successfully!", Toast.LENGTH_SHORT).show()
        }
    }
}
