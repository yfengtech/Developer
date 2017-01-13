package cz.developer.library.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.ListView;

import com.quant.titlebar.TitleBarActivity;

import java.util.List;

import cz.developer.library.DeveloperManager;
import cz.developer.library.R;
import cz.developer.library.adapter.DebugListAdapter;
import cz.developer.library.model.DebugItem;
import cz.developer.library.xml.ListConfigReader;
import xyqb.library.config.PrefsManager;

public class DeveloperActivity extends TitleBarActivity {
    private static final String PACKAGE_NAME = "cz.developer.library";
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_developer);
        setTitleText(R.string.debug_list);
        setOnBackClickListener(v->finish());

        listView= (ListView) findViewById(R.id.list);
        final List<DebugItem> debugItems = PrefsManager.readConfig(ListConfigReader.class);
        listView.setAdapter(new DebugListAdapter(this,debugItems));
        listView.setOnItemClickListener((parent, view, position, id) -> {
            DebugItem item = debugItems.get(position);
            String className=item.clazz;
            if(!TextUtils.isEmpty(className)){
                if(className.startsWith(".")){
                    className=PACKAGE_NAME+item.clazz;
                }
                try {
                    Class<?> clazz = Class.forName(className);
                    Fragment fragment= (Fragment) clazz.newInstance();
                    Bundle args=new Bundle();
                    args.putString("title",item.title);
                    fragment.setArguments(args);
                    DeveloperManager.toFragment(this,fragment);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
