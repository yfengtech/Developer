package cz.developer.library.ui.filesystem.adapter

import android.content.Context
import android.view.ViewGroup
import com.cz.recyclerlibrary.adapter.BaseViewAdapter
import com.cz.recyclerlibrary.adapter.BaseViewHolder
import cz.developer.library.R
import cz.developer.library.ui.filesystem.model.FileItem

/**
 * Created by cz on 2017/10/19.
 */
class CacheFileAdapter(context: Context, items: List<FileItem>?) : BaseViewAdapter<FileItem>(context, items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(inflateView(parent, R.layout.cache_file_item))
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item=getItem(position)
        holder.textView(R.id.fileText).text=item.file.name
        holder.textView(R.id.filePathText).text=item.file.absolutePath
        holder.textView(R.id.titleText).text=item.title
        holder.textView(R.id.descText).text=item.desc

    }
}