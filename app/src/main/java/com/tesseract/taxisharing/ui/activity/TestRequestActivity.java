package com.tesseract.taxisharing.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tesseract.taxisharing.R;
import com.tesseract.taxisharing.model.TaxiRequest;
import com.tesseract.taxisharing.util.App;

public class TestRequestActivity extends AppCompatActivity {

    FirebaseDatabase reqdb;
    DatabaseReference reqref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_request);
        reqdb = FirebaseDatabase.getInstance();
        reqref = reqdb.getReference("taxirequest");
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaxiRequest taxiRequest=new TaxiRequest();
                taxiRequest.setName("Amit");
                taxiRequest.setStatus(App.TAXI_REQUST_YES);
                taxiRequest.setEmail("afdf@df");
                taxiRequest.setSourceLatitude("23.752564");
                taxiRequest.setSourceLongitude("90.3632766");
                taxiRequest.setDestinationLatitude("23.755678");
                taxiRequest.setDestinationLongitude("90.363244");
                taxiRequest.setTime("Dummy Time 2");
                reqref.push().setValue(taxiRequest);

            }
        });


    }
}
