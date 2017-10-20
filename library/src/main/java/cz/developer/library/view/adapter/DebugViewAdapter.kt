package cz.developer.library.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView

import java.util.ArrayList

import cz.developer.library.R
import cz.developer.library.ui.view.DebugViewFragment

/**
 * Created by cz on 9/5/2017.
 */
internal class DebugViewAdapter(context: Context, viewItems: List<DebugViewFragment.Companion.Item>?) : BaseAdapter() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val viewItems: MutableList<DebugViewFragment.Companion.Item>
    private val selectItems: MutableList<DebugViewFragment.Companion.Item>

    init {
        this.selectItems = ArrayList<DebugViewFragment.Companion.Item>()
        this.viewItems = ArrayList<DebugViewFragment.Companion.Item>()
        if (null != viewItems) {
            this.viewItems.addAll(viewItems)
            this.selectItems.addAll(viewItems)
        }
    }

    override fun getCount(): Int =viewItems.size

    override fun getItem(position: Int): DebugViewFragment.Companion.Item =viewItems[position]

    override fun getItemId(position: Int): Long =position.toLong()

    fun getSelectItems(): List<DebugViewFragment.Companion.Item> =selectItems

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.debug_view_item, parent, false)
        }
        val item = getItem(position)
        val viewName = convertView?.findViewById(R.id.tv_view_name) as TextView
        val viewDesc = convertView?.findViewById(R.id.tv_view_desc) as TextView
        val checkBox = convertView?.findViewById(R.id.check_box) as CheckBox
        viewName.text = item.title
        viewDesc.text = item.desc
        checkBox.isChecked = selectItems.contains(item)
        return convertView
    }

    fun selectItem(position: Int) {
        val item = getItem(position)
        if (selectItems.contains(item)) {
            selectItems.remove(item)
        } else {
            selectItems.add(item)
        }
        notifyDataSetChanged()
    }
}
