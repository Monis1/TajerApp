package tawseel.com.tajertawseel.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
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

import tawseel.com.tajertawseel.R;
import tawseel.com.tajertawseel.activities.ActivityNotification;
import tawseel.com.tajertawseel.activities.DateOfConnectionsActivity;
import tawseel.com.tajertawseel.activities.DateOfConnectionsData;
import tawseel.com.tajertawseel.activities.HomeActivity;
import tawseel.com.tajertawseel.activities.HomePickSetActivity;
import tawseel.com.tajertawseel.activities.NotificationData;
import tawseel.com.tajertawseel.activities.PostNewGroupActivity;
import tawseel.com.tajertawseel.activities.functions;

/**
 * Created by Junaid-Invision on 7/29/2016.
 */
public class DateOfConnectionsAdapter extends BaseAdapter {


    private LayoutInflater inflater;
    private Context context;
    private ArrayList<DateOfConnectionsData> List=new ArrayList<>();
    Window W;

    public DateOfConnectionsAdapter (Context c, ArrayList<DateOfConnectionsData> list, Window W)
    {
        List=list;
        context =c;
        inflater = LayoutInflater.from(c);
        this.W=W;
    }
    @Override
    public int getCount() {
        return List.size();
    }

    @Override
    public Object getItem(int position) {
        return List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void showDialogue(final String gid) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.feedback_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(W.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.BOTTOM;
        lp.dimAmount = 0.3f;
        dialog.show();
        final RatingBar feedbackStars=(RatingBar)dialog.findViewById(R.id.frating);
        dialog.findViewById(R.id.BtnGiveFeedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue requestQueue;
                requestQueue= Volley.newRequestQueue(context);
                final ProgressDialog progress = new ProgressDialog(context, ProgressDialog.THEME_HOLO_DARK);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setMessage("Loading...");
                progress.show();
                StringRequest dataRequest = new StringRequest(Request.Method.GET,  functions.add+"setfeedback.php?id="+ gid+"&type=d&rating="+feedbackStars.getRating(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

                                    progress.dismiss();
                                    Toast.makeText(context,response,Toast.LENGTH_SHORT).show();
                                    dialog.hide();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    progress.dismiss();

                                };
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progress.dismiss();
                            }
                        });
                int socketTimeout = 3000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                dataRequest.setRetryPolicy(policy);
                requestQueue.add(dataRequest);
            }
        });

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView== null ){


            final DateOfConnectionsData data=(DateOfConnectionsData) getItem(position);
        if(data.getTitle().compareTo("")!=0){
        convertView = inflater.inflate(R.layout.date_tag,null,false);
        TextView date=(TextView)convertView.findViewById(R.id.dt_title);
            date.setText(data.getTitle());
        }
        else
        {
            convertView = inflater.inflate(R.layout.date_of_connections_list_item,null,false);
            TextView gname=(TextView)convertView.findViewById(R.id.group_name);
            TextView error=(TextView)convertView.findViewById(R.id.error_msg);
            TextView gid=(TextView)convertView.findViewById(R.id.GroupID);
            TextView time=(TextView)convertView.findViewById(R.id.gtime);
            TextView date=(TextView)convertView.findViewById(R.id.gdate);
            TextView dname=(TextView)convertView.findViewById(R.id.g_dname);
            TextView qc=(TextView) convertView.findViewById(R.id.g_quantity_customer);
            RatingBar bar=(RatingBar)convertView.findViewById(R.id.g_drating);
             if(data.isError())
                error.setText("Feedback not\nprovided!");
            error.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialogue(data.getGid());
                }
            });
            gname.setText(data.getGname());
            gid.setText(data.getGid());
            time.setText(data.getTime());
            date.setText(data.getDate());
            dname.setText(data.getDname());
            qc.setText(data.getDelivers());
            bar.setRating(Float.parseFloat(data.getStars()));
        }



        }

        return convertView;
    }
}
