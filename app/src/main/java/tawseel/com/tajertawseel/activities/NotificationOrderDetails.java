package tawseel.com.tajertawseel.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tawseel.com.tajertawseel.CustomBoldTextView;
import tawseel.com.tajertawseel.R;
import tawseel.com.tajertawseel.adapters.PostGroupListAdapter;

    /**
     * Created by Junaid-Invision on 7/12/2016.
     */
    public class NotificationOrderDetails extends BaseActivity implements OnMapReadyCallback{

        ListView productList;
        private RequestQueue requestQueue;
        ArrayList<PostGroupData> list = new ArrayList<>();;
        private TextView total_orders;
        private GoogleMap mMap=null;
        LatLng origin ;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_notification_order_details);
            requestQueue = Volley.newRequestQueue(this);

            origin= new LatLng(LocationManage.Lat,LocationManage.Long);


            TextView ButtonSave= (TextView)findViewById(R.id.ButtonSave);
         //   Toast.makeText(NotificationOrderDetails.this,"http://192.168.0.100/ms/DeligateAcceptRequest.php?id="+getIntent().getExtras().getString("id")+"&hash=CCB612R&DeligateID="+LoginActivity.DeligateID,Toast.LENGTH_SHORT).show();
            TextView ButtonContinue= (TextView)findViewById(R.id.ButtonAccept);

            ButtonContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ProgressDialog progress = new ProgressDialog(NotificationOrderDetails.this, ProgressDialog.THEME_HOLO_DARK);
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                    progress.setMessage("Loading...");
                    progress.show();
                   SharedPreferences settings;

                    settings = NotificationOrderDetails.this.getSharedPreferences("deligate", Context.MODE_PRIVATE); //1
                    String id  = settings.getString("id2", null);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,  functions.add+"DeligateAcceptRequest.php?id="+getIntent().getExtras().getString("id")+"&hash=CCB612R&DeligateID="+id,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {

                                        JSONArray jsonArr=response.getJSONArray("info");

                                        for(int i=0;i<jsonArr.length();i++) {
                                            final JSONObject jsonObj = jsonArr.getJSONObject(i);
                                            if (jsonObj.names().get(0).equals("success"))
                                            {
                                                if (jsonObj.getString("success").toString().equals("Taken"))
                                                {
                                                    Toast.makeText(NotificationOrderDetails.this,"SomeOne Already Taken",Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                    Toast.makeText(NotificationOrderDetails.this,"You Got. Check your Groups ",Toast.LENGTH_SHORT).show();
                                            }else
                                                Toast.makeText(NotificationOrderDetails.this,"Failed",Toast.LENGTH_SHORT).show();

                                        }
                                        finish();
                                        progress.dismiss();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        progress.dismiss();
                                        Toast.makeText(NotificationOrderDetails.this,"Failed",Toast.LENGTH_SHORT).show();
                                    };
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Volley", "Error");
                                    progress.dismiss();
                                    Toast.makeText(NotificationOrderDetails.this,"Failed",Toast.LENGTH_SHORT).show();
                                }
                            });

                    //dummy Adapter
                    // groupListView.setAdapter(new DileveryGroupAdapter(DeliveryGroupActivity.this,list));
                    int socketTimeout = 3000;//30 seconds - change to what you want
                    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    jsonObjectRequest.setRetryPolicy(policy);
                    requestQueue.add(jsonObjectRequest);
                }
            });
            TextView ButtonCancel= (TextView)findViewById(R.id.ButtonCancel);
            ButtonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            // origin= new LatLng(21.470285, 39.238547);
            LinearLayout ll = (LinearLayout)findViewById(R.id.LayoutAdd) ;
           ll.setVisibility(View.GONE);
            ButtonSave.setVisibility(View.GONE);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            total_orders = (TextView)findViewById(R.id.request_count);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            setUpToolbar();
            setUpComponents();

        }
        public void setUpComponents ()
        {
            productList = (ListView)findViewById(R.id.product_list);
            Toast.makeText(NotificationOrderDetails.this,getIntent().getExtras().getString("id"),Toast.LENGTH_SHORT).show();
            final ProgressDialog progress = new ProgressDialog(NotificationOrderDetails.this, ProgressDialog.THEME_HOLO_DARK);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            progress.setMessage("Loading...");
            progress.show();
            String file;
            Toast.makeText(this,getIntent().getExtras().getString("gtype"),Toast.LENGTH_SHORT).show();
            if(getIntent().getExtras().getString("gtype").compareTo("lap")==0)
                file="laporders.php";
            else
                file="GroupItem.php";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,  functions.add+file+"?id="+getIntent().getExtras().getString("id"),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                JSONArray jsonArr=response.getJSONArray("info");
                                total_orders.setText(jsonArr.length()+"");
                                for(int i=0;i<jsonArr.length();i++) {
                                    final JSONObject jsonObj = jsonArr.getJSONObject(i);
                                    PostGroupData item= new PostGroupData();
                                    item.setPriceRange(jsonObj.getString("PriceRange"));
                                    item.setCustomerEmail(jsonObj.getString("Email"));
                                    item.setCustomerName(jsonObj.getString("UserName"));
                                    item.setCustomerPhone(jsonObj.getString("Mobile"));
                                    item.setItemsPrice(jsonObj.getString("ItemsPrice"));
                                    item.setPayMethod(jsonObj.getString("PayMethod"));
                                    item.setOrderProductQuantity(jsonObj.getString("OrderMember"));
                                    item.setOrderID(jsonObj.getString("OrderID"));
                                    item.setLatitude(jsonObj.getString("Latitude"));
                                    item.setLongitude(jsonObj.getString("Longitude"));
                                    item.setID(jsonObj.getString("ID"));

                                    list.add(item);
                                }
                                for (int i=0;i<list.size();i++)
                                {
                                    addMarker(Double.parseDouble(list.get(i).getLatitude()),Double.parseDouble(list.get(i).getLongitude()),"Customer", R.drawable.person_marker);
                                }
                                productList.setAdapter(new PostGroupListAdapter(NotificationOrderDetails.this,list,"false"));
                                progress.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progress.dismiss();
                                if ((e.getClass().equals(TimeoutError.class)) || e.getClass().equals(NoConnectionError.class)){
                                    Snackbar.make(findViewById(android.R.id.content), "Internet Connection Error", Snackbar.LENGTH_LONG)
                                            .setAction("Reload", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    finish();
                                                    startActivity(getIntent());
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
                                                finish();
                                                startActivity(getIntent());
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
            productList.setOnTouchListener(new View.OnTouchListener() {
                // Setting on Touch Listener for handling the touch inside ScrollView
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

        }


        public void setUpToolbar()
        {
            Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
            CustomBoldTextView title = (CustomBoldTextView)toolbar.findViewById(R.id.title_text);
            title.setText(getString(R.string.create_new_group));
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
            LatLng positionUpdate = new LatLng(origin.latitude,origin.longitude);
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(positionUpdate,13);
            mMap.animateCamera(update);
            addMarker(origin.latitude,origin.longitude,"Deligate", R.drawable.car_marker);
        }

        private void addMarker(double lat, double lng, String title,int markericon) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions
                    .position(new LatLng(lat, lng))
                    .title(title)
                    .anchor(.5f, 1f).icon(BitmapDescriptorFactory.fromResource(markericon));
            mMap.addMarker(markerOptions);

        }
    }


