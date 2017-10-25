package cz.developer.library.ui.filesystem

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.ShareActionProvider
import android.view.*
import com.cz.recyclerlibrary.onItemClick
import cz.developer.library.DeveloperManager
import cz.developer.library.R
import cz.developer.library.ui.filesystem.adapter.CacheFileAdapter
import cz.developer.library.ui.filesystem.model.FileItem
import kotlinx.android.synthetic.main.fragment_cache_file_content.*
import java.io.File
import java.net.URI

/**
 * Created by cz on 2017/10/19.
 */
internal class CacheFileContentFragment: Fragment(){
    lateinit var file:File
    companion object {
        fun newInstance(file:File)=CacheFileContentFragment().apply {
            this.file=file
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cache_file_content,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity=activity
        if(activity is AppCompatActivity){
            toolBar.title = file.name
            setHasOptionsMenu(true)
            activity.setSupportActionBar(toolBar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolBar.setNavigationOnClickListener{ fragmentManager.popBackStack() }
        }
        textContent.text=file.readText()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_share_item, menu)
        val item = menu.findItem(R.id.menu_item_share)
        val shareActionProvider = MenuItemCompat.getActionProvider(item) as ShareActionProvider
        shareActionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
        shareActionProvider.setShareIntent(shareIntent)
    }

}