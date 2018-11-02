package android.bignerdranch.com.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import io.grpc.Context;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    private static final int RESULT_LOAD_IMAGE = 1;

    private ImageView userImage;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        final Usuario user = DataBase.getDataBase().currentUser;


        progressBar = v.findViewById(R.id.progressBarUser);
        progressBar.setIndeterminate(true);

        progressBar.setVisibility(View.GONE);

        userImage = v.findViewById(R.id.profile_photo);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

        if(user.getImageReference() != null){
            progressBar.setVisibility(View.VISIBLE);
            userImage.setVisibility(View.GONE);

            StorageReference islandRef = FirebaseStorage.getInstance().getReference().child("ProfileImages/"+ user.getImageReference());

            final long ONE_MEGABYTE = 1024 * 1024;
            islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    userImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                    progressBar.setVisibility(View.GONE);
                    userImage.setVisibility(View.VISIBLE);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    progressBar.setVisibility(View.GONE);
                    userImage.setVisibility(View.VISIBLE);
                }
            });
        }



        TextView nombre = v.findViewById(R.id.username_profile);
        nombre.setText(user.getName());

        ProgressBar bar = v.findViewById(R.id.progressbar_profile);
        bar.setProgress((int) user.getCurrentExperience());

        TextView nivel = v.findViewById(R.id.number_level);
        nivel.setText("" + user.getLevel());

        Button logout = v.findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBase.getDataBase().cleanUser();
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();
            }
        });

        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK){
            final Uri imageUri = data.getData();

            userImage.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            final StorageReference riversRef = storageRef.child("ProfileImages/"+imageUri.getLastPathSegment());


            final UploadTask uploadTask = riversRef.putFile(imageUri);
            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getContext(), "Fallo al subir la foto", Toast.LENGTH_SHORT).show();
                    userImage.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    userImage.setImageURI(imageUri);
                    userImage.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Log.i("Upload Image", riversRef.getName());


                    if(DataBase.getDataBase().currentUser.getImageReference() != null)
                        FirebaseStorage.getInstance().getReference().child("ProfileImages/" + DataBase.getDataBase().currentUser.getImageReference()).delete();

                    DataBase.getDataBase().currentUser.setImageReference(riversRef.getName());
                    DataBase.getDataBase().setCampo("imageReference", riversRef.getName());
                }
            });
        }
    }
}