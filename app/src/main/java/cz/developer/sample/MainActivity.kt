package cz.developer.sample

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.cz.demo.database.Database1
import com.cz.demo.database.Database2
import cz.developer.library.prefs.DeveloperPrefs
import cz.developer.library.widget.DeveloperLayout
import cz.developer.sample.network.NetPrefs
import cz.netlibrary.request

import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk25.coroutines.onLongClick
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layout.setOnClickListener { Toast.makeText(this, "Click Layout!", Toast.LENGTH_SHORT).show() }
        layout.setOnLongClickListener{
            toast("Long Click Layout!")
            true
        }
        val person=Person()
        person.name="Tom"
        val tomChild= Child()
        tomChild.name="baby"
        tomChild.age=2
        tomChild.parent=person
        (1..10).forEach { tomChild.attr.put("key:$it","value:$it") }
        person.child.add(tomChild)

        for(i in 0..2){
            val child= Child()
            child.name="baby:$i"
            child.age=i
            tomChild.friends.add(child)
        }

        image.tag=person
        image.setTag(R.id.action0,"2")
        image.setTag(R.id.action_bar,tomChild)
        image.setTag(R.id.action_bar_root,tomChild)
        image.setTag(R.id.action_bar_spinner,"Hello world!")
        image.onLongClick { toast("图片长按点击") }

        btn1.setOnClickListener { startActivity(Intent(this, ListActivity::class.java)) }
        btn2.setOnClickListener { startActivity(Intent(this, RecyclerListActivity::class.java)) }
        btn3.setOnClickListener { startActivity(Intent(this, WebViewActivity::class.java)) }
        btn4.setOnClickListener { supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ListFragment()).commit() }
        btn5.setOnClickListener { startActivity(Intent(this, PrivacyLockActivity::class.java)) }


        //插入初始测试数据
        val sharedPrefs=getSharedPreferences("test",Context.MODE_PRIVATE)
        val init=sharedPrefs.getBoolean("init",false)
        if(!init){
            //打开控件测试开关
            DeveloperPrefs.debugView =true
            val layout=findViewById(cz.developer.library.R.id.developerContainer)
            if(null!=layout&&layout is DeveloperLayout){
                layout.setViewDebug(true)
            }
            val dialog=ProgressDialog(this)
            dialog.setMessage("正在初始测试数据...")
            dialog.setCancelable(false)
            dialog.show()
            doAsync {
                //插入5个SharedPrefs文件
                insertSharedPrefsItems(5)
                //为2个数据库表,插入数据
                insertDatabase(Database1(this@MainActivity).writableDatabase,1000)
                insertDatabase(Database2(this@MainActivity).writableDatabase,1000)
                sharedPrefs.edit().putBoolean("init",true).commit()
                uiThread { dialog.dismiss() }
            }
        }

        //横拟请求
        request<String>(NetPrefs.WHITE_CREDIT_TEMPLATE_LIST) {
            params= arrayOf(1,10)
            map{it}
            success {

            }
            failed {

            }
        }
    }

    /**
     * 插入测试的SharedPrefs条目
     */
    fun insertSharedPrefsItems(count:Int){
        //插入SharedPrefs
        for(index in 0..count-1){
            val preferences = getSharedPreferences("test" + (index+1), Context.MODE_PRIVATE)
            val edit = preferences.edit()
            for (i in 0..9) {
                edit.putString("KI" + i, "Value" + i).commit()
            }
            edit.putBoolean("BOOLEAN", true).commit()
            edit.putFloat("Float", 1f).commit()
            edit.putInt("Integer", 250).commit()
            edit.putLong("Long", 2134324L).commit()
            val items = HashSet<String>()
            items.add("a")
            items.add("b")
            items.add("c")
            edit.putStringSet("items", items).commit()
        }
    }

    fun insertDatabase(db: SQLiteDatabase, count:Int){
        db.beginTransaction()
        for(i in 0..count){
            val items=(0..9).map { DataProvider.randomName() }.toTypedArray()
            for(k in 1..5){
                //插入数据
                db.execSQL("insert into person$k(name1,name2,name3,name4,name5,name6,name7,name8,name9,name10,age) values(?,?,?,?,?,?,?,?,?,?,?)", arrayOf(*items, 20))
            }
        }
        db.setTransactionSuccessful()
        db.endTransaction()
        //打印插入个数
        for(k in 1..5){
            //插入数据
            var cursor: Cursor?=null
            try{
                cursor=db.rawQuery("select count(*) from person$k", null)
                cursor.moveToFirst()
                val count = cursor.getLong(0)
                Log.e(this::class.java.simpleName,"插入表:person$k 个数:$count")
            } catch (e:Exception){
                e.printStackTrace()
            } finally {
                cursor?.close()
            }
        }
        db.close()
    }


    //测试java数据
    class Person{
        var name:String?=null
        var child= arrayListOf<Child>()
        var age=0
    }

    class Child{
        var name:String?=null
        var attr= mutableMapOf<String,String>()
        var friends= arrayListOf<Child>()
        var parent:Person?=null
        var age=0
        var sex=1
    }

}
