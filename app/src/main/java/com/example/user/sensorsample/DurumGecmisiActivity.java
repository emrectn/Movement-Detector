package com.example.user.sensorsample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

public class DurumGecmisiActivity extends AppCompatActivity {

    private Button btn_back, btn_next;
    private TextView txt_veri, txt_veri1, txt_veri2;
    private  int id, walking_time, stop_time, running_time;
    private ArrayList<String> state_list = new ArrayList<String>();
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private String TAG = getClass().getSimpleName();

    private final static String  jString = "{"
            + "    \"geodata\": ["
            + "        {"
            + "                \"id\": \"1\","
            + "                \"name\": \"Julie Sherman\","
            + "                \"gender\" : \"female\","
            + "                \"latitude\" : \"37.33774833333334\","
            + "                \"longitude\" : \"-121.88670166666667\""
            + "                }"
            + "        },"
            + "        {"
            + "                \"id\": \"2\","
            + "                \"name\": \"Johnny Depp\","
            + "                \"gender\" : \"male\","
            + "                \"latitude\" : \"37.336453\","
            + "                \"longitude\" : \"-121.884985\""
            + "                }"
            + "        }"
            + "    ]"
            + "}";
    private static JSONObject jObject = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_durum_gecmisi);

        sharedPref = this.getSharedPreferences("sensor", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        txt_veri = findViewById(R.id.txt_veri);
        txt_veri1 = findViewById(R.id.txt_veri1);
        txt_veri2 = findViewById(R.id.txt_veri2);

        btn_back = findViewById(R.id.btn_back);
        btn_next = findViewById(R.id.btn_next);

        final String JSON_STRING="{\"employee\":{\"name\":\"Sachin\",\"salary\":56000}}";
        try{
            JSONObject emp=(new JSONObject(JSON_STRING)).getJSONObject("employee");
            String empname=emp.getString("name");
            int empsalary=emp.getInt("salary");

            String str="Employee Name:"+empname+"\n"+"Employee Salary:"+empsalary;
            System.out.println(str);

        }catch (Exception e) {e.printStackTrace();}
        //Do when JSON has problem.


        Intent i = getIntent();
        id = i.getIntExtra("id",-1);
        walking_time = i.getIntExtra("walking_time", 0);
        stop_time = i.getIntExtra("stop_time", 0);
        running_time = i.getIntExtra("running_time", 0);
//        state_list = i.getStringArrayListExtra("state_list");
//

        getStateList();
        System.out.println("-------------");
        for (String s : state_list) {
            Log.i(getClass().getSimpleName(), s);
        }
        System.out.println("-------------");


        txt_veri1.setText("Stop Time : " + stop_time);
        txt_veri.setText("Walk Time : " + walking_time);
        txt_veri2.setText("Run Time : " + running_time);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back_intent = new Intent();
                setResult(RESULT_OK, back_intent);
                finish();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent movement_intent = new Intent(DurumGecmisiActivity.this, StateListActivity.class);
                movement_intent.putExtra("id", id);
                startActivity(movement_intent);
            }
        });


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
