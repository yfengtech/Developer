package cz.developer.library.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import cz.developer.library.DeveloperActivity


import java.util.ArrayList

import cz.developer.library.R


/**
 * Created by cz on 2017/9/05.
 * 调试信息列表
 */
class DebugListAdapter(context: Context, items: List<DeveloperActivity.Companion.DeveloperItem>?) : BaseAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val items: ArrayList<DeveloperActivity.Companion.DeveloperItem> = ArrayList()

    init {
        if (null != items) {
            this.items.addAll(items)
        }
    }

    override fun getCount(): Int =items.size

    override fun getItem(i: Int): DeveloperActivity.Companion.DeveloperItem =this.items[i]

    override fun getItemId(i: Int): Long =i.toLong()

    override fun getView(i: Int, view: View?, parent: ViewGroup): View {
        var view = view
        if (null == view) {
            view = inflater.inflate(R.layout.debug_list_item, parent, false)
            view.tag = ViewHolder(view)
        }
        val holder = view?.tag as ViewHolder
        val item = getItem(i)
        holder.title.text = item.title
        holder.subInfo.text = item.desc
        return view
    }

    /**
     * Created by cz on 2017/9/05.
     */
    inner class ViewHolder(view: View) {
        val title: TextView = view.findViewById(R.id.tv_title) as TextView
        val subInfo: TextView = view.findViewById(R.id.tv_sub_info) as TextView
    }
}
