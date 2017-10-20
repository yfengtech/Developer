package cz.developer.library

import cz.developer.okhttp3.adapter.NetworkAdapter
import cz.developer.okhttp3.intercept.DebugIntercept

/**
 * Created by cz on 2016/11/7.
 */

class DeveloperConfig {
    //配置渠道
    var channel: String?=null
    //切换控制条目
    internal var switchItem:SwitchItem?=null
    internal var network: NetworkAdapter?=null
    //网络配置
    fun network(action:NetworkAdapter.()->Unit){
        network =NetworkAdapter().apply(action)
        //添加网络拦截器
        network?.client?.forEach {
            it?.interceptors()?.add(DebugIntercept(network))
        }
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
