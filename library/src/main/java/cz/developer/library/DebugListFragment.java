package cz.developer.library;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.quant.titlebar.TitleBarFragment;


/**
 * Created by cz on 15/12/1.
 */
public class DebugListFragment extends TitleBarFragment {
//    @Id( R.id.list)
    private ListView mListView;


    @Nullable
    @Override
    public View onCreateView(Context context, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitleText(R.string.debug_mode);
        mListView.setDividerHeight(0);
        //
//        mListView.setAdapter(new DebugListAdapter(getActivity(),);
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            Bundle extras = new Bundle();
            extras.putString("title", mListView.getAdapter().getItem(position).toString());
            switch (position) {
                case 0:
//                    DebugAppInfoFragment.class, extras;
                    break;
                case 1:
//                    DebugInfoFragment.class, extras);
                    break;
                case 2:
//                    DebugConfigFragment.class, extras);
                    break;
                case 3:
//                    DebugOtherFragment.class, extras);
                    break;
            }
        });
    }
}
