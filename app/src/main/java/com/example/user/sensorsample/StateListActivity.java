package com.example.user.sensorsample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class StateListActivity extends AppCompatActivity {
    private ListView listView;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private ArrayList<String> state_list = new ArrayList<String>();
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_list);

        sharedPref = this.getSharedPreferences("sensor", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        Intent i = getIntent();
        id = i.getIntExtra("id",-1);
        getStateList();

        //listView'i tasarımdakiyle bağlıyoruz.
        listView = (ListView)findViewById(R.id.listView1);

        //ArrayAdapter'i hazırlıyoruz.
        ArrayAdapter<String> adapter = new  ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, state_list);

        //listView için hazırladığımız adapter'i ayarlıyoruz.
        listView.setAdapter(adapter);
    }

    public String getSharedPreference(String id) {
        String savedString = sharedPref.getString(""+id, "Kayıt Yok");
        //Log.i(getClass().getSimpleName(), "Okunan : "+ savedString);
        return savedString;
    }

    public void getStateList(){
        for (int i=id; i>=0; i--){
            String s = getSharedPreference(""+ i);

            state_list.add(s);
        }

//        for (String a : state_list) {
//            System.out.println(a);
//       }
    }


}
