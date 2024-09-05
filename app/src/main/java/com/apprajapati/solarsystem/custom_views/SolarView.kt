package com.apprajapati.solarsystem.custom_views

import com.apprajapati.solarsystem.R
import kotlinx.coroutines.Dispatchers
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ComposePathEffect
import android.graphics.CornerPathEffect
import android.graphics.DiscretePathEffect
import android.graphics.Paint
import android.graphics.PathEffect
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

//holds values of scaled up canvas to work with smaller numbers
var CANVAS_WIDTH    = 0f
var CANVAS_HEIGHT   = 0f

class SolarView(context: Context, attrs: AttributeSet?= null) : View(context, attrs){

    private var angle = 0f //angle rotation for earth/sun
    private var moonAngle = 180f // self explanatory

    private val paint = Paint()
    private var startThread = false

    private var points = arrayListOf(RandomCircle()) //random circle points list

    private var colorPercent = 0f // to lerp between two values of color

    private var job : Job ?= null

    init {
        paint.isAntiAlias = true
        paint.textSize = 30f
        paint.color = Color.rgb(122,33,222,)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f

        points = randomPoints()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.d("Ajay", "canvas w/h $width .. $height")
    }

    fun PointF.getCenterPoint() {
        x = width/2.toFloat()
        y = height/2.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(ContextCompat.getColor(context, R.color.solar_background)); //PorterDuff.Mode.CLEAR -> read more about this.
        drawPlanets(canvas)
    }

    private fun drawPlanets(canvas: Canvas){

        //1440 .. 2621 width height
        canvas.save()
        //if you wanna work with 6 units, then divide by some number to get to 6..
        // 6 * 100 = 600, ... 600/100 = 6...
        //this will convert it into 6 x 6
        // translate must be used with that unit

        // 600 * 6 = 100  ,
        //2621/100 = 26 units
        canvas.scale(100f, 100f) //14 x 26
        CANVAS_WIDTH = width.toFloat()/100
        CANVAS_HEIGHT = height.toFloat()/100

        paint.strokeWidth = 0.5f
        paint.color = Color.parseColor("#FFFF00")


        val start = Color.parseColor("#FFFF00") //  #fffff5
        // change this color to transition from one to another. currently its same.
        val end = Color.parseColor("#FFFF00") //#FFFF00


        val c = lerpColor(start.toColor(), end.toColor(), colorPercent)
        paint.color = c
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 0.01f
        for(point in points){
            //canvas.drawCircle(point.getPosition().x, point.getPosition().y, point.getR(), paint)
            val x = point.getPosition().x
            val y = point.getPosition().y
            for(i in 0..360 step 72){
                paint.style = Paint.Style.FILL
                val px = x + point.getR() * cos(degreesToRadians(i.toFloat()))
                val py = y + point.getR() * sin(degreesToRadians(i.toFloat()))

                canvas.drawPoint(px.toFloat(), py.toFloat(), paint)

                val p1x = x + point.getR() * cos(degreesToRadians(i.toFloat()+144))
                val p1y = y + point.getR() * sin(degreesToRadians(i.toFloat()+144))

                canvas.drawPoint(p1x.toFloat(), p1y.toFloat(), paint)

                canvas.drawLine(px.toFloat(),py.toFloat(), p1x.toFloat(), p1y.toFloat(), paint)
            }
        }
        canvas.restore()


        canvas.save()
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 10f
        paint.pathEffect = ComposePathEffect(CornerPathEffect(200f), DiscretePathEffect(0.5f, 15f))
        paint.color = Color.parseColor("#AA4203")
        canvas.translate(width/2.toFloat(), height/2.toFloat())
        canvas.rotate(angle)
        paint.setShadowLayer(80f, 0f, 0f, Color.parseColor("#AA4210"))
        canvas.drawCircle(0f, 0f, 60f, paint) //The sun circle
        canvas.restore()

        paint.clearShadowLayer()
        paint.pathEffect = PathEffect()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 0.8f
        canvas.save()
        canvas.translate(width/2.toFloat(), height/2.toFloat())
        canvas.rotate(angle)
        canvas.drawCircle(0f, 0f, 300f , paint ) // outer orbit circle

        canvas.translate(300f, 0f)
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#809fff")
        canvas.rotate(moonAngle)
        canvas.drawCircle(0f, 0f, 30f , paint )
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(0f, 0f, 50f, paint) //Earth orbit

        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#bfbfbf")
        paint.strokeWidth = 3f
        paint.style  = Paint.Style.FILL
        canvas.drawCircle(35f, 35f, 10f, paint) //moon orbit circle
        //canvas.drawLine(-50f, -50f, 50f, 50f, paint)
        canvas.restore()

    }

