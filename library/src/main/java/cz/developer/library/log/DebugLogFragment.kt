package cz.developer.library.log

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by cz on 2017/10/20.
 */
class DebugLogFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}
internal val DEBUG=true
inline internal fun <reified T> T.debugLog(message:String){
    if(DEBUG){
        FilePrefs.eventLog(message)
    }
}