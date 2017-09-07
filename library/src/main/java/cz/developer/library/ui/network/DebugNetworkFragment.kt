package cz.developer.library.ui.network

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

import java.util.ArrayList

import cz.developer.library.DeveloperManager
import cz.developer.library.R
import cz.developer.library.model.NetItem
import cz.developer.library.prefs.DeveloperPrefs
import cz.developer.library.ui.dialog.EditDialog
import kotlinx.android.synthetic.main.fragment_debug_view.*

/**
 * Created by czz on 2016/10/29.
 */
class DebugNetworkFragment : Fragment(), FragmentManager.OnBackStackChangedListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val networkAdapter = DeveloperManager.developerConfig.networkAdapter
        initTitleBar(networkAdapter)
        listView.postDelayed({ initAdapter(networkAdapter) }, 300)
        fragmentManager.addOnBackStackChangedListener(this)
    }

    private fun initTitleBar(networkAdapter: NetworkAdapter?) {
//        setTitleText(title)
//        setOnBackClickListener { v -> fragmentManager.popBackStack() }
//        val searchView = SearchView(context)
//        searchView.setSearchDeleteDrawable(ContextCompat.getDrawable(context, R.drawable.abc_ic_clear_mtrl_alpha))
//        searchView.setHintDrawable(ContextCompat.getDrawable(context, R.drawable.abc_ic_search_white))
//        searchView.setEditTextColor(Color.WHITE)
//        searchView.setOnSubmitListener({ item -> adapter!!.filter(null) })
//        searchView.setOnTextChangeListener({ newItem, oldItem, count ->
//            if (TextUtils.isEmpty(newItem)) {
//                adapter!!.filter(null)
//            } else {
//                adapter!!.filter(newItem.toString().toLowerCase())
//            }
//        })
//        addImageMenuItem(R.drawable.abc_ic_search_white, searchView)
//        addImageMenuItem(R.drawable.ic_settings_white)
//        setOnMenuItemClickListener { v, index ->
//            if (null != networkAdapter) {
//                val serverUrl = networkAdapter.getServerUrl()
//                val networkItems = networkAdapter.networkItems
//                val items = networkAdapter.selectUrl
//                var actionItems: Array<String>? = null
//                if (null != networkItems) {
//                    actionItems = arrayOfNulls<String>(networkItems.size)
//                    for (i in networkItems.indices) {
//                        actionItems[i] = networkItems[i].action
//                    }
//                }
//                var selectItems: Array<String>? = null
//                items?.toTypedArray()
//                DeveloperManager.Companion.toFragmentInner(activity, NetworkSettingFragment.newInstance(actionItems, selectItems, serverUrl))
//            }
//        }
    }

    private fun initAdapter(networkAdapter: NetworkAdapter?) {
//        var serverUrl: String? = null
//        var networkItems: List<NetItem>? = null
//        val selectItems = ArrayList<String>()
//        if (null != networkAdapter) {
//            serverUrl = networkAdapter.selectUrl
//            networkItems = networkAdapter.networkItems
//            val items = networkAdapter.selectUrl
//            if (null != items) {
//                selectItems.addAll(items)
//            }
//        }
//        val finalUrl = serverUrl
//        listView!!.adapter = NetworkItemAdapter(context, networkItems, serverUrl)
//        listView!!.setOnItemClickListener { adapterView, view, i, l ->
//            val item = adapter!!.getItem(i)
//            val url = DeveloperPrefs.getString(item.action)
//            val editDialog = EditDialog.newInstance(selectItems, if (!TextUtils.isEmpty(url)) url else finalUrl)
//            editDialog.setOnSubmitListener { text ->
//                if (text != url) {
//                    DeveloperPrefs.setString(item.action, text)
//                    adapter!!.removeUrlItem(i)
//                    adapter!!.notifyDataSetChanged()
//                }
//            }
//            editDialog.show(fragmentManager, null)
//        }
    }

    override fun onDestroyView() {
        fragmentManager.removeOnBackStackChangedListener(this)
        super.onDestroyView()
    }

    override fun onBackStackChanged() {

    }

//    override fun onBackStackChanged() {
//        if (null != adapter) {
//            adapter.clearUrlItems()
//            adapter.notifyDataSetChanged()
//        }
//    }
}
