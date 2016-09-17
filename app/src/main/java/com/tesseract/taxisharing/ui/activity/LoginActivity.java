package com.tesseract.taxisharing.ui.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;
import com.tesseract.taxisharing.R;
import com.tesseract.taxisharing.util.App;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;

    EditText etEmail, etPassword;

    String strEmail;
    String strPassword;
    String strFullName;
    String strSex;
    String strRating;
    String strImage;

    boolean ret = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginSucessfull()) {

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(App.heyTaxiUserLogIn, "Yes");
                    editor.putString(App.heyTaxiUserEmail, strEmail);
                    editor.putString(App.heyTaxiUserFName, strFullName);
                    editor.putString(App.heyTaxiUserSex, strSex);
                    editor.putString(App.heyTaxiUserImage, strImage);
                    editor.commit();

                    startActivity(new Intent(getApplicationContext(), UserMapsActivity.class));
                    finish();
                }
            }
        });

    }

    public boolean loginSucessfull() {

        ret = false;
        if (isNotEmpty(etEmail) && isNotEmpty(etPassword)) {
            strEmail = etEmail.getText().toString();
            strPassword = etPassword.getText().toString();

            String url = "http://team-tesseract.xyz/taxishare/login_check.php";

            Log.d(TAG, url);
            StringRequest sr = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                strFullName = jsonObject.getString("user_fullname");
                                strEmail = jsonObject.getString("user_email");
                                strSex = jsonObject.getString("user_sex");
                                strImage = jsonObject.getString("user_image_link");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (response != null) ret = true;
                            else ret = false;
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("user_email", strEmail);
                    map.put("user_password", strPassword);

                    return map;
                }
            };

            RequestQueue rq = Volley.newRequestQueue(this);
            rq.add(sr);

            return true;
        }
        return false;
    }

    private boolean isNotEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return true;

        return false;
    }

}

