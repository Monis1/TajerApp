package tawseel.com.tajertawseel.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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

import tawseel.com.tajertawseel.R;
import tawseel.com.tajertawseel.activities.Delegate;
import tawseel.com.tajertawseel.activities.HomeActivity;
import tawseel.com.tajertawseel.activities.PostGroupActivity;
import tawseel.com.tajertawseel.activities.TajerConfirmationTab;
import tawseel.com.tajertawseel.activities.functions;
import tawseel.com.tajertawseel.adapters.CardDelivevryAdapter;
import tawseel.com.tajertawseel.adapters.DeliveryGroupAdapter2Home;

/**
 * Created by Junaid-Invision on 8/2/2016.
 */
public class CardDeliveryFragment3 extends Fragment {

    private ListView listView;
    private View mRootView;
    private RequestQueue requestQueue;
    ArrayList<CardDeliveryFragment3Data> list = new ArrayList<>();
    ArrayList<Delegate> dlist = new ArrayList<>();
    LinearLayout ll;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_card_delivery2,null,false);
        ll=(LinearLayout) mRootView.findViewById(R.id.llayout);
        //setupContentView();
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupContentView();
    }

    public void setupContentView()
    {      list.clear();
        requestQueue = Volley.newRequestQueue(getActivity());
        listView = (ListView)mRootView.findViewById(R.id.listView);


        final ProgressDialog progress = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_DARK);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage("Loading...");
        progress.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,  functions.add+"CardDeliveryFragment.php?id="+HomeActivity.id
                ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArr=response.getJSONArray("info");

                            for(int i=0;i<jsonArr.length();i++) {
                                final JSONObject jsonObj = jsonArr.getJSONObject(i);
                                CardDeliveryFragment3Data item= new CardDeliveryFragment3Data();
                                item.setName(jsonObj.getString("name"));
                                item.setNoOfOrders(jsonObj.getString("members"));
                                item.setGroupID(jsonObj.getString("groupID"));
                                item.setItemPrice(jsonObj.getString("ItemsPrice"));
                                item.setPriceRange(jsonObj.getString("PriceRange"));
                                item.setConfirmationCode(jsonObj.getString("ConfirmationCode"));
                                item.setStatusCode(jsonObj.getString("StatusCode"));
                                list.add(item);
                                Delegate delegate=new Delegate();
                                //delegate.setLast_deliver(jsonObj.getString("LastDeliver"));
                                delegate.setLatitude(jsonObj.getString("lat"));
                                delegate.setLongitude(jsonObj.getString("long"));
                               // delegate.setRating(Float.parseFloat(jsonObj.getString("rating")));
                                delegate.setMobile(jsonObj.getString("mobile"));
                                delegate.setName(jsonObj.getString("dname"));
                                dlist.add(delegate);
                            }
                            listView.setAdapter(new CardDelivevryAdapter(getActivity(),list,getActivity().getWindow()));

                            progress.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progress.dismiss();
                            if ((e.getClass().equals(TimeoutError.class)) || e.getClass().equals(NoConnectionError.class)){
                                Snackbar.make(ll, "Internet Connection Error", Snackbar.LENGTH_LONG)
                                        .setAction("Reload", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                startActivity(getActivity().getIntent());getActivity().finish();
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
                            Snackbar.make(ll, "Internet Connection Error", Snackbar.LENGTH_LONG)
                                    .setAction("Reload", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            getActivity().finish(); startActivity(getActivity().getIntent());
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



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(),TajerConfirmationTab.class);
    //            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
               i.putExtra("gid",list.get(position).getGroupID()+"");
                i.putExtra("StatusCode",list.get(position).getStatusCode());
                i.putExtra("datetime","12:00 A.M. 2/28/2017");
                i.putExtra("rating","4.5");
                i.putExtra("Name",dlist.get(position).getName());
                i.putExtra("Contact",dlist.get(position).getMobile());
                i.putExtra("Lat",dlist.get(position).getLatitude());
                i.putExtra("Lng",dlist.get(position).getLongitude());
                startActivity(i);
            }
       });
    }
}