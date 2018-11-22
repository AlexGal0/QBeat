package android.bignerdranch.com.myapplication;

import android.media.Image;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Receta implements Comparable {
    public String id;
    public String chefId;
    private String chefName;
    private ArrayList<Paso> pasos;
    private String name;
    private String description;
    private ArrayList<Ingrediente> ingredientes;
    private String recipeImage;
    private byte[] image;
    private Date create;


    /*
        constructors
     */
    public Receta(){
        this.id = UUID.randomUUID().toString();
    };
    public Receta(String chefId){
        this();
        this.pasos = new ArrayList<>();
        this.chefId = chefId;
    }

    public Receta(String title, String description, String chefId){
        this(chefId);
        setName(title);
        setDescription(description);
    }

    public Receta(Receta receta){
        this.id = receta.id;
        this.chefId = receta.chefId;
        this.chefName = receta.chefName;
        this.pasos = receta.getPasos();
        this.name = receta.name;
        this.description = receta.description;
        this.ingredientes = receta.getIngredientes();
        this.recipeImage = receta.recipeImage;
        this.image = receta.getImage();
        this.create = receta.getCreate();
    }

    /*
        getters and setters
     */
    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
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


    public boolean addPaso(Paso paso){
        if(paso == null)
            throw new NullPointerException();
        return this.pasos.add(paso);
    }

    public boolean addIngrediente(Ingrediente ingrediente){
        if(ingrediente == null)
            return false;
        return this.ingredientes.add(ingrediente);
    }

    public void removePaso(int index){
        if(index >= pasos.size() || index < 0)
            return;
        pasos.remove(index);
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
        return this.id.compareTo(((Receta)o).id);
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }

    public String getChefName() {
        return chefName;
    }

    public void setChefName(String chefName) {
        this.chefName = chefName;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Date getCreate() {
        return create;
    }

    public void setCreate(Date create) {
        this.create = create;
    }
}
