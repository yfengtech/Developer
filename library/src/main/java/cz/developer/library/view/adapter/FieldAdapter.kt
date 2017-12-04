package cz.developer.library.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import com.cz.recyclerlibrary.adapter.BaseViewHolder
import com.cz.recyclerlibrary.adapter.tree.TreeAdapter
import cz.developer.library.R
import cz.developer.library.ui.view.model.FieldItem

/**
 * Created by cz on 16/1/23.
 */
class FieldAdapter(context: Context, rootNode: TreeAdapter.TreeNode<FieldItem>) : TreeAdapter<FieldItem>(context, rootNode) {
    private val PADDING: Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, context.resources.displayMetrics).toInt()

    override fun onBindViewHolder(holder: BaseViewHolder, node: TreeAdapter.TreeNode<FieldItem>, file: FieldItem, viewType: Int, position: Int) {
        val itemView = holder.itemView
        itemView.setPadding(PADDING * node.level, itemView.paddingTop, itemView.paddingRight, itemView.paddingBottom)
        when (viewType) {
            FIELD_ITEM -> {
                (holder.itemView.findViewById(R.id.tv_simple_name) as TextView).text = file.type.simpleName
                (holder.itemView.findViewById(R.id.tv_name) as TextView).text = file.name
            }
            OBJECT_ITEM -> {
                (holder.itemView.findViewById(R.id.tv_simple_name) as TextView).text = file.type.simpleName
                (holder.itemView.findViewById(R.id.tv_name) as TextView).text = file.name
                holder.itemView.findViewById(R.id.iv_flag).isSelected = node.expand
            }
        }
    }

    override fun onNodeExpand(node: TreeAdapter.TreeNode<FieldItem>, holder:BaseViewHolder, expand: Boolean) {
        super.onNodeExpand(node, holder, expand)
        holder.itemView.findViewById(R.id.iv_flag).isSelected = expand
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            OBJECT_ITEM -> BaseViewHolder(createView(parent, R.layout.object_item))
            else -> BaseViewHolder(createView(parent, R.layout.field_item))
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        var viewType = OBJECT_ITEM
        if (baseType.any { it.isAssignableFrom(item.type) }) {
            viewType = FIELD_ITEM
        }
        return viewType
    }


    companion object {
        private val OBJECT_ITEM = 0
        private val FIELD_ITEM = 1
        val baseType= arrayOf(Byte::class.java,Int::class.java,Integer::class.java,Long::class.java,Char::class.java,
                Boolean::class.java,Short::class.java,Float::class.java,Double::class.java,
                String::class.java)
    }

}
