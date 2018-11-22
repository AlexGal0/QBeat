package android.bignerdranch.com.myapplication;

import android.support.annotation.NonNull;
import android.util.MonthDisplayHelper;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.UUID;

public class Usuario implements Comparable{
    public final static double MAX_EXP = 100.0;
    public String id;
    private String name;
    private String email;
    private int level;
    private double currentExperience;
    private String imageReference;
    private byte[] image;

    /*
        constructor
     */

    public Usuario(){
        id = UUID.randomUUID().toString();
        level = 0;
        currentExperience = 0;
    }
    public Usuario(String email, String name){
        this();
        this.name = name;
        this.email = email;
    }

    public Usuario(Usuario usuario){
        this.id = usuario.id;
        this.level = usuario.level;
        this.currentExperience = usuario.currentExperience;
        this.email = usuario.email;
        this.imageReference = usuario.imageReference;
        this.name = usuario.name;
    }



    /*
        getters and setters
     */

    public String getImageReference(){
        return imageReference;
    }
    public String getName() {
        return name;
    }


    public String getEmail() {
        return email;
    }


    public int getLevel() {
        return level;
    }


    public double getCurrentExperience() {
        return currentExperience;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageReference(String imageReference) {
        this.imageReference = imageReference;
    }


    public boolean addExperience(double exp){
        if(Double.isInfinite(exp))
            throw new IllegalArgumentException("Infinite Experience");
        if(Double.isNaN(exp))
            throw new IllegalArgumentException("Non double experience");
        if(exp < 0)
            throw new IllegalArgumentException("Negative Experience");

        if(exp > MAX_EXP)
            return false;
        this.currentExperience += exp;

        if(this.currentExperience >= 100){
            this.level += 1;
            this.currentExperience -= 100;
            /*
            HERE ADD ACTION TO REACH A NEW LEVEL
             */
        }
        return true;
    }


    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return id.compareTo(((Usuario)o).id);
    }
}
