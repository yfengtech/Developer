package cz.developer.library.ui.data.sp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.cz.recyclerlibrary.onItemClick
import cz.developer.library.DeveloperManager
import cz.developer.library.R
import cz.developer.library.data.model.PrefsType
import cz.developer.library.ui.data.adapter.SharedPrefsAdapter
import cz.developer.library.ui.data.model.SharedPrefsItem
import kotlinx.android.synthetic.main.fragment_shared_prefs_field_list.*

/**
 * Created by cz on 2017/9/11.
 */
internal class SharedPrefsFieldListFragment: Fragment(){
    companion object {
        fun newInstance(name:String)=SharedPrefsFieldListFragment().apply {
            arguments=Bundle().apply {
                putString("name",name)
            }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shared_prefs_field_list,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity=activity
        val name=arguments.getString("name")
        if(activity is AppCompatActivity){
            toolBar.title = name
            setHasOptionsMenu(true)
            activity.setSupportActionBar(toolBar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolBar.setNavigationOnClickListener{ fragmentManager.popBackStack() }
        }
        val sharedPrefs=context.getSharedPreferences(name,Context.MODE_PRIVATE)
        val items=sharedPrefs.all?.map { (key,value)->
            val item= SharedPrefsItem()
            item.name=key
            item.value=value
            item.type=when(value){
                is Set<*>-> PrefsType.SET
                is Int->PrefsType.INTEGER
                is Boolean->PrefsType.BOOLEAN
                is Float->PrefsType.FLOAT
                is Long->PrefsType.LONG
                else -> PrefsType.STRING
            }
            item
        }
        recyclerView.layoutManager=LinearLayoutManager(context)
        val adapter=SharedPrefsAdapter(context,items)
        recyclerView.adapter=adapter
        recyclerView.onItemClick { v, _, adapterPosition ->
            val item=adapter[adapterPosition]
            AlertDialog.Builder(context).
                    setTitle(R.string.edit_shared_prefs).
                    setItems(resources.getStringArray(R.array.edit_sp_array),{ _, which->
                when(which){
                    0->{
                        //跳转到编辑条目
                        val fragment=EditSharedPrefsFragment.newInstance(item)
                        //操作回调,此处不必计较通信方式
                        fragment.onChangedSharedPrefs {commitPrefsItem(sharedPrefs,it,adapterPosition) }
                        DeveloperManager.toDeveloperFragment(activity,fragment)
                    }
                    1->{
                        //移除条目
                        if(sharedPrefs.edit().remove(item.name).commit()){
                            adapter.removeNotify(item)
                            Snackbar.make(recyclerView,getString(R.string.remove_prefs_value,item.name),Snackbar.LENGTH_SHORT).show()
                        } else {
                            Snackbar.make(recyclerView,getString(R.string.remove_prefs_value_fail,item.name),Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }).show()
        }
    }

    /**
     * 更新配置项
     */
    private fun commitPrefsItem(sharedPrefs: SharedPreferences,item:SharedPrefsItem,updatePosition:Int=-1) {
        try{
            val value=when(item.type){
                PrefsType.SET->item.value as Set<String>
                PrefsType.INTEGER->item.value.toString().toInt()
                PrefsType.BOOLEAN->item.value.toString().toBoolean()
                PrefsType.FLOAT->item.value.toString().toFloat()
                PrefsType.LONG->item.value.toString().toLong()
                else->item.value as String
            }
            if(when(item.type){
                PrefsType.SET->sharedPrefs.edit().putStringSet(item.name,value as Set<String>).commit()
                PrefsType.INTEGER->sharedPrefs.edit().putInt(item.name,value as Int).commit()
                PrefsType.BOOLEAN->sharedPrefs.edit().putBoolean(item.name,value as Boolean).commit()
                PrefsType.FLOAT->sharedPrefs.edit().putFloat(item.name,value as Float).commit()
                PrefsType.LONG->sharedPrefs.edit().putLong(item.name,value as Long).commit()
                else->sharedPrefs.edit().putString(item.name,value as String).commit()
            }){
                val adapter=(recyclerView.adapter as SharedPrefsAdapter)
                if(-1==updatePosition){
                    val items=adapter.getItems()
                    if(items.any { it.name==item.name }){
                        Snackbar.make(recyclerView,getString(R.string.add_shared_prefs_exists,item.name),Snackbar.LENGTH_SHORT).show()
                    } else {
                        //添加条目
                        adapter.addItemNotify(item)
                        Snackbar.make(recyclerView,R.string.add_shared_prefs_complete,Snackbar.LENGTH_SHORT).show()
                    }
                } else {
                    //更新条目
                    adapter.setItemNotify(updatePosition,item)
                    Snackbar.make(recyclerView,R.string.update_shared_prefs_complete,Snackbar.LENGTH_SHORT).show()
                }
            } else {
                Snackbar.make(recyclerView,R.string.update_shared_prefs_failed,Snackbar.LENGTH_SHORT).show()
            }
        } catch (e:Exception){
            Snackbar.make(recyclerView,R.string.shared_prefs_value_error,Snackbar.LENGTH_SHORT).show()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        menu?.clear()
        inflater.inflate(R.menu.menu_shared_prefs,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(R.id.action_add_prefs==item.itemId){
            val fragment=AddSharedPrefsFragment.newInstance(arguments)
            fragment.onChangedSharedPrefs {
                val name=arguments.getString("name")
                commitPrefsItem(context.getSharedPreferences(name,Context.MODE_PRIVATE),it)
            }
            DeveloperManager.toDeveloperFragment(activity,fragment)
        }
        return super.onOptionsItemSelected(item)
    }
}