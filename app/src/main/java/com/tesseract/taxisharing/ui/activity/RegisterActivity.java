package com.tesseract.taxisharing.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.tesseract.taxisharing.R;
import com.tesseract.taxisharing.model.User;
import com.tesseract.taxisharing.model.UserLocation;
import com.tesseract.taxisharing.model.UserSC;
import com.tesseract.taxisharing.util.App;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    private static final String TAG = "RegisterActivity";
    private static final int RC_SIGN_IN = 9001;


    //view declaretion
    View btnGoogleSignIn, btnFacebookSignIn;
   // ImageView ivUserImage;
    User user;

    EditText etFullName;
   // EditText etLastName;
    EditText etEamil;

    EditText etPassword;
    EditText etNid;
    RadioGroup rgSex;

    String strFullName;
    String strEmai;

    String strSex;
    String strNid;
    String strPassword;
    String strImageLink;


    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;

    FirebaseDatabase db;
    DatabaseReference ref;
    FirebaseDatabase regdb;
    DatabaseReference regref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_sch);
        db = FirebaseDatabase.getInstance();
        ref = db.getReference(App.userlocations);

        regdb = FirebaseDatabase.getInstance();
        regref = db.getReference(App.register);
        //view initialization
        btnGoogleSignIn = findViewById(R.id.google_login_reg);


        etFullName = (EditText) findViewById(R.id.et_reg_full_name);
        //etLastName = (EditText) findViewById(R.id.et_register_last_name);
        etEamil = (EditText) findViewById(R.id.et_reg_email);
       // etMobile = (EditText) findViewById(R.id.et_register_mobile);
        etPassword = (EditText) findViewById(R.id.et_reg_password);
        etNid = (EditText) findViewById(R.id.et_reg_nid);
       // ivUserImage = (ImageView) findViewById(R.id.iv_register_profile_pic);
        rgSex = (RadioGroup) findViewById(R.id.rg_register_sex_sc);


        findViewById(R.id.btn_reg_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do here login check
                if (registerRequest()) {

                    UserLocation userLication = new UserLocation();
                    userLication.setTime(App.dateTimeNow());
                    userLication.setEmail(strEmai);
                    userLication.setLatitude(App.dLat);
                    userLication.setLongitude(App.dlon);
                    userLication.setUsername(strFullName);
                    userLication.setSex(strSex);

                    //firebase user creation
                    ref.push().setValue(userLication);

                    // store to share preference
                    saveUserData();
                    startActivity(new Intent(getApplicationContext(), MenueActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Fill All fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //google signin formalitis
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        //click to fill all info
        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartMethod();
                signIn();
                btnGoogleSignIn.setEnabled(false);
                //btnGoogleSignIn.setVisibility(View.GONE);
            }
        });
        // [END customize_button]
    }

    boolean ret = false;

    public boolean registerRequest() {


        if (isNotEmpty(etFullName) && isNotEmpty(etEamil)
                && isNotEmpty(etNid) && isNotEmpty(etPassword)) {
            strFullName = etFullName.getText().toString();

            Log.d(TAG, strFullName);
            strEmai = etEamil.getText().toString();

            if (rgSex.getCheckedRadioButtonId() == R.id.rb_register_sex_male_sc) {
                strSex = "Male";
            } else if (rgSex.getCheckedRadioButtonId() == R.id.rb_register_sex_female_sc) {
                strSex = "Female";
            }
            strNid = etNid.getText().toString();
            strPassword = etPassword.getText().toString();
            UserSC userSC=new UserSC();
            userSC.email=strEmai;
            userSC.fullName=strFullName;
            userSC.gender=strSex;
            userSC.nid=strNid;
            userSC.password=strPassword;
            if(findViewById(R.id.sw_disiblity_mode).isEnabled())
            {
                userSC.disibalestatus=App.disibilityYes;
            }
            else userSC.disibalestatus=App.disibilityNO;

            try{
                regref.push().setValue(userSC);
            }catch (Exception e)
            {
                return false;
            }
            return  true;
        }
        return false;
    }

    public void saveUserData() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(App.heyTaxiUserLogIn, "Yes");
        editor.putString(App.heyTaxiUserEmail, strEmai);
        Log.d(TAG, strEmai);
        editor.commit();
    }

    void onStartMethod() {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);


            handleSignInResult(result);

        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()+acct.getEmail()+acct.getGivenName()+acct.getIdToken()+acct.getId()+acct.getFamilyName()));

            user = new User();
            user.setFullname(acct.getGivenName() + "  " + acct.getFamilyName());
            //user.setImage_link(acct.getPhotoUrl().toString()); //error
            user.setSex("Male");
            user.setEmail(acct.getEmail());
            user.setUsername(acct.getId());


            /*Picasso.with(getApplicationContext())
                    .load(acct.getPhotoUrl())
                    .into(ivUserImage);*/
            etEamil.setText(acct.getEmail());
            etFullName.setText(acct.getGivenName()+" "+acct.getFamilyName());


            //etLastName.setText(acct.getFamilyName());


        } else {
            // Signed out, show unauthenticated UI.
            //we are not imliment signin otion
        }
    }


    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]

                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]

                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Toast.makeText(RegisterActivity.this, "Failed to fetch info from google", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
        }


    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private boolean isNotEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return true;
        return false;
    }

}
