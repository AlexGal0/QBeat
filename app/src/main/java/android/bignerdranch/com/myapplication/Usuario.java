package android.bignerdranch.com.myapplication;

import android.util.MonthDisplayHelper;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.UUID;

public class Usuario {
    public final static double MAX_EXP = 100.0;
    public String id;
    private String name;
    private String email;
    private int level;
    private double currentExperience;
    private ArrayList<String> misRecetas;
    private ArrayList<String> misRecetasGuardadas;
    private ImageView imageView;

    /*
        constructor
     */

    public Usuario(){
        id = UUID.randomUUID().toString();
        misRecetas = new ArrayList<>();
        misRecetasGuardadas = new ArrayList<>();
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
        this.misRecetas = usuario.misRecetas;
        this.misRecetasGuardadas = usuario.misRecetasGuardadas;
        this.level = usuario.level;
        this.currentExperience = usuario.currentExperience;
        this.email = usuario.email;
        this.imageView = usuario.imageView;
    }



    /*
        getters and setters
     */

    public String getName() {
        return name;
    }


    public String getEmail() {
        return email;
    }


    public int getLevel() {
        return level;
    }

    public ArrayList<String> getMisRecetas() {
        return misRecetas;
    }

    public ArrayList<String> getMisRecetasGuardadas() {
        return misRecetasGuardadas;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public double getCurrentExperience() {
        return currentExperience;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
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

}
