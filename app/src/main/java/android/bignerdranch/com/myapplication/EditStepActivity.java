package android.bignerdranch.com.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EditStepActivity extends FragmentActivity{

    private Paso step;
    private TextInputEditText description;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_step_view);

        int index = getIntent().getIntExtra(CreateRecipe.TAG_NUMBER_STEP, -1);
        step = DataBase.getDataBase().receta.getPasos().get(index);

        final TextView number = findViewById(R.id.number_edit_step);

        number.setText((index + 1) + "");

        description = findViewById(R.id.new_description_step);
        if(step.getDescription() != null && step.getDescription().trim().length() != 0)
            description.setText(step.getDescription());

        Button finish = findViewById(R.id.finish_button_step);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                step.setDescription(description.getText().toString().trim());
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        step.setDescription(description.getText().toString().trim());
        finish();
    }
}
