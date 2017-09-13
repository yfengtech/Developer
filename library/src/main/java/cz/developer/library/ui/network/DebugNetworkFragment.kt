package cz.developer.library.ui.network

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.*

import java.util.ArrayList

import cz.developer.library.DeveloperManager
import cz.developer.library.R
import cz.developer.library.model.NetItem
import cz.developer.library.prefs.DeveloperPrefs
import cz.developer.library.ui.dialog.EditDialog
import android.support.v7.widget.SearchView
import kotlinx.android.synthetic.main.fragment_list.*


/**
 * Created by czz on 2016/10/29.
 */
class DebugNetworkFragment : Fragment(), FragmentManager.OnBackStackChangedListener {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity=activity
        if(activity is AppCompatActivity){
            toolBar.title = arguments.getString("title")
            setHasOptionsMenu(true)
            activity.setSupportActionBar(toolBar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolBar.setNavigationOnClickListener{ fragmentManager.popBackStack() }
        }

        val networkAdapter = DeveloperManager.developerConfig.networkAdapter
        if(null!=networkAdapter){
            initAdapter(networkAdapter)
            fragmentManager.addOnBackStackChangedListener(this)
        } else {
            Snackbar.make(listView,"未配置网络数据适配器",Snackbar.LENGTH_SHORT).show()
        }
    }


    private fun initAdapter(networkAdapter: NetworkAdapter) {
        val serverUrls=networkAdapter.serverUrl
        var networkItems = networkAdapter.networkItems
        val adapter=NetworkItemAdapter(context, networkItems, serverUrls?.first())
        listView.adapter =adapter
        listView.setOnItemClickListener { _, _, i, _ ->
            val item = adapter.getItem(i)
            val url = DeveloperPrefs.getString(item.action)
            val editDialog = EditDialog.newInstance(networkAdapter.serverUrl,serverUrls?.first())
            editDialog.setOnSubmitListener(object :EditDialog.OnSubmitListener{
                override fun onSubmit(text: String) {
                    if (text != url) {
                        DeveloperPrefs.setString(item.action, text)
                        adapter.removeUrlItem(i)
                        adapter.notifyDataSetChanged()
                    }
                }
            })
            editDialog.show(fragmentManager, null)
        }
    }

    override fun onBackStackChanged() {

    }

    override fun onDestroyView() {
        fragmentManager.removeOnBackStackChangedListener(this)
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_network,menu)
        val searchView = menu.findItem(R.id.ab_search).actionView as SearchView
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                val adapter=listView.adapter as NetworkItemAdapter
                adapter.filter(newText?.toLowerCase())
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean=false
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.ab_setting){
            val networkAdapter = DeveloperManager.developerConfig.networkAdapter
            if (null != networkAdapter) {
                DeveloperManager.toDeveloperFragment(activity, NetworkSettingFragment.newInstance(networkAdapter.serverUrl))
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
