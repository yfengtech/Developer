package cz.developer.library.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout

/**
 * Created by cz on 2017/9/8.
 * 采用以下控件作为根节点布局的界面,将忽略全局检索
 */
interface DeveloperFilter

class FilterLinearLayout(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs),DeveloperFilter

class FilterRelativeLayout(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs),DeveloperFilter

class FilterFrameLayout(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs),DeveloperFilter