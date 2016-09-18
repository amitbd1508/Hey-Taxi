package com.tesseract.taxisharing.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tesseract.taxisharing.R;
import com.tesseract.taxisharing.model.TripHistory;
import com.tesseract.taxisharing.ui.adapter.TripHistoryAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityTripHistory extends AppCompatActivity {
    private List<TripHistory> tripHistories = new ArrayList<TripHistory>();
    RecyclerView recyclerView;
    TripHistoryAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history);

        adapter = new TripHistoryAdapter(tripHistories, this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_trip_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        addItems();
    }

    private void addItems() {
        String url = "http://team-tesseract.xyz/taxishare/get_from_trip_history_user.php";

        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                TripHistory tripHistory = new TripHistory();
                                tripHistory.setLocationFrom(jsonObject.getString("loaction_from"));
                                tripHistory.setLocationTo(jsonObject.getString("location_to"));
                                tripHistory.setDriverName(jsonObject.getString("driver_name"));
                                tripHistory.setTripTime(jsonObject.getString("time"));
                                tripHistory.setAmount(jsonObject.getString("amount"));
                                tripHistory.setPerson(jsonObject.getString("person"));
                                tripHistory.setShared(jsonObject.getString("share"));
                                adapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(sr);
    }
}
