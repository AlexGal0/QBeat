package android.bignerdranch.com.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        Usuario user = DataBase.getDataBase().currentUser;

        TextView nombre = v.findViewById(R.id.username_profile);
        nombre.setText(user.getName());

        ProgressBar bar = v.findViewById(R.id.progressbar_profile);
        bar.setProgress((int) user.getCurrentExperience());

        TextView nivel = v.findViewById(R.id.number_level);
        nivel.setText("" + user.getLevel());

        Button logout = v.findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBase.getDataBase().cleanUser();
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();
            }
        });

        return v;

    }
}