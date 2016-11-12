package com.tesseract.taxisharing.ui.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tesseract.taxisharing.R;
import com.tesseract.taxisharing.model.LOF;
import com.tesseract.taxisharing.util.App;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewLOFRequest extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference ref;

    TextView lost,found;
    String status="lost";
    LOF lof;
    EditText title,des,phone,loca;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_lofrequest);
        lost= (TextView) findViewById(R.id.tv_forum_lof_lost);
        found= (TextView) findViewById(R.id.tv_forum_lof_found);

        db = FirebaseDatabase.getInstance();
        ref = db.getReference(App.LostAndFound);

        title= (EditText) findViewById(R.id.et_forum_lof_name);
        des= (EditText) findViewById(R.id.et_forum_lof_description);
        phone= (EditText) findViewById(R.id.et_forum_lof_cont_numb);
        loca= (EditText) findViewById(R.id.et_forum_lof_Location);
        lof=new LOF();

        lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status="lost";
                lost.setBackgroundColor(Color.parseColor("#bdbdbd"));
                found.setBackgroundColor(Color.parseColor("#ffffff"));

            }
        });
        found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                status="lost";
                found.setBackgroundColor(Color.parseColor("#bdbdbd"));
                lost.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        });

        findViewById(R.id.btn_forum_lof_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyy:HH:mmss");
                String currentDateandTime = sdf.format(new Date());
                lof.setLocation(loca.getText().toString());
                lof.setDescription(des.getText().toString());
                lof.setPhone(phone.getText().toString());
                lof.setType(status);
                lof.setTime(currentDateandTime);
                lof.setTitle(title.getText().toString());

                ref.push().setValue(lof);


            }
        });
    }
}
