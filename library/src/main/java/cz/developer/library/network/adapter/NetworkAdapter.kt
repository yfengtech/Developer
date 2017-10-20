package cz.developer.okhttp3.adapter

import cz.developer.library.network.model.NetItem
import okhttp3.OkHttpClient

/**
 * Created by cz on 11/8/16.
 */

class NetworkAdapter {
    //获取url集
    var serverUrl: Array<String>?=null
    //获取当前使用接口信息集
    var networkItems: List<NetItem>?=null
    //okhttp请求客户端
    var client:Array<OkHttpClient?>?=null
}
