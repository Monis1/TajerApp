package tawseel.com.tajertawseel.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tawseel.com.tajertawseel.R;
import tawseel.com.tajertawseel.adapters.DelegatesHomeAdapter;
import tawseel.com.tajertawseel.firebase.FirebaseInstanceIDService;

/**
 * Created by AbdulMoeed on 8/13/2016.
 */
public class DeligateHomeActivity extends BaseActivity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult> {
    static String DeligateID;
    private ViewPager homePager;
    private FrameLayout homeTabLayout;
    private DrawerLayout mDrawerLayout;
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest locationRequest;
    int REQUEST_CHECK_SETTINGS = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.button_screen);
        System.gc();
        Intent i = new Intent(this, UpdateLocation.class);
        startService(i);
        DeligateID=LoginActivity.DeligateID;
        setUpContents();
        setupListeners();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);

    }


    private void setUpContents() {

        boolean CheckLogin=false;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.homeDrawer);
        mDrawerLayout.openDrawer(Gravity.RIGHT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.findViewById(R.id.drawerIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
                    mDrawerLayout.closeDrawer(Gravity.RIGHT);
                else
                    mDrawerLayout.openDrawer(Gravity.RIGHT);

            }
        });
        TextView DeligateID2= (TextView)mDrawerLayout.findViewById(R.id.DeligateID);
        DeligateID2.setText(LoginActivity.DeligateID);
        TextView DeligateName= (TextView)mDrawerLayout.findViewById(R.id.TajerName);
        DeligateName.setText(LoginActivity.email);

        try {
            CheckLogin = getIntent().getExtras().getBoolean("flag");
        }
        catch (Exception e)
        {}
        if (CheckLogin) {
            CheckLogin=false;
            FirebaseMessaging.getInstance().subscribeToTopic("test");
            FirebaseInstanceIDService dd = new FirebaseInstanceIDService();
            String token = FirebaseInstanceId.getInstance().getToken();
            DeligateID = getIntent().getExtras().getString("DeligateID");
            // Toast.makeText(getApplicationContext(),DeligateID,Toast.LENGTH_SHORT).show();
            dd.registerToken(token, DeligateID, Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID) + "");
//            Intent i = new Intent(DeligateHomeActivity.this,UpdateLocation.class);
//            startService(i);
        }


