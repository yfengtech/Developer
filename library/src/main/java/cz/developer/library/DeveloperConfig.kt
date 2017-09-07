package cz.developer.library

import android.widget.ImageView
import cz.developer.library.ui.network.NetworkAdapter

/**
 * Created by cz on 2016/11/7.
 */

class DeveloperConfig {
    //配置渠道
    var channel: String?=null
    //切换控制条目
    internal var switchItem:SwitchItem?=null
    internal var networkAdapter:NetworkAdapter?=null
    //图片加载
    internal var imageLoader:((ImageView, String)->Unit)?=null
    //网络配置
    fun network(action:NetworkAdapter.()->Unit){
        networkAdapter=NetworkAdapter().apply(action)
    }
    //图片加载
    fun imageLoader(loader:(ImageView, String)->Unit){
        imageLoader=loader
    }
    //状态切换配置
    fun switch(config:SwitchItem.()->Unit){
        switchItem=SwitchItem().apply(config)
    }

    class SwitchItem {
        internal val items= mutableListOf<Item>()
        //条目是否选中
        internal var itemChecked:((String,Boolean)->Unit)?=null
        //获取附加集
        fun item(key:String?=null,desc:String?=null){
            items.add(Item(key,desc))
        }

        fun onItemChecked(action:(String,Boolean)->Unit){
            this.itemChecked=action
        }

        class Item(var key:String?=null, var desc:String?=null)


    }

}
