package com.maha.builder

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import org.junit.Rule
import org.junit.Test

class VisualQATest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_6)

    @Test
    fun verifyCanvasUIMatchesGoldenImage() {
        // Runs safely in pure Java environment
        val view = paparazzi.inflate<androidx.constraintlayout.widget.ConstraintLayout>(R.layout.activity_main)
        paparazzi.snapshot(view, "Master_UI_Check")
    }
}
