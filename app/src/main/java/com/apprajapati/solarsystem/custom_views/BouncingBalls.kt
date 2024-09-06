package com.apprajapati.solarsystem.custom_views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

open class GraphicalObject(private var position: PointF) {

    private var velocity = 10f

    fun moveRight(){
        position.x += velocity
    }

    fun moveLeft(){
        position.x -= velocity
    }

    fun moveUp(){
        position.y -= velocity
    }

    open fun moveDown(){
        position.y += velocity
    }

    fun setVelocity(v: Float){
        velocity = v
    }
}

class Square(private var squarePosition: PointF,
             private val area : RectF,
             private val squareColor: Int) : GraphicalObject(squarePosition){


    private val squarePaint = Paint().apply {
        color = squareColor
        style = Paint.Style.FILL
    }

    private val squareSize = Random.nextInt(1, 60).toFloat()

    fun drawSquare(canvas: Canvas){
        val rectF = RectF(squarePosition.x, squarePosition.y, squarePosition.x+ squareSize, squarePosition.y + squareSize)
        canvas.drawRect(rectF, squarePaint)
    }

    override fun moveDown(){
        if(squarePosition.y >= area.height()) {
            resetPositionY()
        }
        super.moveDown()
    }

    private fun resetPositionY(){
        squarePosition.y = Random.nextDouble(-300.0, -1.0).toFloat()
    }

}

class Ball(private var ballPosition: PointF = PointF(0f,0f),
           private val area : RectF,
           private val ballColor: Int) : GraphicalObject(ballPosition){

    private val paint = Paint().apply {
        color = ballColor
        style = Paint.Style.FILL
    }

    private val radius = Random.nextInt(1,61).toFloat()

    fun drawBall(canvas: Canvas){
        canvas.drawCircle(ballPosition.x, ballPosition.y, radius, paint )
    }

    override fun moveDown(){
        if(ballPosition.y - radius >= area.height()) {
            resetPositionY()
        }
        super.moveDown()
    }

    private fun resetPositionY(){
        ballPosition.y = Random.nextDouble(-300.0, -1.0).toFloat()
    }
}

class BouncingBallsView(
    context: Context,
    attributeSet: AttributeSet
    ) : View(context, attributeSet) {

        private val paint = Paint()

    var viewWidth = 0f
    var viewHeight = 0f

    private var particles = arrayListOf<GraphicalObject>()

    //running thread vars
    private var job : Job ?= null
    private var startThread = false

    init {
        paint.style = Paint.Style.FILL
        paint.color = Color.WHITE
    }

    private fun placeRandomBalls(area: RectF) : ArrayList<GraphicalObject> {
        val list = arrayListOf<GraphicalObject>()
        for(i in 0..59){
            val randomY = Random.nextDouble(-2600.0, -1.0).toFloat()
            val randomX = Random.nextDouble(0.0, 1400.0).toFloat()

            val points = PointF(randomX, randomY)

            val color = Color.argb(255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256));
            if(Random.nextInt(0,2) == 1){
                val ball = Ball(points, area, color)
                list.add(ball)
            }else{
                val square = Square(points, area, color)
                list.add(square)
            }

        }
        return list
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        viewWidth = w.toFloat()
        viewHeight = h.toFloat()

        val screenArea = RectF(0f, 0f, viewWidth, viewHeight)

        particles = placeRandomBalls(screenArea)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for(particle in particles){
            if(particle is Ball){
                particle.drawBall(canvas)
            }

            if(particle is Square){
                particle.drawSquare(canvas)
            }
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

    private fun startThread(){
        job = findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
            if(!startThread){
                startThread =  true
                run()
            }
        }
    }

    private fun stopThread(){
        if(startThread){
            startThread = false
            job?.cancel()
            job = null
        }
    }


    suspend fun run(){
        while(startThread){
            invalidate()
            moveBallsDown()
            delay(30)
        }
    }

    private fun moveBallsDown(){
        for(particle in particles){
            particle.moveDown()
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.action
        when(action){
            MotionEvent.ACTION_DOWN -> {
                Log.d("Ajay :: Bouncing balls", "Pressed X ${event.x/100}, Y ${event.y/100}")

                for(ball in particles){
                    ball.moveDown()
                }

               // Log.d("Ajay :: Bouncing balls", "Ball X ${ball.points.x/100}, Y ${ball.points.y/100}")

                invalidate()
                return true
            }
        }
        return true
    }
}