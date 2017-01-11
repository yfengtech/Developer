package cz.developer.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.developer.library.adapter.IAdapterItem;

/**
 * Created by cz on 1/11/17.
 */

public class RecyclerListActivity extends AppCompatActivity {
    private static final String TAG = "RecyclerListActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_list);
        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SimpleAdapter(getLayoutInflater()));
    }

    public static class SimpleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IAdapterItem<String> {
        private final LayoutInflater layoutInflater;

        public SimpleAdapter(LayoutInflater layoutInflater) {
            this.layoutInflater = layoutInflater;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RecyclerView.ViewHolder(layoutInflater.inflate(android.R.layout.simple_list_item_1,parent,false)){};
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            TextView textView= (TextView) holder.itemView;
            textView.setText(DataProvider.ITEMS[position]);
        }

        @Override
        public int getItemCount() {
            return DataProvider.ITEMS.length;
        }

        @Override
        public String getItem(int position) {
            return DataProvider.ITEMS[position];
        }

    }
}
