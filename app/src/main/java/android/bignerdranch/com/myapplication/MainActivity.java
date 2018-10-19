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

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public static final int mNumberOfFragment = 4;
    private ViewPager mViewPager;
    private Fragment[] fragments;
    private ImageButton[] menu;


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            finish();
            return;
        }
        DataBase.getDataBase().setUser();
        DataBase.getDataBase(); // Start Database




        setContentView(R.layout.activity_main);

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Conectando");
        progress.setMessage("Por favor espere mientras conectamos...");
        progress.show();

        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                progress.cancel();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 3000);


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

    @Override
    public void onBackPressed() {
        return;
    }
}
