package cz.developer.library.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

import cz.developer.library.R;
import cz.developer.library.model.DebugItem;


/**
 * Created by cz on 15/12/1.
 * 调试信息列表
 */
public class DebugListAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final ArrayList<DebugItem> items;

    public DebugListAdapter(Context context, List<DebugItem> items) {
        this.inflater=LayoutInflater.from(context);
        this.items=new ArrayList<>();
        if(null!=items){
            this.items.addAll(items);
        }
    }
    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public DebugItem getItem(int i) {
        return this.items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(null==view){
            view=inflater.inflate(R.layout.debug_list_item,viewGroup,false);
            view.setTag(new ViewHolder(view));
        }
        ViewHolder holder= (ViewHolder) view.getTag();
        DebugItem item = getItem(i);
        holder.title.setText(item.title);
        holder.subInfo.setText(item.subInfo);
        return view;
    }

    /**
     * Created by author on 2015/12/01.
     */
    static class ViewHolder {
        private TextView title;
        private TextView subInfo;

        public ViewHolder(View view) {
            title= (TextView) view.findViewById(R.id.tv_title);
            subInfo= (TextView) view.findViewById(R.id.tv_sub_info);
        }
    }
}
