package cz.developer.library.widget.draw

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

/**
 * Created by cz on 2017/9/6.
 */
interface ViewDrawInterface{
    fun onDraw(canvas: Canvas, paint: Paint, rect: Rect)
}