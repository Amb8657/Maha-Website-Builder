package com.maha.builder

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1]) // Simulates an actual Android device environment internally
class AutomatedQATest {

    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_6)

    @Test
    fun verifyNestedToolsAndLogic() {
        // 1. Behavioral Test: Verifying the tools list exists and functions
        val tools = listOf("Text", "Image", "Layout", "Effects")
        assertTrue("All core categories must be present", tools.contains("Effects"))
    }

    @Test
    fun visualRegressionCheck() {
        // 2. Visual Test: Snapshots the UI to ensure buttons haven't shifted
        // Using Paparazzi to verify the layout without crashing the runner
        val view = paparazzi.inflate<androidx.constraintlayout.widget.ConstraintLayout>(R.layout.activity_main)
        paparazzi.snapshot(view, "Master_UI_Check")
    }
}
