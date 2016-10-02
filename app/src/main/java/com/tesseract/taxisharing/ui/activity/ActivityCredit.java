package com.tesseract.taxisharing.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.tesseract.taxisharing.R;
import com.tesseract.taxisharing.util.App;

public class ActivityCredit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);
        TextView mcr = (TextView) findViewById(R.id.mcredit);
        mcr.setText("My mCredit  : " + App.account);
    }
}
