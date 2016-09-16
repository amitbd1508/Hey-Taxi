package com.tesseract.taxisharing.ui.activity;
/*
* library link
* #1 : //https://github.com/akexorcist/Android-GoogleDirectionLibrary
*
*
* */

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
import com.tesseract.taxisharing.model.UserLocation;
import com.tesseract.taxisharing.util.App;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    Button btnLocationPin;

    //declare variable
    private GoogleMap mMap;
    LocationTracker tracker;
    TrackerSettings settings;

    //location search variable here
    List<String> searchResult;
    EditText etSearchLocation;
    ListView lvSearchList;
    ArrayAdapter<String> lvAdapter;
    List<Address> addressList = null;

    ImageView ivMenu;

    //firebase variable
    FirebaseDatabase db;
    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_maps);
        //initalize view
        btnLocationPin = (Button) findViewById(R.id.btnLocationPin);
        etSearchLocation = (EditText) findViewById(R.id.etLocationSearchbar);
        lvSearchList = (ListView) findViewById(R.id.listView);
        ivMenu = (ImageView) findViewById(R.id.iv_map_drawer);


        //initalize variable
        searchResult = new ArrayList<String>();
        lvAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, searchResult);
        lvSearchList.setAdapter(lvAdapter);
        db = FirebaseDatabase.getInstance();
        ref = db.getReference("userlocations");   //i saved in userlocations


        //code here
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Drawer();


    }

    public void Drawer() {

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile))
                )
                .build();

        final Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withDrawerWidthDp(250)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("payment").withSetSelected(true).withIdentifier(1),
                        new PrimaryDrawerItem().withName("mCredit").withSetSelected(true).withIdentifier(2),
                        new PrimaryDrawerItem().withName("Lost and found").withSetSelected(true).withIdentifier(3),
                        new PrimaryDrawerItem().withName("settings").withSetSelected(true).withIdentifier(4)


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

                updateLocation(location);
                updateLocationInMap(location);
                setLocationInMapFromFireBase();

            }

            @Override
            public void onTimeout() {

                Toast.makeText(getApplicationContext(), "Time out", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void setMarker(UserLocation userLocation, String markerName, float zoom) {
        // Add a marker in Sydney and move the camera
        LatLng latLng = new LatLng(Double.parseDouble(userLocation.getLatitude()), Double.parseDouble(userLocation.getLongitude()));
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(markerName)
                .snippet("Last update" + userLocation.getTime())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi_cab)

                ));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera( CameraUpdateFactory.zoomTo( zoom) );
    }

    private void updateLocationInMap(Location location) {
        LatLng newlocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions()
                        .position(newlocation)
                        .title("You")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.man))

                //reference : http://stackoverflow.com/questions/14811579/how-to-create-a-custom-shaped-bitmap-marker-with-android-map-api-v2
        );

        mMap.moveCamera(CameraUpdateFactory.newLatLng(newlocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM));
    }


    private void setLocationInMapFromFireBase() {

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMap.clear();
                //Log.d("location", dataSnapshot.getValue().toString());
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    UserLocation pr = child.getValue(UserLocation.class);
                    setMarker(pr, pr.getUsername(), ZOOM);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateLocation(final Location location) {

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.d("location", dataSnapshot.getValue().toString());
                for (DataSnapshot child : dataSnapshot.getChildren()) {


                    UserLocation pr = child.getValue(UserLocation.class);


                    if (pr.getUsername().equals(App.CURRENT_USER)) {
                        pr.setLatitude(String.valueOf(location.getLatitude()));
                        pr.setLongitude(String.valueOf(location.getLongitude()));
                        pr.setTime(DateFormat.getTimeInstance().format(new Date()));
                        child.getRef().setValue(pr);
                        //Toast.makeText(getApplicationContext(), "Sucessfully Updated", Toast.LENGTH_SHORT).show();
                    }
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
        if (tracker != null)
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

    private void stopTracking() {
        if (tracker != null)
            tracker.stopListening();
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
}
