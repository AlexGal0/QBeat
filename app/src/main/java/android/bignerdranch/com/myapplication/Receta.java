package android.bignerdranch.com.myapplication;

import android.media.Image;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.UUID;

public class Receta {
    public String id;
    public String chefId;
    private ArrayList<Paso> pasos;
    private String name;
    private String description;
    private ArrayList<ImageView> images;

    /*
        constructors
     */
    public Receta(){};
    public Receta(UUID chefId){
        this.id = UUID.randomUUID().toString();
        this.pasos = new ArrayList<>();
        this.images = new ArrayList<>();
        this.chefId = chefId.toString();
    }

    public Receta(String title, String description, UUID chefId){
        this(chefId);
        setName(title);
        setDescription(description);
    }

    /*
        getters and setters
     */
    public String getName() {
        return name + "";
    }

    public String getId() {
        return id.toString();
    }
    public String getChefId() {
        return chefId.toString();
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

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Receta))
            return false;
        return this.id.equals(((Receta) obj).id);
    }
}