    private fun degreesToRadians(degrees: Float): Double{
        return Math.PI * degrees / 180
    }

    private suspend fun run() {
        Log.d("Ajay", "Thread name ${Thread.currentThread().name}")
        while(startThread){
            postInvalidate()
            try {
                delay(40)
                angle += 1
                moonAngle += 3f
                if(angle > 360){
                    angle = 1f
                }

                if(moonAngle > 360){
                    moonAngle = 1f
                }

                colorPercent = (sin(angle*angle) + 1)/2
                pointsMove()
            }catch (e: Exception){
                e.printStackTrace()
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

    fun startThread() {
        Log.d("Ajay", "Thread start!")
        job = findViewTreeLifecycleOwner()?.lifecycleScope?.launch(Dispatchers.IO) {
            if(!startThread){
                startThread = true
                run()
            }
        }
    }

    fun stopThread() {
        Log.d("Ajay", "Thread stop!")
        if (startThread) {
            startThread = false
            job?.cancel()
            job = null
        }
    }

    private fun pointsMove() {
        for(point in points){
            point.move()
        }
    }

    private fun randomPoints(): ArrayList<RandomCircle> {
        points.clear()
        for(i in 0..10){
            val v = RandomCircle()
            points.add(v)
        }
        return points
    }


    private fun lerpColor(a: Color, b: Color, p: Float) : Int {

        val r = a.red() + (b.red() - a.red()) * p
        val g = a.green() + (b.green() - a.green()) * p
        val b = a.blue() + (b.blue() - a.blue()) * p
        val c = Color.rgb(r, g, b)

        return c
    }

    private fun addPoints(point: PointF){
        repeat(50){
            val p = RandomCircle()
            p.setPosition(point)
            points.add(p)
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event!!.action

        when(action){
            ACTION_DOWN -> {
                Log.d("Ajay", "Pressed X ${event.x/100}, Y ${event.y/100}")
                val point = PointF(event.x/100, event.y/100)
                addPoints(point)
                return true
            }
        }

        return false
        //return super.onTouchEvent(event)
    }
}

class RandomCircle {
    private var position = PointF(0f,0f)
    private var velocity = 0f // 0.1 to 1
    private var radius = 0f
    private var direction = 0

    init {
        position.x = getX()
        position.y = getY()
        velocity = getV()
        radius = getRadius()
        direction = getDirection()
    }

    private fun getX(): Float{
        return Random.nextDouble(0.0, 14.4).toFloat()
    }

    private fun getY(): Float{
        return Random.nextDouble(0.0, 26.21).toFloat()
    }

    private fun getV() : Float{
        return Random.nextDouble(0.005, 0.03).toFloat()
    }

    private fun getRadius(): Float {
        return Random.nextDouble(0.01, 0.2).toFloat()
    }

    fun getPosition() : PointF {
        return position
    }

    fun getR(): Float {
        return radius
    }

    private fun getDirection(): Int {
        return Random.nextInt(1, 9) // 1-8, 9, until 9 isn't inclusive.
    }

    fun setPosition(point: PointF) {
        this.position.x = point.x
        this.position.y = point.y
    }

    fun move(){
        when(direction) {
            1 -> {
                position.x += velocity
            }
            2 -> {
                position.y += velocity
            }
            3 -> {
                position.x -= velocity
            }
            4 -> {
                position.y -= velocity
            }
            5 -> {
                position.x += velocity
                position.y += velocity
            }
            6 -> {
                position.x -= velocity
                position.y -= velocity
            }
            7-> {
                position.x += velocity
                position.y -= velocity
            }
            8-> {
                position.x -= velocity
                position.y += velocity
            }
        }

        if((position.x > CANVAS_WIDTH || position.y > CANVAS_HEIGHT) ||
            (position.x < 0 || position.y < 0)){
            reset()
        }
    }

    private fun reset(){
        val point = PointF(getX(), getY())
        this.velocity = getV()
        this.radius = getRadius()
        this.position = point
        this.direction = getDirection()
    }
}