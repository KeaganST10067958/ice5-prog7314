package com.fake.ice5


import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Tower (screenWidth: Int, screenHeight: Int) {
    var x = screenWidth.toFloat()
    val width = 150f
    private val gapHeight = 400f
    private val gapTop = (100..(screenHeight - 500)).random().toFloat()
    private val speed = 10f
    var passed = false

    private val paint = Paint().apply {
        color = Color.rgb(34, 139, 34) // Forest Green
    }

    fun update() {
        x -= speed
    }

    fun draw(canvas: Canvas) {
        canvas.drawRect(RectF(x, 0f, x + width, gapTop), paint)
        canvas.drawRect(RectF(x, gapTop + gapHeight, x + width, canvas.height.toFloat()), paint)
    }

    fun collidesWith(bird: Bird): Boolean {
        val birdRect = bird.getBounds()
        val topRect = RectF(x, 0f, x + width, gapTop)
        val bottomRect = RectF(x, gapTop + gapHeight, x + width, 3000f)
        return RectF.intersects(birdRect, topRect) || RectF.intersects(birdRect, bottomRect)
    }
}