package cz.developer.library.ui.data.sp

import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.view.*
import android.widget.EditText
import android.widget.ScrollView
import cz.developer.library.R
import cz.developer.library.data.model.PrefsType
import cz.developer.library.ui.data.model.SharedPrefsItem
import kotlinx.android.synthetic.main.add_edit_from_item.*
import kotlinx.android.synthetic.main.edit_form_item.*
import kotlinx.android.synthetic.main.fragment_add_shared_prefs.*
import kotlinx.android.synthetic.main.shared_prefs_bool_item.*

/**
 * Created by cz on 2017/9/11.
 */
internal class AddSharedPrefsFragment: Fragment(){
    companion object {
        fun newInstance(args:Bundle?)=AddSharedPrefsFragment().apply { arguments=args }
    }
    private var lastTypeView:View?=null
    private var callback:((SharedPrefsItem)->Unit)?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_shared_prefs,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity=activity
        if(activity is AppCompatActivity){
            toolBar.setTitle(R.string.add_shared_prefs)
            setHasOptionsMenu(true)
            activity.setSupportActionBar(toolBar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolBar.setNavigationOnClickListener{ fragmentManager?.popBackStack() }
        }
        fieldName.requestFocus()
        lastTypeView=valueViewStub.inflate()
        typeLayout.setOnCheckedChangeListener { _, checkedId ->
            //清空所有带焦点编辑框
            valueEditLayout.focusedChild?.clearFocus()
            when(checkedId){
                R.id.boolButton->{
                    boolViewStub?.inflate()
                    lastTypeView?.visibility=View.GONE
                    boolContainer.visibility=View.VISIBLE
                    lastTypeView=boolContainer
                }
                R.id.setButton->{
                    lastTypeView?.visibility=View.GONE
                    setLayout.visibility=View.VISIBLE
                    lastTypeView=setLayout
                    //最后一个获得焦点
                    if(1<valueEditLayout.childCount){
                        setValueEditorFocus(valueEditLayout.childCount-1,true)
                    }
                }
                else ->{
                    lastTypeView?.visibility=View.GONE
                    textInputLayout.visibility=View.VISIBLE
                    lastTypeView=textInputLayout
                    fieldValue.text = null
                    fieldValue.inputType = when(checkedId){
                        R.id.intButton,R.id.longButton-> InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL
                        R.id.floatButton-> InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                        else->InputType.TYPE_CLASS_TEXT
                    }
                }
            }
        }
        //初始化set配置布局
        initPrefsSetLayout(savedInstanceState)
        //添加条目
        applyButton.setOnClickListener {
            addSharedPrefs()
        }
    }

    private fun setValueEditorFocus(index:Int,focus:Boolean){
        val layout=valueEditLayout.getChildAt(index)
        val editor=layout.findViewById(R.id.addEditor) as EditText
        if(focus) editor.requestFocus()
        else editor.clearFocus()
    }

    /**
     * 初始化set配置布局
     */
    private fun initPrefsSetLayout(savedInstanceState: Bundle?) {
        deleteView.visibility = View.GONE
        val focusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            addEditButton.isClickable = hasFocus
            if (hasFocus) {
                applyButton.animate().alpha(0f).translationX(applyButton.right * 1f)
                addEditButton.animate().setStartDelay(300).alpha(1f).translationY(applyButton.top * 1f)
            } else {
                applyButton.animate().setStartDelay(300).alpha(1f).translationX(0f)
                addEditButton.animate().alpha(0f).translationY(0f)
            }
        }
        addEditor.onFocusChangeListener = focusChangeListener
        setDefaultViewTranslation(valueEditLayout)
        //添加布局
        val layoutInflater = LayoutInflater.from(context)
        addEditButton.setOnClickListener {
            val view = layoutInflater.inflate(R.layout.add_edit_from_item, valueEditLayout, false)
            val editor = view.findViewById(R.id.addEditor) as EditText
            editor.onFocusChangeListener = focusChangeListener
            //设置移除事件
            view.findViewById<View>(R.id.deleteView).setOnClickListener {
                valueEditLayout.removeView(view)
                scrollView.fullScroll(ScrollView.FOCUS_DOWN)
                //设置为最后一个获得焦点
                valueEditLayout.getChildAt(valueEditLayout.childCount-1).requestFocus()
            }
            //添加布局
            valueEditLayout.addView(view,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            //滚动到底部
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
            //获取焦点
            editor.requestFocus()
        }
    }

