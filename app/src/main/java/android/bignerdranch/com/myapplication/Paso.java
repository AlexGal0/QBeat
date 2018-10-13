package android.bignerdranch.com.myapplication;

import android.text.Editable;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;

public class Paso implements Serializable{
    private String description;
    private ImageView image;
    private long time;

    /*
        constructors
     */
    public Paso(){
    }

    public Paso(String description){
        setDescription(description);
    }


    public Paso(String description, ImageView image, long time){
        this(description);
        setImage(image);
        setTime(time);
    }

    /*
        getters and setters
     */
    public String getDescription() {
        return description;
    }

    public ImageView getImage() {
        return image;
    }

    public long getTime() {
        return time;
    }



    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public void setTime(long time) {
        if(time < 0)
            throw new IllegalArgumentException("Negative Time");
        this.time = time;
    }

}
