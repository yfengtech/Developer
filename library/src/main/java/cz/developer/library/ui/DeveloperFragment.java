package cz.developer.library.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.quant.titlebar.TitleBarFragment;

import java.util.List;

import cz.developer.library.DeveloperManager;
import cz.developer.library.R;
import cz.developer.library.adapter.DebugListAdapter;
import cz.developer.library.mode.DebugItem;
import cz.developer.library.xml.ListConfigReader;
import xyqb.library.config.PrefsManager;

public class DeveloperFragment extends TitleBarFragment {
    private static final String PACKAGE_NAME = "cz.developer.library";
    private ListView listView;
    public static Fragment newInstance(){
        return new DeveloperFragment();
    }

    @Override
    public View onCreateView(Context context, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_developer,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView= (ListView) view.findViewById(R.id.list);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitleText(R.string.debug_list);
        setOnBackClickListener(v->getFragmentManager().popBackStack());
        final List<DebugItem> debugItems = PrefsManager.readConfig(ListConfigReader.class);
        listView.setAdapter(new DebugListAdapter(getContext(),debugItems));
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
                    DeveloperManager.toFragment(getActivity(),fragment);
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