    /**
     * 添加配置项
     */
    private fun addSharedPrefs() {
        if(TextUtils.isEmpty(fieldName.text)){
            Snackbar.make(scrollView,R.string.input_shared_prefs_key,Snackbar.LENGTH_SHORT).show()
        } else {
            val newItem=SharedPrefsItem()
            //设定名字
            newItem.name=fieldName.text.toString()
            //设定类型
            newItem.type=when(typeLayout.checkedRadioButtonId){
                R.id.setButton-> PrefsType.SET
                R.id.intButton-> PrefsType.INTEGER
                R.id.boolButton-> PrefsType.BOOLEAN
                R.id.floatButton-> PrefsType.FLOAT
                R.id.longButton-> PrefsType.LONG
                else -> PrefsType.STRING
            }
            //给定取值
            newItem.value=when(typeLayout.checkedRadioButtonId){
                R.id.boolButton->boolLayout.checkedRadioButtonId==R.id.boolTrue
                R.id.setButton->(0 until valueEditLayout.childCount).map {
                    val child=valueEditLayout.getChildAt(it)
                    val editor=child.findViewById(R.id.addEditor) as EditText
                    editor.text.toString()
                }.toSet()
                else->fieldValue.text.toString()
            }
            callback?.invoke(newItem)
            //弹出当前界面
            fragmentManager?.popBackStack()
        }
    }


    @SuppressLint("ObjectAnimatorBinding")
    private fun setDefaultViewTranslation(view: ViewGroup) {
        val layoutTransition = LayoutTransition()
        //view出现时 view自身的动画效果
        val animator1 = ObjectAnimator.ofFloat(null, "alpha", 0f, 1f).setDuration(layoutTransition.getDuration(LayoutTransition.APPEARING))
        layoutTransition.setAnimator(LayoutTransition.APPEARING, animator1)

        val animator2 = ObjectAnimator.ofFloat(null, "alpha", 1f, 0f).setDuration(layoutTransition.getDuration(LayoutTransition.DISAPPEARING))
        layoutTransition.setAnimator(LayoutTransition.DISAPPEARING, animator2)
        //view 动画改变时，布局中的每个子view动画的时间间隔
        layoutTransition.setStagger(LayoutTransition.CHANGE_APPEARING, 30)
        layoutTransition.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 30)

        val pvhLeft = PropertyValuesHolder.ofInt("left", 0, 1)
        val pvhTop = PropertyValuesHolder.ofInt("top", 0, 1)
        val pvhRight = PropertyValuesHolder.ofInt("right", 0, 1)
        val pvhBottom = PropertyValuesHolder.ofInt("bottom", 0, 1)
        val changeIn = ObjectAnimator.ofPropertyValuesHolder(view, pvhLeft, pvhTop, pvhRight, pvhBottom).setDuration(layoutTransition.getDuration(LayoutTransition.CHANGE_APPEARING))
        layoutTransition.setAnimator(LayoutTransition.CHANGE_APPEARING, changeIn)

        val changeOut = ObjectAnimator.ofPropertyValuesHolder(view, pvhLeft, pvhTop, pvhRight, pvhBottom).setDuration(layoutTransition.getDuration(LayoutTransition.CHANGE_DISAPPEARING))
        layoutTransition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, changeOut)
        view.layoutTransition = layoutTransition
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()//清空之前面menu菜单
        super.onCreateOptionsMenu(menu, inflater)
    }

    fun onChangedSharedPrefs(action:(SharedPrefsItem)->Unit){
        this.callback=action
    }
}