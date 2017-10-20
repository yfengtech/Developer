package cz.developer.library.ui.view.adapter

import android.content.Context
import android.util.TypedValue
import android.view.ViewGroup
import com.cz.recyclerlibrary.adapter.BaseViewAdapter
import com.cz.recyclerlibrary.adapter.BaseViewHolder
import com.cz.recyclerlibrary.adapter.tree.TreeAdapter
import cz.developer.library.R
import cz.developer.library.ui.view.model.ViewHierarchyItem

/**
 * Created by cz on 2017/9/8.
 */
class ViewHierarchyAdapter(val context: Context, rootNode: TreeNode<ViewHierarchyItem.Item>) : TreeAdapter<ViewHierarchyItem.Item>(context, rootNode) {
    private val PADDING: Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, context.resources.displayMetrics).toInt()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(createView(parent, R.layout.hierarchy_item))
    }

    override fun onBindViewHolder(holder: BaseViewHolder, node: TreeNode<ViewHierarchyItem.Item>, e: ViewHierarchyItem.Item, viewType: Int, position: Int) {
        val item=node.e
        val itemView=holder.itemView
        itemView.setPadding(PADDING * node.level, itemView.paddingTop, itemView.paddingRight, itemView.paddingBottom)
        holder.textView(R.id.viewDeath).text=context.getString(R.string.death_value,item.death)
        holder.textView(R.id.viewName).text=item.className
        val desc="PATH:${item.classPath}\n" +
                "ID:${item.resourceId}\n"+
                "RECT:${item.rect}"
        holder.textView(R.id.viewDesc).text=desc
    }

}