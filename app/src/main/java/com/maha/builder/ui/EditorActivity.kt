package com.maha.builder.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.maha.builder.R
import com.maha.builder.data.MahaDatabase
import com.maha.builder.data.WebNode
import com.maha.builder.engine.HtmlEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditorActivity : AppCompatActivity() {
    private var projectId: Int = -1
    private lateinit var webView: WebView
    private lateinit var db: MahaDatabase

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        
        projectId = intent.getIntExtra("PROJECT_ID", -1)
        db = MahaDatabase.getDatabase(this)
        
        lifecycleScope.launch(Dispatchers.IO) {
            val proj = db.projectDao().getProject(projectId)
            withContext(Dispatchers.Main) {
                findViewById<TextView>(R.id.tvProjectName).text = proj?.name ?: "Editor"
            }
        }
        
        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        
        // Attach JS Bridge
        webView.addJavascriptInterface(WebAppInterface(), "Android")
        webView.loadUrl("file:///android_asset/editor.html")
        
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) { refreshCanvas() }
        }

        findViewById<Button>(R.id.btnExport).setOnClickListener {
            Toast.makeText(this, "Compiling Real HTML/CSS...", Toast.LENGTH_SHORT).show()
        }
    }
    
    inner class WebAppInterface {
        @JavascriptInterface
        fun addNode(type: String) {
            lifecycleScope.launch(Dispatchers.IO) {
                db.projectDao().insertNode(WebNode(projectId = projectId, type = type))
                withContext(Dispatchers.Main) { refreshCanvas() }
            }
        }
    }
    
    private fun refreshCanvas() {
        lifecycleScope.launch(Dispatchers.IO) {
            val nodes = db.projectDao().getNodesForProject(projectId)
            val html = HtmlEngine.compileNodes(nodes)
            withContext(Dispatchers.Main) {
                // Safely escape backticks and inject HTML into the canvas
                val escapedHtml = html.replace("`", "\\`")
                webView.evaluateJavascript("renderCanvas(`$escapedHtml`);", null)
            }
        }
    }
}
