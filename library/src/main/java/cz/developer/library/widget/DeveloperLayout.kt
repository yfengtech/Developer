package cz.developer.library.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.*
import android.widget.AdapterView
import android.widget.FrameLayout
import cz.developer.library.debugLog
import cz.developer.library.widget.draw.ViewDebugDrawHelper

/**
 * Created by cz on 2017/9/5.
 * 主要实现
 * 1:模拟实现控件长按事件,以触发特定需求的信息
 * 2:绘制边界,以及居中线,查看控件边界
 */
class DeveloperLayout(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : FrameLayout(context, attrs, defStyleAttr){

    constructor(context: Context?):this(context,null,0)
    constructor(context: Context?, attrs: AttributeSet?):this(context,attrs,0)
    private var longClickAction:Runnable?=null
    private var itemLongClickListener:((View)->Unit)?=null
    private var touchView:View?=null
    private val debugDrawHelper= ViewDebugDrawHelper()
    private val rect = Rect()

    init {
        setWillNotDraw(false)

        //此处注释控件树绘图监听,子控件变化时,直接刷新主控件,确保边距效果的一致性
//        viewTreeObserver.addOnPreDrawListener {
//            true
//        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val x=ev.x
        val y=ev.y
        when(ev.actionMasked){
            MotionEvent.ACTION_DOWN->{
                val findView=findViewByPoint(this,x.toInt(),y.toInt())
                //实现了DeveloperFilter过滤接口的布局,将不再被处理
                if(null!=findView&&findView !is DeveloperFilter){
                    touchView=findView
                    debugLog("按下:$findView ${ViewConfiguration.getLongPressTimeout()}")
                    removeCallbacks(longClickAction)
                    longClickAction= Runnable {
                        itemLongClickListener?.invoke(findView)
                        removeLongClickCallback()
                    }
                    postDelayed(longClickAction, ViewConfiguration.getLongPressTimeout().toLong())
                }
            }
            MotionEvent.ACTION_MOVE->{
                if(null!=touchView&& null!=longClickAction&&
                        touchView!=findViewByPoint(this,x.toInt(),y.toInt())){
                    debugLog("移动x:$x y:$y 越界移除长按事件")
                    removeLongClickCallback()
                }
            }
            //长按有两种情况不触发,一种为短暂点击,一种为滑动
            MotionEvent.ACTION_UP->removeLongClickCallback()
        }
        //这里反向模仿控件长按点击
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 移除长按点击事件
     */
    private fun removeLongClickCallback(){
        removeCallbacks(longClickAction)
        touchView=null
        longClickAction=null
    }

    /**
     * 根据按下x,y坐标,获取上层触发事件的view控件
     */
    private fun findViewByPoint(parent: View, x: Int, y: Int): View? {
        var findView: View? = null
        if(parent is DeveloperFilter){
            return parent
        } else if (parent is ViewGroup) {
            //遍历的父控件
            for (i in 0..parent.childCount - 1) {
                val child = parent.getChildAt(i)
                val childView = findViewByPoint(child, x, y)
                if (null != childView) {
                    findView = childView
                }
            }
        }
//        如果viewGroup找不到想要控件,则检测当前viewGroup
        if(null==findView){
            //普通控件
            parent.getGlobalVisibleRect(rect)
            if (rect.contains(x, y)) {
                findView = parent
            }
        }
        return findView
    }

    override fun setOnLongClickListener(l: OnLongClickListener?) {
        super.setOnLongClickListener(l)
    }

    fun onItemLongClickListener(listener:(View)->Unit){
        this.itemLongClickListener=listener
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        //绘制所有控件边界
//        debugDrawHelper.draw(canvas,this)
    }

}