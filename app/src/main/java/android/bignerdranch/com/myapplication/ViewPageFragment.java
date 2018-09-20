package android.bignerdranch.com.myapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ViewPageFragment extends ViewPager{

    private boolean enable;

    public ViewPageFragment(@NonNull Context context) {
        super(context);
        enable = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(this.enable)
            return super.onTouchEvent(ev);
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(this.enable)
            return super.onInterceptTouchEvent(ev);
        return false;
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

    public void setEnable(boolean b){
        enable = b;
    }

}
