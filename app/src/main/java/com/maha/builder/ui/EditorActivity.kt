package com.maha.builder.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
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

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        
        projectId = intent.getIntExtra("PROJECT_ID", -1)
        if (projectId == -1) finish()
        
        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        
        refreshCanvas()
        
        val db = MahaDatabase.getDatabase(this).projectDao()
        
        findViewById<Button>(R.id.btnAddHeader).setOnClickListener {
            insertNode(WebNode(projectId = projectId, type = "HEADER", content = "New Header", cssRules = "font-size:32px; color:#1A0505; font-weight:bold; margin-bottom:12px;"))
        }
        findViewById<Button>(R.id.btnAddText).setOnClickListener {
            insertNode(WebNode(projectId = projectId, type = "PARAGRAPH", content = "This is a new paragraph block ready for editing.", cssRules = "font-size:16px; color:#333333; line-height:1.5;"))
        }
        findViewById<Button>(R.id.btnAddButton).setOnClickListener {
            insertNode(WebNode(projectId = projectId, type = "BUTTON", content = "Click Me", cssRules = "background-color:#D4AF37; color:#1A0505; padding:12px 24px; border:none; border-radius:6px; font-weight:bold;"))
        }
        findViewById<Button>(R.id.btnExport).setOnClickListener {
            Toast.makeText(this, "Validating and Exporting HTML Engine...", Toast.LENGTH_LONG).show()
        }
    }
    
    private fun insertNode(node: WebNode) {
        lifecycleScope.launch(Dispatchers.IO) {
            MahaDatabase.getDatabase(this@EditorActivity).projectDao().insertNode(node)
            withContext(Dispatchers.Main) { refreshCanvas() }
        }
    }
    
    private fun refreshCanvas() {
        lifecycleScope.launch(Dispatchers.IO) {
            val nodes = MahaDatabase.getDatabase(this@EditorActivity).projectDao().getNodesForProject(projectId)
            val generatedHtml = HtmlEngine.compileWebsite(nodes)
            withContext(Dispatchers.Main) {
                webView.loadDataWithBaseURL(null, generatedHtml, "text/html", "UTF-8", null)
            }
        }
    }
}
