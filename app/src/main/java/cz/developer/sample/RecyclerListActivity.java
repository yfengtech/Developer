package cz.developer.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    public static class SimpleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
        private final LayoutInflater layoutInflater;

        public SimpleAdapter(LayoutInflater layoutInflater) {
            this.layoutInflater = layoutInflater;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder=null;
            if(0==viewType){
                viewHolder= new RecyclerView.ViewHolder(layoutInflater.inflate(android.R.layout.simple_list_item_1,parent,false)){};
            } else if(1==viewType){
                viewHolder= new RecyclerView.ViewHolder(layoutInflater.inflate(R.layout.layout2_item,parent,false)){};
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            int itemViewType = getItemViewType(position);
            if(0==itemViewType){
                TextView textView= (TextView) holder.itemView;
                textView.setTag(DataProvider.ITEMS[position]);
                textView.setText(DataProvider.ITEMS[position]);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            } else if(1==itemViewType){
                TextView textView= (TextView) holder.itemView.findViewById(R.id.text1);
                ImageView imageView= (ImageView) holder.itemView.findViewById(R.id.iv_image);
                textView.setText(DataProvider.ITEMS[position]);
                textView.setTag(DataProvider.ITEMS[position]);

                imageView.setTag(DataProvider.ITEMS[position]);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(layoutInflater.getContext(), "Click:"+holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemViewType(int position) {
            int viewType=0;
            if(0!=position%2){
                viewType=1;
            }
            return viewType;
        }

        @Override
        public int getItemCount() {
            return DataProvider.ITEMS.length;
        }

    }
}
