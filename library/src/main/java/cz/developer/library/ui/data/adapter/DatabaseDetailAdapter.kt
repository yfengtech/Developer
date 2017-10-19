package cz.developer.library.ui.data.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.cz.recyclerlibrary.layoutmanager.table.TableAdapter
import com.cz.recyclerlibrary.layoutmanager.table.TableColumnLayout
import cz.developer.library.R

/**
 * Created by cz on 2017/9/29.
 */
class DatabaseDetailAdapter(context: Context, val headerItems:Array<String>,items: List<Array<String>>?) : TableAdapter<Array<String>>(context, items) {

    override fun getColumnCount(): Int =headerItems.size

    override fun getItemView(parent: TableColumnLayout, row: Int, column: Int): View =inflateView(parent, R.layout.table_field_item)

    override fun onBindItemView(parent: TableColumnLayout, view: View, row: Int, column: Int) {
        val array=getItem(row)
        val textView=view as TextView
        textView.text=array[column]
    }

    override fun getHeaderItemView(headerLayout: TableColumnLayout, index: Int): View =inflateView(headerLayout,R.layout.table_header_item)

    override fun onBindHeaderItemView(headerLayout: TableColumnLayout, view: View, column: Int) {
        val textView=view.findViewById(R.id.header) as TextView
        textView.text=headerItems[column]
    }
}