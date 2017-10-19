package cz.developer.library.callback

import android.app.Activity
import android.app.Application
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout


import cz.developer.library.DeveloperActivityManager
import cz.developer.library.DeveloperManager
import cz.developer.library.R
import cz.developer.library.ui.hierarchy.HierarchyFragment
import cz.developer.library.widget.DeveloperLayout
import cz.developer.library.widget.hierarchy.HierarchyNode


/**
 * Created by cz on 9/7/16.
 */
class MyActivityLifecycleCallback : Application.ActivityLifecycleCallbacks {
    init {
        DeveloperActivityManager.clear()
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        if (null != activity) {
            DeveloperActivityManager.add(activity)
            injectLayout(activity)
        }
    }

    /**
     * 注入布局
     */
    private fun injectLayout(activity: Activity) {
        val decorView = activity.window.decorView
        if (null != decorView && decorView is ViewGroup) {
            if (0 < decorView.childCount) {
                //加入统计浮层
                val childView = decorView.getChildAt(0)
                decorView.removeView(childView)
                val layout = DeveloperLayout(activity)
                layout.addView(childView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
                decorView.addView(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

                //初始化调试信息
                View.inflate(activity,R.layout.developer_menu_layout,layout)
                //回调长按监听
                layout.onItemLongClickListener{ DeveloperManager.onItemLongClick(activity,it) }
                //打开界面视图
                layout.findViewById(R.id.memoryButton1).setOnClickListener {
                    //将当前界面所有控件节点信息扫描出来
                    val decorView=activity.window.decorView
                    val root= HierarchyNode(0,decorView::class.java.simpleName)
                    if(decorView is ViewGroup){
                        (0..decorView.childCount-1).
                                map { decorView.getChildAt(it) }.
                                forEach { hierarchyViewer(it,root,1) }
                    }
                    if(activity is FragmentActivity){
                        DeveloperManager.toDeveloperFragment(activity,HierarchyFragment.newInstance(root))
                    }
                }
            }
        }
    }

    /**
     * 遍历所有控件层级节点
     */
    private fun hierarchyViewer(view: View, parent: HierarchyNode, level:Int){
        val node= HierarchyNode(level,view::class.java.simpleName)
        //记录id
        node.id=view.id
        //记录控件描述
        node.description=view.contentDescription
        if(view.id!= View.NO_ID){
            //记录id
            node.entryName=view.resources.getResourceEntryName(view.id)
        }
        //记录控件所占矩阵
        val rect= Rect()
        view.getGlobalVisibleRect(rect)
        node.rect.set(rect)

        //记录父节点
        node.parent=parent
        //记录子节点
        parent.children.add(node)
        if(view is ViewGroup){
            (0..view.childCount-1).
                    map { view.getChildAt(it) }.
                    forEach { hierarchyViewer(it,node,level+1) }
        }
    }

    override fun onActivityStarted(activity: Activity?)=Unit

    override fun onActivityResumed(activity: Activity?)=Unit

    override fun onActivityPaused(activity: Activity?)=Unit

    override fun onActivityStopped(activity: Activity?)=Unit

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?)=Unit

    override fun onActivityDestroyed(activity: Activity?) {
        DeveloperActivityManager.remove(activity)
    }
}
