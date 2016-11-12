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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tesseract.taxisharing.R;
import com.tesseract.taxisharing.model.LOF;
import com.tesseract.taxisharing.model.OverBridgeLocation;
import com.tesseract.taxisharing.model.UserSC;
import com.tesseract.taxisharing.util.App;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;

    FirebaseDatabase db;
    DatabaseReference ref;
    EditText etEmail, etPassword;

    String strEmail;
    String strPassword;


    boolean ret = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseDatabase.getInstance();
        ref = db.getReference(App.register);

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
                    editor.commit();

                    startActivity(new Intent(getApplicationContext(), MenueActivity.class));
                    finish();
                }
            }
        });

    }

    public boolean loginSucessfull() {

        //ret = false;
        if (isNotEmpty(etEmail) && isNotEmpty(etPassword)) {
            strEmail = etEmail.getText().toString();
            strPassword = etPassword.getText().toString();
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        UserSC pr = child.getValue(UserSC.class);
                        Log.i("++__",pr.password+etPassword.getText().toString()+" "+pr.email+etEmail.getText().toString());
                        if (pr.password.toString().equals(etPassword.getText().toString()) && pr.email.toString().equals(etEmail.getText().toString()))
                        {
                            ret=true;
                            Log.i("++__","sdgfdfgd");
                            break;
                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    ret=false;
                    Log.i("++__ cancle",ret+"");
                }

            });
            /*Log.i("++__ final return",ret+"");
            return ret;*/
        }
        return ret;
    }

    private boolean isNotEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return true;

        return false;
    }

}

