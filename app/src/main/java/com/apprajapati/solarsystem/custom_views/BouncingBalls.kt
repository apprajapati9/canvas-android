package com.apprajapati.solarsystem.custom_views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View

class Balls(points: PointF, radius: Float) {

}

class BouncingBallsView(
    context: Context,
    attributeSet: AttributeSet
    ) : View(context, attributeSet) {

        private val paint = Paint()

    init {
        paint.style = Paint.Style.FILL
        paint.color = Color.WHITE
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val text = "Ajay's custom view"
        canvas.drawText(text, 0, text.length,width/2.toFloat(), height/2.toFloat(), paint)
    }
}