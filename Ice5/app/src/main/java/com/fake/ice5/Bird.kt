package com.fake.ice5

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF

class Bird (context: Context) {
    var x = 200f
    var y = 500f
    private val gravity = 3f
    private var velocity = 0f

    private val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.turquoise_bird)

    fun update() {
        velocity += gravity
        y += velocity
    }

    fun flap() {
        velocity = -30f
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, null, RectF(x, y, x + 100, y + 100), null)
    }

    fun getBounds(): RectF = RectF(x, y, x + 100, y + 100)

    fun isOutOfBounds(screenHeight: Int): Boolean {
        return y > screenHeight
    }
}