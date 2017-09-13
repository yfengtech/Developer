package cz.developer.library.ui.data.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.ViewGroup

import com.cz.recyclerlibrary.adapter.BaseViewAdapter
import com.cz.recyclerlibrary.adapter.BaseViewHolder

import cz.developer.library.R
import cz.developer.library.ui.data.model.PrefsType
import cz.developer.library.ui.data.model.SharedPrefsItem


/**
 * Created by cz on 16/1/23.
 * SharedPrefs展示列
 */
class SharedPrefsAdapter(context: Context, items: List<SharedPrefsItem>?) : BaseViewAdapter<SharedPrefsItem>(context, items) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        var holder: BaseViewHolder
        when (viewType) {
            SET_ITEM -> holder = BaseViewHolder(inflateView(parent, R.layout.shared_prefs_set_item))
            else -> holder = BaseViewHolder(inflateView(parent, R.layout.shared_prefs))
        }
        return holder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val value: String
        val itemViewType = getItemViewType(position)
        val item = getItem(position)
        when (itemViewType) {
            PREFS_ITEM -> {
                if (PrefsType.STRING == item.type) {
                    value = "<" + item.type + " name='" + item.name + "'>" + item.value + "</" + item.type + ">"
                } else {
                    value = "<" + item.type + " name='" + item.name + "' value='" + item.value + "'/>"
                }
                holder.textView(R.id.fieldName).text = value
            }
            SET_ITEM -> {
                value = "<" + item.type + " name='" + item.name + "'>"
                holder.textView(R.id.fieldName).text = value
                val itemValue=item.value as Set<String>
                holder.textView(R.id.fieldValue).text=itemValue?.joinToString("\n"){ "<string>$it</string>" }
                holder.textView(R.id.fieldEndTag).text = "</set>"
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        var viewType = PREFS_ITEM
        if (PrefsType.SET == item.type) {
            viewType = SET_ITEM
        }
        return viewType
    }

    companion object {
        private val PREFS_ITEM = 0
        private val SET_ITEM = 1
    }

}
