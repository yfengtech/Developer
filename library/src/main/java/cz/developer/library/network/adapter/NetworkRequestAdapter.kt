package cz.developer.library.network.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.cz.recyclerlibrary.adapter.BaseViewHolder
import com.cz.recyclerlibrary.adapter.expand.ExpandAdapter
import cz.developer.library.R
import java.util.LinkedHashMap

/**
 * Created by cz on 2017/10/25.
 */
class NetworkRequestAdapter(context: Context, items: LinkedHashMap<String, List<String?>>, expand: Boolean) : ExpandAdapter<String, String?>(context, items, expand) {

    override fun createChildHolder(parent: ViewGroup): BaseViewHolder {
        return BaseViewHolder(inflateView(parent, R.layout.request_text_item))
    }

    override fun createGroupHolder(parent: ViewGroup): BaseViewHolder {
        return BaseViewHolder(inflateView(parent, R.layout.request_header_item))
    }

    override fun onBindChildHolder(holder: BaseViewHolder, groupPosition: Int, position: Int) {
        (holder.itemView as TextView).text=getChild(groupPosition,position)
    }

    override fun onBindGroupHolder(holder: BaseViewHolder, groupPosition: Int) {
        (holder.itemView as TextView).text=getGroup(groupPosition)
    }
}