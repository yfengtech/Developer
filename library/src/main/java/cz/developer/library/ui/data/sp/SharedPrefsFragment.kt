package cz.developer.library.ui.data.sp

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cz.recyclerlibrary.onItemClick
import cz.developer.library.DeveloperManager
import cz.developer.library.R
import cz.developer.library.debugLog
import cz.developer.library.ui.data.adapter.SharedPrefsAdapter
import cz.developer.library.ui.data.adapter.SharedPrefsFileAdapter
import cz.developer.library.ui.data.model.SharedPrefsFileItem
import kotlinx.android.synthetic.main.fragment_shared_prefs.*
import java.io.File
import java.util.ArrayList

/**
 * Created by cz on 2017/9/11.
 */
internal class SharedPrefsFragment: Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shared_prefs,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity=activity
        if(activity is AppCompatActivity){
            toolBar.title = arguments?.getString("title")
            activity.setSupportActionBar(toolBar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolBar.setNavigationOnClickListener{ fragmentManager.popBackStack() }
        }
        val prefsItems = getSharedPrefsItems()
        if(prefsItems.isEmpty()){
            AlertDialog.Builder(context).
                    setTitle(R.string.no_shared_prefs).
                    setCancelable(false).
                    setNegativeButton(R.string.exit,
                            {_, _ -> fragmentManager.popBackStack() }).show()
        } else {
            recyclerView.layoutManager=LinearLayoutManager(context)
            val adapter= SharedPrefsFileAdapter(context,prefsItems)
            recyclerView.adapter=adapter
            recyclerView.onItemClick { _, _, adapterPosition ->
                val item= adapter[adapterPosition]
                DeveloperManager.toDeveloperFragment(activity,SharedPrefsFieldListFragment.newInstance(item.name))
            }
        }
    }

    fun getSharedPrefsItems(): List<SharedPrefsFileItem> {
        val prefsItems = ArrayList<SharedPrefsFileItem>()
        val sharedPrefs = File("/data/data/" + context.packageName + "/shared_prefs")
        if (sharedPrefs.exists()) {
            val files = sharedPrefs.listFiles()
            if (null != files) {
                for (i in files.indices) {
                    val file = files[i]
                    val name = file.name
                    if (name.endsWith(".xml")) {
                        val item = SharedPrefsFileItem(name=name.substring(0, name.lastIndexOf(".")),
                                lastModifier = file.lastModified())
                        prefsItems.add(item)
                    }
                }
            }
        }
        return prefsItems
    }
}