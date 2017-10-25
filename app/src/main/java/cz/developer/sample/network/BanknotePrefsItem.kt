package cz.developer.sample.network

import cz.netlibrary.model.NetPrefsItem
import cz.netlibrary.model.RequestMethod

/**
 * Created by cz on 2017/7/19.
 * 白条分期相关
 */
class BanknotePrefsItem : NetPrefsItem(){
	init {
		item {
	        action = NetPrefs.WHITE_CREDIT_TEMPLATE_LIST
	        info = "描述"
	        url = "api/mall/3c/main/template-list?"
	        params = arrayOf("pageNo", "pageSize")
	    }
		item {
	        action = NetPrefs.REPAY_URL
	        info = "RePayUrl"
	        url = "api/blank-note/my-repay"
	    }
	}
}
