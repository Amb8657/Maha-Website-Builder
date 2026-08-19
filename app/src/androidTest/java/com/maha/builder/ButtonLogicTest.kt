package com.maha.builder

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.web.assertion.WebViewAssertions.webMatches
import androidx.test.espresso.web.sugar.Web.onWebView
import androidx.test.espresso.web.webdriver.DriverAtoms.findElement
import androidx.test.espresso.web.webdriver.DriverAtoms.getText
import androidx.test.espresso.web.webdriver.Locator
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.maha.builder.editor.CanvasActivity
import org.hamcrest.Matchers.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ButtonLogicTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(CanvasActivity::class.java)

    @Test
    fun verifyHeaderButtonActuallyInjectsHeaderIntoCanvas() {
        // 1. Physically click the Native Android "+ Header" button
        onView(withId(R.id.btnAddHeader)).perform(click())

        // 2. Look inside the WebView HTML Canvas to verify the text was actually created
        onWebView(withId(R.id.webViewCanvas))
            .forceJavascriptEnabled()
            .withElement(findElement(Locator.ID, "canvas"))
            .check(webMatches(getText(), containsString("New Section Title")))
    }
}
