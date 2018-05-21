package com.example.user.sensorsample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager m;
    private Sensor mSensor;
    private TextView show_squart, info, info2;
    private ImageView imageView, state_image;
    private double mAccelCurrent, average_mAccel, end_mAccel, last_mAccel;
    private int period, status, time, id, last_status;
    private Button btn_veriler, btn_back_start;
    private int stop_time, walking_time, running_time;
    private ArrayList<String> state_list;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, MyService.class));
        setContentView(R.layout.activity_main);
        initialize();

        btn_veriler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent veriler_intent = new Intent(MainActivity.this, DurumGecmisiActivity.class);
                veriler_intent.putExtra("id", id);
                veriler_intent.putExtra("stop_time", stop_time);
                veriler_intent.putExtra("walking_time", walking_time);
                veriler_intent.putExtra("running_time", running_time);
                //veriler_intent.putStringArrayListExtra("state_list", (ArrayList<String>) state_list);

                saveSharedPreference(status, time);
                //getStateList();
                time = 0;
                status = 0;

                startActivity(veriler_intent);
            }
        });

        btn_back_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back_intent = new Intent();
                setResult(RESULT_OK, back_intent);
                finish();

            }
        });
    }

    public void initialize()
    {
        // Biz sensör bilgilerini alabilmemiz için ilk olarak sensör yöneticisi oluşturacağız.
        m = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        // sensörü belirle
        mSensor = m.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        show_squart = (TextView)findViewById(R.id.show_squart);
        info = (TextView)findViewById(R.id.info);
        info2 = (TextView)findViewById(R.id.info2);

        imageView = (ImageView)findViewById(R.id.correct);
        state_image = (ImageView)findViewById(R.id.state_image);

        btn_veriler = findViewById(R.id.btn_veriler);
        btn_back_start = findViewById(R.id.btn_back_start);

        mAccelCurrent = SensorManager.GRAVITY_EARTH;

        average_mAccel = end_mAccel = period = 0;
        status = 0;
        stop_time = walking_time = running_time = last_status = id = time = 0;

        sharedPref = this.getSharedPreferences("sensor", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        state_list = new ArrayList<String>();


    }

    @Override
    protected void onResume() {
        super.onResume();
        m.registerListener(this, mSensor, 500000);
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        m.unregisterListener(this);
//    }

    // Sensör üzerinden gelen bilgilerin tutultuğu fonksiyondur. SensorEvent class’ı altında
    // bir bilgi alır bu bilgi bizim sensörümüzün tüm bilgilerini içermektedir.
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            double x = sensorEvent.values[0];
            double y = sensorEvent.values[1];
            double z = sensorEvent.values[2];

            period += 1;
            mAccelCurrent = Math.sqrt(x * x + y * y + z * z);

            info.setText("Anlık : " + mAccelCurrent);
            average_mAccel += mAccelCurrent;

            //Log.i("İÇERDE", "!!!! : istek oldu"  + Calendar.getInstance().getTime());
            if (period == 12) {
                //Log.i(MainActivity.class.getSimpleName(), "Period : " + period + "--" + Calendar.getInstance().getTime());
                end_mAccel = (average_mAccel * 1.0) / period;
                show_squart.setText("Ort : " + end_mAccel);
                period = 0;
                average_mAccel = 0;
                time = time + 2;

                if ((end_mAccel > 9.4) && (end_mAccel < 10.121)) {
                    Log.i(MainActivity.class.getSimpleName(), "Duruyor : " + end_mAccel);
                    state_image.setBackgroundResource(R.drawable.stop);

                    stop_time += 2;

                    if (status != 0){
                        saveSharedPreference(status, time);
                        time = 0;
                        status = 0;
                    }

                }

                else if((end_mAccel > 10.121) && (end_mAccel < 13.5)){
                    Log.i(MainActivity.class.getSimpleName(), "Walking : " + end_mAccel);
                    state_image.setBackgroundResource(R.drawable.walking);

                    walking_time += 2;

                    if (status != 1){
                        saveSharedPreference(status, time);
                        time = 0;
                        status = 1;
                    }

                }
                else {
                    Log.i(MainActivity.class.getSimpleName(), "Running : " + end_mAccel);
                    state_image.setBackgroundResource(R.drawable.running);
                    imageView.setImageResource(R.mipmap.hata);

                    running_time += 2;

                    if (status != 2){
                        saveSharedPreference(status, time);
                        time = 0;
                        status = 2;
                    }
                }
            }
        }
    }

    // Genelde log tutmak amacıyla kullanılır.
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void saveSharedPreference(int Status, int Time){
        Data data = new Data(Status, "" + Time);
        data.setActivity();

        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString(""+id, json);
        editor.commit();
        id += 1;

    }
}
