package com.fake.ice5

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView (context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private val thread: GameThread
    private val bird = Bird(context)
    private val towers = mutableListOf<Tower>()
    private var score = 0
    private var gameOver = false

    private val paint = Paint().apply {
        color = Color.WHITE
        textSize = 70f
        typeface = Typeface.DEFAULT_BOLD
    }

    private val buttonPaint = Paint().apply {
        color = Color.rgb(50, 50, 50)
        style = Paint.Style.FILL
    }

    private val buttonTextPaint = Paint().apply {
        color = Color.CYAN
        textSize = 50f
        typeface = Typeface.DEFAULT_BOLD
        textAlign = Paint.Align.CENTER
    }

    private var playAgainButtonRect = RectF()

    init {
        holder.addCallback(this)
        thread = GameThread(holder, this)
        isFocusable = true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        towers.clear()
        score = 0
        gameOver = false
        thread.setRunning(true)
        thread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        thread.setRunning(false)
        while (retry) {
            try {
                thread.join()
                retry = false
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            if (gameOver && playAgainButtonRect.contains(event.x, event.y)) {
                resetGame()
            } else if (!gameOver) {
                bird.flap()
            }
        }
        return true
    }

    fun update() {
        if (!gameOver) {
            bird.update()
            if (bird.isOutOfBounds(height)) {
                gameOver = true
                return
            }

            if (towers.isEmpty() || towers.last().x < width - 400) {
                towers.add(Tower(width, height))
            }

            val iterator = towers.iterator()
            while (iterator.hasNext()) {
                val tower = iterator.next()
                tower.update()

                if (tower.collidesWith(bird)) {
                    gameOver = true
                    return
                }

                if (!tower.passed && tower.x + tower.width < bird.x) {
                    score++
                    tower.passed = true
                }

                if (tower.x + tower.width < 0) {
                    iterator.remove()
                }
            }
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawColor(Color.rgb(135, 206, 250)) // Light Sky Blue

        bird.draw(canvas)

        for (tower in towers) {
            tower.draw(canvas)
        }

        canvas.drawText("Score: $score", 50f, 100f, paint)

        if (gameOver) {
            canvas.drawText("Game Over!", width / 3f, height / 2f, paint)

            val buttonWidth = 400f
            val buttonHeight = 120f
            val buttonX = (width - buttonWidth) / 2f
            val buttonY = (height / 2f) + 100f
            playAgainButtonRect.set(buttonX, buttonY, buttonX + buttonWidth, buttonY + buttonHeight)

            canvas.drawRoundRect(playAgainButtonRect, 20f, 20f, buttonPaint)
            canvas.drawText("Play Again", playAgainButtonRect.centerX(), playAgainButtonRect.centerY() + 20f, buttonTextPaint)
        }
    }

    private fun resetGame() {
        bird.y = 500f
        score = 0
        towers.clear()
        gameOver = false
    }
}