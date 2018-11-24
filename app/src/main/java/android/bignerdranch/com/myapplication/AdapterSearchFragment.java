package android.bignerdranch.com.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class AdapterSearchFragment extends  RecyclerView.Adapter<AdapterSearchFragment.SearchViewHolder> {
    private ArrayList<Receta> recetas;

    public class SearchViewHolder extends RecyclerView.ViewHolder{

        public ImageView image;
        public TextView title;
        public TextView description;
        public ProgressBar progressBar;


        public SearchViewHolder(View view) {
            super(view);

            image = view.findViewById(R.id.image_search_user);
            title = view.findViewById(R.id.chef_name_search);
            description = view.findViewById(R.id.description_search_element);
            progressBar = view.findViewById(R.id.single_search_progressbar);
        }
    }

    public AdapterSearchFragment(ArrayList<Receta> recetas){
        this.recetas = recetas;
    }


    @NonNull
    @Override
    public AdapterSearchFragment.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_element_search,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterSearchFragment.SearchViewHolder holder, int position) {
        final Receta receta = recetas.get(position);

        holder.title.setText(receta.getName());
        holder.description.setText(receta.getDescription());
        holder.progressBar.setVisibility(View.GONE);

        if(receta.getRecipeImage() != null) {
            if (receta.getImage() == null) {
                holder.progressBar.setVisibility(View.VISIBLE);

                StorageReference islandRef = FirebaseStorage.getInstance().getReference().child("Recetas Images/"+ receta.getRecipeImage());

                final long ONE_MEGABYTE = 1024 * 1024 * 2;
                islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        receta.setImage(bytes);

                        holder.image.setImageBitmap(Util.fixSizeRectangle(bytes));
                        holder.progressBar.setVisibility(View.GONE);
                        holder.image.setVisibility(View.VISIBLE);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        holder.progressBar.setVisibility(View.GONE);
                    }
                });

            } else
                holder.image.setImageBitmap(Util.fixSizeRectangle(receta.getImage()));
        }
        else
            holder.image.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return recetas.size();
    }



}
