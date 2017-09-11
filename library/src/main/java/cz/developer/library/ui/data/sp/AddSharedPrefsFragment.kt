package cz.developer.library.ui.data.sp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cz.developer.library.R

/**
 * Created by cz on 2017/9/11.
 */
class AddSharedPrefsFragment: Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shared_prefs,container,false)
    }
}