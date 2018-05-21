package com.example.user.sensorsample;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import de.nitri.gauge.Gauge;

public class GpsActivity extends AppCompatActivity implements LocationListener {

    private TextView txt_speed;
    Gauge gauge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        txt_speed = findViewById(R.id.txt_speed);
        txt_speed.setText("0.0 m/s");

        gauge = findViewById(R.id.gauge);
        gauge.setValue(0);

        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        this.onLocationChanged(null);
    }

    @Override
    public void onLocationChanged(Location location) {

        if( location == null) {
            txt_speed.setText("-.- m/s");

        }
        else {
            float nCurrentSpeed = location.getSpeed();
            if (nCurrentSpeed > 0 ) {

                gauge.moveToValue(nCurrentSpeed);
                Log.i(getClass().getSimpleName(), "CurrentSpeed : " + nCurrentSpeed);
                txt_speed.setText(nCurrentSpeed + " m/s");
            }
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
