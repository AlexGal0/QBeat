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


public class AdapterSearchFragmentUser extends RecyclerView.Adapter<AdapterSearchFragmentUser.SearchViewHolderUsers> {
    private final ArrayList<Usuario> users;

    public class SearchViewHolderUsers extends RecyclerView.ViewHolder{

        public ImageView image;
        public TextView name;
        public TextView level;
        public ProgressBar progressBar;


        public SearchViewHolderUsers(View view) {
            super(view);

            image = view.findViewById(R.id.image_search_user);
            name = view.findViewById(R.id.chef_name_search);
            level = view.findViewById(R.id.level_search_text);
            progressBar = view.findViewById(R.id.single_search_progressbar_user);
        }
    }

    public AdapterSearchFragmentUser(ArrayList<Usuario> users){
        this.users = users;
    }


    @NonNull
    @Override
    public AdapterSearchFragmentUser.SearchViewHolderUsers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchViewHolderUsers(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_element_search_user,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterSearchFragmentUser.SearchViewHolderUsers holder, int position) {
        final Usuario usuario = users.get(position);

        holder.name.setText(usuario.getName());
        holder.level.setText(usuario.getLevel() + "");
        holder.progressBar.setVisibility(View.GONE);

        if(usuario.getImageReference() != null) {
            if (usuario.getImage() == null) {
                holder.progressBar.setVisibility(View.VISIBLE);

                StorageReference islandRef = FirebaseStorage.getInstance().getReference().child("ProfileImages/"+ usuario.getImageReference());

                final long ONE_MEGABYTE = 1024 * 1024 * 2;
                islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        usuario.setImage(bytes);

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
                holder.image.setImageBitmap(Util.fixSizeRectangle(usuario.getImage()));
        }
        else
            holder.image.setImageBitmap(Util.fixSizeRectangle(DataBase.getDataBase().f));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }



}
