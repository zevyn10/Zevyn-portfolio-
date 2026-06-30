package com.headscroll.app

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.util.DisplayMetrics
import android.view.accessibility.AccessibilityEvent
import android.view.WindowManager

class ScrollAccessibilityService : AccessibilityService() {

    companion object {
        var instance: ScrollAccessibilityService? = null
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Not used - we only need gesture dispatch capability
    }

    override fun onInterrupt() {}

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

    /**
     * Performs a vertical swipe to scroll reels.
     * scrollUp = true  -> swipe finger from bottom to top (moves to NEXT reel)
     * scrollUp = false -> swipe finger from top to bottom (moves to PREVIOUS reel)
     */
    fun performSwipe(scrollUp: Boolean) {
        val metrics = getDisplayMetrics()
        val width = metrics.widthPixels
        val height = metrics.heightPixels

        val startX = width / 2f
        val startY = if (scrollUp) height * 0.75f else height * 0.25f
        val endY = if (scrollUp) height * 0.25f else height * 0.75f

        val path = Path()
        path.moveTo(startX, startY)
        path.lineTo(startX, endY)

        val gestureBuilder = GestureDescription.Builder()
        val strokeDescription = GestureDescription.StrokeDescription(path, 0, 250)
        gestureBuilder.addStroke(strokeDescription)

        dispatchGesture(gestureBuilder.build(), null, null)
    }

    private fun getDisplayMetrics(): DisplayMetrics {
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.getMetrics(metrics)
        return metrics
    }
}
