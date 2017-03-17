package the31st.my_pharmacy;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION_CODES.M;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,

        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    String pres, patient_sick, stage, dr_Name, dr_Email, adress, kin_Name, kin_Email, kin_Number;

    FirebaseAuth mAuth;

    private static final int PROXIMITY_RADIUS = 5000;
    protected GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Marker mCurrLocationMarker;
    public Location mLastLocation;
    MapFragment mMapFragment;
    private Button nButton;
    private Button mDummyBtn;
    LatLng myPosition;

    double latitude, longitude;

    private GoogleMap mMap;


    TextView title;

    private static String URLDATA = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-26.2611766,27.9495675&radius=5000&type=pharmacy&sensor=true&key=AIzaSyATuUiZUkEc_UgHuqsBJa1oqaODI-3mLs0";
    private RecyclerView recyclerView;
    private List<List_Item> listItems;
    private RecyclerView.Adapter adapter;

    private String googlesearchUrl;

    ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        if (android.os.Build.VERSION.SDK_INT >= M) {
            checkLocationPermission();
        }

//        mDummyBtn = (Button) findViewById(R.id.dummy);
//
//        mDummyBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//
//            }
//        });


        //Check if Google Play Services Available or not
        if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        } else {
            Log.d("onCreate", "Google Play Services available.");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String nextDis = getIntent().getStringExtra("next");
        Toast.makeText(getApplicationContext(), nextDis, Toast.LENGTH_SHORT).show();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String data = prefs.getString("pharmacy_place_name", "no_id"); //no id: default value

        String placedata = getIntent().getStringExtra("clickedDataItem.getHead()");
        Toast.makeText(getApplicationContext(), "Place: " + data, Toast.LENGTH_SHORT).show();

        System.out.println();
//        if (nextDis == null) {

//            startActivity(new Intent(getApplicationContext(), landing_page.class));

//        }

    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        Button btnLibrary = (Button) findViewById(R.id.btnPharma);
        btnLibrary.setOnClickListener(new View.OnClickListener() {
            String Pharmacy = "pharmacy";

            @Override
            public void onClick(View v) {
                Log.d("onClick", "Button is Clicked");
                mMap.clear();
                String url = getUrl(latitude, longitude, Pharmacy);
                Object[] DataTransfer = new Object[2];
                DataTransfer[0] = mMap;
                DataTransfer[1] = url;
                Log.d("onClick", url);
                GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                getNearbyPlacesData.execute(DataTransfer);
                Toast.makeText(getApplicationContext(), "Nearby Pharmacies", Toast.LENGTH_LONG).show();
            }
        });

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyATuUiZUkEc_UgHuqsBJa1oqaODI-3mLs0");
        Log.d("getUrl", googlePlacesUrl.toString());
        System.out.println("Get This: " + googlePlacesUrl.toString());

        googlesearchUrl = googlePlacesUrl.toString();
        nearbyPlacesList(googlePlacesUrl.toString());

        return (googlePlacesUrl.toString());
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.snippet("I am here ...");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(latLng, 11);
        mMap.animateCamera(camera);

//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        Toast.makeText(getApplicationContext(), "Your Current Location", Toast.LENGTH_LONG).show();

        Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f", latitude, longitude));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }
        Log.d("onLocationChanged", "Exit");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }


    }

    public void nearbyPlacesList(String url) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data ...");
        progressDialog.show();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerList);

        recyclerView.setHasFixedSize(true);

//        LinearLayoutManager layoutManager
//                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


//        new GridLayoutManager(MainActivity.this, 4);

        listItems = new ArrayList<>();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("results");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject o = jsonArray.getJSONObject(i);

                                List_Item item = new List_Item(o.getString("name"), o.getString("vicinity"));
                                listItems.add(item);
                                System.out.println("My List Item: " + listItems);


//                                JSONArray ingArray = o.getJSONArray("photos");
//
//                                // getting inner array photos
////                                JSONArray ja = jsonObject.getJSONArray("photos");
//
//                                for(int j=0; j<ingArray.length(); j++) {
//
//                                    JSONObject ingredObject= ingArray.getJSONObject(j);
////                                    JSONObject json = ja.getJSONObject(j);
//                                    String ingName = ingredObject.getString("photo_reference");
//
//                                    String pic = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+ingName+"&key=AIzaSyBaC4lOH6Y5mMsP0JSYRF_B03zbWwFvC-Y";
//                                    mImageView.setImageURI(Uri.parse(pic));
//
//
//                                    ArrayList<String> list = new ArrayList<>();
//                                    list.add(ingName);
//
//
//                                    System.out.println("ArrayList JSON Items: " +  list );
//
//                                }


                            }

                            adapter = new MyAdapter(listItems, getApplicationContext());
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    public void readDetails(String place) {

        mAuth = FirebaseAuth.getInstance();

        pres = getIntent().getStringExtra("prescription");
        patient_sick = getIntent().getStringExtra("sickness");
        stage = getIntent().getStringExtra("stage_sickness");
        dr_Name = getIntent().getStringExtra("dr_name");
        dr_Email = getIntent().getStringExtra("dr_email");
        adress = getIntent().getStringExtra("address");
        kin_Name = getIntent().getStringExtra("kin_name");
        kin_Email = getIntent().getStringExtra("kin_email");
        kin_Number = getIntent().getStringExtra("kin_number");

        Intent antent = new Intent(Intent.ACTION_SENDTO);
        antent.setData(Uri.parse("mailto:" + "sk.manana@gmail.com")); // only email apps should handle this
        antent.putExtra(Intent.EXTRA_SUBJECT, "Patient Prescription");
        antent.putExtra(Intent.EXTRA_TEXT, "Greetings " + place + " Pharmacy\n\n" + "Patient Prescription: " + pres + "\nSick Type: " + patient_sick + "\nSick Stage: " + stage + "\nAddress: " + adress
                + "\nDoctor's Name: " + dr_Name + "\nDoctor's Email: " + dr_Email + "\nNext of Kin Name: " + kin_Name + "\nNext of Kin Email: " + kin_Email
                + "\nNext of Kin Number: " + kin_Number + "\n\nKind Regards" + "\n" + mAuth.getCurrentUser().getDisplayName());
        if (antent.resolveActivity(getPackageManager()) != null) {
            startActivity(antent);
        }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }


}
