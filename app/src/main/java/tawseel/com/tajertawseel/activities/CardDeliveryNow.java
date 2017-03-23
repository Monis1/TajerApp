package tawseel.com.tajertawseel.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import tawseel.com.tajertawseel.CustomBoldTextView;
import tawseel.com.tajertawseel.R;
import tawseel.com.tajertawseel.adapters.CarDeliveryPagerAdapter;

/**
 * Created by Abdul Moeed on 12/5/2016.
 */

public class CardDeliveryNow extends BaseActivity {
    private ViewPager homePager;
    private TabLayout homeTabLayout;
    private CustomBoldTextView title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_delivery_now);
        setUpToolbar();
        SetUpComponent();

    }

    private void setUpToolbar ()
    {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        title = (CustomBoldTextView)toolbar.findViewById(R.id.title_text);
        title.setText(getString(R.string.drawer_option2));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

    }

    private void SetUpComponent() {
        homePager = (ViewPager) findViewById(R.id.homePager);
        homeTabLayout = (TabLayout) findViewById(R.id.home_tabLayout);
        homePager.setAdapter(new CarDeliveryPagerAdapter(getSupportFragmentManager()));
        homeTabLayout.setupWithViewPager(homePager);
        LayoutInflater inflater = LayoutInflater.from(this);

        // TabLayout.Tab tab1= homeTabLayout.getTabAt(0);

        View view1 = inflater.inflate(R.layout.tab_text_layout, null, false);
        TextView text = (TextView) view1.findViewById(R.id.tab_text);
        text.setText("TajerTawseel");
        homeTabLayout.getTabAt(0).setCustomView(view1);
         View view2 = inflater.inflate(R.layout.tab_text_layout, null, false);
        TextView text2 = (TextView) view2.findViewById(R.id.tab_text);
        text2.setText("TajerLap");
        homeTabLayout.getTabAt(1).setCustomView(view2);
    }
}
