package tawseel.com.tajertawseel.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import tawseel.com.tajertawseel.fragments.CardDeliveryFragment3;
import tawseel.com.tajertawseel.fragments.CardDeliveryFragment3Data;
import tawseel.com.tajertawseel.fragments.HomeFragment1;
import tawseel.com.tajertawseel.fragments.pickSetHome1fragment;
import tawseel.com.tajertawseel.fragments.pickSetHome2fragment;

/**
 * Created by Junaid-Invision on 8/2/2016.
 */
public class CarDeliveryPagerAdapter extends FragmentPagerAdapter {
    public CarDeliveryPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if(position ==1)
        {
            return new CardDeliveryFragment3();
        }
        return new pickSetHome2fragment();
    }

    @Override
    public int getCount() {
        return 2;
    }
}
