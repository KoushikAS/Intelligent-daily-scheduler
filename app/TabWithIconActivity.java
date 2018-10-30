package com.example.android.intellischeduler;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class TabWithIconActivity extends AppCompatActivity {

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    ViewPagerAdapter adapter;

    //Fragments

    MondayFragment mondayFragment;
    TuesdayFragment tuesdayFragment;
    WednesdayFragment wednesdayFragment;
    ThursdayFragment thursdayFragment;
    FridayFragment fridayFragment;
    SaturdayFragment saturdayFragment;
    SundayFragment sundayFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_with_icon);
        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);

        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(),false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




    }



    private void setupViewPager(ViewPager viewPager)
    {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mondayFragment = new MondayFragment();
        tuesdayFragment = new TuesdayFragment();
        wednesdayFragment = new WednesdayFragment();
        thursdayFragment = new ThursdayFragment();
        fridayFragment = new FridayFragment();
        saturdayFragment = new SaturdayFragment();
        sundayFragment = new SundayFragment();
        adapter.addFragment(mondayFragment,"Mon");
        adapter.addFragment(tuesdayFragment,"Tue");
        adapter.addFragment(wednesdayFragment,"Wed");
        adapter.addFragment(thursdayFragment,"Thu");
        adapter.addFragment(fridayFragment,"Fri");
        adapter.addFragment(saturdayFragment,"Sat");
        adapter.addFragment(sundayFragment,"Sun");
        viewPager.setAdapter(adapter);
    }

    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        // Associate searchable configuration with the SearchView
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.navigation_new:
                startActivity(new Intent(getApplicationContext(),NewActivity.class));
                return true;
            case R.id.navigation_lulz:
                startActivity(new Intent(getApplicationContext(),TabWithIconActivity.class));
                return true;
            case R.id.navigation_all:
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                return true;
            case R.id.navigation_schedule:
                startActivity(new Intent(getApplicationContext(),ScheduleDisplay.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }}

}
