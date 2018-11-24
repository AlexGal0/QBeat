package android.bignerdranch.com.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class SelectTag extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_tag_layout);

        ArrayList<String> tags = DataBase.getDataBase().currTags;

        final Receta receta = DataBase.getDataBase().receta;

        Collections.sort(tags);


        LinearLayout container = findViewById(R.id.select_tag_container);

        for(String s: tags){
            View view = getLayoutInflater().inflate(R.layout.single_ingredent_list, null);

            TextView name = view.findViewById(R.id.name_ingredient_list);

            name.setText(s);

            container.addView(view);

            view.setOnClickListener(new TagListener(s));
        }

    }


    private class TagListener implements View.OnClickListener {
        String tag;
        public TagListener(String s) {
            tag = s;
        }

        @Override
        public void onClick(View view) {
            DataBase.getDataBase().receta.setTags(tag);
            DataBase.getDataBase().currTags.remove(tag);
            SelectTag.this.finish();
        }
    }
}
