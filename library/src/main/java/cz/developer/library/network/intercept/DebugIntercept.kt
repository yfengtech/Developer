package cz.developer.okhttp3.intercept

import android.text.TextUtils
import cz.developer.library.DeveloperManager
import cz.developer.library.log.FilePrefs
import cz.developer.library.prefs.DeveloperPrefs
import cz.developer.okhttp3.adapter.NetworkAdapter
import okhttp3.*
import okio.Buffer
import okio.BufferedSink
import java.util.regex.Pattern

/**
 * Created by cz on 2017/10/20.
 */
class DebugIntercept : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val config=DeveloperManager.developerConfig
        //默认服务器地址
        val serverArray=config.network?.serverUrl
        //当前所有接口对象
        val items=config.network?.networkItems
        var request=chain.request()
        var url=request.url().toString()
        //当请求接口不在配置名单内,不进行拦截
        val out=StringBuilder()
        val findItem=items?.find { url.contains(it.url,true) }
        if(serverArray?.any { url.startsWith(it) }?:false){
            var serverUrl:String?=if(!TextUtils.isEmpty(DeveloperPrefs.url))
                DeveloperPrefs.url else serverArray?.getOrNull(0)
            out.append("Url=$url\n")
            if(null==serverUrl){
                out.append("Intercept=$serverUrl $findItem\n")
            } else {
                //单独修改
                if(null!=findItem){
                    serverUrl=DeveloperPrefs.getString(findItem.url.hashCode())?:serverUrl
                    out.append("Info=${findItem.info}\n")
                }
                //拦截请求
                url=url.replace("https?://[^/]+/".toRegex(),serverUrl)
                out.append("Intercept=$url\n")
                //替换url后重新请求
                request=request.newBuilder().url(url).build()
            }
        }
        //收集信息
        val method=request.method().toUpperCase()
        out.append("Method=$method\n")
        out.append("Https=${request.isHttps}\n")
        out.append("Tag=${request.tag()}\n")
        if("GET"==method&&url.contains("?")){
            val start=url.indexOf("?")+1
            val params=getRequestParams(url.substring(start))
            out.append("Params=")
            out.append(params.entries.joinToString("&"){ "${it.key}:${if(!TextUtils.isEmpty(it.value)) it.value else "null"}" }+"\n")
        }
        //header
        appendHeaders(out, request.headers())
        var requestBody=request.body()
        request.body()
        if(null!=requestBody){
            if(requestBody is FormBody){
                appendFormBody(out, requestBody)
            } else if(requestBody is MultipartBody){
//                val buffer=Buffer()
//                requestBody.writeTo(buffer)
//                val string=buffer.readUtf8()
//                out.append("Content-Type: ${requestBody.contentType()}\n")
            }
            out.append("Content-Type=${requestBody.contentType()}\n")
            out.append("Content-Length=${requestBody.contentLength()}\n")
        }
        //请求参数
        out.append("=====================================\n")
        val requestFile=FilePrefs.newRequestFile(findItem?.info)
        FilePrefs.log(requestFile,out.toString())
        out.delete(0,out.length)

        //请求成功
        val response = chain.proceed(request)
        //记录
        out.append("Code=${response.code()}\n")
        out.append("OK=${response.isSuccessful}\n")
        out.append("Protocol=${response.protocol()}\n")
        out.append("Time=${response.receivedResponseAtMillis()-response.sentRequestAtMillis()}\n")
        appendHeaders(out,response.headers())
        //记录结果
        val body=response.peekBody(response.body().contentLength())
        if(null!=body){
            out.append("Content-Type=${body.contentType()}\n")
            out.append("Content-Length=${body.contentLength()}\n")
            out.append("Result=${body.string()}")
        }
        //记录响应结果
        FilePrefs.log(requestFile,out.toString())
        return response
    }

    /**
     * 添加Headers信息
     */
    private fun appendHeaders(out: StringBuilder, headers: Headers) {
        out.append("Headers=")
        val headerItems= mutableMapOf<String,String?>()
        for (index in (0..headers.size() - 1)) {
            headerItems.put(headers.name(index),headers.value(index))
        }
        out.append(headerItems.entries.joinToString("&"){ "${it.key}:${if(!TextUtils.isEmpty(it.value)) it.value else "null"}" }+"\n")
    }

    /**
     * 添加表单对象体
     */
    private fun appendFormBody(out: StringBuilder, requestBody: FormBody) {
        out.append("Form=")
        val formItems= mutableMapOf<String,String?>()
        for (index in (0..requestBody.size() - 1)) {
            formItems.put(requestBody.name(index),requestBody.value(index))
        }
        out.append(formItems.entries.joinToString("&"){ "${it.key}:${if(!TextUtils.isEmpty(it.value)) it.value else "null"}" }+"\n")
    }

    /**
     * 获取schema配置参数集
     */
    private fun getRequestParams(params: String):MutableMap<String,String?> {
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