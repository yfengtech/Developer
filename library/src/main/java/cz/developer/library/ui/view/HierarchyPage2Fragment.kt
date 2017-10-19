package cz.developer.library.ui.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cz.recyclerlibrary.adapter.tree.TreeAdapter
import cz.developer.library.R
import cz.developer.library.ui.view.model.ViewHierarchyItem
import kotlinx.android.synthetic.main.fragment_view_hierarchy2.*

/**
 * Created by cz on 2017/9/7.
 */
internal class HierarchyPage2Fragment :Fragment(){
    var hierarchyItem: ViewHierarchyItem?=null
    companion object {
        fun newInstance(item: ViewHierarchyItem?):Fragment= HierarchyPage2Fragment().apply {
            hierarchyItem=item
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_view_hierarchy2, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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