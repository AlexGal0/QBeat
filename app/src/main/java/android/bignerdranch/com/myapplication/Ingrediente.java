package android.bignerdranch.com.myapplication;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.UUID;

public class Ingrediente implements Serializable, Comparable {
    private String id;
    private String name;
    private ImageView image;


    /*
        constructors
     */
    public Ingrediente(){
        this.id = UUID.randomUUID().toString();
    }
    public Ingrediente(String name){
        this();
        this.name = name;
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

    public String getId(){
        return id  + "";
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Ingrediente))
            return false;
        return this.id.equals(((Ingrediente)obj).id);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if(!(o instanceof Ingrediente ))
            throw new IllegalArgumentException();
        return this.name.compareTo(((Ingrediente) o).name);
    }
}
