package com.tesseract.taxisharing.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tesseract.taxisharing.R;
import com.tesseract.taxisharing.util.App;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityLostAndFount extends AppCompatActivity {

    EditText title,descriptin;
    public  final static String TAG="ActivityLostAndFount";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_fount);
        title= (EditText) findViewById(R.id.title);
        descriptin= (EditText) findViewById(R.id.description);


        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "http://team-tesseract.xyz/taxishare/extras/inset_loast_found.php?user_email="+App.CURRENT_USER_EMAIL+"&title="+title.getText().toString()+"&description="+descriptin.getText().toString();


                url = url.replaceAll(" ", "%20");
                Log.e(TAG, url);
                StringRequest sr = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Toast.makeText(ActivityLostAndFount.this, "Sucessfully posted", Toast.LENGTH_SHORT).show();

                                finish();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("GetData", error.getMessage());
                        Toast.makeText(ActivityLostAndFount.this, "Faild to post", Toast.LENGTH_SHORT).show();
                    }
                });

                RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
                rq.add(sr);
            }
        });

    }
}
