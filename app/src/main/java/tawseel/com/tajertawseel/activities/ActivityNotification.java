package tawseel.com.tajertawseel.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tawseel.com.tajertawseel.CustomBoldTextView;
import tawseel.com.tajertawseel.R;
import tawseel.com.tajertawseel.adapters.NotificationAdapter;
import tawseel.com.tajertawseel.adapters.TajerConfirmationTabAdapter;

/**
 * Created by Abdul Moeed on 2/20/2017.
 */

public class ActivityNotification extends BaseActivity {

    ListView mListview;
    ArrayList<NotificationData> list=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        SetUpToolbar();
        mListview = (ListView)this.findViewById(R.id.listView);
        GetData();
    }

    private void GetData()
    {
        RequestQueue requestQueue;
        requestQueue= Volley.newRequestQueue(this);
        final ProgressDialog progress = new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage("Loading...");
        progress.show();
        String file="NotificationDetails.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,  functions.add+file+"?id="+HomeActivity.id,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArr=response.getJSONArray("info");
                            for(int i=0;i<jsonArr.length();i++) {
                                final JSONObject jsonObj = jsonArr.getJSONObject(i);
                                NotificationData item= new NotificationData();
                                item.setDate(jsonObj.getString("Date"));
                                item.setTime(jsonObj.getString("Time"));
                                item.setGroupName(jsonObj.getString("name"));
                                item.setDelegateName(jsonObj.getString("dname"));
                                item.setStatusCode(jsonObj.getString("StatusCode"));
                                item.setPrice(jsonObj.getString("ItemsPrice"));
                                list.add(item);

                            }
                            progress.dismiss();

                            mListview.setAdapter(new NotificationAdapter(ActivityNotification.this,list));
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

    private void SetUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CustomBoldTextView title = (CustomBoldTextView) toolbar.findViewById(R.id.title_text);
        title.setText("Notification");
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
