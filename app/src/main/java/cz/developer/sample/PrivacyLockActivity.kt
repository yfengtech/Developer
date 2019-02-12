package cz.developer.sample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

import com.cz.library.widget.PrivacyLockView
import cz.developer.library.DeveloperActivity
import cz.developer.library.DeveloperManager

/**
 * Created by cz on 1/12/17.
 */

class PrivacyLockActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_lock)
        val lockView = findViewById<PrivacyLockView>(R.id.lock_view)
        val textView = findViewById<TextView>(R.id.tv_info)
        lockView.setOnTextSubmitListener { editable ->
            if ("1234" == editable.toString()) {
                DeveloperManager.startDeveloperActivity(this)
                finish()
            } else {
                textView.setText(R.string.lock_password)
                lockView.clearEditText()
            }
        }
    }

    companion object {
        private val TAG = "PrivacyLockFragment"
    }


}
