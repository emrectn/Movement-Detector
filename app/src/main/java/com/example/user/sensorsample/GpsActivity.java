package com.example.user.sensorsample;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.BoringLayout;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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


        getLocation();
    }

    public Location getLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
            return null;
        }
        LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        Boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isGPSEnabled) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,6000,10,this);
            Location l =lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            this.onLocationChanged(null);
            return l;
        }else {
            Toast.makeText(this, "Please enable GPS", Toast.LENGTH_SHORT).show();
        }
        return null;
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
