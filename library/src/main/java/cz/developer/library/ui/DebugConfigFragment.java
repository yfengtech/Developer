package cz.developer.library.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.quant.titlebar.TitleBarFragment;

import java.io.File;

import cz.developer.library.R;


/**
 * Created by cz on 15/11/30.
 * debug配置设置界面
 */
public class DebugConfigFragment extends TitleBarFragment {
//    @Id(R.id.tv_old_url)
    private TextView mDefaultUrl;
//    @Id( R.id.et_server_url)
    private EditText mEidtor;
    private String title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (null != arguments) {
            title = arguments.getString("title");
        }
    }

    @Nullable
    @Override
    public View onCreateView(Context context, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_debug_config, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mDefaultUrl.setText(getString(R.string.old_server_url, NetUtils.getServerUrl()));
        mDefaultUrl.setOnLongClickListener(v -> {
//            copyText(NetWorkConfig.SERVER_URL);
            return true;
        });
//        mEidtor.setText(PrefernceUtils.getString(ConfigName.DEBUG_URL));
        view.findViewById(R.id.btn_default).setOnClickListener(v -> {
//            PrefernceUtils.remove(ConfigName.DEBUG_URL);
            clearUserInfo();
        });
        view.findViewById(R.id.btn_debug_url).setOnClickListener(v -> {
//            PrefernceUtils.setString(ConfigName.DEBUG_URL, NetWorkConfig.DEBUG_SERVER_URL);
            clearUserInfo();
        });
        view.findViewById(R.id.btn_use).setOnClickListener(v -> {
            Editable text = mEidtor.getText();
            clearUserInfo();
        });

    }

    private void clearUserInfo() {
        Context context = getContext();
        File cacheDir = context.getCacheDir();
        if(cacheDir.exists()){
            File[] files = cacheDir.listFiles();
            if(null!=files){
                for(int i=0;i<files.length;i++){
                    files[i].delete();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        restartApplication();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitleText(title);
    }

    private void copyText(String text) {
        if (!TextUtils.isEmpty(text)) {
            ClipboardManager clip = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            clip.setText(text);
        }
    }

    /**
     * 根据包名启动其他应用
     *
     */
    private void restartApplication() {
        Context appContext = getContext();
        if (null != appContext) {
            PackageManager packageManager = appContext.getPackageManager();
            Intent intent = null;
            try {
                intent = packageManager.getLaunchIntentForPackage(appContext.getPackageName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (null != intent) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                appContext.startActivity(intent);
            }
        }
    }


}
