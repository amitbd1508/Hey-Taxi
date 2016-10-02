package com.tesseract.taxisharing.ui.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tesseract.taxisharing.R;
import com.tesseract.taxisharing.model.User;
import com.tesseract.taxisharing.ui.dependency.ITaskDoneListener;
import com.tesseract.taxisharing.util.App;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivitySettings extends AppCompatActivity implements ITaskDoneListener {

    ITaskDoneListener iTaskDoneListener;
    EditText etfirstname, etLastName, etPhone, etPersonalId, etEmail;
    TextView etAccount;
    User user;
    String personalId, account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        iTaskDoneListener = this;
        etfirstname = (EditText) findViewById(R.id.et_settings_first_name);
        etLastName = (EditText) findViewById(R.id.et_settings_last_name);
        etPersonalId = (EditText) findViewById(R.id.et_settings_personalid);
        etPhone = (EditText) findViewById(R.id.et_settings_mobile);
        etEmail = (EditText) findViewById(R.id.et_settings_email);
        etAccount = (TextView) findViewById(R.id.account);
        user = new User();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user.email = preferences.getString(App.heyTaxiUserEmail, "ex@ex.com");

        String url = "http://team-tesseract.xyz/taxishare/get_user_by_email.php?user_email=" + user.email;
        url = url.replaceAll(" ", "%20");

        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            user.username = jsonObject.getString("user_fullname");
                            user.image_link = jsonObject.getString("user_image_link");
                            user.sex = jsonObject.getString("user_sex");
                            user.phone = jsonObject.getString("user_mobile");
                            personalId = jsonObject.getString("user_personalid");
                            account = jsonObject.getString("account");
                            iTaskDoneListener.taskDone(true);


                        } catch (JSONException e) {

                            Log.d("EEE", e.getMessage());

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("EEE", error.getMessage());
            }
        });

        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(sr);
    }

    @Override
    public void taskDone(boolean status) {
        etfirstname.setText(user.username);
        etLastName.setText(user.username);
        etPhone.setText(user.phone);
        etPersonalId.setText(personalId);
        etEmail.setText(user.email);
        etAccount.setText("You have :  " + account + "credit");


    }
}
