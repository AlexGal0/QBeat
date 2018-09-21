package android.bignerdranch.com.myapplication;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private final int mNumberOfFragment = 4;
    private Fragment[] fragments;
    private ImageButton[] menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



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
               setChecked(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });


    }

    private void setChecked(int position){
        for(int i = 0; i < 4; i++){
            menu[i].setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
        }
        menu[position].setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ce6804")));
    }

    private void setInitialFragment() {
        if(fragments == null){
            fragments = new Fragment[mNumberOfFragment];
            fragments[0] = new HomeFragment();
            fragments[1] = new SearchFragment();
            fragments[2] = new ProfileFragment();
            fragments[3] = new MyRecipeFragment();
        }

        if(menu == null){
            menu = new ImageButton[4];
            menu[0] = findViewById(R.id.imageButton1);
            menu[1] = findViewById(R.id.imageButton2);
            menu[2] = findViewById(R.id.imageButton3);
            menu[3] = findViewById(R.id.imageButton4);

        }

        menu[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0);
            }
        });
        menu[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(1);
            }
        });
        menu[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(2);
            }
        });
        menu[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(3);
            }
        });
        mViewPager.setCurrentItem(0);
        setChecked(0);
    }

}
