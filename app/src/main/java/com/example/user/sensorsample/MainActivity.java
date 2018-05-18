package com.example.user.sensorsample;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager m;
    private Sensor mSensor;
    private TextView show, show_squart;
    private ImageView imageView;
    private double thresh = 0.4;
    private double G = 9.8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    public void initialize()
    {
        m = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mSensor = m.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        show = (TextView)findViewById(R.id.show);
        show_squart = (TextView)findViewById(R.id.show_squart);
        imageView = (ImageView)findViewById(R.id.correct);
    }

    @Override
    protected void onResume() {
        super.onResume();
        m.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        m.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x =  sensorEvent.values[0];
        double y =  sensorEvent.values[1];
        double z =  sensorEvent.values[2];

        double result = Math.sqrt(x*x + y*y + z*z);
        show_squart.setText("Result : " + result);

        if ((result < x) && (x < result))
        {
            imageView.setImageResource(R.mipmap.tik);
        }
        else
        {
            imageView.setImageResource(R.mipmap.hata);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

//Accelerometerın sensörünün doğru veri üretme testi: Düz zemine koyulduğunda kontrol edilebilir. (Su terazisi)