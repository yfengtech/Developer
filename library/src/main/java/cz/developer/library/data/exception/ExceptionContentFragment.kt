package cz.developer.library.ui.data.exception

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.ShareActionProvider
import android.view.*
import cz.developer.library.R
import cz.developer.library.ui.data.model.ExceptionItem
import kotlinx.android.synthetic.main.fragment_exception_content.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by cz on 2017/9/13.
 */
internal class ExceptionContentFragment: Fragment(){
    companion object {
        fun newInstance(item:ExceptionItem)=ExceptionContentFragment().apply {
            arguments=Bundle().apply { putParcelable("content",item) }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_exception_content,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity=activity
        if(activity is AppCompatActivity){
            toolBar.setTitle(R.string.exception_detail)
            setHasOptionsMenu(true)
            activity.setSupportActionBar(toolBar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolBar.setNavigationOnClickListener{ fragmentManager?.popBackStack() }
        }
        val item:ExceptionItem?=arguments?.getParcelable("content")
        if(null!=item){
            val formatter=SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val text="崩溃日期:${formatter.format(Date(item.lastModified))}\n" +
                    "崩溃方法:${item.methodName}\n" +
                    "崩溃线程:${item.threadName}\n" +
                    "崩溃异常:\n${item.className}\n" +
                    "异常堆栈:\n${item.desc}"
            content.text=text
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_share_item, menu)
        val item = menu.findItem(R.id.menu_item_share)
        val shareActionProvider = MenuItemCompat.getActionProvider(item) as ShareActionProvider
        shareActionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, content.text.toString())
        shareActionProvider.setShareIntent(shareIntent)
    }

}