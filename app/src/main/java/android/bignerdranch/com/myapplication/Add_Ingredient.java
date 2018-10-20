package android.bignerdranch.com.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Add_Ingredient extends FragmentActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_receta_view);


        final EditText name = findViewById(R.id.add_ingredient_name);
        final Button button = findViewById(R.id.add_ingredient_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setEnabled(false);
                if(name.getText().length() == 0){
                    Toast.makeText(view.getContext(), "Nombre vacio", Toast.LENGTH_SHORT).show();
                    button.setEnabled(true);
                    return;
                }

                Ingrediente ingrediente = new Ingrediente(name.getText().toString());

                if(DataBase.getDataBase().isNull()){
                    Toast.makeText(view.getContext(), "Fail to connect with database :(", Toast.LENGTH_SHORT).show();
                    button.setEnabled(true);
                    return;
                }

                DataBase.getDataBase().addIngrediente(ingrediente);
                Toast.makeText(view.getContext(), "Ingrediente: " + name.getText().toString() + " Agregado correctamente" , Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }
}
