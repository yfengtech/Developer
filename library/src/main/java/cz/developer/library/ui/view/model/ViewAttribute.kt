package cz.developer.library.ui.view.model

import android.content.res.Resources
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import java.util.*

/**
 * Created by cz on 2017/9/8.
 * 根据控件获取控件所有属性集
 */
class ViewAttribute(view: View){
    private val attributeItems= mutableMapOf<String,MutableList<Item>>()
    init {
        //当前动画属性

        var items=attributeItems.getOrPut("Animation"){ mutableListOf<Item>()}
        items.add(Item("alpha",view.alpha))
        items.add(Item("rotation",view.rotation))
        items.add(Item("rotationX",view.rotationX))
        items.add(Item("rotationY",view.rotationY))
        items.add(Item("translationX",view.translationX))
        items.add(Item("translationY",view.translationY))
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            items.add(Item("translationZ",view.translationZ))
            items.add(Item("elevation",view.elevation))
        }
        items.add(Item("X",view.x))
        items.add(Item("Y",view.y))
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
            items.add(Item("Z",view.z))

        items.add(Item("scaleX",view.scaleX))
        items.add(Item("scaleY",view.scaleY))
        items.add(Item("visibility",view.visibility))

        //屏幕坐标位置
        items=attributeItems.getOrPut("Point"){ mutableListOf<Item>()}
        val outLocation= IntArray(2)
        view.getLocationInWindow(outLocation)
        items.add(Item("LocationInWindow", Arrays.toString(outLocation)))
        view.getLocationOnScreen(outLocation)
        items.add(Item("LocationOnScreen",Arrays.toString(outLocation)))
        //屏幕位置
        items=attributeItems.getOrPut("Location"){ mutableListOf<Item>()}

