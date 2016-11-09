package cz.developer.library.ui.operation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quant.titlebar.TitleBarFragment;

import java.io.IOException;

import cz.developer.library.R;

/**
 * Created by cz on 15/11/30.
 * debug其他设置界面
 */
public class DebugOperationFragment extends TitleBarFragment {
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
        setOnBackClickListener(v->getFragmentManager().popBackStack());
    }

    private void clearAppData() {
        String appPackage = getContext().getPackageName();
        String cmd = "pm clear " + appPackage + " HERE";
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(cmd);
        } catch (IOException e) {
        }
    }
}
