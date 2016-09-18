package com.tesseract.taxisharing.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.tesseract.taxisharing.ui.dependency.ITaskDoneListener;

import java.util.HashMap;
import java.util.Map;

public class ActivityPayment extends AppCompatActivity implements ITaskDoneListener {

    ITaskDoneListener iTaskDoneListener;
    View layoutMCash;
    CardView cardViewTripDetails;

    TextView tvFrom;
    TextView tvTo;
    TextView tvDriverName;
    TextView tvCarName;
    private ProgressDialog progress;

    public String strFrom = " ";
    public String strTo = " ";
    public String strTime = " ";
    public String strShare = " ";
    public String strPerson = " ";
    public String strPersonEmail = " ";
    public String strDriver = " ";
    public String strDriverEmail = " ";
    public String strCarName = " ";
    public String strUserName = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        iTaskDoneListener = this;
        getPaymentData();

        progress = new ProgressDialog(this);
        progress.setMessage("Loading Data ..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

        layoutMCash = findViewById(R.id.layout_mcash_payment);
        cardViewTripDetails = (CardView) findViewById(R.id.layout_source_destination_payment);

        tvFrom = (TextView) findViewById(R.id.tvFrom_payment);
        tvTo = (TextView) findViewById(R.id.tvTo_payment);
        tvDriverName = (TextView) findViewById(R.id.tvDriverName_payment);
        tvCarName = (TextView) findViewById(R.id.tvCarName_payment);

        if (TripHistory.getInstance().strFrom.equals(" ")) {
            cardViewTripDetails.setVisibility(View.GONE);
        } else {

            tvFrom.setText(strFrom);
            tvTo.setText(strTo);
            tvDriverName.setText(strDriver);
            tvCarName.setText(strCarName);
            layoutMCash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog();
                }
            });
        }


    }

    private void getPaymentData() {
        strFrom = TripHistory.getInstance().strFrom;
        strTo = TripHistory.getInstance().strTo;
        strShare = TripHistory.getInstance().strShare;
        strPerson = TripHistory.getInstance().strPerson;
        strPersonEmail = TripHistory.getInstance().strPersonEmail;
        strDriver = TripHistory.getInstance().strDriver;
        strDriverEmail = TripHistory.getInstance().strDriverEmail;
        strCarName = TripHistory.getInstance().strCarName;
        strTime = TripHistory.getInstance().strTime;
        strUserName = TripHistory.getInstance().strUserName;
    }

    public void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_make_payment_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);


        final EditText popEtAmount = (EditText) dialog.findViewById(R.id.et_amount_make_payment_dialog);
        Button popBtnCancel = (Button) dialog.findViewById(R.id.btn_cancel_make_payment_dialog);
        Button popBtnOk = (Button) dialog.findViewById(R.id.btn_pay_make_payment_dialog);

        popBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        popBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNotEmpty(popEtAmount)) {
                    requestPayment(popEtAmount.getText().toString());
                    progress.show();
                }
            }
        });


        if (dialog != null)
            dialog.show();
    }

    private boolean isNotEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return true;
        return false;
    }

    boolean ret = false;

    public boolean requestPayment(String strAmount) {
        String url = "http://team-tesseract.xyz/taxishare/insert_user_trip_history.php?loaction_from=" + strFrom
                + "&location_to=" + strTo
                + "&time=" + strTime
                + "&person=" + strPerson
                + "&share=" + strShare
                + "&amount=" + strAmount
                + "&driver_name=" + strDriver
                + "&user_email=" + strPersonEmail
                + "&driver_email=" + strDriverEmail
                + "&user_name=" + strUserName;


        url = url.replaceAll(" ", "%20");
        Log.d("*****", url);


        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ActivityPayment.this, response, Toast.LENGTH_SHORT).show();
                        Log.d("*****", response);

                        if (response.equals("0")) ret = false;
                        else iTaskDoneListener.taskDone(true);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ActivityPayment.this, "EEEEEEEEEEEEEEEEEEEEEEEEE", Toast.LENGTH_SHORT).show();
                ret = false;
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                return map;
            }
        };

        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(sr);

        return ret;
    }

    @Override
    public void taskDone(boolean status) {
        if (status) {
            progress.dismiss();
            startActivity(new Intent(getApplicationContext(), UserMapsActivity.class));
            finish();
        } else Toast.makeText(ActivityPayment.this, "Payment faild", Toast.LENGTH_SHORT).show();
    }


}

