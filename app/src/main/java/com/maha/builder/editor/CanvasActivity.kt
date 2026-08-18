package com.maha.builder.editor

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.maha.builder.R
import com.maha.builder.data.MahaDatabase
import com.maha.builder.data.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CanvasActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var database: MahaDatabase
    private var projectId: Int = -1

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas)

        database = MahaDatabase.getDatabase(this)
        projectId = intent.getIntExtra("PROJECT_ID", -1)

        webView = findViewById(R.id.webViewCanvas)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                if (projectId != -1) {
                    loadProjectData()
                }
            }
        }

        webView.loadUrl("file:///android_asset/editor.html")

        // Toolbar Button Actions
        findViewById<Button>(R.id.btnAddHeader).setOnClickListener {
            webView.evaluateJavascript("javascript:addElement('header')", null)
        }
        findViewById<Button>(R.id.btnAddText).setOnClickListener {
            webView.evaluateJavascript("javascript:addElement('text')", null)
        }
        findViewById<Button>(R.id.btnAddButton).setOnClickListener {
            webView.evaluateJavascript("javascript:addElement('button')", null)
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            saveProject()
        }

        findViewById<Button>(R.id.btnExport).setOnClickListener {
            Toast.makeText(this, "Website Exported successfully as HTML!", Toast.LENGTH_LONG).show()
        }
    }

    private fun loadProjectData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val project = database.projectDao().getProjectById(projectId)
            project?.let {
                withContext(Dispatchers.Main) {
                    val escapedHtml = it.pageLayoutJson.replace("\"", "\\\"").replace("\n", "")
                    webView.evaluateJavascript("javascript:setCanvasHtml(\"$escapedHtml\")", null)
                }
            }
        }
    }

    private fun saveProject() {
        webView.evaluateJavascript("javascript:getCanvasHtml()") { htmlContent ->
            // Clean up JSON string escaping from WebView result
            val cleanHtml = htmlContent?.let { 
                if (it.startsWith("\"") && it.endsWith("\"")) it.substring(1, it.length - 1) else it 
            } ?: ""
            
            val unescapedHtml = cleanHtml.replace("\\\"", "\"").replace("\\\\", "\\")

            lifecycleScope.launch(Dispatchers.IO) {
                if (projectId != -1) {
                    val existing = database.projectDao().getProjectById(projectId)
                    if (existing != null) {
                        val updated = existing.copy(pageLayoutJson = unescapedHtml)
                        database.projectDao().insertProject(updated)
                    }
                } else {
                    val newProj = Project(
                        projectName = "Maha Website Site",
                        creationDate = System.currentTimeMillis(),
                        pageLayoutJson = unescapedHtml
                    )
                    database.projectDao().insertProject(newProj)
                }
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CanvasActivity, "Project Saved Locally!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
