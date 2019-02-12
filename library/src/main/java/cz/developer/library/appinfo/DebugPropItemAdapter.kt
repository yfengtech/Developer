package cz.developer.library.appinfo

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cz.recyclerlibrary.adapter.BaseViewAdapter
import com.cz.recyclerlibrary.adapter.BaseViewHolder
import com.cz.recyclerlibrary.callback.StickyCallback
import com.cz.recyclerlibrary.strategy.GroupingStrategy
import cz.developer.library.R
import cz.developer.library.ui.appinfo.PropItem

/**
 * Created by cz on 2017/9/13.
 */
class DebugPropItemAdapter(context: Context, items: List<PropItem>?) : BaseViewAdapter<PropItem>(context, items),StickyCallback<PropItem>{
    val strategy=GroupingStrategy.of(this).reduce { p1, p2 -> p1.group!=p2.group }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(inflateView(parent,R.layout.debug_prop_item))
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item=getItem(position)
        val header=holder.textView(R.id.tv_header)
        header.visibility=if(strategy.isGroupIndex(position)) View.VISIBLE else View.GONE
        header.text=item.group
        holder.textView(R.id.tv_text).text=item.value
    }

    override fun getGroupingStrategy()=strategy

    override fun initStickyView(view: View, position: Int) {
        val headerView=view.findViewById(R.id.tv_header) as TextView
        val item=getItem(position)
        headerView.text=item.group
    }

}