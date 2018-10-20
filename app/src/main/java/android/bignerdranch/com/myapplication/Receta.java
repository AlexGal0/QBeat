package android.bignerdranch.com.myapplication;

import android.media.Image;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.UUID;

public class Receta implements Comparable {
    public String id;
    public String chefId;
    private ArrayList<Paso> pasos;
    private String name;
    private String description;
    private ArrayList<ImageView> images;
    private ArrayList<Ingrediente> ingredientes;


    /*
        constructors
     */
    public Receta(){
        this.id = UUID.randomUUID().toString();
        this.chefId = DataBase.getDataBase().currentUser.id;
    };
    public Receta(String chefId){
        this();
        this.pasos = new ArrayList<>();
        this.images = new ArrayList<>();
        this.chefId = chefId;
    }

    public Receta(String title, String description, String chefId){
        this(chefId);
        setName(title);
        setDescription(description);
    }

    public Receta(Receta receta){
        this(receta.name, receta.description, receta.chefId);
        setPasos(receta.getPasos());
        setImages(receta.getImages());
        setIngredientes(receta.ingredientes);
    }

    /*
        getters and setters
     */
    public String getName() {
        return name + "";
    }

    public String getId() {
        return id;
    }
    public String getChefId() {
        return chefId;
    }

    public String getDescription() {
        return description + "";
    }

    public ArrayList<ImageView> getImages() {
        return images;
    }

    public ArrayList<Paso> getPasos() {
        return pasos;
    }

    public ArrayList<Ingrediente> getIngredientes() {
        return ingredientes;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public boolean addImage(ImageView imageView){
        if(imageView == null)
            throw new NullPointerException();
        return this.images.add(imageView);
    }

    public boolean addPaso(Paso paso){
        if(paso == null)
            throw new NullPointerException();
        return this.pasos.add(paso);
    }

    public boolean setPasos(ArrayList<Paso> pasos){
        if(pasos == null)
            return false;
        if(this.pasos == null)
            this.pasos = new ArrayList<>(pasos);
        else
            this.pasos.addAll(pasos);
        return true;
    }

    public boolean setImages(ArrayList<ImageView> images){
        if(images == null)
            return false;
        if(this.images == null)
            this.images = new ArrayList<>(images);
        else
            this.images.addAll(images);
        return true;
    }

    public boolean setIngredientes(ArrayList<Ingrediente> ingredientes){
        if(ingredientes == null)
            return false;
        if(this.ingredientes == null)
            this.ingredientes = new ArrayList<>(ingredientes);
        else
            this.ingredientes.addAll(ingredientes);
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Receta))
            return false;
        return this.id.equals(((Receta) obj).id);
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return this.name.compareTo(((Receta)o).name);
    }
}
