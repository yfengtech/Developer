package cz.developer.library.widget.dialog

import android.app.Dialog
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDialogFragment
import android.view.View
import android.view.ViewGroup
import cz.developer.library.DeveloperManager
import cz.developer.library.R
import cz.developer.library.ui.hierarchy.HierarchyFragment
import cz.developer.library.widget.DeveloperLayout
import cz.developer.library.widget.hierarchy.HierarchyNode
import java.lang.NullPointerException

/**
 * Created by cz on 2017/10/24.
 */
class DeveloperDialog : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity=activity
        val layout=activity?.findViewById(R.id.developerContainer) as? DeveloperLayout
        return when {
            null==layout -> super.onCreateDialog(savedInstanceState)
            null!=activity -> AlertDialog.Builder(activity).
                    setTitle(R.string.view_developer).
                    setItems(resources.getStringArray(R.array.debug_array)) { _, which ->
                        when(which){
                            0->showHierarchyFragment()
                            1->layout.toggleMemoryView()
                            2->layout.toggleViewBorder()
                        }
                    }.create()
            else -> throw NullPointerException("activity is null!")
        }
    }

    private fun showHierarchyFragment() {
        val activity=activity?:return
        val decorView=activity.window.decorView
        val root= HierarchyNode(0,decorView::class.java.simpleName)
        if(decorView is ViewGroup){
            (0 until decorView.childCount).
                    map { decorView.getChildAt(it) }.
                    forEach { hierarchyViewer(it,root,1) }
        }
        if(activity is FragmentActivity){
            DeveloperManager.toDeveloperFragment(activity, HierarchyFragment.newInstance(root))
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
            try{
                node.entryName=view.resources.getResourceEntryName(view.id)
            } catch (e:Exception){
                node.entryName="unknow"
            }
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


}