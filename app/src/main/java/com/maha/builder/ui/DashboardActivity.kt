package com.maha.builder.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.maha.builder.R
import com.maha.builder.data.MahaDatabase
import com.maha.builder.data.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        
        val db = MahaDatabase.getDatabase(this).projectDao()
        val container = findViewById<LinearLayout>(R.id.projectContainer)
        
        fun loadProjects() {
            lifecycleScope.launch(Dispatchers.IO) {
                val projects = db.getAllProjects()
                withContext(Dispatchers.Main) {
                    container.removeAllViews()
                    projects.forEach { p ->
                        val btn = Button(this@DashboardActivity).apply {
                            text = p.name
                            setTextColor(Color.parseColor("#F5F5F5"))
                            background = GradientDrawable().apply {
                                setColor(Color.parseColor("#2D1515"))
                                cornerRadius = 16f
                            }
                            setPadding(40, 40, 40, 40)
                            setOnClickListener {
                                startActivity(Intent(this@DashboardActivity, EditorActivity::class.java).putExtra("PROJECT_ID", p.id))
                            }
                        }
                        container.addView(btn, LinearLayout.LayoutParams(-1, -2).apply { setMargins(0, 0, 0, 24) })
                    }
                }
            }
        }
        
        loadProjects()
        
        findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabCreate).setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                db.insertProject(Project(name = "Website Project " + System.currentTimeMillis().toString().takeLast(3)))
                withContext(Dispatchers.Main) { loadProjects() }
            }
        }
    }
}
