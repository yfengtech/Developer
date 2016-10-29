package cz.developer.library;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quant.titlebar.TitleBarFragment;

import java.io.IOException;

/**
 * Created by cz on 15/11/30.
 * debug其他设置界面
 */
public class DebugOtherFragment extends TitleBarFragment {
    @Nullable
    @Override
    public View onCreateView(Context context, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_debug_other, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.tv_clear_cache).setOnClickListener(v -> clearAppData());

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitleText(R.string.debug_info);
    }

    private void clearAppData() {
        String appPackage = getContext().getPackageName();
        String cmd1 = "pm clear " + appPackage;
        String cmd2 = "pm clear " + appPackage + " HERE";
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(cmd1);
            runtime.exec(cmd2);
        } catch (IOException e) {
        }
    }
}
