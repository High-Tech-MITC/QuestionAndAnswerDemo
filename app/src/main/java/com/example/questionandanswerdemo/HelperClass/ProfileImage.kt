package com.example.questionandanswerdemo.HelperClass

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import java.util.*
import kotlin.math.roundToInt
/*
you can use this class if you want a rectangle initial letter in the imageviews

 */

class ProfileImage(context: Context, color: Int, name: String, padding: Int) : ColorDrawable(color) {
    var pLetters: String? = name.first().toString().capitalize(Locale.ROOT)

    var paint: Paint = Paint()

    var circlepaint:Paint= Paint()
    var pSize = 0
    private var one_dp=0.0f
    private var pPadding = 0
    init {
        one_dp=1*context.resources.displayMetrics.density
        this.pPadding= (padding * one_dp).roundToInt()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        paint.isAntiAlias = true
        do {
            paint.textSize = (++pSize).toFloat()
            paint.getTextBounds(pLetters, 0, pLetters!!.length, bounds)
        } while (bounds.height() < canvas.height - pPadding && paint.measureText(pLetters) < canvas.width - pPadding)

        paint.textSize = pSize.toFloat()
        val pMesuredTextWidth = paint.measureText(pLetters)
        val pBoundsTextHeight = bounds.height()

        val xOffset: Float = (canvas.width - pMesuredTextWidth) / 2
        val yOffset: Float = ((pBoundsTextHeight + (canvas.height - pBoundsTextHeight) / 2).toFloat())
        paint.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        paint.color = -0x1
        canvas.drawText(pLetters!!, xOffset, yOffset, paint)
    }
}