package tawseel.com.tajertawseel.adapters;

/**
 * Created by AbdulMoeed on 11/24/2016.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import tawseel.com.tajertawseel.R;
import tawseel.com.tajertawseel.activities.AddNewOrderActivity;
import tawseel.com.tajertawseel.activities.HomeActivity;
import tawseel.com.tajertawseel.activities.WaitingForAcceptanceActivity;
import tawseel.com.tajertawseel.activities.functions;
import tawseel.com.tajertawseel.fragments.CardDeliveryFragment3Data;
import tawseel.com.tajertawseel.fragments.HomeFragment3Data;

import static tawseel.com.tajertawseel.R.id.ll;

public class CardDelivevryAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    ArrayList<CardDeliveryFragment3Data> List;


    public CardDelivevryAdapter(Context c,ArrayList<CardDeliveryFragment3Data> list)
    {
        context = c ;
        inflater = LayoutInflater.from(c);
       List=list;
    }


    @Override
    public int getCount() {
        return List.size();
        // List.size();
    }

    @Override
    public Object getItem(int position) {
        return List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder ;
        if (convertView==null) {
            convertView = inflater.inflate(R.layout.card_delivery_now2, null, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.group_name);
            holder.noOfOrders  = (TextView) convertView.findViewById(R.id.quantity_customer);
            holder.ConfrmatonCode = (TextView)convertView.findViewById(R.id.ConfirmationCode);
            holder.Cancel=(TextView)convertView.findViewById(R.id.textView66);
            holder.ItemPrice = (TextView) convertView.findViewById(R.id.ItemsPrice);
            holder.PriceRange = (TextView) convertView.findViewById(R.id.PriceRange);
            holder.grpID = (TextView) convertView.findViewById(R.id.GroupID);
            convertView.setTag(holder);
        }
        else
            holder=(ViewHolder)convertView.getTag();

        final CardDeliveryFragment3Data data = (CardDeliveryFragment3Data)getItem(position);

        holder.name.setText(data.getName());
        holder.noOfOrders.setText(data.getNoOfOrders());
        holder.ItemPrice.setText(data.getItemPrice());
        holder.PriceRange.setText(data.getPriceRange());
        holder.grpID.setText(data.getGroupID());
       holder.ConfrmatonCode.setText(data.getConfirmationCode());

         holder.Cancel.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 RequestQueue requestQueue;
                 requestQueue = Volley.newRequestQueue(context);
                 final ProgressDialog progress = new ProgressDialog(context, ProgressDialog.THEME_HOLO_DARK);
                 progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                 progress.setMessage("Loading...");
                 progress.show();
                 JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,  functions.add+"cancelorder.php?gid="+ data.getGroupID()
                         ,
                         new Response.Listener<JSONObject>() {
                             @Override
                             public void onResponse(JSONObject response) {
                                 try {
                                         JSONArray jsonArr=response.getJSONArray("info");
                                         final JSONObject jsonObj = jsonArr.getJSONObject(0);
                                         String message=jsonObj.getString("message");
                                     if(message.compareTo("deleted")==0){
                                         Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
                                     }
                                         else
                                     Toast.makeText(context,"Error: Cannot be Cancelled",Toast.LENGTH_SHORT).show();
                                         progress.dismiss();

                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                     progress.dismiss();
                                     if ((e.getClass().equals(TimeoutError.class)) || e.getClass().equals(NoConnectionError.class)){

                                     }
                                 };
                             }
                         },
                         new Response.ErrorListener() {
                             @Override
                             public void onErrorResponse(VolleyError error) {
                                 Log.e("Volley", "Error");
                                 progress.dismiss();
                                 if ((error.getClass().equals(TimeoutError.class)) || error.getClass().equals(NoConnectionError.class)){
                                    }
                             }
                         });

                 int socketTimeout = 3000;//30 seconds - change to what you want
                 RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                 jsonObjectRequest.setRetryPolicy(policy);
                 requestQueue.add(jsonObjectRequest);

             }
         });
//
//
//
//
//        final TextView startDeliveryButton = (TextView)convertView.findViewById(R.id.start_delivery_button);
//
//        startDeliveryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                Intent i = new Intent(context, WaitingForAcceptanceActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                i.putExtra("GroupID",v.getTag()+"");
//                context.startActivity(i);
//
//
//            }
//        });

        return convertView;
    }
}
