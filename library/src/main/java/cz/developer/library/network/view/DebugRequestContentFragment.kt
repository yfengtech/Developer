package cz.developer.library.network.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.ShareActionProvider
import android.text.TextUtils
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import cz.developer.library.R
import cz.developer.library.log.FilePrefs
import cz.developer.library.network.adapter.NetworkRequestAdapter
import cz.developer.library.network.model.RequestData
import kotlinx.android.synthetic.main.fragment_debug_request_content.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.regex.Pattern
import org.json.JSONTokener



/**
 * Created by cz on 2017/10/24.
 */
internal class DebugRequestContentFragment: Fragment(){
    companion object {
        fun newInstance(file:File)=DebugRequestContentFragment().apply {
            this.file=file
        }
    }
    private lateinit var shareActionProvider: ShareActionProvider
    private lateinit var shareText: String
    lateinit var file:File
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_debug_request_content, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = activity?:return
        if (activity is AppCompatActivity) {
            toolBar.title = file.name
            setHasOptionsMenu(true)
            activity.setSupportActionBar(toolBar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolBar.setNavigationOnClickListener { fragmentManager?.popBackStack() }
        }
        //解析请求文件
        val data = parserText(file.readLines())
        shareText=data.request.toString()
        //初始化选择列表
        initSpinner(data)
        //初始化数据
        initRequestData(activity, data)
    }

