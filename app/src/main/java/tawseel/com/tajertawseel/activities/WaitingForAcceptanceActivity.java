package tawseel.com.tajertawseel.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tawseel.com.tajertawseel.CustomBoldTextView;
import tawseel.com.tajertawseel.R;
import tawseel.com.tajertawseel.adapters.PostGroupListAdapter;

/**
 * Created by Junaid-Invision on 7/16/2016.
 */
public class WaitingForAcceptanceActivity extends AppCompatActivity implements OnMapReadyCallback {
    static PendingIntent pendingIntent;
    static AlarmManager manager;

    LatLng origin;
    private GoogleMap mMap;
    static    int i=0;
    static String GrpID;
    static Activity c;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptance_waiting);
        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        origin= new LatLng(LocationManage.Lat,LocationManage.Long);
        findViewById(R.id.ButtonCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent alarmIntent = new Intent(this, DeligateRequest.class);

      alarmIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        c=this;
        GrpID=getIntent().getExtras().getString("GroupID");





       setUpToolbar();
        setupMap();
    }


    private void setupMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CustomBoldTextView title = (CustomBoldTextView) toolbar.findViewById(R.id.title_text);
        title.setText(getString(R.string.waiting_for_acceptance));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
        LatLng positionUpdate = origin;
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(positionUpdate, 12);
        mMap.animateCamera(update);


        addMarker(origin.latitude,origin.longitude, "Seller", R.drawable.destination_marker);
        GetDeligates(this.origin);


    }

    private void addMarker(double lat, double lng, String title, int markericon) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions
                .position(new LatLng(lat, lng))
                .title(title)
                .anchor(.5f, 1f).icon(BitmapDescriptorFactory.fromResource(markericon));


        mMap.addMarker(markerOptions);


//        LatLng latLng = new LatLng(lat,lng);
//        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, Application.DEFAULT_ZOOM);
//        mMap.animateCamera(update);

    }


    public void GetDeligates (LatLng origin)
    {
         RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(WaitingForAcceptanceActivity.this);
        final  ProgressDialog progress = new ProgressDialog(WaitingForAcceptanceActivity.this, ProgressDialog.THEME_HOLO_DARK);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage("Loading...");
        progress.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, functions.add+"EligibleDeligates.php?latitude="+this.origin.latitude+"&longitude="+this.origin.longitude+"&GroupID="+GrpID,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArr=response.getJSONArray("info");
                            functions.AvailableDeligates=jsonArr.length()+"";
                            for(int i=0;i<jsonArr.length();i++) {
                                final JSONObject jsonObj = jsonArr.getJSONObject(i);
                                addMarker(Double.parseDouble(jsonObj.getString("Latitude")),Double.parseDouble(jsonObj.getString("Longitude")), jsonObj.getString("Name"), R.drawable.car_marker);
                            }
                            progress.dismiss();
                            start();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progress.dismiss();
                            if ((e.getClass().equals(TimeoutError.class)) || e.getClass().equals(NoConnectionError.class)){
                                Snackbar.make(findViewById(android.R.id.content), "Internet Connection Error", Snackbar.LENGTH_LONG)
                                        .setAction("Reload", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                manager.cancel(pendingIntent);  startActivity(getIntent());finish();
                                            }
                                        })
                                        .setActionTextColor(Color.RED)

                                        .show();}

                        };
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                        progress.dismiss();
                        if ((error.getClass().equals(TimeoutError.class)) || error.getClass().equals(NoConnectionError.class)){
                            Snackbar.make(findViewById(android.R.id.content), "Internet Connection Error", Snackbar.LENGTH_LONG)
                                    .setAction("Reload", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            manager.cancel(pendingIntent);  startActivity(getIntent());finish();
                                        }
                                    })
                                    .setActionTextColor(Color.RED)

                                    .show();}

                    }
                });

        //dummy Adapter
        // groupListView.setAdapter(new DileveryGroupAdapter(DeliveryGroupActivity.this,list));
        int socketTimeout = 3000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        requestQueue.add(jsonObjectRequest);
    }
    public void start() {
   //     Toast.makeText(WaitingForAcceptanceActivity.this,"yes1",Toast.LENGTH_SHORT).show();

        int interval = 20000;


        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);


    }
    @Override
    public void onBackPressed()
    {
        // code here to show dialog

      manager.cancel(pendingIntent);
        finish();
    }
}