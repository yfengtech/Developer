package cz.developer.library.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.*
import android.widget.AdapterView
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import cz.developer.library.R
import cz.developer.library.debugLog
import cz.developer.library.widget.draw.ViewDebugDrawHelper
import cz.developer.library.widget.memory.MemoryView
import kotlinx.android.synthetic.main.developer_menu_layout.view.*

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
    private var isIntercept=false
    private var memoryView=MemoryView(context)
    private val debugDrawHelper= ViewDebugDrawHelper()
    private val rect = Rect()

    init {
        setWillNotDraw(false)
        //此处注释控件树绘图监听,子控件变化时,直接刷新主控件,确保边距效果的一致性
//        viewTreeObserver.addOnPreDrawListener {
//            true
//        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        //加入调试菜单
        View.inflate(context,R.layout.developer_menu_layout,this)
        toggleMenu.post {
            eachMenuItem { view, _ -> view.translationY=toggleMenu.top- view.top*1f }
        }
        toggleMenu.setOnClickListener {
            eachMenuItem { view, i ->
                if(0f==view.translationY){
                    view.animate().setStartDelay((i*100).toLong()).translationY(toggleMenu.top- view.top*1f)
                } else {
                    view.animate().setStartDelay((1*100).toLong()).translationY(0f)
                }
            }
        }
    }

    private fun eachMenuItem(action:(View,Int)->Unit){
        (0..debugMenuLayout.childCount-1)
                .map { debugMenuLayout.getChildAt(it) }
                .filter { it !=toggleMenu }
                .forEachIndexed { index, view -> action(view,index) }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val x=ev.x
        val y=ev.y
//        debugLog("dispatchTouchEvent:${ev.action}")
        when(ev.actionMasked){
            MotionEvent.ACTION_DOWN->{
                val findView=findViewByPoint(this,x.toInt(),y.toInt())
                //实现了DeveloperFilter过滤接口的布局,将不再被处理
                if(null!=findView&&findView !is DeveloperFilter){
                    touchView=findView
                    //由控件是否可以开启点击,决定是否拦截,因为不可点击控件,父类也不会纷发事件,导致up/cancel事件无法回调
                    isIntercept=!findView.isClickable||findView.isLongClickable
                    debugLog("按下:$findView ${findView.isClickable} ${findView.isLongClickable}}")
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
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP->removeLongClickCallback()
        }
        //这里反向模仿控件长按点击
        return isIntercept||super.dispatchTouchEvent(ev)
    }

    /**
     * 移除长按点击事件
     */
    private fun removeLongClickCallback(){
        debugLog("释放长按事件!")
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
        super.dispatchDraw(canvas)
        //绘制所有控件边界
//        debugDrawHelper.draw(canvas,this)
    }

}