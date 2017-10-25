package cz.developer.sample.network

import cz.netlibrary.model.Configuration

/**
 * Created by cz on 2017/10/24.
 */
object NetPrefs{
    //-------------------banknote 白条分期相关----------------
    val BLANKNOTE_REPAY = "banknote_repay"
    val BANKNOTE_REPAY = "banknote_repay"
    val BLANKNOTE_MY_STATUS = "banknote_my_status"
    val ORDER_INSTALLMENT = "order_installment"
    val ORDER_REPAY = "order_repay"
    val WHITE_CREDIT = "white_credit"
    val BLACK_NOTE_MORE = "black_note_more"
    val WHITE_CREDIT_TAB_CONFIG = "white_credit_tab_config"
    val WHITE_CREDIT_TEMPLATE_LIST = "white_credit_template_list"
    val BLANK_NOTE_STATUS = "blank_note_status"
    val WHITE_CREDIT_RECOMMEND_LIST = "white_credit_recommend_list"
    val QUERY_STATUS = "query_status"
    val REPAY_URL = "repay_url"

    init {
        Configuration.register(BanknotePrefsItem())
    }
}