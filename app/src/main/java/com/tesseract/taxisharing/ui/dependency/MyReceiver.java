package com.tesseract.taxisharing.ui.dependency;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.tesseract.taxisharing.ui.activity.MenueActivity;
import com.tesseract.taxisharing.util.App;

/**
 * Created by BlackFlag on 11/12/2016.
 */

public class MyReceiver extends BroadcastReceiver {
    static int countPowerOff = 0;
    private Activity activity = null;

    public MyReceiver(Activity activity) {
        this.activity = activity;
    }

    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.v("onReceive", "Power button is pressed.");



        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            countPowerOff++;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            if (countPowerOff >= 4) {
                Log.i("==++","pressed");
                countPowerOff=0;
                Toast.makeText(context, "power button clicked", Toast.LENGTH_LONG)
                        .show();
                sendSMS("01675209053","I am in trouble \n Click To See my location \nhttps://www.google.com.bd/maps/@"+ App.myLocationLat+","+App.myLocationLon+",18z?hl=en");
            }
        }
    }
    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }
}
