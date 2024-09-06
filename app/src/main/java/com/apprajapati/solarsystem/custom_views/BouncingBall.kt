package com.apprajapati.solarsystem.custom_views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.apprajapati.solarsystem.common.getRandomColor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class Circle(private val position: PointF, private val bouncingArea: RectF){

    private val radius = Random.nextInt(5,80).toFloat()

    private var dx = Random.nextInt(9, 31)
    private var dy = Random.nextInt(9, 31)

    val paint = Paint().apply {
        style = Paint.Style.FILL
        color = getRandomColor()
    }


    fun draw(canvas: Canvas){
        canvas.save()
        canvas.translate(position.x, position.y)
        canvas.drawCircle(0f, 0f, radius, paint)
        canvas.restore()
    }

    fun update(){
        if(position.x >= bouncingArea.right - radius){
            dx *= -1
        }

        if(position.x < radius){
            dx *= -1 //remember adverse effect when *1 because it increases minus values
        }

        if(position.y <= bouncingArea.top+radius) {
            dy *= -1
        }

        if(position.y >= bouncingArea.height()-radius){
            dy *= -1
        }

        position.x += dx
        position.y += dy
    }
    //TODO:implement collision detection on circle boundaries and bounce off of eachother.
}

class BouncingBall(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var job : Job ?= null
    private var startThread = false

    private val balls = arrayListOf<Circle>()


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val screenArea = RectF(0f, 0f, w.toFloat(), h.toFloat())
        for(i in 1..20){
            val pointX = Random.nextInt(15, w-10).toFloat()
            val pointY = Random.nextInt(15, h-10).toFloat()
            val point = PointF(pointX, pointY)
            val ball = Circle(point, screenArea)
            balls.add(ball)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for(ball in balls){
            ball.draw(canvas)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startThread()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopThread()
    }

    fun startThread(){
        if(!startThread){
            job = findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                startThread = true
                run()
            }
        }
    }

    fun stopThread(){
        startThread = false
        job?.cancel()
        job = null
    }

    suspend fun run(){
        while(startThread){
            invalidate()
            try{
//                Log.d("Ajay", "BouncingBall.kt -> thread running..")
                delay(40)
                bounce()
            }catch (e : Exception){
                e.printStackTrace()
            }

        }
    }

    private fun bounce(){
        for(ball in balls){
            ball.update()
        }
    }
}