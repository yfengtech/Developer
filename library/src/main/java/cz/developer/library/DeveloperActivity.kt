package cz.developer.library

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.Toast
import cz.developer.library.adapter.DebugListAdapter
import cz.developer.library.network.view.DebugRequestListFragment
import cz.developer.library.ui.appinfo.DebugAppInfoFragment
import cz.developer.library.ui.data.database.DatabaseFragment
import cz.developer.library.ui.data.exception.ExceptionListFragment
import cz.developer.library.ui.data.sp.SharedPrefsFragment
import cz.developer.library.ui.filesystem.CacheFileFragment
import cz.developer.library.ui.operation.DebugOperationFragment
import cz.developer.library.ui.switchs.DebugSwitchFragment
import cz.developer.library.ui.view.DebugViewFragment
import cz.developer.okhttp3.ui.DebugNetworkSettingFragment
import kotlinx.android.synthetic.main.activity_developer.*

class DeveloperActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developer)
        val id=intent.getIntExtra("id",0)
        toolBar.title = intent.getStringExtra("title")
        toolBar.subtitle=intent.getStringExtra("desc")
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolBar.setNavigationOnClickListener{ finish() }
        val adapter = DebugListAdapter(this, items[id])
        list.adapter=adapter
        list.setOnItemClickListener { _, _, position, _ ->
            val item = adapter.getItem(position)
            if(items.any { it.key==item.id }){
                startActivity(Intent(this,DeveloperActivity::class.java).apply {
                    putExtra("id",item.id)
                    putExtra("title",item.title)
                })
            } else {
                val clazz=item.clazz
                if (null!=clazz) {
                    val fragment = clazz.newInstance() as Fragment
                    fragment.arguments=Bundle().apply {
                        putString("title", item.title)
                        putString("desc", item.desc)
                    }
                    DeveloperManager.toDeveloperFragment(this, fragment)
                } else {
                    Toast.makeText(baseContext,"未配置跳转class",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        val items = mutableMapOf<Int, MutableList<DeveloperItem>>()
        fun item(closure:DeveloperItem.()->Unit){
            val newItem=DeveloperItem().apply(closure)
            val items= items.getOrPut(newItem.pid){ mutableListOf<DeveloperItem>() }
            items.add(newItem)
        }

        open class DeveloperItem(var id:Int?=null,
                                 var pid:Int=0,
                                 var title: String? = null,
                                 var desc: String? = null,
                                 var clazz: Class<out Fragment>? = null)
        init {
            item{
                id=1
                title="应用信息"
                desc="查看应用渠道,版本等"
                clazz= DebugAppInfoFragment::class.java
            }
            item{
                id=2
                title="调试信息"
                desc="开启应用调试模块开关"
                clazz= DebugSwitchFragment::class.java
            }
            item{
                id=3
                title="控件信息"
                desc="查看应用调试部分控件加载信息开关"
                clazz= DebugViewFragment::class.java
            }
            item{
                id=4
                title="网络调试"
                desc="修改单个,所有接口的服务器,或其他信息"
                clazz= DebugNetworkSettingFragment::class.java
            }
            item{
                id=5
                title="信息查看"
                desc="查看日志,配置信息等"
                item{
                    pid=5
                    title="崩溃日志查看"
                    desc="清空应用,本地操作"
                    clazz= ExceptionListFragment::class.java
                }
                item{
                    pid=5
                    title="网络请求缓存文件"
                    desc="查看当前几次打开的请求缓存文件,网络框架限定为:OKhttp3,且需要配置DebugIntercept"
                    clazz= DebugRequestListFragment::class.java
                }
                item{
                    pid=5
                    title="Cache文件目录查看"
                    desc="查看一些隐藏的应用目录"
                    clazz= CacheFileFragment::class.java
                }
                item{
                    pid=5
                    title="SharedPreference查看"
                    desc="查看编辑SharedPreference查看操作"
                    clazz= SharedPrefsFragment::class.java
                }
                item{
                    pid=5
                    title="Database查看"
                    desc="查看编辑数据库操作"
                    clazz= DatabaseFragment::class.java
                }
            }
            item{
                id=6
                title="其他操作"
                desc="清空应用缓存,日志,异常,等信息"
                clazz= DebugOperationFragment::class.java
            }
        }
    }
}
