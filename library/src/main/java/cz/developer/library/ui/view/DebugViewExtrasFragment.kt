package cz.developer.library.ui.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.SparseArray
import android.util.SparseBooleanArray
import android.util.SparseIntArray
import android.util.SparseLongArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cz.recyclerlibrary.adapter.tree.TreeAdapter
import com.cz.sample.adapter.FieldAdapter
import cz.developer.library.R
import cz.developer.library.ui.view.model.FieldItem
import kotlinx.android.synthetic.main.fragment_debug_view_extras_info.*
import java.lang.reflect.Modifier

/**
 * Created by cz on 2017/9/7.
 */
class DebugViewExtrasFragment:Fragment(){
    var viewTag:Any?=null
    var viewTagItems:Any?=null
    companion object {
        val sparseType = arrayOf(SparseArray::class.java,SparseIntArray::class.java,SparseBooleanArray::class.java)

        fun newInstance(tag:Any?,tagItems:Any?):Fragment=DebugViewExtrasFragment().apply {
            viewTag=tag
            viewTagItems=tagItems
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_debug_view_extras_info, container, false)
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

        val rootNode = TreeAdapter.TreeNode(FieldItem(Any::class.java,"root",null))
        val viewTag=viewTag
        if(null!=viewTag){
            val clazz=viewTag::class.java
            val childItem=FieldItem(clazz,getSimpleName(clazz.simpleName),viewTag)
            rootNode.child.add(TreeAdapter.TreeNode(childItem))
        }
        val viewTagItems=viewTagItems
        if(null!=viewTagItems&&viewTagItems is SparseArray<*>){
            for(i in 0..viewTagItems.size()-1){
                val key=viewTagItems.keyAt(i)
                val value=viewTagItems.valueAt(i)
                val clazz=value::class.java
                val childItem=FieldItem(clazz,"KEY:$key\nVALUE:${getSimpleName(value.toString())}",viewTag)
                rootNode.child.add(TreeAdapter.TreeNode(childItem))
            }
        }
        recyclerView.layoutManager=LinearLayoutManager(context)
        val fieldAdapter=FieldAdapter(context,rootNode)
        //展开时,加载数据
        fieldAdapter.setOnNodeLoadCallback {
            val nodeItems=getNodeItemsFromItem(it,it.e)
            if(null!=nodeItems){
                it.child.addAll(nodeItems)
            }
        }
        fieldAdapter.setOnNodeItemClickListener(object :TreeAdapter.OnNodeItemClickListener<FieldItem>{
            override fun onNodeItemClick(node: TreeAdapter.TreeNode<FieldItem>, v: View, position: Int) {
                //点击子条目
            }
        })
        recyclerView.adapter=fieldAdapter
    }

    /**
     * 从条目字段内获取所有的非静态FieldItems对象
     */
    private fun getNodeItemsFromItem(node: TreeAdapter.TreeNode<FieldItem>,item:FieldItem):List<TreeAdapter.TreeNode<FieldItem>>?{
        val value=item.value ?:null
        value as Any
        if(Collection::class.java.isAssignableFrom(value::class.java)){
            //处理集合
            val collection=value as Collection<*>
            return collection.map {
                val item=it as Any
                val fieldItem=FieldItem(item::class.java,item::class.java.simpleName,item)
                TreeAdapter.TreeNode(node,fieldItem)
            }
        } else if(Map::class.java.isAssignableFrom(value::class.java)){
            //处理map
            val mapItem=value as Map<*,*>
            return mapItem.map { (key,value)->
                val item=value as Any
                val fieldItem=FieldItem(item::class.java,"$key = ${item::class.java.simpleName}",item)
                TreeAdapter.TreeNode(node,fieldItem)
            }
        } else if(sparseType.any { it.isAssignableFrom(value::class.java) }){
            //sparseArray系列
            val sparseArray=value as SparseArray<*>
            val items= arrayListOf<TreeAdapter.TreeNode<FieldItem>>()
            for(i in 0..sparseArray.size()){
                val key=sparseArray.keyAt(i)
                val item=sparseArray.valueAt(i)
                val fieldItem=FieldItem(item::class.java,"KEY=$key\nVALUE:${item::class.java.simpleName}",item)
                items.add(TreeAdapter.TreeNode(node,fieldItem))
            }
            return items
        } else {
            //自定义数据类型
            val declaredFields=value.javaClass.declaredFields
            return declaredFields
                    ?.filterNot { Modifier.isStatic(it.modifiers) }
                    ?.map { field ->
                        field.isAccessible=true
                        val value=field.get(value)
                        val fieldItem=FieldItem(field.type,"${field.name} $value",value)
                        TreeAdapter.TreeNode(node,fieldItem)
                    }
        }
    }

    private fun getSimpleName(name:String):String=name.substring(name.lastIndexOf(".")+1,name.length)
}