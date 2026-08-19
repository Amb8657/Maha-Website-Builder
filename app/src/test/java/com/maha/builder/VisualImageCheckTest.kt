package com.maha.builder

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import org.junit.Rule
import org.junit.Test

class VisualImageCheckTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_6)

    @Test
    fun checkCanvasEditorUIMatchesMasterImage() {
        // This takes a screenshot of your XML layout to compare against the Golden Image
        val view = paparazzi.inflate<androidx.constraintlayout.widget.ConstraintLayout>(R.layout.activity_canvas)
        paparazzi.snapshot(view, "Canvas_Editor_UI_Master_Image")
    }
}
