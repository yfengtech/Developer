package cz.developer.okhttp3.intercept

import cz.developer.library.log.FilePrefs
import cz.developer.okhttp3.adapter.NetworkAdapter
import okhttp3.*
import java.util.regex.Pattern

/**
 * Created by cz on 2017/10/20.
 */
class DebugIntercept(adapter: NetworkAdapter?) : Interceptor {
    //默认服务器地址
    val serverUrl=adapter?.serverUrl?.getOrNull(0)
    //当前所有接口对象
    val items=adapter?.networkItems

    override fun intercept(chain: Interceptor.Chain): Response {
        var request=chain.request()
        var url=request.url().toString()
        val findItem=items?.find { url.contains(it.url,true) }
        val out=StringBuilder()
        out.append("Url:$url\n")
        if(null==serverUrl||null==findItem){
            out.append("Intercept:$serverUrl $findItem\n")
        } else {
            //拦截请求
            var index=url.indexOf("/")
            if(serverUrl.endsWith("/")) index--
            url=serverUrl+url.substring(index)
            out.append("Intercept:$url\n")
            //替换url后重新请求
            request=request.newBuilder().url(url).build()
        }
        //收集信息
        val method=request.method().toUpperCase()
        out.append("Method:$method\n")
        out.append("Https:${request.isHttps}\n")
        out.append("Tag:${request.tag()}\n")
        if("GET"==method&&url.contains("?")){
            val start=url.indexOf("?")+1
            val params=getRequestParams(url.substring(start))
            out.append("Params:${params.size}\n")
            params.forEach {
                out.append("${it.key}: ${it.value}\n")
            }
        }
        //header
        appendHeaders(out, request.headers())
        var requestBody=request.body()
        request.body()
        if(null!=requestBody){
            if(requestBody is FormBody){
                appendFormBody(out, requestBody)
            }
            out.append("Content-Type: ${requestBody.contentType()}\n")
            out.append("Content-Length: ${requestBody.contentLength()}\n")
        }
        //请求参数
        out.append("=================================================\n")
        out.append("=================================================\n")
        val requestFile=FilePrefs.newRequestFile()
        FilePrefs.log(requestFile,out.toString())
        out.delete(0,out.length)

        //请求成功
        val response = chain.proceed(request)
        //记录
        out.append("Code:${response.code()}")
        out.append("OK:${response.isSuccessful}")
        out.append("Protocol:${response.protocol()}")
        appendHeaders(out,response.headers())
        //记录结果
        val body=response.body()
        if(null!=body){
            out.append("Content-Type: ${body.contentType()}\n")
            out.append("Content-Length: ${body.contentLength()}\n")
            out.append("\n")
            out.append("${body.string()}")
        }
        //记录响应结果
        FilePrefs.log(requestFile,out.toString())
        return response
    }

    /**
     * 添加Headers信息
     */
    private fun appendHeaders(out: StringBuilder, headers: Headers) {
        out.append("Headers:${headers.size()}\n")
        for (index in (0..headers.size() - 1)) {
            out.append("${headers.name(index)}: ${headers.value(index)}\n")
        }
    }

    /**
     * 添加表单对象体
     */
    private fun appendFormBody(out: StringBuilder, requestBody: FormBody) {
        out.append("Form:${requestBody.size()}\n")
        for (index in (0..requestBody.size() - 1)) {
            out.append("${requestBody.name(index)}: ${requestBody.value(index)}\n")
        }
    }

    /**
     * 获取schema配置参数集
     */
    private fun getRequestParams(params: String):MutableMap<String,String> {
        val items= mutableMapOf<String,String>()
        val paramPattern = Pattern.compile("(\\w+)=([^=&]+)&?")
        val matcher1 = paramPattern.matcher(params)
        while (matcher1.find()) {
            try {
                val key = matcher1.group(1)
                val value = matcher1.group(2)
                items.put(key, value)
            } catch (e: Exception) { }
        }
        //自然排序
        return items.toSortedMap()
    }


}