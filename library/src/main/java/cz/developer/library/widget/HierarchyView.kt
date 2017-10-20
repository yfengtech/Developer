package cz.developer.library.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import cz.developer.library.R
import cz.developer.library.ui.view.model.ViewHierarchyItem

/**
 * Created by cz on 2017/9/8.
 * 展示单层级视图
 */
class HierarchyView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr){
    private val decorPaint= Paint(Paint.ANTI_ALIAS_FLAG)
    private val paint= Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context?):this(context,null,0)
    constructor(context: Context?, attrs: AttributeSet?):this(context,attrs,0)

    init {
        decorPaint.style=Paint.Style.FILL
        decorPaint.color=ContextCompat.getColor(context, R.color.developerReview)

        paint.style=Paint.Style.STROKE
        paint.color=ContextCompat.getColor(context, R.color.developerReviewItem)
        paint.strokeWidth=resources.getDimension(R.dimen.developerHierarchyWidth)
    }

    private var viewHierarchyItem: ViewHierarchyItem?=null

    fun setHierarchyItem(item: ViewHierarchyItem?){
        this.viewHierarchyItem=item
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(rootView.width,rootView.height)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制视图层级
        drawHierarchy(canvas,viewHierarchyItem)
    }

    /**
     * 绘制视图层级
     */
    private fun drawHierarchy(canvas: Canvas,viewHierarchyItem: ViewHierarchyItem?){
        var root=viewHierarchyItem?.root?:return
        //绘根视图
        canvas.drawRect(root.rect,decorPaint)
        //绘所有子视图
        var item:ViewHierarchyItem.Item?=root
        while(null!=item?.child){
            item=item.child
            if(null!=item){
                canvas.drawRect(item.rect,paint)
            }
        }
    }
}