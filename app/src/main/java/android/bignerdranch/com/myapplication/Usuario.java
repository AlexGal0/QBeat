package android.bignerdranch.com.myapplication;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.UUID;

public class Usuario {
    public final static double MAX_EXP = 100.0;
    public UUID id;
    private String name;
    private int level;
    private double currentExperience;
    private ArrayList<Integer> misRecetas;
    private ArrayList<Integer> misRecetasGuardadas;
    private ImageView imageView;

    /*
        constructor
     */
    public Usuario(String name){
        id = UUID.randomUUID();
        ArrayList<Integer>a=new ArrayList<>();
        a.add(2);
        a.add(4);
        a.add(6);

        misRecetas =a;
        ArrayList<Integer>b=new ArrayList<>();
        misRecetasGuardadas = b;
        level = 0;
        this.name = name;
    }

    /*
        getters and setters
     */

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public ArrayList<Integer> getMisRecetas() {
        return misRecetas;
    }

    public ArrayList<Integer> getMisRecetasGuardadas() {
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

   /** public boolean addReceta(Receta receta){
        if(receta == null)
            throw new NullPointerException();
        if(receta.chefId.equals(this.id)){
            if(misRecetas.contains(receta))
                return true;
            return misRecetas.add(receta);
        }
        else{
            if(misRecetasGuardadas.contains(receta))
                return true;
            return misRecetasGuardadas.add(receta);
        }
    }
**/
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
