package tawseel.com.tajertawseel.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tawseel.com.tajertawseel.CustomBoldTextView;
import tawseel.com.tajertawseel.R;
import tawseel.com.tajertawseel.adapters.ConfirmationTabAdapter;
import tawseel.com.tajertawseel.adapters.TajerConfirmationTabAdapter;

/**
 * Created by Monis on 2/20/2017.
 */

public class TajerConfirmationTab extends BaseActivity {

    ListView mListView;
    TextView tdelivery;
    TextView tprice;
    ArrayList<PostGroupData> list = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tajer_confirmation_tab);
        setupToolbar();
        setupComponents();

    }

    private void setupComponents() {

        mListView = (ListView) findViewById(R.id.mListView);
        tdelivery=(TextView)findViewById(R.id.tdelivery);
        tprice=(TextView)findViewById(R.id.tprice);
        ImageView im2 = (ImageView)findViewById(R.id.BtnDelegateLoc);
        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TajerConfirmationTab.this,ComfirmationActivity.class);
                i.putExtra("Lat",getIntent().getExtras().getString("Lat"));
                i.putExtra("Lng",getIntent().getExtras().getString("Lng"));
                startActivity(i);
            }
        });
        ImageView im = (ImageView)findViewById(R.id.BtnDelegateContact);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+getIntent().getExtras().getString("Contact")));
                if (ActivityCompat.checkSelfPermission(TajerConfirmationTab.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(TajerConfirmationTab.this,"Call Permission Required",Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(callIntent);
            }
        });
        int statuscode = Integer.parseInt(getIntent().getExtras().getString("StatusCode"));
        setstatus(statuscode);
        TextView name = (TextView)findViewById(R.id.DeligateName);
        name.setText(getIntent().getExtras().getString("Name"));
        RatingBar rating=(RatingBar)findViewById(R.id.delegate_ratingbar);
        rating.setRating(Float.parseFloat(getIntent().getExtras().getString("rating")));
        TextView datetime=(TextView)findViewById(R.id.timeStamp);
        datetime.setText(getIntent().getExtras().getString("datetime"));
        showGroupOrders();
    }

    void   setstatus(int statuscode)
    {
        TextView BtnStatus = (TextView)this.findViewById(R.id.BtnStatusGroup) ;

        if (statuscode == 1 ) {

        }else
        {
            BtnStatus.setText("Picked By Deligate");
            BtnStatus.setBackground( getResources().getDrawable(R.drawable.eclipse_green));
            BtnStatus.setTextColor(getResources().getColor(R.color.green2));
        }
    }

    void showGroupOrders()
    {
        RequestQueue requestQueue;
        requestQueue= Volley.newRequestQueue(this);
        final ProgressDialog progress = new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage("Loading...");
        progress.show();
        String file="laporders.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,  functions.add+file+"?id="+getIntent().getExtras().getString("gid"),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArr=response.getJSONArray("info");
                            double delivery=0,price=0;
                            for(int i=0;i<jsonArr.length();i++) {
                                final JSONObject jsonObj = jsonArr.getJSONObject(i);
                                PostGroupData item= new PostGroupData();
                                item.setCustomerEmail(jsonObj.getString("Email"));
                                item.setCustomerName(jsonObj.getString("UserName"));
                                item.setPayMethod(jsonObj.getString("PayMethod"));
                                item.setPriceRange(jsonObj.getString("PriceRange"));
                                item.setCustomerPhone(jsonObj.getString("Mobile"));
                                item.setOrderProductQuantity(jsonObj.getString("OrderMember"));
                                item.setItemsPrice(jsonObj.getString("ItemsPrice"));
                                item.setOrderID(jsonObj.getString("OrderID"));
                                item.setID(jsonObj.getString("ID"));
                                item.setConfirmationCode(jsonObj.getString("ConfirmationCode"));
                                item.setLatitude(jsonObj.getString("Latitude"));
                                item.setLongitude(jsonObj.getString("Longitude"));
                                item.setIsConfirmed(jsonObj.getString("IsConfirmed"));
                                list.add(item);
                                delivery+=Double.parseDouble(jsonObj.getString("PriceRange"));
                                price+=Double.parseDouble(jsonObj.getString("ItemsPrice"));
                            }
                            progress.dismiss();
                            tdelivery.setText(String.valueOf(delivery));
                            tprice.setText(String.valueOf(price));
                            mListView.setAdapter(new TajerConfirmationTabAdapter(TajerConfirmationTab.this,list));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progress.dismiss();
                            Snackbar.make(findViewById(android.R.id.content), "Internet Connection Error", Snackbar.LENGTH_LONG)
                                    .setAction("Reload", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            finish();
                                            startActivity(getIntent());
                                        }
                                    })
                                    .setActionTextColor(Color.RED)

                                    .show();
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
    }



    public void setupToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CustomBoldTextView title = (CustomBoldTextView) toolbar.findViewById(R.id.title_text);
        title.setText("");
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
}
