package cz.developer.library.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cz.developer.library.R;

/**
 * Created by cz on 1/11/17.
 */

public class DebugItemInfoAdapter extends BaseAdapter {
    private final LayoutInflater layoutInflater;
    private final List<String> fieldItems;

    public DebugItemInfoAdapter(Context context, List<String> fieldItems) {
        this.layoutInflater=LayoutInflater.from(context);
        this.fieldItems = fieldItems;
    }

    @Override
    public int getCount() {
        return fieldItems.size();
    }

    @Override
    public String getItem(int position) {
        return fieldItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(null==convertView){
            convertView=layoutInflater.inflate(R.layout.debug_info_item,parent,false);
        }
        TextView textView= (TextView) convertView;
        textView.setText(getItem(position));
        return convertView;
    }
}
