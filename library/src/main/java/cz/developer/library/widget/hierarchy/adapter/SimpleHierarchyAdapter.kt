package cz.developer.library.widget.hierarchy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import cz.developer.library.R
import cz.developer.library.widget.hierarchy.HierarchyLayout
import cz.developer.library.widget.hierarchy.HierarchyNode

/**
 * Created by cz on 2017/10/13.
 */
internal class SimpleHierarchyAdapter(val context: Context, node: HierarchyNode) : HierarchyLayout.HierarchyAdapter(node) {
    private val layoutInflate = LayoutInflater.from(context)
    override fun getView(parent: ViewGroup): View {
        val view=layoutInflate.inflate(R.layout.activity_hierarchy_item, parent, false)
        view.setTag(R.id.hierarchyItemView,ViewHolder(view))
        return view
    }

    override fun bindView(view: View, node: HierarchyNode) {
        val holder=view.getTag(R.id.hierarchyItemView) as ViewHolder
        holder.viewClassNameText.text = node.name
        holder.viewResourceName.text = node.entryName
        holder.viewRectText.text = node.rect.toString()
        holder.viewDescriptionText.text = node.description
        view.setOnClickListener {
            Toast.makeText(context, "点击:${node.name} 节点!", Toast.LENGTH_SHORT).show()
        }
    }

    private class ViewHolder(itemView:View){
        val viewClassNameText=itemView.findViewById(R.id.viewClassNameText) as TextView
        val viewResourceName=itemView.findViewById(R.id.viewResourceName) as TextView
        val viewRectText=itemView.findViewById(R.id.viewRectText) as TextView
        val viewDescriptionText=itemView.findViewById(R.id.viewDescriptionText) as TextView
    }

}