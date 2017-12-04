package cz.developer.library.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import cz.developer.library.R
import cz.developer.library.prefs.DeveloperPrefs
import cz.developer.library.widget.draw.ViewDebugDrawHelper

/**
 * Created by cz on 2017/9/5.
 * 主要实现
 * 1:模拟实现控件长按事件,以触发特定需求的信息
 * 2:绘制边界,以及居中线,查看控件边界
 */
class DeveloperLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : FrameLayout(context, attrs, defStyleAttr){

    constructor(context: Context):this(context,null,0)
    constructor(context: Context, attrs: AttributeSet?):this(context,attrs,0)
    private var longClickAction:Runnable?=null
    private var itemLongClickListener:((View)->Unit)?=null
    private var touchView:View?=null
    private val debugDrawHelper= ViewDebugDrawHelper()
    private val rect = Rect()
    //是否开启控件调试
    private var isViewDebug=DeveloperPrefs.debugView

    init {
        setWillNotDraw(false)
        id=R.id.developerContainer
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        //模拟控件点击
        if(isViewDebug){
            debugViewEvent(ev)
        }
        //这里反向模仿控件长按点击
        return super.dispatchTouchEvent(ev)
    }

    private fun debugViewEvent(ev: MotionEvent) {
        val x=ev.x
        val y=ev.y
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                val findView = findViewByPoint(this, x.toInt(), y.toInt())
                //实现了DeveloperFilter过滤接口的布局,将不再被处理
                if (null != findView && viewIsClickable(findView)) {
                    touchView = findView
                    //由控件是否可以开启点击,决定是否拦截,因为不可点击控件,父类也不会纷发事件,导致up/cancel事件无法回调
                    removeCallbacks(longClickAction)
                    longClickAction = Runnable {
                        itemLongClickListener?.invoke(findView)
                        removeLongClickCallback()
                    }
                    postDelayed(longClickAction, ViewConfiguration.getLongPressTimeout().toLong())
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (null != touchView && null != longClickAction &&
                        touchView != findViewByPoint(this, x.toInt(), y.toInt())) {
                    removeLongClickCallback()
                }
            }
        //长按有两种情况不触发,一种为短暂点击,一种为滑动
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> removeLongClickCallback()
        }
    }

    private fun viewIsClickable(view:View)=view !is DeveloperFilter&&
            (view.isClickable||view.isLongClickable||null!=view.tag)

    /**
     * 移除长按点击事件
     */
    private fun removeLongClickCallback(){
        removeCallbacks(longClickAction)
        longClickAction=null
        touchView=null
    }

    /**
     * 根据按下x,y坐标,获取上层触发事件的view控件
     */
    private fun findViewByPoint(parent: View, x: Int, y: Int): View? {
        var findView: View? = null
        if(parent is DeveloperFilter){
            return parent
        } else if (parent is ViewGroup) {
            for (i in 0..parent.childCount - 1) {
                val child = parent.getChildAt(i)
                val childView = findViewByPoint(child, x, y)
                if (null != childView) {
                    return childView
                }
            }
        }
        //遍历的父控件
        if(null==findView){
            rect.setEmpty()
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
        try{
            super.dispatchDraw(canvas)
            //绘制所有控件边界
            debugDrawHelper.draw(canvas,this)
        } catch (e:Exception){
            invalidate()
        }
    }

    /**
     * 设置调试面板开关
     */
    fun toggleMemoryView(){
        val memoryView=findViewById(R.id.memoryView)
        if(null!=memoryView){
            memoryView.visibility=if(memoryView.isShown) View.GONE else View.VISIBLE
        }
    }

    /**
     * 开启边界绘制
     */
    fun toggleViewBorder(){
        debugDrawHelper.toggle()
        invalidate()
    }

    fun showViewBorder()=debugDrawHelper.isEnabled()

    fun isViewDebug()=isViewDebug

    fun setViewDebug(debug:Boolean){
        this.isViewDebug=debug
    }

    fun openDeveloperLayout(){
        val layout=findViewById(R.id.developerLayout)
        if(null!=layout){
            layout.visibility=View.VISIBLE
        }
    }

    fun closeDeveloperLayout(){
        val layout=findViewById(R.id.developerLayout)
        if(null!=layout){
            layout.visibility=View.GONE
        }
    }
}