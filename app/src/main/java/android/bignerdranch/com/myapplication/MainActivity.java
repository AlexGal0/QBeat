package android.bignerdranch.com.myapplication;

import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public static final int mNumberOfFragment = 4;
    private ViewPager mViewPager;
    private Fragment[] fragments;
    private ImageButton[] menu;

    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        Log.i("FLUSH", "OnCreate() MainActivity");

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            finish();
            return;
        }
        if(DataBase.getDataBase().loadLogin == 0)
            DataBase.getDataBase().updateLogin(this);


        progressBar = new ProgressBar(this);
        setContentView(progressBar);

        progressBar.setIndeterminate(true);
    }


    private void setChecked(int position){
        for(int i = 0; i < 4; i++){
            menu[i].setBackgroundTintList(getResources().getColorStateList(R.color.transparent));
        }
        menu[position].setBackgroundTintList(getResources().getColorStateList(R.color.ColorSecundary
        ));
    }

    private void setInitialFragment() {
        if(fragments == null){
            fragments = new Fragment[mNumberOfFragment];
            fragments[0] = new HomeFragment();
            fragments[1] = new SearchFragment();
            fragments[2] = new MyRecipeFragment();
            fragments[3] = new ProfileFragment();
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


    public void updateFrame(){
        if(DataBase.getDataBase().loadLogin == (1<<2) - 1){
            setContentView(R.layout.activity_main);


            mViewPager = new ViewPager(this);
            mViewPager.setId(R.id.view_pager);
            mViewPager.setOffscreenPageLimit(4);


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
    }

    @Override
    public void onBackPressed() {
        return;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
            finish();

    }
}
