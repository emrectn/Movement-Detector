package com.example.user.sensorsample;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager m;
    private Sensor mSensor;
    private TextView show_squart, info, info2;
    private ImageView imageView, state_image;
    private double mAccelCurrent, average_mAccel, end_mAccel, last_mAccel;
    private int period, status, time, id, last_status;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
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

        mAccelCurrent = SensorManager.GRAVITY_EARTH;

        average_mAccel = end_mAccel = period = 0;
        status = -1;
        last_status = id = time = 0;

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();


    }

    @Override
    protected void onResume() {
        super.onResume();
        m.registerListener(this, mSensor, 500000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        m.unregisterListener(this);
    }

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

            info.setText("Curr : " + mAccelCurrent);
            average_mAccel += mAccelCurrent;

            if (period == 10) {
                //Log.i(MainActivity.class.getSimpleName(), "Period : " + period + "--" + Calendar.getInstance().getTime());
                end_mAccel = (average_mAccel * 1.0) / period;
                show_squart.setText("Result : " + end_mAccel);
                period = 0;
                average_mAccel = 0;
                time = time + 2;

                if ((end_mAccel > 9.4) && (end_mAccel < 10.121)) {
                    Log.i(MainActivity.class.getSimpleName(), "Duruyor : " + end_mAccel);
                    state_image.setBackgroundResource(R.drawable.stop);

                    if (status != 0){
                        saveSharedPreference(status, time);
                        time = 0;
                        status = 0;
                    }

                }

                else if((end_mAccel > 10.121) && (end_mAccel < 13.5)){
                    Log.i(MainActivity.class.getSimpleName(), "Walking : " + end_mAccel);
                    state_image.setBackgroundResource(R.drawable.walking);

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
        getSharedPreference(""+id);
        id += 1;

    }

    public void getSharedPreference(String id) {
        String savedString = sharedPref.getString(""+id, "Kayıt Yok");
        Log.i(getClass().getSimpleName(), "Okunan : "+ savedString);
    }
}

//Accelerometerın sensörünün doğru veri üretme testi: Düz zemine koyulduğunda kontrol edilebilir. (Su terazisi)