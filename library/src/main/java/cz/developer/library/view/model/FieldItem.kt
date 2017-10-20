package cz.developer.library.ui.view.model


/**
 * Created by cz on 2017/9/7.
 */
class FieldItem(val type:Class<*>,val name:String,val value:Any?=null){
    override fun toString(): String =name
}