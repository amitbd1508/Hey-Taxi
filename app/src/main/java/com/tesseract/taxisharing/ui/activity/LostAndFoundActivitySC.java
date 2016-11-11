package com.tesseract.taxisharing.ui.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tesseract.taxisharing.R;
import com.tesseract.taxisharing.model.LOF;
import com.tesseract.taxisharing.model.OverBridgeLocation;
import com.tesseract.taxisharing.ui.adapter.LostAndFoundAdapter;
import com.tesseract.taxisharing.util.App;

import java.util.ArrayList;
import java.util.List;

public class LostAndFoundActivitySC extends AppCompatActivity {

    RecyclerView rv;
    List<LOF>lofs;
    FirebaseDatabase db;
    DatabaseReference ref;
    LOF lof;

    LostAndFoundAdapter lostAndFoundAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost_and_found_sc_main);
        db = FirebaseDatabase.getInstance();
        ref = db.getReference(App.LostAndFound);

        rv= (RecyclerView) findViewById(R.id.lost_and_found_rv);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(linearLayoutManager);
        lofs=new ArrayList<LOF>();
        //lofs.add(new LOF("sdfs","sdfdsf"));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action

            }
        });



        lostAndFoundAdapter=new LostAndFoundAdapter(lofs,getApplicationContext());
        rv.setAdapter(lostAndFoundAdapter);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    LOF pr = child.getValue(LOF.class);
                    lofs.add(pr);
                    lostAndFoundAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    void dummy()
    {
        lof=new LOF();
        lof.setTime("12/11/16 2:08 AM");
        lof.setDescription("I lost my passport in taxi. The Taxi number is 345346");
        lof.setLocation("Dhanmondi 15");
        lof.setType("lost");

        lof.setPhone("01675209053");
        lof.setTitle("Found a Passport");
        ref.push().setValue(lof);

        lof.setTime("11/11/16 2:08 AM");
        lof.setDescription("I find a passport in taxi form dhanmodi 15 no road the passwort number is AE35345");
        lof.setLocation("Dhanmondi 15");
        lof.setType("found");

        lof.setPhone("01675209053");
        lof.setTitle("Lost My passport");
        ref.push().setValue(lof);
    }
}