try {

    homePager = (ViewPager) findViewById(R.id.homePager);
    homeTabLayout = (FrameLayout) findViewById(R.id.home_tabLayout);

    homePager.setAdapter(new DelegatesHomeAdapter(getSupportFragmentManager()));

    LayoutInflater inflater = LayoutInflater.from(this);

    View view1 = inflater.inflate(R.layout.tab_text_layout, null, false);
    TextView text = (TextView) view1.findViewById(R.id.tab_text);
    text.setText(getString(R.string.the_map));
    homeTabLayout.addView(view1);
}
catch (Exception e)
{
    Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
}
    }

    public void setupListeners() {
        // mDrawerLayout.findViewById(R.id.option1).setOnClickListener(this);
        //mDrawerLayout.findViewById(R.id.option2).setOnClickListener(this);
        mDrawerLayout.findViewById(R.id.option3).setOnClickListener(this);
        mDrawerLayout.findViewById(R.id.option4).setOnClickListener(this);
        mDrawerLayout.findViewById(R.id.option5).setOnClickListener(this);
        mDrawerLayout.findViewById(R.id.option6).setOnClickListener(this);
        mDrawerLayout.findViewById(R.id.option7).setOnClickListener(this);
        mDrawerLayout.findViewById(R.id.option9).setOnClickListener(this);
        mDrawerLayout.findViewById(R.id.confirmation).setOnClickListener(this);
        // findViewById(R.id.new_button).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.option3) {
            Intent intent = new Intent(DeligateHomeActivity.this, DeligateDateOfConnectionActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.confirmation){
            RunVolley();
        }
        else if (v.getId() == R.id.option4){

            Intent intent = new Intent(DeligateHomeActivity.this,TradersFavouriteActivity.class);
            startActivity(intent);
        }
        else if (v.getId() == R.id.option9){

            SharedPreferences settings;
            SharedPreferences.Editor editor;
            settings =this.getSharedPreferences("deligate", Context.MODE_PRIVATE); //1
            editor = settings.edit(); //2

            editor.putString("id2",null); //3
            editor.commit(); //4
            stopService(new Intent(DeligateHomeActivity.this,UpdateLocation.class));
            finish();
            startActivity(new Intent(DeligateHomeActivity.this,LoginActivity.class));

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );

        result.setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:

                // NO need to show the dialog;

                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().

                    status.startResolutionForResult(DeligateHomeActivity.this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {

                    //failed to show
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {

            if (resultCode == RESULT_OK) {

                Toast.makeText(getApplicationContext(), "Location Enabled", Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(getApplicationContext(), "Location is Disabled. Please Enable Location From Setting", Toast.LENGTH_LONG).show();
            }

        }}


    void RunVolley(){


        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(DeligateHomeActivity.this);
        StringRequest request;
        final ProgressDialog progress = new ProgressDialog(DeligateHomeActivity.this, ProgressDialog.THEME_HOLO_DARK);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage("Loading");
        progress.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,  functions.add+"InfoGroupConfirmation.php?id="+DeligateID,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArr=response.getJSONArray("info");
                            final JSONObject jsonObj = jsonArr.getJSONObject(0);
                            if (jsonObj.getString("name").equals("test786w")){
                                Toast.makeText(DeligateHomeActivity.this,"You have no pending Delivery",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent pending = new Intent(DeligateHomeActivity.this,ConfirmationTab.class);
                                pending.putExtra("ConfirmationCode",jsonObj.getString("ConfirmationCode"));
                                pending.putExtra("GroupID",jsonObj.getString("GroupID"));
                                pending.putExtra("TajerID",jsonObj.getString("TajerID"));
                                pending.putExtra("ItemsPrice",jsonObj.getString("ItemsPrice"));
                                pending.putExtra("PriceRange",jsonObj.getString("PriceRange"));
                                pending.putExtra("TajerContact",jsonObj.getString("TajerContact"));
                                pending.putExtra("StatusCode",jsonObj.getString("StatusCode"));
                                pending.putExtra("TajerName",jsonObj.getString("TajerName"));
                                pending.putExtra("TajerLat",jsonObj.getString("TajerLat"));
                                pending.putExtra("TajerLng",jsonObj.getString("TajerLng"));
                                pending.putExtra("GroupType",jsonObj.getString("GroupType"));
                                startActivity(pending);
                            }




//                                final JSONObject jsonObj = jsonArr.getJSONObject(0);
//
//                                Customer_request_item_data item = new Customer_request_item_data();
//                                item.setPriceRange(jsonObj.getString("PriceRange"));
//                                item.setCustomerEmail(jsonObj.getString("Email"));
//                                item.setCustomerName(jsonObj.getString("UserName"));
//                                item.setCustomerPhone(jsonObj.getString("Mobile"));
//
//                                item.setItemsPrice(jsonObj.getString("ItemsPrice"));
//                                item.setPayMethod(jsonObj.getString("PayMethod"));
//                                item.setOrderProductQuantity(jsonObj.getString("OrderMember"));
//                                item.setOrderID(jsonObj.getString("OrderID");
//                                item.setLatitude(jsonObj.getString("Latitude"));
//                                item.setLongitude(jsonObj.getString("Longitude"));
//                                item.setID(jsonObj.getString("ID"));

                            progress.dismiss();


                        } catch (JSONException e) {

                            e.printStackTrace();
                            progress.dismiss();
                            if ((e.getClass().equals(TimeoutError.class)) || e.getClass().equals(NoConnectionError.class)){
                                Snackbar.make(findViewById(android.R.id.content), "Internet Connection Error", Snackbar.LENGTH_LONG)
                                        .setAction("Undo", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

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
                                    .setAction("Undo", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

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

}