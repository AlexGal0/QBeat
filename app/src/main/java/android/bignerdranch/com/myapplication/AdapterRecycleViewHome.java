package android.bignerdranch.com.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AdapterRecycleViewHome extends RecyclerView.Adapter<AdapterRecycleViewHome.ViewHolder>{

    public ArrayList<Receta> recetas;

    public AdapterRecycleViewHome(ArrayList<Receta> list){
        recetas = list;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // en este ejemplo cada elemento consta solo de un título
        public TextView titulo;
        public TextView chef;
        public TextView descripcion;
        public TextView fecha;
        public ImageView imagen;
        public ProgressBar progressBar;
        public Button download;
        public TextView dificult;
        public TextView time;
        public TextView tag;



        public ViewHolder(CardView view) {
            super(view);
            titulo      = view.findViewById(R.id.title_card);
            chef        = view.findViewById(R.id.chef_card);
            descripcion = view.findViewById(R.id.description_card);
            fecha       = view.findViewById(R.id.fecha_card);
            imagen      = view.findViewById(R.id.image_card);
            progressBar = view.findViewById(R.id.progress_card);
            download    = view.findViewById(R.id.load_button_image_card);
            dificult    = view.findViewById(R.id.difficult_card_view);
            time        = view.findViewById(R.id.time_card_view);
            tag         = view.findViewById(R.id.tag_card_view);
        }

    }

    @NonNull
    @Override
    public AdapterRecycleViewHome.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.single_element_home, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterRecycleViewHome.ViewHolder holder, final int position) {
        final Receta receta = recetas.get(position);
        holder.titulo.setText(receta.getName());
        if(receta.getChefName() == null){
            holder.chef.setVisibility(View.GONE);
        }
        else{
            holder.chef.setVisibility(View.VISIBLE);
            holder.chef.setText(receta.getChefName());
        }
        holder.descripcion.setText(receta.getDescription());

        holder.fecha.setText(new SimpleDateFormat("yy/MM/dd").format(receta.getCreate()));


        holder.dificult.setText(receta.getDificult() + "");
        holder.time.setText(receta.getTime() + " min");
        holder.tag.setText(receta.getTags());

        holder.imagen.setVisibility(View.GONE);
        holder.download.setVisibility(View.VISIBLE);
        if(receta.getRecipeImage() == null){
            holder.progressBar.setVisibility(View.GONE);
            holder.download.setVisibility(View.GONE);
        }
        else{
            if(receta.getImage() == null){
                holder.progressBar.setVisibility(View.GONE);
                holder.download.setOnClickListener(new DownloadButtonClickListener(receta, holder.imagen, holder.download, holder.progressBar));
            }
            else{
                holder.imagen.setImageBitmap(Util.fixSize(receta.getImage()));
                holder.progressBar.setVisibility(View.GONE);
                holder.imagen.setVisibility(View.VISIBLE);
                holder.download.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return recetas.size();
    }

    private class DownloadButtonClickListener implements View.OnClickListener {

        private Receta receta;
        private ImageView imagen;
        private Button download;
        private ProgressBar progressBar;

        public DownloadButtonClickListener(Receta receta, ImageView imagen, Button download, ProgressBar progressBar) {
            this.receta = receta;
            this.imagen = imagen;
            this.download = download;
            this.progressBar = progressBar;
        }

        @Override
        public void onClick(View view) {
            if(receta.getImage() == null) {
                download.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                StorageReference islandRef = FirebaseStorage.getInstance().getReference().child("Recetas Images/" + receta.getRecipeImage());

                final long ONE_MEGABYTE = 1024 * 1024 * 2;
                islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {

                        receta.setImage(bytes);
                        Log.i("DESCARGA DE IMAGENES", "AVISO!!!");
                        imagen.setImageBitmap(Util.fixSize(bytes));
                        progressBar.setVisibility(View.GONE);
                        imagen.setVisibility(View.VISIBLE);
                        download.setVisibility(View.GONE);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressBar.setVisibility(View.GONE);
                        download.setVisibility(View.VISIBLE);
                    }
                });

            }
            else{
                imagen.setImageBitmap(Util.fixSize(receta.getImage()));
                progressBar.setVisibility(View.GONE);
                imagen.setVisibility(View.VISIBLE);
                download.setVisibility(View.GONE);

            }
        }
    }
}
