package cz.developer.library.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

import cz.developer.library.DeveloperManager
import cz.developer.library.R
import cz.developer.library.adapter.DebugListAdapter
import cz.developer.library.ui.appinfo.DebugAppInfoFragment
import cz.developer.library.ui.switchs.DebugSwitchFragment
import cz.developer.library.ui.view.DebugViewFragment
import kotlinx.android.synthetic.main.activity_developer.*

/**
 *  触发控件调试信息
 *  设计一个庞大的 各种控件信息查看模式
 *  <!--开发者模式的边距效果,以及具体大小指示-->
<!--当前界面的层级,以及控件树展示-->
<!--当前cpu/内存消耗时时展示状态-->

select开关界面

控件信息查看,大小,属性
textView
ImageView
Button
控件大小,属性调试
界面的层级,

界面级操作:
想查看整个界面层级
查看整个界面控件边距.

控件级的操作
控件信息查看,大小,属性
textView
ImageView
Button
 */
class DeveloperActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_AppBarOverlay)
        setContentView(R.layout.activity_developer)
        val id=intent.getIntExtra("id",0)
        val title = intent.getStringExtra("title")
        if(null==title) {
            toolBar.setTitle(R.string.app_name)
            setSupportActionBar(toolBar)
        } else {
            toolBar.title = title
            toolBar.subtitle=intent.getStringExtra("desc")
            setSupportActionBar(toolBar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolBar.setNavigationOnClickListener{ finish() }
        }
        val adapter = DebugListAdapter(this, items[id])
        list.adapter=adapter
        list.setOnItemClickListener { _, _, position, id ->
            val item = adapter.getItem(position)
            val clazz=item.clazz
            if (null!=clazz) {
                val fragment = clazz.newInstance() as Fragment
                fragment.arguments=Bundle().apply {
                    putString("title", item.title)
                    putString("desc", item.desc)
                }
                DeveloperManager.toDeveloperFragment(this, fragment)
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

        open class DeveloperItem(var id:Int=0,
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
            }
            item{
                id=5
                title="信息查看"
                desc="查看日志,配置信息等"
                item{
                    pid=5
                    title="日志信息查看"
                    desc="清空应用,本地操作"
                }
                item{
                    pid=5
                    title="网络请求日志查看"
                    desc="清空应用,本地操作"
                }
                item{
                    pid=5
                    title="崩溃日志查看"
                    desc="清空应用,本地操作"
                }
                item{
                    pid=5
                    title="SharedPreference查看"
                    desc="查看编辑SharedPreference查看操作"
                }
                item{
                    pid=5
                    title="Database查看"
                    desc="查看编辑数据库操作"
                }
            }
            item{
                id=6
                title="其他操作"
                desc="清空应用缓存,日志,异常,等信息"
            }
        }
    }
}
