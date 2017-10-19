package cz.developer.library.ui.data.model

/**
 * Created by cz on 2017/9/29.
 */
class TableFieldItem(name:String,var columnNames:Array<String>){
    var values= mutableListOf<Array<String>>()
}