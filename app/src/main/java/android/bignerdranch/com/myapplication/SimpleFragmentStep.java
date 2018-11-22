package android.bignerdranch.com.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SimpleFragmentStep extends Fragment {
    public static SimpleFragmentStep newInstance(int pos, String description){
        SimpleFragmentStep fragment = new SimpleFragmentStep();

        Bundle bundle = new Bundle();
        bundle.putInt("POSITION", pos);
        bundle.putString("TEXT", description);

        fragment.setArguments(bundle);
        return fragment;
    }

    private TextView num;
    private TextView description;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.single_element_step_play, container, false);

        int pos = getArguments().getInt("POSITION");
        String descrption = getArguments().getString("TEXT");

        num = view.findViewById(R.id.num_step_play);
        description = view.findViewById(R.id.description_step_play);

        num.setText("Paso #" + (pos + 1));
        description.setText(descrption);

        return view;
    }
}
