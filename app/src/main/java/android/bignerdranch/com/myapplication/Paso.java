package android.bignerdranch.com.myapplication;

import android.widget.ImageView;

import java.util.ArrayList;

public class Paso {
    private ArrayList<Ingrediente> ingredientes;
    private String description;
    private ImageView image;
    private long time;
    private String title;

    /*
        constructors
     */
    public Paso(){
        ingredientes = new ArrayList<>();
    }

    public Paso(String title){
        this();
        setTitle(title);
    }

    public Paso(String title, String description){
        this(title);
        setDescription(description);
    }

    public Paso(ArrayList<Ingrediente> listIngredientes){
        this();
        setIngredientes(listIngredientes);
    }

    public Paso(String title, String description, ArrayList<Ingrediente> listIngredientes, ImageView image, long time){
        this(title, description);
        setIngredientes(listIngredientes);
        setImage(image);
        setTime(time);
    }

    /*
        getters and setters
     */
    public ArrayList<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public String getDescription() {
        return description;
    }

    public ImageView getImage() {
        return image;
    }

    public long getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public void setIngredientes(ArrayList<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
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
    public void setTitle(String title) {
        this.title = title;
    }


    /*
        Add one ingredient to list of ingredients
     */
    public boolean addIngredient(Ingrediente ingrediente){
        if(ingrediente == null)
            throw new NullPointerException();
        if(this.ingredientes.contains(ingrediente))
            return true;
        return ingredientes.add(ingrediente);
    }

}
