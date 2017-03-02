package tawseel.com.tajertawseel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tawseel.com.tajertawseel.R;
import tawseel.com.tajertawseel.activities.BaseActivity;
import tawseel.com.tajertawseel.activities.NotificationData;

/**
 * Created by Abdul Moeed on 2/20/2017.
 */

public class NotificationAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<NotificationData> list=new ArrayList<>();
    public NotificationAdapter (Context c,    ArrayList<NotificationData> list)
    {
        context = c;
        inflater = LayoutInflater.from(c);
        this.list=list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup){
            convertView  = inflater.inflate(R.layout.single_notification,null,false);
        TextView NotificationStatus=(TextView)convertView.findViewById(R.id.ProductName);
        TextView DateTime=(TextView)convertView.findViewById(R.id.ProductPrice);
        if(list.get(i).getStatusCode().compareTo("0")==0)
        {
            NotificationStatus.setText("Group "+list.get(i).getGroupName()+" is not assigned yet "+list.get(i).getPrice()+".");
        }
        else if(list.get(i).getStatusCode().compareTo("1")==0)
        {
            NotificationStatus.setText("Group "+list.get(i).getGroupName()+" has been assigned but not picked by Delegate "+list.get(i).getDelegateName()+" "+list.get(i).getPrice()+".");
        }
        else if(list.get(i).getStatusCode().compareTo("2")==0)
        {
            NotificationStatus.setText("Group "+list.get(i).getGroupName()+" has been assigned and picked by Delegate "+list.get(i).getDelegateName()+" "+list.get(i).getPrice()+".");
        }
        else if(list.get(i).getStatusCode().compareTo("3")==0)
        {
            NotificationStatus.setText("Group "+list.get(i).getGroupName()+" has been delivered by Delegate "+list.get(i).getDelegateName()+" "+list.get(i).getPrice()+".");
        }
        DateTime.setText(list.get(i).getDate()+" "+list.get(i).getTime());
        return convertView;
    }
}
