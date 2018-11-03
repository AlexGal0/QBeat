package android.bignerdranch.com.myapplication;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class SetIngredientes {
    private static ArrayList<Ingrediente> ingredientes;
    public static Ingrediente removed;

    public SetIngredientes(){
        reset();
    }

    public static void reset(){
        ingredientes = new ArrayList<>(DataBase.getDataBase().getListIngredients());
    }

    public static void remove(Ingrediente ingrediente){
        ingredientes.remove(ingrediente);
        removed = ingrediente;
    }

    public static ArrayList<Ingrediente> getListIngredientes(){
        ArrayList<Ingrediente> list = new ArrayList<>(ingredientes);
        Log.i("INGREDIENTE", ingredientes.size() + "xdxd");

        Collections.sort(ingredientes, new Comparator<Ingrediente>() {
            @Override
            public int compare(Ingrediente x, Ingrediente y) {
                return x.getName().toLowerCase().compareTo(y.getName().toLowerCase());
            }
        });
        return list;
    }

    public static void add(Ingrediente ingrediente){
        ingredientes.add(ingrediente);
    }
    public static int size(){
        return ingredientes.size();
    }

}
