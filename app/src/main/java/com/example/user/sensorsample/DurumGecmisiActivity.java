package com.example.user.sensorsample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.xml.transform.Result;

public class DurumGecmisiActivity extends AppCompatActivity {

    private Button btn_back;
    private TextView txt_veri, txt_veri1, txt_veri2;
    private  int id, walking_time, stop_time, running_time;
    private List<String> status_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_durum_gecmisi);

        txt_veri = findViewById(R.id.txt_veri);
        txt_veri1 = findViewById(R.id.txt_veri1);
        txt_veri2 = findViewById(R.id.txt_veri2);

        btn_back = findViewById(R.id.btn_back);


        Intent i = getIntent();
        id = i.getIntExtra("id",-1);
        walking_time = i.getIntExtra("walking_time", 0);
        stop_time = i.getIntExtra("stop_time", 0);
        running_time = i.getIntExtra("running_time", 0);

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


    }

}
