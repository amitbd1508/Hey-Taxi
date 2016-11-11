package com.tesseract.taxisharing.ui.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tesseract.taxisharing.R;
import com.tesseract.taxisharing.model.OverBridgeLocation;
import com.tesseract.taxisharing.ui.dependency.MyReceiver;

public class MenueActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menue);
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        MyReceiver mReceiver = new MyReceiver (this);
        registerReceiver(mReceiver, filter);
    }

    public void onFindVehicle(View view) {
        startActivity(new Intent(getApplicationContext(),UserMapsActivity.class));
    }

    public void OnLiveTraffic(View view) {
    }

    public void onEmergency(View view) {

        startActivity(new Intent(this,EmergencyActivity.class));
    }

    public void onOverBridge(View view) {
        startActivity(new Intent(this,OverBridgeLocation.class));
    }

    public void OnLAF(View view) {
        //trafic live fild
        startActivity(new Intent(getApplicationContext(),LostAndFoundActivitySC.class));

    }

    public void onAwarness(View view) {
    }

    public void onComplain(View view) {
    }

    public void OnSettings(View view) {
    }
}
