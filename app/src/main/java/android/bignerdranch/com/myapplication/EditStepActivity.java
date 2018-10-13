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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_step_view);

        int index = getIntent().getIntExtra(CreateRecipe.TAG_NUMBER_STEP, -1);
        final Paso step = CreateRecipe.pasos.get(index);

        final TextView number = findViewById(R.id.number_edit_step);

        number.setText((index + 1) + "");

        final TextInputEditText description = findViewById(R.id.new_description_step);
        description.setText(step.getDescription());

        Button finish = findViewById(R.id.finish_button_step);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                step.setDescription(description.getText().toString());
                finish();
            }
        });

    }

}
