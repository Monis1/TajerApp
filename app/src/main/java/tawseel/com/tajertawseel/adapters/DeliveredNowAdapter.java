package tawseel.com.tajertawseel.adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tawseel.com.tajertawseel.R;
import tawseel.com.tajertawseel.activities.PostGroupData;
import tawseel.com.tajertawseel.activities.PostGroupListData;
import tawseel.com.tajertawseel.customviews.ExpandablePanel;

/**
 * Created by Junaid-Invision on 8/9/2016.
 */
public class DeliveredNowAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;

    ArrayList< PostGroupData> List = new ArrayList<>();

    private RequestQueue requestQueue;



    public DeliveredNowAdapter(Context c,ArrayList< PostGroupData> list)
    {
        context = c;
        inflater = LayoutInflater.from(c);
        requestQueue = Volley.newRequestQueue(context);
        List=list;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        final PostGroupData data = (PostGroupData) getItem(position);
        if (convertView == null) {
            convertView  = inflater.inflate(R.layout.delivered_now_list_item,null,false);
            holder = new ViewHolder();
            holder.CustomerName = (TextView) convertView.findViewById(R.id.CustomerName);
            holder.CustomerEmail = (TextView) convertView.findViewById(R.id.CustomerEmail);
            holder.CustomerPhone = (TextView) convertView.findViewById(R.id.CustomerPhone);
            holder.OrderProductQuantity= (TextView)convertView.findViewById(R.id.OrderProductQuantity);
            holder.BtnCall = (ImageView) convertView.findViewById(R.id.BtnCustomerContact);
           holder. moreView = (TextView) convertView.findViewById(R.id.moreButton);
            holder. panel = (ExpandablePanel)convertView.findViewById(R.id.expandableLayout);
            holder. productsList = (ListView) convertView.findViewById(R.id.product_list);
            convertView.setTag(holder);
        }
       else
            holder=(ViewHolder) convertView.getTag();
            holder.CustomerName.setText(data.getCustomerName());
            holder.CustomerEmail.setText(data.getCustomerEmail());
            holder.CustomerPhone.setText(data.getCustomerPhone());
            holder.OrderProductQuantity.setText(data.getOrderProductQuantity());
                   holder.BtnCall.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           Intent callIntent = new Intent(Intent.ACTION_CALL);
                           callIntent.setData(Uri.parse("tel:"+data.getCustomerPhone()));
                           if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                               Toast.makeText(context,"Call Permission Required",Toast.LENGTH_SHORT).show();
                               return;
                           }
                           context.startActivity(callIntent);
                       }
                   });
             holder.productsList.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,  "http://192.168.0.100/ms/OrderDetails.php?id="+data.getOrderID(),
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        };
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                });

        //dummy Adapter
        // groupListView.setAdapter(new DileveryGroupAdapter(DeliveryGroupActivity.this,list));
        requestQueue.add(jsonObjectRequest);





        holder.panel.setOnExpandListener(new ExpandablePanel.OnExpandListener() {
             ViewHolder holder2 = holder;
            @Override
            public void onExpand(View handle, View content) {
                holder.moreView.setText(content.getResources().getString(R.string.less));


                    holder2.PriceRangeIcon = (View) content.findViewById(R.id.PriceMark);
                    holder2.PriceRange2 = (TextView) content.findViewById(R.id.riyalPrice);
                    holder2.ItemsPrice = (TextView) content.findViewById(R.id.ItemsPrice);
                    holder2.PriceRangeText = (TextView) content.findViewById(R.id.PriceRange);
                    holder2.TotalPrice= (TextView) content.findViewById(R.id.TotalPrice);
                    holder2.PayMethod= (TextView)content.findViewById(R.id.PaymentType) ;


                holder2.ItemsPrice.setText(data.getItemsPrice());
                holder2.PriceRangeText.setText(data.getPriceRange());
                holder2.TotalPrice.setText(Integer.parseInt(data.getItemsPrice()) + (Integer.parseInt(data.getPriceRange())) + "");
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

            }

            @Override
            public void onCollapse(View handle, View content) {
              holder.  moreView.setText(content.getResources().getString(R.string.more));
            }
        });
        return convertView ;
    }
}