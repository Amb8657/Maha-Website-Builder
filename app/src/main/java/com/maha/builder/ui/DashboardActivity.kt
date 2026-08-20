package com.maha.builder.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
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
                        // Mimicking the exact card layout from the image
                        val card = LinearLayout(this@DashboardActivity).apply {
                            orientation = LinearLayout.VERTICAL
                            setPadding(30, 40, 30, 40)
                            background = GradientDrawable().apply {
                                setColor(Color.parseColor("#26282E"))
                                cornerRadius = 24f
                                setStroke(2, Color.parseColor("#3A3D45"))
                            }
                        }
                        
                        val title = TextView(this@DashboardActivity).apply {
                            text = p.name
                            setTextColor(Color.parseColor("#FFFFFF"))
                            textSize = 18f
                        }
                        
                        val bottomRow = LinearLayout(this@DashboardActivity).apply {
                            orientation = LinearLayout.HORIZONTAL
                            setPadding(0, 20, 0, 0)
                            gravity = Gravity.CENTER_VERTICAL
                        }
                        
                        val status = TextView(this@DashboardActivity).apply {
                            text = "Draft"
                            setTextColor(Color.parseColor("#4CAF50"))
                            layoutParams = LinearLayout.LayoutParams(0, -2, 1f)
                        }
                        
                        val btnEdit = Button(this@DashboardActivity).apply {
                            text = "Edit"
                            setTextColor(Color.parseColor("#D4AF37"))
                            background = GradientDrawable().apply {
                                setColor(Color.TRANSPARENT)
                                setStroke(3, Color.parseColor("#D4AF37"))
                                cornerRadius = 12f
                            }
                            setOnClickListener {
                                startActivity(Intent(this@DashboardActivity, EditorActivity::class.java).putExtra("PROJECT_ID", p.id))
                            }
                        }
                        
                        bottomRow.addView(status)
                        bottomRow.addView(btnEdit)
                        card.addView(title)
                        card.addView(bottomRow)
                        
                        container.addView(card, LinearLayout.LayoutParams(-1, -2).apply { setMargins(0, 0, 0, 32) })
                    }
                }
            }
        }
        
        loadProjects()
        
        findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabCreate).setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                db.insertProject(Project(name = "E-Commerce Store " + System.currentTimeMillis().toString().takeLast(3)))
                withContext(Dispatchers.Main) { loadProjects() }
            }
        }
    }
}
