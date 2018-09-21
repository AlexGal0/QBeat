package android.bignerdranch.com.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        Usuario user = new Usuario("Carlos Satanas");
        user.addExperience(50);

        TextView nombre = container.findViewById(R.id.username_profile);
        nombre.setText(user.getName());

        ProgressBar bar = container.findViewById(R.id.progressbar_profile);
        bar.setProgress((int) user.getCurrentExperience());

        return v;

    }
}
