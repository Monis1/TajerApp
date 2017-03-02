package tawseel.com.tajertawseel.adapters;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tawseel.com.tajertawseel.R;
import tawseel.com.tajertawseel.activities.ComfirmationActivity;
import tawseel.com.tajertawseel.activities.PostGroupData;
import tawseel.com.tajertawseel.activities.PostGroupListData;
import tawseel.com.tajertawseel.activities.functions;
import tawseel.com.tajertawseel.customviews.ExpandablePanel;

/**
 * Created by Monis on 2/20/2017.
 */

public class TajerConfirmationTabAdapter extends BaseAdapter {

    ArrayList<PostGroupData> List = new ArrayList<>();
    Context context;
    LayoutInflater inflater;
    private RequestQueue requestQueue;

    public TajerConfirmationTabAdapter(Context context,ArrayList<PostGroupData> list) {
        List = list;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public int getCount() {
        return List.size();
    }

    @Override
    public Object getItem(int i) {

            return   List.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View convertview2=convertView;
        final  ViewHolder holder;
        final   PostGroupData data = (PostGroupData) getItem(i);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView  = inflater.inflate(R.layout.tajer_confirmation_tab_list_item,null,false);
            convertview2 = convertView;
            holder.CustomerName = (TextView) convertView.findViewById(R.id.CustomerName);
            holder. panel = (ExpandablePanel)convertView.findViewById(R.id.expandableLayout);
            holder.CustomerEmail = (TextView) convertView.findViewById(R.id.CustomerEmail);
            holder.CustomerPhone = (TextView) convertView.findViewById(R.id.CustomerPhone);
            holder.Price = (TextView) convertView.findViewById(R.id.Price);
            holder.OrderProductQuantity= (TextView)convertView.findViewById(R.id.OrderProductQuantity);
            holder.BtnLoc= (ImageView)convertView.findViewById(R.id.BtnDeligateLoc2);

            holder. productsList = (ListView) convertView.findViewById(R.id.product_list);
            holder.BtnCall= (ImageView) convertView.findViewById(R.id.BtnDeligateCall);
            convertView.setTag(holder);
        }
        else
            holder=(ViewHolder) convertView.getTag();
        holder.CustomerName.setText(data.getCustomerName());
        holder.CustomerEmail.setText(data.getCustomerEmail());
        holder.CustomerPhone.setText(data.getCustomerPhone());
        holder.OrderProductQuantity.setText(data.getOrderProductQuantity());
        holder.BtnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,ComfirmationActivity.class);
                i.putExtra("Lat",data.getLatitude());
                i.putExtra("Lng",data.getLongitude());
                context.startActivity(i);
            }
        });
        holder.BtnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+data.getCustomerPhone()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context,"Call Permission Required",Toast.LENGTH_SHORT).show();
                    return;
                }
                context.startActivity(callIntent);
            }
        });

        int x =Integer.parseInt(data.getIsConfirmed());
        int y = Integer.parseInt(data.getOrderID());
        setstatus(x,convertview2,data.getConfirmationCode(),y,i);

        holder.panel.setOnExpandListener(new ExpandablePanel.OnExpandListener() {
            ViewHolder holder2 = holder;
            @Override
            public void onExpand(View handle, View content) {
                holder2.PriceRangeIcon = (View) content.findViewById(R.id.PriceMark);
                holder2.PriceRange2 = (TextView) content.findViewById(R.id.riyalPrice);
                holder2.ItemsPrice = (TextView) content.findViewById(R.id.ItemsPrice);
                holder2.PriceRangeText = (TextView) content.findViewById(R.id.PriceRange);
                holder2.TotalPrice= (TextView) content.findViewById(R.id.TotalPrice);
                holder2.PayMethod= (TextView)content.findViewById(R.id.PaymentType) ;


                holder2.ItemsPrice.setText(data.getItemsPrice());

                holder2.PriceRangeText.setText(data.getPriceRange());
                holder2.TotalPrice.setText(Double.parseDouble(data.getItemsPrice()) + (Double.parseDouble(data.getPriceRange())) + "");
                if (data.getPayMethod().equals("1"))
                {
                    holder2.PayMethod.setText(R.string.wire_transfer);
                }
                else if (data.getPayMethod().equals("2"))
                {
                    holder2.PayMethod.setText(R.string.payment_on_delivery);
                }
                if (data.getPriceRange().equals("20")) {
                    holder2.PriceRangeIcon.setBackgroundResource(R.drawable.solid_green_circle);
                    holder2.PriceRange2.setText(R.string.ryal_20);

                } else if (data.getPriceRange().equals("30")) {
                    holder2.PriceRangeIcon.setBackgroundResource(R.drawable.orange_circle);
                    holder2.PriceRange2.setText(R.string.ryal30);
                } else if (data.getPriceRange().equals("40")) {
                    holder2.PriceRangeIcon.setBackgroundResource(R.drawable.maroon_circle);
                    holder2.PriceRange2.setText(R.string.ryal40);
                } else if (data.getPriceRange().equals("50")) {
                    holder2.PriceRangeIcon.setBackgroundResource(R.drawable.red_circle);
                    holder2.PriceRange2.setText(R.string.ryal50);
                }


                final ProgressDialog progress = new ProgressDialog(context, ProgressDialog.THEME_HOLO_DARK);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                progress.setMessage("Loading...");
                progress.show();
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, functions.add+"OrderDetails.php?id="+data.getOrderID(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    JSONArray jsonArr=response.getJSONArray("info");
                                    ArrayList<PostGroupListData> list = new ArrayList<>();
                                    for(int i=0;i<jsonArr.length();i++) {
                                        final JSONObject jsonObj = jsonArr.getJSONObject(i);
                                        PostGroupListData item= new PostGroupListData();
                                        item.setProductID(jsonObj.getString("ProductID"));
                                        item.setDescription(jsonObj.getString("Description"));
                                        item.setPrice(jsonObj.getString("Price"));
                                        item.setProductName(jsonObj.getString("ProductName"));
                                        item.setQuantity(jsonObj.getString("Quantity"));
                                        list.add(item);
                                    }
                                    holder.productsList.setAdapter(new DeliveredNowChildItemAdapter(context,list));
                                    progress.dismiss();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progress.dismiss();
                                    if ((e.getClass().equals(TimeoutError.class)) || e.getClass().equals(NoConnectionError.class)){
                                        Snackbar.make(((Activity)context).findViewById(android.R.id.content), "Internet Connection Error", Snackbar.LENGTH_LONG)
                                                .setAction("Reload", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        context.startActivity(((Activity)context).getIntent());  ((Activity)context).finish();
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
                                    Snackbar.make(((Activity)context).findViewById(android.R.id.content), "Internet Connection Error", Snackbar.LENGTH_LONG)
                                            .setAction("Reload", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    context.startActivity(((Activity)context).getIntent());  ((Activity)context).finish();
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


                holder.productsList.setOnTouchListener(new View.OnTouchListener() {
                    // Setting on Touch Listener for handling the touch inside ScrollView
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // Disallow the touch request for parent scroll on touch of child view
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        return false;
                    }
                });


            }

            @Override
            public void onCollapse(View handle, View content) {
            }
        });
        return convertView;
    }

    void   setstatus(int statuscode, View cv, final String s, final int OrderID, final int position)
    {
        TextView BtnStatus = (TextView)cv.findViewById(R.id.BtnStatus) ;

        if (statuscode == 1) {
        }else
        {
            BtnStatus.setText(context.getResources().getString(R.string.delivered2));
            BtnStatus.setBackground( context.getResources().getDrawable(R.drawable.eclipse_green));
            BtnStatus.setTextColor(context.getResources().getColor(R.color.green2));
        }
    }


}
