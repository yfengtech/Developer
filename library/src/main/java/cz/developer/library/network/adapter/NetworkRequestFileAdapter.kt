package cz.developer.library.network.adapter

import android.content.Context
import android.support.v7.app.AlertDialog
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import com.cz.recyclerlibrary.adapter.BaseViewAdapter
import com.cz.recyclerlibrary.adapter.BaseViewHolder
import com.cz.recyclerlibrary.adapter.expand.ExpandAdapter
import cz.developer.library.R
import java.io.File
import java.util.LinkedHashMap

/**
 * Created by cz on 2017/10/24.
 */
class NetworkRequestFileAdapter(val context: Context, items: LinkedHashMap<File, List<File>>, expand: Boolean) : ExpandAdapter<File, File>(context, items, expand) {
    private val PADDING: Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, context.resources.displayMetrics).toInt()
    override fun createChildHolder(parent: ViewGroup): BaseViewHolder {
        return BaseViewHolder(inflateView(parent, R.layout.filesystem_file_item))
    }

    override fun createGroupHolder(parent: ViewGroup): BaseViewHolder {
        return BaseViewHolder(inflateView(parent, R.layout.filesystem_folder_item))
    }

    override fun onBindChildHolder(holder: BaseViewHolder, groupPosition: Int, position: Int) {
        val itemView=holder.itemView
        itemView.setPadding(PADDING, itemView.paddingTop, itemView.paddingRight, itemView.paddingBottom)
        val item=getChild(groupPosition,position)
        holder.textView(R.id.fileName).text=item.name
        holder.textView(R.id.filePath).text=item.absolutePath
    }

    override fun onBindGroupHolder(holder: BaseViewHolder, groupPosition: Int) {
        val item=getGroup(groupPosition)
        holder.textView(R.id.folderName).text=item.name
        holder.textView(R.id.folderPath).text=item.absolutePath
        val childCount=item.listFiles()?.size?:0
        holder.view(R.id.imageRight).visibility=if(0<childCount) View.VISIBLE else View.INVISIBLE
    }


}