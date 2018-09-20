package android.bignerdranch.com.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ViewPager mViewPager;
    private final int mNumberOfFragment = 4;
    private Fragment[] fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);


        mViewPager = new ViewPageFragment(this);
        mViewPager.setId(R.id.view_pager);


        final FrameLayout frameLayout = findViewById(R.id.main_fragment_placeholder);
        frameLayout.addView(mViewPager);

        FragmentManager fm = getSupportFragmentManager();
        setInitialFragment();

        mViewPager.setAdapter(new FragmentPagerAdapter(fm){
                @Override
                public Fragment getItem(int position) {
                    return fragments[position];
                }

                @Override
                public int getCount() {
                    return mNumberOfFragment;
                }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                navigation.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });


    }

    private void setInitialFragment() {
        if(fragments == null){
            fragments = new Fragment[mNumberOfFragment];
            fragments[0] = new HomeFragment();
            fragments[1] = new SearchFragment();
            fragments[2] = new ProfileFragment();
            fragments[3] = new MyRecipeFragment();
        }

        mViewPager.setCurrentItem(0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home_button:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.search_navigator_bottom:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.profile_button:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.my_recipe_button:
                mViewPager.setCurrentItem(3);
                break;
        }
        return true;
    }

}
