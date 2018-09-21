package android.bignerdranch.com.myapplication;

import android.widget.ImageView;

import java.util.UUID;

public class Ingrediente {
    public UUID id;
    private String name;
    private ImageView image;


    /*
        constructors
     */
    public Ingrediente(){
        this.id = UUID.randomUUID();
    }

    /*
        getters and setters
     */
    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name + "";
    }

    public ImageView getImage(){
        return this.image;
    }

    public void setImage(ImageView image){
        this.image = image;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Receta))
            return false;
        return this.id.equals(((Ingrediente)obj).id);
    }
}
