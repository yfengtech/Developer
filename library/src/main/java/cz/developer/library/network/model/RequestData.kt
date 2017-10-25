package cz.developer.library.network.model

/**
 * Created by cz on 2017/10/24.
 * 请求条目数据
 */
class RequestData {
    val request=Request()
    val response=Response()

    class Request{
        var url:String?=null
        var info:String?=null
        var interceptUrl:String?=null
        var method:String?=null
        var isHttps:Boolean=false
        var tag:String?=null
        var contentType:String?=null
        var contentLength=0
        var params= mutableMapOf<String,String>()
        var headers= mutableMapOf<String,String>()

        override fun toString(): String {
            val out=StringBuilder()
            out.append("Url:$url\n")
            out.append("Intercept:$interceptUrl\n")
            out.append("Info:$info\n")
            out.append("Method:$method\n")
            out.append("IsHttps:$isHttps\n")
            out.append("ContentType:$contentType\n")
            out.append("ContentLength:$contentLength\n")
            out.append("Params:${params.entries.joinToString("\n"){"${it.key}=${it.value}"}}")
            out.append("Headers:${headers.entries.joinToString("\n"){"${it.key}=${it.value}"}}")
            return out.toString()
        }
    }

    class Response{
        var code=0
        var isOK=false
        var time=0
        var protocol:String?=null
        var headers= mutableMapOf<String,String>()
        var result:String?=null
        var contentType:String?=null
        var contentLength=0
        override fun toString(): String {
            val out=StringBuilder()
            out.append("Code:$code\n")
            out.append("OK:$isOK\n")
            out.append("Time:$time\n")
            out.append("Protocol:$protocol\n")
            out.append("ContentType:$contentType\n")
            out.append("ContentLength:$contentLength\n")
            out.append("Headers:${headers.entries.joinToString("\n"){"${it.key}=${it.value}"}}")
            return out.toString()
        }
    }
}