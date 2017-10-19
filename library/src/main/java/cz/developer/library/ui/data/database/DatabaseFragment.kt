package cz.developer.library.ui.data.database

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cz.recyclerlibrary.onExpandItemClick
import cz.developer.library.DeveloperManager
import cz.developer.library.R
import cz.developer.library.ui.data.adapter.DatabaseAdapter
import kotlinx.android.synthetic.main.fragment_basebase.*
import java.io.File

/**
 * Created by cz on 2017/9/29.
 */
internal class DatabaseFragment: Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_basebase,container,false)
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
        val items = getDatabaseItems()
        if(0==items.size){
            AlertDialog.Builder(context).
                    setTitle(R.string.no_database).
                    setCancelable(false).
                    setNegativeButton(R.string.exit,
                            {_, _ -> fragmentManager.popBackStack() }).show()
        } else {
            databaseRecyclerView.layoutManager=LinearLayoutManager(context)
            val adapter = DatabaseAdapter(context, items, true)
            databaseRecyclerView.adapter=adapter
            databaseRecyclerView.onExpandItemClick { _, groupPosition, position ->
                val name=adapter.getGroup(groupPosition)
                val tableName = adapter.getChild(groupPosition, position)
                DeveloperManager.toDeveloperFragment(activity,DatabaseDetailFragment.newInstance(name,tableName))
            }
        }
    }

    /**
     * 获得数据库表名
     */
    fun getDatabaseItems(): java.util.LinkedHashMap<String,List<String>> {
        val tables=java.util.LinkedHashMap<String,List<String>>()
        val databaseFile = File("/data/data/${context.packageName}/databases")
        if (databaseFile.exists()) {
            val files = databaseFile.listFiles()
            if (null != files) {
                for (i in files.indices) {
                    val file = files[i]
                    val name = file.name
                    //表异常信息数据
                    if (!name.endsWith("-journal")) {
                        val db = context.openOrCreateDatabase(name, Context.MODE_PRIVATE, null)
                        val cursor = db.rawQuery("select name from sqlite_master where type='table' order by name", null)
                        while (null != cursor&&cursor.moveToNext()) {
                            //遍历出表名
                            val items=tables.getOrPut(name){ ArrayList<String>()}
                            if(items is ArrayList<String>){
                                items.add(cursor.getString(0))
                            }
                        }
                        cursor?.close()
                    }
                }
            }
        }
        return tables
    }

}