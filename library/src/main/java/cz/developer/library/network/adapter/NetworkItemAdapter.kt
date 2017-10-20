package cz.developer.library.network.adapter

import android.content.Context
import android.graphics.Color
import android.support.annotation.StringRes
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.SparseArray
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView

import java.util.ArrayList

import cz.developer.library.R
import cz.developer.library.network.model.NetItem
import cz.developer.library.prefs.DeveloperPrefs

/**
 * Created by cz on 11/8/16.
 */

class NetworkItemAdapter(private val context: Context, items: List<NetItem>?, private var serverUrl: String?) : BaseAdapter(), Filterable {
    private val inflater: LayoutInflater
    private val items: MutableList<NetItem>
    private val urlItems: SparseArray<String>
    private var filterText: CharSequence? = null
    private var filter: ItemFilter= ItemFilter(items)
    private val textColor: Int

    init {
        val typedValue = TypedValue()
        this.textColor = typedValue.data
        this.inflater = LayoutInflater.from(context)
        this.items = ArrayList<NetItem>()
        this.urlItems = SparseArray<String>()
        if (null != items) {
            this.items.addAll(items)
        }
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(i: Int): NetItem {
        return items[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, convertView: View?, parent: ViewGroup): View {
        var view:View
        if (null == convertView) {
            view = inflater.inflate(R.layout.network_item, parent, false)
            view.tag = ViewHolder(view)
        } else {
            view=convertView
        }
        val holder = view.tag as ViewHolder
        val item = getItem(i)
        val info=item.info
        if(null==info){
            holder.actionView.text = getString(R.string.network_desc_value, "无")
        } else {
            holder.actionView.text = getString(R.string.network_desc_value, info)
        }
        holder.pathView.text = getString(R.string.path_value, item.url)
        var url: String? = urlItems.get(i)
        if (null == url) {
            val key=System.identityHashCode(item.url).toString()
            val dynamicUrl = DeveloperPrefs.getString(key)
            url = if (TextUtils.isEmpty(dynamicUrl)) "" else dynamicUrl
            urlItems.put(i, url)
        }
        holder.urlView.setTextColor(if (TextUtils.isEmpty(url)) textColor else Color.RED)
        val serverUrl=serverUrl
        if(null!=serverUrl){
            holder.urlView.text = getString(R.string.url_value, if (TextUtils.isEmpty(url)) serverUrl else url)
        }
        setColorSpan(holder.actionView, Color.GREEN, filterText)
        setColorSpan(holder.pathView, Color.GREEN, filterText)
        return view
    }

    fun removeUrlItem(i: Int) {
        urlItems.remove(i)
    }

    fun clearUrlItems() {
        urlItems.clear()
    }

    fun getString(@StringRes res: Int, vararg params: Any): String {
        return context.getString(res, *params)
    }

    fun setColorSpan(textView: TextView, color: Int, vararg words: CharSequence?) {
        val text = textView.text
        val span = SpannableStringBuilder(text)
        if (!TextUtils.isEmpty(text) && null != words) {
            for (i in words.indices) {
                if (null != words[i]) {
                    var index = 0
                    var start = 0
                    while (-1 != start) {
                        start = text.toString().indexOf(words[i].toString(), index)
                        if (-1 != start) {
                            span.setSpan(ForegroundColorSpan(color), start, start + words[i].toString().length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                            index = start + 1
                        }
                    }
                }
            }
        }
        textView.text = span
    }

    fun swapItems(items: List<NetItem>?) {
        if (null != items) {
            this.items.clear()
            this.items.addAll(items)
            notifyDataSetChanged()
        }
    }

    override fun getFilter(): Filter {
        return filter
    }

    fun filter(text: CharSequence?) {
        this.filterText = text
        getFilter().filter(text)
    }

    fun setServerUrl(serverUrl: String) {
        this.serverUrl = serverUrl
    }

    private inner class ItemFilter(items: List<NetItem>?) : Filter() {
        private val finalItems: ArrayList<NetItem>

        init {
            finalItems = ArrayList<NetItem>()
            if (null != items) {
                finalItems.addAll(items)
            }
        }

        override fun performFiltering(constraint: CharSequence): Filter.FilterResults {
            val filterResults = Filter.FilterResults()
            val filterItems = ArrayList<NetItem>()
            for (i in finalItems.indices) {
                val entity = finalItems[i]
                if (!TextUtils.isEmpty(entity.info) && entity.info!!.contains(constraint) || !TextUtils.isEmpty(entity.url) && entity.url.contains(constraint)) {
                    filterItems.add(entity)
                }
            }
            filterResults.values = filterItems
            filterResults.count = filterItems.size
            return filterResults
        }

        override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
            items.clear()
            if (results.count > 0) {
                items.addAll(results.values as ArrayList<NetItem>)
            } else if (TextUtils.isEmpty(constraint)) {
                items.addAll(finalItems)
            }
            notifyDataSetChanged()
        }
    }

    class ViewHolder(itemView:View) {
        var actionView = itemView.findViewById(R.id.tv_name) as TextView
        var pathView =  itemView.findViewById(R.id.tv_path) as TextView
        var urlView = itemView.findViewById(R.id.tv_url) as TextView
    }
}
