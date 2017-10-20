package cz.developer.library.ui.data.adapter

import android.content.Context
import android.view.ViewGroup
import com.cz.recyclerlibrary.adapter.BaseViewAdapter
import com.cz.recyclerlibrary.adapter.BaseViewHolder
import cz.developer.library.R
import cz.developer.library.ui.data.model.SharedPrefsFileItem
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by cz on 2017/9/12.
 */
class SharedPrefsFileAdapter(val context: Context, items: List<SharedPrefsFileItem>?) : BaseViewAdapter<SharedPrefsFileItem>(context, items) {
    companion object {
        val formatter=SimpleDateFormat("MM/dd HH:mm")
    }
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        return BaseViewHolder(inflateView(parent, R.layout.shared_prefs_item))

    }
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = getItem(position)
        holder.textView(android.R.id.text1).text=item.name
        holder.textView(R.id.lastModify).text=context.getString(R.string.last_modify_value,formatter.format(Date(item.lastModifier)))
    }


}