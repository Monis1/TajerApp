package tawseel.com.tajertawseel.adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import tawseel.com.tajertawseel.R;
import tawseel.com.tajertawseel.activities.DateOfConnectionsData;
import tawseel.com.tajertawseel.activities.functions;

/**
 * Created by Junaid-Invision on 8/18/2016.
 */
public class DelegatesDateOfConnectionAdapter extends BaseAdapter {


    private Context context;
    private LayoutInflater li;
    private ArrayList<DateOfConnectionsData> List=new ArrayList<>();
    Window W;

    public DelegatesDateOfConnectionAdapter (Context c,ArrayList<DateOfConnectionsData> list, Window W)
    {
        List=list;
        context =c;
        li = LayoutInflater.from(c);
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


    public void ShowDialogue(final String gid){
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
                StringRequest dataRequest = new StringRequest(Request.Method.GET,  functions.add+"setfeedback.php?id="+ gid+"&type=t&rating="+feedbackStars.getRating(),
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView== null ){
            final DateOfConnectionsData data=(DateOfConnectionsData) getItem(position);
            if(data.getTitle().compareTo("")!=0){
                convertView = li.inflate(R.layout.date_tag,null,false);
                TextView date=(TextView)convertView.findViewById(R.id.dt_title);
                date.setText(data.getTitle());
            }
            else
            {
                convertView = li.inflate(R.layout.date_of_connections_list_item,null,false);
                TextView gname=(TextView)convertView.findViewById(R.id.group_name);
                TextView gid=(TextView)convertView.findViewById(R.id.GroupID);
                TextView time=(TextView)convertView.findViewById(R.id.gtime);
                TextView date=(TextView)convertView.findViewById(R.id.gdate);
                TextView dname=(TextView)convertView.findViewById(R.id.g_dname);
                TextView qc=(TextView) convertView.findViewById(R.id.g_quantity_customer);
                TextView error=(TextView) convertView.findViewById(R.id.error);
                RatingBar bar=(RatingBar)convertView.findViewById(R.id.g_drating);
                gname.setText(data.getGname());
                gid.setText(data.getGid());
                time.setText(data.getTime());
                date.setText(data.getDate());
                dname.setText(data.getDname());
                qc.setText(data.getDelivers());
                bar.setRating(Float.parseFloat(data.getStars()));
                if(data.isError())
                    error.setText("Feedback\nnot provided!");
                error.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ShowDialogue(data.getGid());
                    }
                });
            }



        }
        return convertView;
    }
}
