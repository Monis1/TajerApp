package tawseel.com.tajertawseel.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tawseel.com.tajertawseel.CustomBoldTextView;
import tawseel.com.tajertawseel.R;

/**
 * Created by Junaid-Invision on 7/23/2016.
 */
public class PostNewGroupTajerLapActivity extends BaseActivity {
    TextView add,name;
    private RequestQueue requestQueue;
    private StringRequest request;
    ProgressDialog progress;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_group_tajerlap);
        add=(TextView)findViewById(R.id.BtnNext);
        name=(TextView)findViewById(R.id.txt_name) ;
        requestQueue = Volley.newRequestQueue(this);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (functions.isNameTrue(name.getText().toString(),PostNewGroupTajerLapActivity.this)) {
                    final  ProgressDialog progress = new ProgressDialog(PostNewGroupTajerLapActivity.this, ProgressDialog.THEME_HOLO_DARK);
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setMessage("Loading...");
                    progress.show();
                    request = new StringRequest(Request.Method.POST, functions.add+"NewGroupTajerLap.php", new Response.Listener<String>() {
                        //if response
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                try {
                                    if (jsonObject.names().get(0).equals("success")) {

                                        progress.dismiss();
                                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(PostNewGroupTajerLapActivity.this, TajerLapOrder.class);
                                        i.putExtra("GroupID",jsonObject.getString("success"));
                                        startActivity(i);
                                        finish();
                                    } else {
                                        progress.dismiss();
                                        Toast.makeText(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                } catch (JSONException e) {
                                    progress.dismiss();
                                    e.printStackTrace();
                                }

                            } catch (JSONException e) {
                                progress.dismiss();
                                e.printStackTrace();
                            }


                        }// in case error
                    }, new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), "Internet Connection Error", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        //send data to server using POST
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> hashMap = new HashMap<String, String>();

                            hashMap.put("name",name.getText().toString());
                            hashMap.put("hash", HASH.getHash());
                            hashMap.put("TajerID",LoginActivity.LoginID);
                            return hashMap;
                        }
                    };
                    int socketTimeout = 3000;//30 seconds - change to what you want
                    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    request.setRetryPolicy(policy);
                    requestQueue.add(request);


                }

            }
        });

        setUpToolbar();
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
}
