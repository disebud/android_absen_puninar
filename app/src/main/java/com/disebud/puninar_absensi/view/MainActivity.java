package com.disebud.puninar_absensi.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.disebud.puninar_absensi.R;
import com.disebud.puninar_absensi.util.SharePrefManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import im.delight.android.location.SimpleLocation;


public class MainActivity extends AppCompatActivity {

    TextView countryNow, kecamatanNow;
    SharePrefManager sharePrefManager;
    SimpleLocation simpleLocation;
    Button btn_check_out,btn_check_in;
    int REQ_PERMISSION = 100;
    double strCurrentLatitude;
    double strCurrentLongitude;
    String strCurrentLatLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kecamatanNow = findViewById(R.id.kecamatanNow);
        countryNow = findViewById(R.id.countryNow);
        sharePrefManager = new SharePrefManager(this);
        btn_check_in = findViewById(R.id.btn_check_in);
        btn_check_out = findViewById(R.id.btn_check_out);

        setPermission();
        setLocation();
        //get nama daerah
        getCurrentLocation();


    }

    @Override
    protected void onResume() {
        super.onResume();
        // make the device update its location
        simpleLocation.beginUpdates();
    }
//
    @Override
    protected void onPause() {
        super.onPause();
        // stop location updates (saves battery)
        simpleLocation.endUpdates();
    }

    public void buttonAction(View view) {
        switch(view.getId()){
            case R.id.btn_check_in: // statement
                Intent detail = new Intent(this, CheckAbsensi.class);
                detail.putExtra("status", "CHECK IN");
                startActivity(detail);
                break;
            case R.id.btn_check_out: //statement
                Intent detailOut = new Intent(this, CheckAbsensi.class);
                detailOut.putExtra("status", "CHECK OUT");
                startActivity(detailOut);
                break;
            case R.id.btn_history: //statement
                Intent detailHistory = new Intent(this, HistoryAbsensi.class);
                startActivity(detailHistory);
                break;
        }
    }


    private void setPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        }
    }

    private void setLocation() {
        simpleLocation = new SimpleLocation(this);

        if (!simpleLocation.hasLocationEnabled()) {
            SimpleLocation.openSettings(this);
        }

        //get location
        strCurrentLatitude = simpleLocation.getLatitude();
        strCurrentLongitude = simpleLocation.getLongitude();

        //set location lat long
        strCurrentLatLong = strCurrentLatitude + "," + strCurrentLongitude;
    }

    private void getCurrentLocation() {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(strCurrentLatitude, strCurrentLongitude, 1);
            if (addressList != null && addressList.size() > 0) {
//                String strCurrentLocation = addressList.get(0).getLocality();
//                tv1.setText(strCurrentLocation);
//                tv1.setSelected(true);
                 sharePrefManager.saveSPString(SharePrefManager.KEY_LAT, ""+addressList.get(0).getLatitude());
                 sharePrefManager.saveSPString(SharePrefManager.KEY_LONG, ""+addressList.get(0).getLongitude());
                 sharePrefManager.saveSPString(SharePrefManager.KEY_COUNTRY, ""+addressList.get(0).getCountryName());
                 sharePrefManager.saveSPString(SharePrefManager.KEY_KEC, ""+addressList.get(0).getLocality());
                 sharePrefManager.saveSPString(SharePrefManager.KEY_ADRESS, ""+addressList.get(0).getAddressLine(0));
                countryNow.setText(addressList.get(0).getCountryName());
                kecamatanNow.setText(addressList.get(0).getLocality());
                countryNow.setSelected(true);
                kecamatanNow.setSelected(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}