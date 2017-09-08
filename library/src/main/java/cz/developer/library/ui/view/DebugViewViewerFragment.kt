package cz.developer.library.ui.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cz.recyclerlibrary.adapter.tree.TreeAdapter
import cz.developer.library.R
import cz.developer.library.ui.view.adapter.ViewHierarchyAdapter
import cz.developer.library.ui.view.model.ViewHierarchyItem
import kotlinx.android.synthetic.main.fragment_debug_view_viewer_info.*

/**
 * Created by cz on 2017/9/7.
 */
class DebugViewViewerFragment:Fragment(){
    var hierarchyItem: ViewHierarchyItem?=null
    companion object {
        fun newInstance(item: ViewHierarchyItem?):Fragment=DebugViewViewerFragment().apply {
            hierarchyItem=item
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_debug_view_viewer_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity=activity
        if(activity is AppCompatActivity){
            toolBar.setTitle(R.string.view_extras)
            activity.setSupportActionBar(toolBar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolBar.setNavigationOnClickListener{ fragmentManager.popBackStack() }
        }

        recyclerView.layoutManager=LinearLayoutManager(context)
        val rootNode=TreeAdapter.TreeNode(ViewHierarchyItem.Item())
        wrapperTreeNode(rootNode,hierarchyItem)
        recyclerView.adapter=ViewHierarchyAdapter(context,rootNode)
        hierarchyView.setHierarchyItem(hierarchyItem)
    }

    /**
     * 包装树形节点
     */
    private fun wrapperTreeNode(rootNode:TreeAdapter.TreeNode<ViewHierarchyItem.Item>,hierarchyItem: ViewHierarchyItem?){
        var root=hierarchyItem?.root?:return
        var item:ViewHierarchyItem.Item?=root
        //添加根
        var childNode=TreeAdapter.TreeNode(rootNode,root)
        rootNode.child.add(childNode)
        childNode.expand=true
        while(null!=item?.child){
            item=item.child
            if(null!=item){
                var newNode=TreeAdapter.TreeNode(childNode,item)
                childNode.child.add(newNode)
                childNode.expand=true
                childNode=newNode
            }
        }
    }


}