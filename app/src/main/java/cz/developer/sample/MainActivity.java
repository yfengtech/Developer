package cz.developer.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cz.developer.library.DeveloperManager;
import cz.developer.sample.model.Person;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View view=findViewById(R.id.image);
        view.setTag("http://pic.7y7.com/Uploads/Picture/2016-11-25/5837eeb3dcda3_550_0_water.jpg");
        View button1 = findViewById(R.id.btn1);
        Person person=new Person();
        person.name="name";
        person.age=21;
        person.job="Teacher";
        button1.setTag(person);
        button1.setOnClickListener(v -> startActivity(new Intent(this,ListActivity.class)));
        findViewById(R.id.btn2).setOnClickListener(v -> startActivity(new Intent(this,RecyclerListActivity.class)));
        findViewById(R.id.btn3).setOnClickListener(v -> DeveloperManager.toDeveloper(MainActivity.this));
    }
}
