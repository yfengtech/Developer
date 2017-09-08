package cz.developer.sample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk25.coroutines.onLongClick
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {
    val TAG="MainActivity"
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
