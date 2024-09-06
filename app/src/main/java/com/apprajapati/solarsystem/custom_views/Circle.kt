package com.apprajapati.solarsystem.custom_views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CircleView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }

    private var circleX = 0f
    private var circleY = 0f
    private var radius = 100f

    init {
        // Initial position
        circleX = width / 2f
        circleY = height / 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(circleX, circleY, radius, paint)
    }

    fun updatePosition(x: Float, y: Float) {
        circleX = x
        circleY = y
        invalidate()  // Request a redraw
    }
}
