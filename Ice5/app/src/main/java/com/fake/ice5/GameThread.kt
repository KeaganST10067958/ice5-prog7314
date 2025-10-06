package com.fake.ice5

import android.graphics.Canvas
import android.view.SurfaceHolder
import com.fake.ice5.GameView

class GameThread (private val surfaceHolder: SurfaceHolder, private val gameView: GameView) : Thread() {
    private var running = false

    fun setRunning(isRunning: Boolean) {
        running = isRunning
    }

    override fun run() {
        var canvas: Canvas?
        while (running) {
            canvas = null
            try {
                canvas = surfaceHolder.lockCanvas()
                synchronized(surfaceHolder) {
                    gameView.update()
                    if (canvas != null) {
                        gameView.draw(canvas)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                canvas?.let {
                    surfaceHolder.unlockCanvasAndPost(it)
                }
            }
            sleep(20)
        }
    }
}