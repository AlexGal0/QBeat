package android.bignerdranch.com.myapplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SetIngredientes {
    private static Set<Ingrediente> ingredientes;
    public static Ingrediente removed;

    public SetIngredientes(){
        reset();
    }

    public static void reset(){
        ingredientes = new HashSet<>(DataBase.getDataBase().getListIngredients());
    }

    public static void remove(Ingrediente ingrediente){
        ingredientes.remove(ingrediente);
        removed = ingrediente;
    }

    public static ArrayList<Ingrediente> getListIngredientes(){
        ArrayList<Ingrediente> list = new ArrayList<>(ingredientes);

        Collections.sort(list);
        return list;
    }

    public static void add(Ingrediente ingrediente){
        ingredientes.add(ingrediente);
    }
    public static int size(){
        return ingredientes.size();
    }

}
