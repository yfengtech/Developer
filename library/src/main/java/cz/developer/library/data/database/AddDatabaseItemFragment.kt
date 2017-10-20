package cz.developer.library.ui.data.database

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import cz.developer.library.R
import cz.developer.library.ui.data.sp.AddSharedPrefsFragment
import kotlinx.android.synthetic.main.fragment_add_basebase.*

/**
 * Created by cz on 2017/9/29.
 */
internal class AddDatabaseItemFragment: Fragment(){
    companion object {
        fun newInstance(args:Bundle)= AddDatabaseItemFragment().apply { arguments=args }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_basebase,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity=activity
        if(activity is AppCompatActivity){
            toolBar.setTitle(R.string.add_database)
            setHasOptionsMenu(true)
            activity.setSupportActionBar(toolBar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolBar.setNavigationOnClickListener{ fragmentManager.popBackStack() }
        }

        val name=arguments.getString("name")
        val tableName=arguments.getString("table")
        getTableItems(name,tableName)

    }


    /**
     * 获得指定数据库内容
     * @param name
     * *
     * @param tableName
     * *
     * @return
     */
    fun getTableItems(name: String, tableName: String) {
        val db = context.openOrCreateDatabase(name, Context.MODE_PRIVATE, null)
        val cursor=db.rawQuery("select sql from sqlite_master where tbl_name = '$tableName' and type='table'",null)
        try{
            //这里获取建表sql,再通过正则解析,获得所有建表约束信息
            if(null!=cursor&&cursor.moveToFirst()){
                val value=cursor.getString(0)
                //constraint id_fk foreign key (id) references student (id)
                //CREATE TABLE person2(personid integer primary key autoincrement,
                // name1 varchar(20),
                // name2 varchar(20),
                // name3 varchar(20),
                // name4 varchar(20),


                //uid integer foren key user.id ,
                // name5 varchar(20),
                // name6 varchar(20),
                // name7 varchar(20),
                // name8 varchar(20),
                // name9 varchar(20),
                // name10 varchar(20),
                // age INTEGER)
                //float/integer default ()/boolean default false/true,char/text/blob

            }
        } finally {
            cursor?.close()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()//清空之前面menu菜单
        super.onCreateOptionsMenu(menu, inflater)
    }
}