        val rect= Rect()
        view.getDrawingRect(rect)
        items.add(Item("DrawingRect",rect))
        view.getFocusedRect(rect)
        items.add(Item("FocusedRect",rect))
        view.getHitRect(rect)
        items.add(Item("HitRect",rect))
        view.getGlobalVisibleRect(rect)
        items.add(Item("GlobalVisibleRect",rect))
        view.getLocalVisibleRect(rect)
        items.add(Item("LocalVisibleRect",rect))
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            view.getClipBounds(rect)
            items.add(Item("ClipBounds",rect))
        }

        //常见尺寸
        items=attributeItems.getOrPut("Size"){ mutableListOf<Item>()}
        items.add(Item("width",view.width))
        items.add(Item("height",view.height))
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
            items.add(Item("minimumWidth",view.minimumWidth))
            items.add(Item("minimumHeight",view.minimumHeight))
        }
        items.add(Item("measuredWidth",view.measuredWidth))
        items.add(Item("measuredHeight",view.measuredHeight))
        items.add(Item("left",view.left))
        items.add(Item("top",view.top))
        items.add(Item("right",view.right))
        items.add(Item("bottom",view.bottom))

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
            items.add(Item("paddingStart",view.paddingStart))
            items.add(Item("paddingEnd",view.paddingEnd))
        }
        items.add(Item("paddingLeft",view.paddingLeft))
        items.add(Item("paddingTop",view.paddingTop))
        items.add(Item("paddingRight",view.paddingRight))
        items.add(Item("paddingBottom",view.paddingBottom))


        //布局属性
        items=attributeItems.getOrPut("layoutParams"){ mutableListOf<Item>()}
        val layoutParams=view.layoutParams
        if(null!=layoutParams){
            if(layoutParams is ViewGroup.MarginLayoutParams){
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
                    items.add(Item("layout_marginStart",layoutParams.marginStart))
                    items.add(Item("layout_marginEnd",layoutParams.marginEnd))
                }
                items.add(Item("layout_leftMargin",layoutParams.leftMargin))
                items.add(Item("layout_topMargin",layoutParams.topMargin))
                items.add(Item("layout_rightMargin",layoutParams.rightMargin))
                items.add(Item("layout_bottomMargin",layoutParams.bottomMargin))
            } else if(layoutParams is LinearLayout.LayoutParams){
                items.add(Item("layout_gravity",layoutParams.gravity))
                items.add(Item("layout_weight",layoutParams.weight))
            } else if(layoutParams is RelativeLayout.LayoutParams){
                //todo 相对属性布局处理
                items.add(Item("layout_alignWithParent",layoutParams.alignWithParent))
                items.add(Item("layout_rule",layoutParams.rules))
                items.add(Item("layout_alignWithParent",layoutParams.alignWithParent))
            } else if(layoutParams is FrameLayout.LayoutParams){
                items.add(Item("layout_gravity",layoutParams.gravity))
            }
        }


        //当前状态
        items=attributeItems.getOrPut("Status"){ mutableListOf<Item>()}
        items.add(Item("isClickable",view.isClickable))
        items.add(Item("isLongClickable",view.isLongClickable))
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            items.add(Item("isContextClickable",view.isContextClickable))
        }
        items.add(Item("isFocusable",view.isFocusable))

        items.add(Item("isShown",view.isShown))
        items.add(Item("isOpaque",view.isOpaque))
        items.add(Item("isPressed",view.isPressed))
        items.add(Item("isEnabled",view.isEnabled))
        items.add(Item("isFocused",view.isFocused))
        items.add(Item("isActivated",view.isActivated))

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            items.add(Item("isNestedScrollingEnabled",view.isNestedScrollingEnabled))
        }

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
            items.add(Item("isTextDirectionResolved", view.isTextDirectionResolved))
            items.add(Item("isTextAlignmentResolved", view.isTextAlignmentResolved))
            items.add(Item("isAttachedToWindow",view.isAttachedToWindow))
            items.add(Item("isLaidOut",view.isLaidOut))
        }
        items.add(Item("isActivated",view.isActivated))


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR2) {
            items.add(Item("isInLayout",view.isInLayout))
        }
        items.add(Item("isDirty",view.isDirty))
        items.add(Item("isDrawingCacheEnabled",view.isDrawingCacheEnabled))
        items.add(Item("isDuplicateParentStateEnabled",view.isDuplicateParentStateEnabled))
        items.add(Item("isFocusableInTouchMode",view.isFocusableInTouchMode))
        items.add(Item("isInEditMode",view.isInEditMode))


        items.add(Item("isVerticalScrollBarEnabled",view.isVerticalScrollBarEnabled))
        items.add(Item("isSoundEffectsEnabled",view.isSoundEffectsEnabled))
        items.add(Item("isHorizontalFadingEdgeEnabled",view.isHorizontalFadingEdgeEnabled))
        items.add(Item("isHapticFeedbackEnabled",view.isHapticFeedbackEnabled))
        items.add(Item("isHardwareAccelerated",view.isHardwareAccelerated))

        items.add(Item("isVerticalScrollBarEnabled",view.isVerticalScrollBarEnabled))
        items.add(Item("isVerticalFadingEdgeEnabled",view.isVerticalFadingEdgeEnabled))



        //其他
        items=attributeItems.getOrPut("Other"){ mutableListOf<Item>()}
        items.add(Item("Id",Integer.toHexString(view.id)))
        var resourceId:String?
        try{
            resourceId=view.resources.getResourceEntryName(view.id)
        } catch (e:Resources.NotFoundException){
            resourceId="Undefined"
        }
        items.add(Item("ResourceId",resourceId))
        items.add(Item("contentDescription",view.contentDescription))
        items.add(Item("solidColor",view.solidColor))
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN) {
            items.add(Item("fitsSystemWindows",view.fitsSystemWindows))
        }
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
            items.add(Item("hasOverlappingRendering",view.hasOverlappingRendering))
        }
    }


    fun getAttributes():List<Item>{
        return attributeItems.onEach{ (key,items)-> items.forEach { it.group=key } }.flatMap { it.value }
    }

    /**
     * 属性条目
     */
    class Item(val name:String, val value:Any?=null){
        var group:String?=null
    }
}