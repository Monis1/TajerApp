package tawseel.com.tajertawseel.activities;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AbdulMoeed on 7/26/2016.
 */
public class DeligateRequest extends BroadcastReceiver {
    RequestQueue requestQueue;


    @Override
    public void onReceive(final Context context, Intent intent) {
        Toast.makeText(context,"Checking Requests",Toast.LENGTH_SHORT).show();
        requestQueue=Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, functions.add + "DeligateRequest.php?id=" + WaitingForAcceptanceActivity.GrpID,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArr = response.getJSONArray("info");
                            for (int i = 0; i < jsonArr.length(); i++) {
                                final JSONObject jsonObj = jsonArr.getJSONObject(i);
                                if (!jsonObj.getString("status").isEmpty() && !jsonObj.getString("status").equals("") && !jsonObj.getString("status").equals("0")) {

                                    Toast.makeText(context, "Deligate Accepted", Toast.LENGTH_SHORT).show();
                                    Intent it = new Intent(context, ChooseDelegateActivity.class);

                                    it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    it.putExtra("DeligateID", jsonObj.getString("status") + "");
                                    it.putExtra("Name", jsonObj.getString("Name") + "");
                                    it.putExtra("CarNo", jsonObj.getString("CarNo") + "");
                                    it.putExtra("CarModel", jsonObj.getString("CarModel") + "");
                                    it.putExtra("Latitude", jsonObj.getString("Latitude"));
                                    it.putExtra("Longitude", jsonObj.getString("Longitude"));
                                    it.putExtra("CarBrand", jsonObj.getString("CarBrand"));
                                    it.putExtra("Contact", jsonObj.getString("Contact"));
                                     context.startActivity(it);
                                    WaitingForAcceptanceActivity.manager.cancel(WaitingForAcceptanceActivity.pendingIntent);
                                    WaitingForAcceptanceActivity.c.finish();
                                    break;


                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        ;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley", "onErrorResponse: " + error);
                    }
                });

        //dummy Adapter
        // groupListView.setAdapter(new DileveryGroupAdapter(DeliveryGroupActivity.this,list));

        requestQueue.add(jsonObjectRequest);

    }

}

