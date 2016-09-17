package com.tesseract.taxisharing.ui.activity;
/*
* library link
* #1 : //https://github.com/akexorcist/Android-GoogleDirectionLibrary
*
*
* */

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.tesseract.taxisharing.R;
import com.tesseract.taxisharing.model.DriverLocation;
import com.tesseract.taxisharing.model.TaxiRequest;
import com.tesseract.taxisharing.model.UserLocation;
import com.tesseract.taxisharing.ui.dependency.DirectionsJSONParser;
import com.tesseract.taxisharing.ui.dependency.JSONParser;
import com.tesseract.taxisharing.util.App;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;

public class UserMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "UserMapsActivity";
    //map settings variable
    public static boolean SET_ZOOM_CONTROL_ENABLED = true;
    public static boolean SET_ZOOM_GESTURES_ENABLED = true;
    public static boolean SET_ZOOM_ALL_GESTURES_ENABLED = true;
    public static float ZOOM = 17.0f;

    public static int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 100;


    //declare view
    ImageButton btnLocationPin;

    //declare variable
    private GoogleMap mMap;
    LocationTracker tracker;
    TrackerSettings settings;

    //location search variable here
    List<String> searchResult;
    EditText etSearchLocation;
    ListView lvSearchList;
    CardView layout_source_destination;
    TextView tvForm, tvTo;
    Button sendRequst;

    CardView layout_response_from_driver;
    TextView tvDriverName, tvCarName;
    Button contactDriver;

    ArrayAdapter<String> lvAdapter;
    List<Address> addressList = null;

    int count = 0;

    ImageView ivMenu;

    //firebase variable
    FirebaseDatabase db;
    DatabaseReference ref;

    FirebaseDatabase driverdb;
    DatabaseReference driverref;

    FirebaseDatabase reqdb;
    DatabaseReference reqref;
    ProgressDialog progressDialog;


    String strEmail;
    String strFullName;
    String strSex;
    String strRating;
    String strImage;

    RangeBar rbPerson;
    CheckBox cbShareRide;

    static double currentLatitude = 21.0, currentLongitude = 90.0;
    static double destinationLatitude = 21.0, destinationLongitude = 90.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_maps);

        rbPerson = (RangeBar) findViewById(R.id.rangebar);
        cbShareRide = (CheckBox) findViewById(R.id.cb_user_map_person);
        //initalize view
        initializeview();
        firebaseInitialization();

        // made change 9/17
        lvAdapter = new ArrayAdapter<String>(this,
                R.layout.item_search, R.id.tv_search_text, searchResult);
        lvSearchList.setAdapter(lvAdapter);


        //code here
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Drawer();


    }


    private void getDatafromSharedPreferences() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        strEmail = preferences.getString(App.heyTaxiUserEmail, "No");
        strFullName = preferences.getString(App.heyTaxiUserFName, "No");
        strSex = preferences.getString(App.heyTaxiUserSex, "No");
        strImage = preferences.getString(App.heyTaxiUserImage, "No");
    }

    public void Drawer() {

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName(strFullName).withEmail(strEmail).
                                withIcon(getResources().getDrawable(R.drawable.hamudi))
                )
                .build();

        final Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withDrawerWidthDp(250)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Payment").withSetSelected(true).withIdentifier(1),
                        new PrimaryDrawerItem().withName("mCredit").withSetSelected(true).withIdentifier(2),
                        new PrimaryDrawerItem().withName("Lost and found").withSetSelected(true).withIdentifier(3),
                        new PrimaryDrawerItem().withName("Profile settings").withSetSelected(true).withIdentifier(4)

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.equals(0)) {

                        } else if (drawerItem.equals(1)) {
                            startActivity(new Intent(UserMapsActivity.this, ActivityPayment.class));
                        } else if (drawerItem.equals(2)) {
                            startActivity(new Intent(UserMapsActivity.this, ActivityCredit.class));

                        } else if (drawerItem.equals(3)) {
                            startActivity(new Intent(UserMapsActivity.this, ActivityLostAndFount.class));

                        } else if (drawerItem.equals(4)) {
                            startActivity(new Intent(UserMapsActivity.this, ActivitySettings.class));
                        }

                        return true;
                    }
                }).withDrawerGravity(Gravity.LEFT)
                .build();
        result.openDrawer();
        result.closeDrawer();
        result.isDrawerOpen();

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (result.isDrawerOpen()) {
                    result.closeDrawer();
                } else {
                    result.openDrawer();
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        setTrakerSettings();
        initalizeTracker();
        startTracking();


        //map settings
        mMap.getUiSettings().setZoomControlsEnabled(SET_ZOOM_CONTROL_ENABLED);
        mMap.getUiSettings().setZoomGesturesEnabled(SET_ZOOM_GESTURES_ENABLED);
        mMap.getUiSettings().setAllGesturesEnabled(SET_ZOOM_ALL_GESTURES_ENABLED);


        //Listener
        contactDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Call Driver", "Chat"};
                AlertDialog.Builder builder = new AlertDialog.Builder(UserMapsActivity.this);
                builder.setTitle("Contact!");
                builder.setCancelable(true);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Call Driver")) {

                        } else if (items[item].equals("Chat")) {

                        }
                    }
                });
                builder.show();
            }

        });

        driverref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mMap.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    DriverLocation pr = child.getValue(DriverLocation.class);
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(pr.getLatitude()), Double.parseDouble(pr.getLongitude())))
                            .title(pr.getName())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi_cab)
                            ));


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        driverref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mMap.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    DriverLocation pr = child.getValue(DriverLocation.class);
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(pr.getLatitude()), Double.parseDouble(pr.getLongitude())))
                            .title(pr.getName())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi_cab)
                            ));


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reqref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    TaxiRequest pr = child.getValue(TaxiRequest.class);
                    if (pr.getEmail().equals(strEmail) && pr.getStatus().equals(App.TAXI_DRIVER_REQUST)) {
                        progressDialog.dismiss();
                        //getdata from driver database and set
                        tvCarName.setText("Alion Premio");
                        tvDriverName.setText(pr.getDriverEmail());
                        layout_response_from_driver.setVisibility(View.VISIBLE);
                        layout_source_destination.setVisibility(View.GONE);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sendRequst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaxiRequest taxiRequest = new TaxiRequest();
                taxiRequest.setName(strFullName);
                taxiRequest.setStatus(App.TAXI_REQUST_YES);
                taxiRequest.setEmail(strEmail);
                taxiRequest.setSourceLatitude(String.valueOf(currentLatitude));
                taxiRequest.setSourceLongitude(String.valueOf(currentLongitude));
                taxiRequest.setDestinationLatitude(String.valueOf(destinationLatitude));
                taxiRequest.setDestinationLongitude(String.valueOf(destinationLongitude));
                taxiRequest.setTime(DateFormat.getTimeInstance().format(new Date()));
                reqref.push().setValue(taxiRequest);
                // prgressbar start  sonet you will impliment a progress bar


                String url = getDirectionsUrl(new LatLng(currentLatitude, currentLongitude), new LatLng(destinationLatitude, destinationLongitude));

                DownloadTask downloadTask = new DownloadTask();

                // Start downloading json data from Google Directions API
                downloadTask.execute(url);

                progressDialog = new ProgressDialog(UserMapsActivity.this);
                progressDialog.setMessage("Waiting for driver response.....");
                progressDialog.setIndeterminate(true);
                progressDialog.show();

            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                destinationLatitude = latLng.latitude;
                destinationLongitude = latLng.longitude;
                Address address = getAddressFromLatLog(new LatLng(currentLatitude, currentLongitude));
                String source = address.getFeatureName() + "," + address.getSubLocality();
                tvForm.setText(source);

                address = getAddressFromLatLog(latLng);
                String destination = address.getFeatureName() + "," + address.getSubLocality();
                tvTo.setText(destination);
                layout_source_destination.setVisibility(View.VISIBLE);
                LatLng mlatLng = new LatLng(destinationLatitude, destinationLongitude);
                mMap.addMarker(new MarkerOptions()
                        .position(mlatLng)
                        .title(tvTo.getText().toString())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.man)
                        ));


            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                layout_source_destination.setVisibility(View.INVISIBLE);
            }
        });
        lvSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LatLng latLng = new LatLng(addressList.get(position).getLatitude(), addressList.get(position).getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(addressList.get(position).getFeatureName())

                );
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM));
                lvSearchList.setVisibility(View.INVISIBLE);
            }
        });

        etSearchLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                lvSearchList.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String location = etSearchLocation.getText().toString();

                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        addressList = geocoder.getFromLocationName(location, 10);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    searchResult.clear();
                    for (int i = 0; i < addressList.size(); i++) {
                        searchResult.add(addressList.get(i).getFeatureName() + "\n" + addressList.get(i).getCountryName());
                    }
                    lvAdapter.notifyDataSetChanged();

                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                lvSearchList.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initalizeTracker() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.


                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_ACCESS_COARSE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
            return;
        }
        tracker = new LocationTracker(this, settings) {

            @Override
            public void onLocationFound(Location location) {
                // Do some stuff when a new location has been found.

                mMap.clear();

                Log.d(TAG, "Location :" + location.describeContents() + location.getSpeed() + "\n" + location.getAltitude() + "\n" + location.getLatitude() + "\n" + location.getLongitude() + "\n" + location.getProvider() + "\n" + location.getAccuracy());

                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
                updateLocation(location);
                updateLocationInMap(location);
                //setLocationInMapFromFireBase();

            }

            @Override
            public void onTimeout() {

                Toast.makeText(getApplicationContext(), "Time out", Toast.LENGTH_SHORT).show();
            }
        };
    }


    private void updateLocationInMap(Location location) {
        LatLng newlocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions()
                        .position(newlocation)
                        .title("You")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.man))

                //reference : http://stackoverflow.com/questions/14811579/how-to-create-a-custom-shaped-bitmap-marker-with-android-map-api-v2
        );

        if (count <= 2) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(newlocation));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM));
        }
        count++;

    }


    /*private void setLocationInMapFromFireBase() {

        driverref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Log.d("location", dataSnapshot.getValue().toString());
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    DriverLocation pr = child.getValue(DriverLocation.class);
                    LatLng latLng = new LatLng(Double.parseDouble(pr.getLatitude()), Double.parseDouble(pr.getLongitude()));
                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(markerName)
                            .snippet("Last update" + userLocation.getTime())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi_cab)
                            ));
                }
                mMap.clear();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

    private void updateLocation(final Location location) {

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.d("location", dataSnapshot.getValue().toString());
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    UserLocation pr = child.getValue(UserLocation.class);


                    if (pr.getEmail().equals(strEmail)) {
                        pr.setLatitude(String.valueOf(location.getLatitude()));
                        pr.setLongitude(String.valueOf(location.getLongitude()));
                        pr.setTime(DateFormat.getTimeInstance().format(new Date()));
                        child.getRef().setValue(pr);

                    }
                    Log.d(TAG, pr.getEmail() + "=" + strEmail);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void setTrakerSettings() {
        settings = new TrackerSettings()
                .setUseGPS(true)
                .setUseNetwork(true)
                .setUsePassive(true)
                .setTimeBetweenUpdates(1000)
                .setMetersBetweenUpdates(1);
    }

    private void startTracking() {
        if (tracker != null) {


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            tracker.startListening();
        }

    }

    private void stopTracking() {
        if (tracker != null)
            tracker.stopListening();
    }

    Address getAddressFromLatLog(LatLng latLng) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        return addresses.get(0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        startTracking();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTracking();
    }
    //methods and other work

    //draw path code

    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);

            }

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while ", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private void firebaseInitialization() {
        db = FirebaseDatabase.getInstance();
        ref = db.getReference(App.userlocations);   //i saved in userlocations
        reqdb = FirebaseDatabase.getInstance();
        reqref = reqdb.getReference(App.taxirequest);
        driverdb = FirebaseDatabase.getInstance();
        driverref = driverdb.getReference(App.taxidriverlocation);
    }

    private void initializeview() {
        btnLocationPin = (ImageButton) findViewById(R.id.btnLocationPin);
        etSearchLocation = (EditText) findViewById(R.id.etLocationSearchbar);
        lvSearchList = (ListView) findViewById(R.id.listView);
        ivMenu = (ImageView) findViewById(R.id.iv_map_drawer);
        sendRequst = (Button) findViewById(R.id.btnRequest);
        tvForm = (TextView) findViewById(R.id.tvFrom);
        tvTo = (TextView) findViewById(R.id.tvTo);


        layout_source_destination = (CardView) findViewById(R.id.layout_source_destination);
        layout_source_destination.setVisibility(View.INVISIBLE);

        tvCarName = (TextView) findViewById(R.id.tvCarName);
        tvDriverName = (TextView) findViewById(R.id.tvDriverName);
        contactDriver = (Button) findViewById(R.id.btnDriverContacat);
        layout_response_from_driver = (CardView) findViewById(R.id.layout_response_from_driver);
        layout_response_from_driver.setVisibility(View.INVISIBLE);


        getDatafromSharedPreferences();

        //initalize variable
        searchResult = new ArrayList<String>();

    }
}
