package cz.developer.library.ui.data.database

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.*
import cz.developer.library.DeveloperManager
import cz.developer.library.R
import cz.developer.library.ui.data.adapter.DatabaseDetailAdapter
import cz.developer.library.ui.data.model.TableItem
import kotlinx.android.synthetic.main.fragment_datebase_detail.*

/**
 * Created by cz on 2017/9/29.
 */
internal class DatabaseDetailFragment : Fragment(){
    companion object {
        fun newInstance(databaseName:String,tableName:String)=DatabaseDetailFragment().apply {
            arguments=Bundle().apply {
                putString("name",databaseName)
                putString("table",tableName)
            }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_datebase_detail,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity=activity
        if(activity is AppCompatActivity){
            toolBar.title = arguments?.getString("title")
            setHasOptionsMenu(true)
            activity.setSupportActionBar(toolBar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolBar.setNavigationOnClickListener{ fragmentManager.popBackStack() }
        }

        val name=arguments.getString("name")
        val tableName=arguments.getString("table")
        if(null==name||null==tableName){
            AlertDialog.Builder(context).
                    setTitle(R.string.invalid_database).
                    setCancelable(false).
                    setNegativeButton(R.string.exit,
                            {_, _ -> fragmentManager.popBackStack() }).show()
        } else {
            //todo 此处改为子线程加载
            val tableItem = getTableItems(name, tableName)
            progressBar.visibility=View.GONE
            if(null!=tableItem){
                val adapter=DatabaseDetailAdapter(context,tableItem.columnNames,tableItem.values)
                tableView.adapter=adapter
                tableView.onItemClick { _, i ->
                    val array=adapter.getItem(i)
                    DeveloperManager.toDeveloperFragment(activity,
                            EditDatabaseFragment.newInstance(tableItem.columnNames,array))
                }
//                tableView.onItemLongClick { view, i ->
//                    AlertDialog.Builder(context).
//                            setTitle(R.string.delete_data).
//                            setPositiveButton(android.R.string.ok,{dialog, which ->  }).
//                            setNegativeButton(android.R.string.cancel,{dialog, which -> }).show()
//                    true
//                }
            }
        }
    }

    /**
     * 获得指定数据库内容
     * @param name
     * *
     * @param tableName
     * *
     * @return
     */
    fun getTableItems(name: String, tableName: String): TableItem? {
            //过程,先查询出总数,超出则以1000每次分批展示
            //                select count(*)from person"
            //            int tableCount = getTableCount(db, tableName);
            //            if (DB_MAX_COUNT < tableCount) {
            //                //超出,最多展示1000
            //                cursor = db.rawQuery("select * from " + tableName + " limit 0  offset " + DB_MAX_COUNT, null);
            //            } else {
//            val sql = "select count(*) from info"
        var tableItem:TableItem?=null
        val db = context.openOrCreateDatabase(name, Context.MODE_PRIVATE, null)
        var cursor = db.rawQuery("select * from $tableName", null)
        try{
            if(null!=cursor){
                tableItem=TableItem(tableName,cursor.columnNames)
                while (cursor.moveToNext()) {
                    tableItem.values.add((0..cursor.columnCount - 1).map { cursor.getString(it) }.toTypedArray())
                }
            }
        } finally {
            cursor?.close()
        }
        return tableItem
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        menu?.clear()
//        inflater.inflate(R.menu.menu_database,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if(R.id.action_add_db==item.itemId){
//            val fragment=AddDatabaseItemFragment.newInstance(arguments)
//            DeveloperManager.toDeveloperFragment(activity,fragment)
//        }
        return super.onOptionsItemSelected(item)
    }
}