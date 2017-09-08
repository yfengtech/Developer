package cz.developer.library.ui.view.model

import android.content.res.Resources
import android.graphics.Rect
import android.view.View

/**
 * Created by cz on 2017/9/8.
 * 控件单层级展示信息
 */
class ViewHierarchyItem(view: View){
    var root:Item?=null
    init {
        //获得控件当前纵深级
        var death=getDeath(view)
        //当前控件信息
        var child=getChildItem(null,view,death--)

        //记录到最后一层
        var parent=view
        while(parent!=view.rootView){
            parent=parent.parent as View
            child=getChildItem(child,parent,death--)
        }
        //记录主节点
        root=child
    }


    /**
     * 获得控件层级
     */
    private fun getDeath(view:View):Int{
        var death=1
        var parent=view.parent
        while(parent!=view.rootView){
            parent=parent.parent
            death++
        }
        return death
    }

    /**
     * 根据控件获得其常规信息
     */
    private fun getChildItem(child:Item?,view:View,death:Int):Item{
        val child=Item(child)
        try{
            child.resourceId=view.resources.getResourceEntryName(view.id)
        } catch (e: Resources.NotFoundException){
            child.resourceId="Undefined"
        }
        child.className=view::class.java.simpleName
        child.classPath=view::class.java.name
        child.id=view.id
        child.death=death
        val rect= Rect()
        view.getGlobalVisibleRect(rect)
        child.rect=rect
        return child
    }

    class Item(var child:Item?=null){
        var id:Int=0
        var resourceId:String?=null
        var className:String?=null
        var classPath:String?=null
        var rect:Rect?=null
        var death:Int=0
    }

}