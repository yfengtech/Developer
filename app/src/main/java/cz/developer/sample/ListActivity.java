package cz.developer.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by cz on 1/11/17.
 */

public class ListActivity extends AppCompatActivity {
    private static final String TAG="ListActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ListView listView= (ListView) findViewById(R.id.list_view);
        listView.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,DataProvider.ITEMS));
    }
}
