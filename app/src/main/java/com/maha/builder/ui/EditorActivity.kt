package com.maha.builder.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.maha.builder.R
import com.maha.builder.data.MahaDatabase
import com.maha.builder.data.WebNode
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
        
        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        
        // 1. Attach the JavaScript Bridge
        webView.addJavascriptInterface(WebAppInterface(), "Android")
        
        // 2. Load the UI Shell
        webView.loadUrl("file:///android_asset/editor.html")
        
        // 3. When UI loads, fetch DB data and render
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) { refreshCanvas() }
        }

        findViewById<Button>(R.id.btnExport).setOnClickListener {
            Toast.makeText(this, "Exporting Website HTML/CSS...", Toast.LENGTH_LONG).show()
        }
    }
    
    // The Bridge: Receives clicks from JS and saves them natively
    inner class WebAppInterface {
        @JavascriptInterface
        fun addNode(type: String, content: String, css: String) {
            lifecycleScope.launch(Dispatchers.IO) {
                db.projectDao().insertNode(WebNode(projectId = projectId, type = type, content = content, cssRules = css))
                withContext(Dispatchers.Main) { refreshCanvas() }
            }
        }
    }
    
    // The Compiler: Reads DB, builds HTML string, pushes it back to JS
    private fun refreshCanvas() {
        lifecycleScope.launch(Dispatchers.IO) {
            val nodes = db.projectDao().getNodesForProject(projectId)
            val html = nodes.joinToString("") { n -> 
                val baseClass = "class='element-node'"
                when(n.type) {
                    "HEADER" -> "<h1 $baseClass style='${n.cssRules}'>${n.content}</h1>"
                    "BUTTON" -> "<button $baseClass style='${n.cssRules}'>${n.content}</button>"
                    "SECTION", "DIVIDER", "SPACER", "IMAGE" -> "<div $baseClass style='${n.cssRules}'>${n.content}</div>"
                    else -> "<p $baseClass style='${n.cssRules}'>${n.content}</p>"
                }
            }
            withContext(Dispatchers.Main) {
                // Execute JS to inject the compiled HTML live
                webView.evaluateJavascript("renderCanvas(`$html`);", null)
            }
        }
    }
}