    private fun initSpinner(data:RequestData) {
        val adapter = ArrayAdapter.createFromResource(context, R.array.shard_request_data,R.layout.spinner_dropdown_item)
        spinner.adapter = adapter
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                    shareText=when (i) {
                        0 -> data.request.toString()
                        1 -> data.response.toString()
                        else -> data.response.result?:"null"
                    }
                    setShareIntent(shareText)
            }
            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
    }

    /**
     * 初始化请求数据体
     */
    private fun initRequestData(context : Context, data:RequestData) {
        val items = LinkedHashMap<String, List<String?>>()
        items.put("请求描述:", listOf(data.request.info))
        items.put("请求Url:", listOf(data.request.url))
        items.put("拦截Url:", listOf(data.request.interceptUrl))
        items.put("请求方法:", listOf(data.request.method))
        items.put("请求TAG:", listOf(data.request.tag))
        items.put("Https:", listOf(data.request.isHttps.toString()))
        items.put("Header:", data.request.headers.map { "${it.key}=${it.value}" })
        items.put("Params:", data.request.params.map { "${it.key}=${it.value}" })
        items.put("contentLength:", listOf(data.request.contentLength.toString()))
        items.put("contentType:", listOf(data.request.contentType))

        items.put("响应Code:", listOf(data.response.code.toString()))
        items.put("响应OK:", listOf(data.response.isOK.toString()))
        items.put("耗时:", listOf(data.response.time.toString()))
        items.put("Protocol:", listOf(data.response.protocol))
        items.put("Header:", data.response.headers.map { "${it.key}=${it.value}" })
        items.put("contentLength:", listOf(data.response.contentLength.toString()))
        items.put("contentType:", listOf(data.response.contentType))

        //格式化json信息
        val out = StringBuilder()
        val result=data.response.result
        if(TextUtils.isEmpty(result)){
            out.append("无数据")
        } else {
            formatJson(out, result)
        }
        items.put("Result:", listOf(out.toString()))

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = NetworkRequestAdapter(context, items, true)
    }

    /**
     * 格式化json
     */
    private fun formatJson(out: StringBuilder, result: String?,level: Int=0) {
        val obj = JSONTokener(result).nextValue()
        if (obj is JSONObject) {
            wrapObject(out,null,level){ formatJsonObject(out,obj,level+1) }
        } else if (obj is JSONArray) {
            wrapArray(out,null,level){ formatJsonArray(out,null,obj,level+1) }
        }
    }

    /**
     * 格式化json
     */
    private fun formatJsonObject(out:StringBuilder, obj:JSONObject, level:Int){
        val keys = obj.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            var value: Any? = obj.opt(key)
            if(value is JSONArray){
                wrapArray(out,key,level){ formatJsonArray(out,key,value,level+1) }
            } else if(value is JSONObject){
                wrapObject(out,key,level){ formatJsonObject(out,value,level+1) }
            } else {
                out.append("".padStart(level,'\t')+"$key : $value\n")
            }
        }
    }

    /**
     * 格式化json array
     */
    private fun formatJsonArray(out:StringBuilder,key:String?,array:JSONArray,level:Int){
        for(index in (0..array.length()-1)){
            val value=array.opt(index)
            if(value is JSONArray){
                wrapArray(out,key,level){ formatJsonArray(out,key,value,level+1) }
            } else if(value is JSONObject){
                wrapObject(out,key,level){ formatJsonObject(out,value,level+1) }
            }
        }
    }

    private inline fun wrapObject(out:StringBuilder, key:String?, level:Int, action:()->Unit){
        out.append("".padStart(level,'\t')+"${if(null==key) "" else key+" : "}{\n")
        action()
        out.append("".padStart(level,'\t')+"}\n")
    }

    private inline fun wrapArray(out:StringBuilder, key:String?, level:Int, action:()->Unit){
        out.append("".padStart(level,'\t')+"${if(null==key) "" else key+" : "}[\n")
        action()
        out.append("".padStart(level,'\t')+"]\n")
    }

    /**
     * 解析文本
     */
    private fun parserText(lines:List<String>):RequestData{
        val data=RequestData()
        val pattern="([\\w-]+)=(.+)".toPattern()
        var isRequest=true
        lines.forEach {
            val matcher=pattern.matcher(it)
            if(isRequest){
                if(matcher.find()){
                    val key=matcher.group(1)
                    val value=matcher.group(2)
                    when(key){
                        "Url"->data.request.url=value
                        "Intercept"->data.request.interceptUrl=value
                        "Info"->data.request.info=value
                        "Method"->data.request.method=value
                        "Https"->data.request.isHttps=value.toBoolean()
                        "Tag"->data.request.tag=value
                        "Params"->data.request.params=getParams(value)
                        "Headers"->data.request.headers=getParams(value)
                        "Content-Type"->data.request.contentType=value
                        "Content-Length"->data.request.contentLength=value.toInt()
                    }
                }
            } else {
                //response`
                if(matcher.find()){
                    val key=matcher.group(1)
                    val value=matcher.group(2)
                    when(key){
                        "Code"->data.response.code=value.toInt()
                        "OK"->data.response.isOK=value.toBoolean()
                        "Protocol"->data.response.protocol =value
                        "Time"->data.response.time=value.toInt()
                        "Headers"->data.response.headers=getParams(value)
                        "Content-Type"->data.response.contentType=value
                        "Content-Length"->data.response.contentLength=value.toInt()
                        "Result"->data.response.result=value
                    }
                }
            }
            if(it.matches("=+".toRegex())){
                isRequest=false
            }
        }
        return data
    }

    /**
     * 获取参数集
     */
    private fun getParams(paramValue: String):MutableMap<String,String> {
        var paramValue = paramValue
        val items= mutableMapOf<String,String>()
        val paramPattern = Pattern.compile("(\\w+):([^=&]+)&?")
        val matcher1 = paramPattern.matcher(paramValue)
        while (matcher1.find()) {
            val key = matcher1.group(1)
            val value = matcher1.group(2)
            items.put(key, value)
        }
        //自然排序
        return items.toSortedMap()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_share_item, menu)
        val item = menu.findItem(R.id.menu_item_share)
        shareActionProvider = MenuItemCompat.getActionProvider(item) as ShareActionProvider
        shareActionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME)
        setShareIntent(shareText)
    }

    private fun setShareIntent(shareData: String) {
//        if(shareData.length>2*1024){
//            //大文件
//            AlertDialog.Builder(context).
//                    setTitle(R.string.shared_file).
//                    setMessage(R.string.shared_file_hint).
//                    setPositiveButton(android.R.string.ok,{_,_ ->
//                        val tmpFile=FilePrefs.tmpFile
//                        tmpFile.writeText(shareData)
//                        //分享文件
//                        val intent = Intent(Intent.ACTION_SEND_MULTIPLE)//发送多个文件
//                        intent.type = "*/*"//多个文件格式
//                        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, arrayListOf(Uri.fromFile(tmpFile)))
//                        startActivity(intent)
//                    }).setNegativeButton(android.R.string.cancel,{_, _ ->
//                        val shareIntent = Intent(Intent.ACTION_SEND)
//                        shareIntent.type = "text/plain"
//                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareData)
//                        if (shareActionProvider != null) {
//                            shareActionProvider.setShareIntent(shareIntent)
//                        }
//                    }).show()
//        } else {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareData)
            if (shareActionProvider != null) {
                shareActionProvider.setShareIntent(shareIntent)
            }
//        }
    }


}