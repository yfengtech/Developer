package cz.developer.sample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

import com.cz.library.widget.PrivacyLockView

import cz.developer.library.ui.DeveloperActivity

/**
 * Created by cz on 1/12/17.
 */

class PrivacyLockActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_lock)
        val lockView = findViewById(R.id.lock_view) as PrivacyLockView
        val textView = findViewById(R.id.tv_info) as TextView
        lockView.setOnTextSubmitListener { editable ->
            if ("1234" == editable.toString()) {
                startActivity(Intent(this, DeveloperActivity::class.java))
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
