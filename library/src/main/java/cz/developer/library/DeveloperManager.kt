package cz.developer.library

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Toast

import cz.developer.library.callback.MyActivityLifecycleCallback
import cz.developer.library.exception.MyUncaughtExceptionHandler
import cz.developer.library.log.FilePrefs
import cz.developer.library.prefs.DeveloperPrefs
import cz.developer.library.ui.view.DebugViewExtrasFragment
import cz.developer.library.ui.view.DebugViewInfoFragment
import cz.developer.library.ui.view.DebugViewHierarchyFragment
import cz.developer.library.ui.view.model.ViewAttribute
import cz.developer.library.ui.view.model.ViewHierarchyItem

/**
 * Created by czz on 2016/10/29.
 * 1:提供单独设定域名方法
 * 2:提供自定义应用内展示信息模块(app info)
 * 3:
 */
object DeveloperManager {
    internal lateinit var developerConfig: DeveloperConfig

    fun startDeveloperActivity(activity: Activity){
        activity.startActivity(Intent(activity,DeveloperActivity::class.java))
        activity.overridePendingTransition(R.anim.pop_in,R.anim.pop_out)
    }

    internal fun toDeveloperFragment(activity: FragmentActivity, fragment: Fragment)= toFragmentInner(activity,fragment,android.R.id.content)

    private fun toFragmentInner(activity: FragmentActivity, fragment: Fragment,id:Int) {
        val fragmentManager = activity.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.pop_in, R.anim.pop_out, R.anim.pop_in, R.anim.pop_out)
        fragmentTransaction.addToBackStack(fragment.javaClass.name).add(id, fragment).commit()
    }

    /**
     * 全局处理控件辅助点击
     */
    fun onItemLongClick(activity: Activity, childView: View){
        //1:展示控件整个层级关系树
        //2:展示控件所有属性集
        //3:属性控件附加数据集
        val context=activity.baseContext
        if(activity !is FragmentActivity){
            AlertDialog.Builder(activity).setTitle(R.string.activity_compat_hint).
                    setPositiveButton(android.R.string.cancel,{dialog, _ -> dialog.dismiss() }).show()
        } else {
            AlertDialog.Builder(activity).setTitle(context.getString(R.string.look_up_view,childView.javaClass.simpleName)).
                    setItems(arrayOf("控件层级","控件属性集","附加数据"),{_, which ->
                        when(which){
                            0-> toDeveloperFragment(activity, DebugViewHierarchyFragment.newInstance(ViewHierarchyItem(childView)))
                            1-> toDeveloperFragment(activity, DebugViewInfoFragment.newInstance(ViewAttribute(childView)))
                            2-> {
                                var keyTags:Any?=null
                                try{
                                    val tagField=View::class.java.getDeclaredField("mKeyedTags")
                                    tagField.isAccessible=true
                                    keyTags=tagField.get(childView)
                                } catch (e:Exception){
                                    Toast.makeText(context,R.string.view_tag_error,Toast.LENGTH_SHORT).show()
                                }
                                if(null!=childView.tag||null!=keyTags){
                                    toDeveloperFragment(activity,DebugViewExtrasFragment.newInstance(childView.tag,keyTags))
                                } else {
                                    Toast.makeText(context,R.string.no_view_tag,Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }).
                    setPositiveButton(android.R.string.cancel,{dialog, _ -> dialog.dismiss() }).show()
        }
    }

}
