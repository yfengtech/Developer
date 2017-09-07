package cz.developer.library.widget.draw

import android.graphics.*
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cz.developer.library.debugLog
import cz.developer.library.widget.draw.impl.TextViewViewDraw

/**
 * Created by cz on 2017/9/6.
 * 控件调试绘图类
 * 1:为常规控件绘制边距
 * 2:可选实现子类,为其他子类,绘制辅助查看信息,如textView,是否居中
 */
class ViewDebugDrawHelper {
    companion object {
        private val paint1= Paint(Paint.ANTI_ALIAS_FLAG)
        private val paint2= Paint(Paint.ANTI_ALIAS_FLAG)
        private val rect = Rect()
        private val path = Path()

        //扩展绘图子类
        val textViewDraw=TextViewViewDraw()

        init {
            path.moveTo(0f,20f)
            path.lineTo(0f,0f)
            path.lineTo(20f,0f)

            paint1.style=Paint.Style.STROKE
            paint1.strokeWidth=1f
            paint1.color= Color.RED

            paint2.style=Paint.Style.STROKE
            paint2.strokeWidth=3f
            paint2.color= Color.BLUE
        }
    }

    /**
     * 子控件绘制
     */
    open fun draw(canvas: Canvas,childView: View){
        drawChildBorder(canvas,childView)
    }

    /**
     * 绘制控件边界
     */
    private fun drawChildBorder(canvas: Canvas,childView: View){
        childView.getGlobalVisibleRect(rect)
        canvas.drawRect(rect,paint1)
        //绘四角
        drawBorderPath(canvas,0f,rect.left.toFloat(),rect.top.toFloat())//左上
        drawBorderPath(canvas,90f,rect.right.toFloat(),rect.top.toFloat())//右上
        drawBorderPath(canvas,270f,rect.left.toFloat(),rect.bottom.toFloat())//左下
        drawBorderPath(canvas,180f,rect.right.toFloat(),rect.bottom.toFloat())//左下

        //扩展绘制
        if(childView is TextView){
            textViewDraw.onDraw(canvas, paint1, rect)
        }
        //遍历所有子控件
        if(childView is ViewGroup){
            (0..childView.childCount-1).
                    map { childView.getChildAt(it) }.
                    forEach { drawChildBorder(canvas,it) }
        }
    }

    /**
     * 绘制边界path
     */
    private fun drawBorderPath(canvas:Canvas,rotate:Float,left:Float,top:Float){
        canvas.save()
        canvas.translate(left,top)
        canvas.rotate(rotate)
        canvas.drawPath(path,paint2)
        canvas.restore()
    }

}