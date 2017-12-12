package cz.developer.okhttp3.intercept

import android.content.Context
import android.text.TextUtils
import android.util.Log
import cz.developer.library.DeveloperManager
import cz.developer.library.log.FilePrefs
import cz.developer.library.prefs.DeveloperPrefs
import cz.developer.okhttp3.adapter.NetworkAdapter
import okhttp3.*
import okio.Buffer
import okio.BufferedSink
import java.io.IOException
import java.util.regex.Pattern

/**
 * Created by cz on 2017/10/20.
 */
class DebugIntercept(private val context: Context) : Interceptor {

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
        //添加请求原url信息
        out.append("Url=$url\n")
        //添加请求info信息
        if(null!=findItem){
            out.append("Info=${findItem.info}\n")
        }
        if(serverArray?.any { url.startsWith(it) }?:false){
            val prefsUrl=DeveloperPrefs.getString(context,DeveloperPrefs.URL)
            var serverUrl:String?=if(!TextUtils.isEmpty(prefsUrl)) prefsUrl else serverArray?.getOrNull(0)
            if(null==serverUrl){
                out.append("Intercept=$serverUrl $findItem\n")
            } else {
                //单独修改
                if(null!=findItem){
                    val key=System.identityHashCode(findItem.url)
                    serverUrl=DeveloperPrefs.getString(context,key)?:serverUrl
                    out.append("Info=${findItem.info}\n")
                }
                //拦截请求
                url=url.replace("https?://[^/]+/".toRegex(),serverUrl)
                out.append("Intercept=$url\n")
                Log.i("HttpLog","拦截后:$url")
                //替换url后重新请求
                request=request.newBuilder().url(url).build()
            }
        } else {
            Log.i("HttpLog","未拦截不在配置Url集内:$url")
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
        var response:Response
        try{
            response = chain.proceed(request)
        } catch (e:IOException){
            //error,记录异常
            throw e
        }
        //记录
        out.append("Code=${response.code()}\n")
        out.append("OK=${response.isSuccessful}\n")
        out.append("Protocol=${response.protocol()}\n")
        out.append("Time=${response.receivedResponseAtMillis()-response.sentRequestAtMillis()}\n")
        appendHeaders(out,response.headers())
        //记录结果
        var body:ResponseBody?=null
        val contentLength = response.body().contentLength()
        if(0<contentLength){
            body=response.peekBody(response.body().contentLength())
        }
        if(null!=body){
            out.append("Content-Type=${body.contentType()}\n")
            out.append("Content-Length=${body.contentLength()}\n")
            out.append("Result=${body.string()}")
        } else {
            out.append("Content-Length=-1\n")
            out.append("Result=无结果")
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