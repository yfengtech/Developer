package cz.developer.library.ui.filesystem.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.cz.recyclerlibrary.adapter.BaseViewAdapter
import com.cz.recyclerlibrary.adapter.BaseViewHolder
import cz.developer.library.R
import java.io.File

/**
 * Created by cz on 2017/10/19.
 */
class FileSystemAdapter(context: Context, items: List<File>?) : BaseViewAdapter<File>(context, items) {
    companion object {
        val FILE_TYPE=0
        val FOLDER_TYPE=1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when(viewType){
            FOLDER_TYPE-> BaseViewHolder(inflateView(parent, R.layout.filesystem_folder_item))
            else-> BaseViewHolder(inflateView(parent, R.layout.filesystem_file_item))
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val viewType=getItemViewType(position)
        val item=getItem(position)
        when(viewType){
            FILE_TYPE->{
                holder.textView(R.id.fileName).text=item.name
                holder.textView(R.id.filePath).text=item.absolutePath
            }
            FOLDER_TYPE->{
                holder.textView(R.id.folderName).text=item.name
                holder.textView(R.id.folderPath).text=item.absolutePath
                val childCount=item.listFiles()?.size?:0
                holder.view(R.id.imageRight).visibility=if(0<childCount) View.VISIBLE else View.INVISIBLE
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val file=getItem(position)
        return if(file.isDirectory) FOLDER_TYPE else FILE_TYPE
    }

}