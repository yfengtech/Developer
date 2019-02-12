package cz.developer.library.ui.view.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cz.recyclerlibrary.adapter.BaseViewAdapter
import com.cz.recyclerlibrary.adapter.BaseViewHolder
import com.cz.recyclerlibrary.callback.StickyCallback
import com.cz.recyclerlibrary.strategy.GroupingStrategy
import cz.developer.library.R
import cz.developer.library.ui.view.model.ViewAttribute

/**
 * Created by cz on 2017/9/8.
 */
class ViewAttributeAdapter(context: Context, items: List<ViewAttribute.Item>?) : BaseViewAdapter<ViewAttribute.Item>(context, items),StickyCallback<ViewAttribute.Item> {
    val strategy=GroupingStrategy.of(this).reduce { item1,item2-> item1.group!=item2.group }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =BaseViewHolder(inflateView(parent, R.layout.view_attribute_item))

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item=getItem(position)
        holder.view(R.id.stickyHeader).visibility=if(strategy.isGroupIndex(position)) View.VISIBLE else View.GONE
        holder.textView(R.id.stickyHeader).text=item.group
        holder.textView(R.id.attributeKey).text=item.name
        holder.textView(R.id.attributeValue).text=item.value?.toString()
    }

    override fun getGroupingStrategy(): GroupingStrategy<ViewAttribute.Item>{
        return strategy
    }

    override fun initStickyView(view: View, position: Int) {
        val item=getItem(position)
        (view as TextView).text=item.group
    }

}