package cz.developer.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.cz.library.widget.PrivacyLockView;

import cz.developer.library.ui.DeveloperActivity;

/**
 * Created by cz on 1/12/17.
 */

public class PrivacyLockActivity extends AppCompatActivity {
    private static final String TAG="PrivacyLockFragment";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_lock);
        final PrivacyLockView lockView= (PrivacyLockView) findViewById(R.id.lock_view);
        final TextView textView= (TextView) findViewById(R.id.tv_info);
        lockView.setOnTextSubmitListener(editable -> {
            if("1234".equals(editable.toString())){
                startActivity(new Intent(this, DeveloperActivity.class));
                finish();
            } else {
                textView.setText(R.string.lock_password);
                lockView.clearEditText();
            }
        });
    }


}
