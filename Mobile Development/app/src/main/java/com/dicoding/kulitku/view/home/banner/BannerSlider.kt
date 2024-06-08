package com.dicoding.kulitku.view.home.banner

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Scroller
import androidx.viewpager.widget.ViewPager
import java.lang.reflect.Field

class BannerSlider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewPager(context, attrs) {

    companion object {
        const val DEFAULT_SCROLL_DURATION = 200
    }

    init {
        setDurationScroll(DEFAULT_SCROLL_DURATION)
        setOnTouchListener { _, _ -> true }
    }

    fun setDurationScroll(millis: Int) {
        try {
            val viewpager = ViewPager::class.java
            val scroller: Field = viewpager.getDeclaredField("mScroller")
            scroller.isAccessible = true
            scroller.set(this, OwnScroller(context, millis))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class OwnScroller(context: Context, private val durationScrollMillis: Int) : Scroller(context, DecelerateInterpolator()) {

        override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
            super.startScroll(startX, startY, dx, dy, durationScrollMillis)
        }
    }
}
