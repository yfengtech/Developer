package cz.developer.library.ui.data.adapter

import android.content.Context
import android.view.ViewGroup
import com.cz.recyclerlibrary.adapter.BaseViewAdapter
import com.cz.recyclerlibrary.adapter.BaseViewHolder
import cz.developer.library.R
import cz.developer.library.ui.data.model.ExceptionItem
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by cz on 2017/9/13.
 */
class ExceptionAdapter(context: Context, items: List<ExceptionItem>?) : BaseViewAdapter<ExceptionItem>(context, items) {
    val formatter=SimpleDateFormat("yy-MM-dd HH:mm")
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        return BaseViewHolder(inflateView(parent, R.layout.exception_item))
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = getItem(position)
        holder.textView(R.id.tv_exception_name).text = item.name
        holder.textView(R.id.tv_exception_date).text = formatter.format(Date(item.lastModified))
        holder.textView(R.id.tv_exception_thread).text = item.threadName
        holder.textView(R.id.tv_exception_class).text = item.className
        holder.textView(R.id.tv_exception_method).text = "(#${item.methodName})"
        holder.textView(R.id.tv_exception_info).text = item.desc
    }

}