package tawseel.com.tajertawseel.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
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

import java.util.ArrayList;

import tawseel.com.tajertawseel.R;
import tawseel.com.tajertawseel.activities.DateOfConnectionsData;
import tawseel.com.tajertawseel.activities.HomePickSetActivity;
import tawseel.com.tajertawseel.activities.PostNewGroupActivity;

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

    public void showDialogue() {
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

        dialog.findViewById(R.id.BtnGiveFeedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView== null ){


            DateOfConnectionsData data=(DateOfConnectionsData) getItem(position);
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
                    Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show();
                    showDialogue();
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
