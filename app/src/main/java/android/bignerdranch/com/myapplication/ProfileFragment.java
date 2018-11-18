package android.bignerdranch.com.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;

import io.grpc.Context;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    private static final int RESULT_LOAD_IMAGE = 1;


    public static ProfileFragment profileFragment;

    private ImageView userImage;
    private ProgressBar progressBar;

    public byte[] bit;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        profileFragment = this;
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        setRetainInstance(true);

        final Usuario user = DataBase.getDataBase().currentUser;


        progressBar = v.findViewById(R.id.progressBarUser);
        progressBar.setIndeterminate(true);

        progressBar.setVisibility(View.GONE);


        userImage = v.findViewById(R.id.profile_photo);
        userImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(bit != null && !FirebaseAuth.getInstance().getCurrentUser().getUid().equals("TrU66plo94hHO8PpPOh0vKR5lgD3")){
                    Vibrator vibrator = (Vibrator) getActivity().getSystemService(android.content.Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        vibrator.vibrate(50);
                    }
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                }
                return false;
            }
        });
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals("TrU66plo94hHO8PpPOh0vKR5lgD3"))
                    return;
                Intent i = new Intent(getContext(), ImageViewCompleteFragment.class);
                i.putExtra(ImageViewCompleteFragment.KEY_IMAGE, 1);
                startActivity(i);
            }
        });

        if(user.getImageReference() != null){
            if(user.getImage() == null){

                progressBar.setVisibility(View.VISIBLE);
                userImage.setVisibility(View.GONE);

                StorageReference islandRef = FirebaseStorage.getInstance().getReference().child("ProfileImages/"+ user.getImageReference());

                final long ONE_MEGABYTE = 1024 * 1024 * 2;
                islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        bit = bytes;
                        user.setImage(bytes);
                        //Log.i("MT", b.getHeight() + "   x  "  + b.getWidth()+ "   x  " + min);
                        userImage.setImageBitmap(Util.fixSize(bytes));
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
            else{
                bit = user.getImage();
                userImage.setImageBitmap(Util.fixSize(user.getImage()));
                progressBar.setVisibility(View.GONE);
                userImage.setVisibility(View.VISIBLE);
            }

        }
        else{
            bit = DataBase.getDataBase().f;
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
            InputStream iStream = null;
            try {
                iStream = getActivity().getContentResolver().openInputStream(imageUri);
                bit = Util.getBytes(iStream);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(bit.length > 1024 * 1024 * 2){
                Toast.makeText(getContext(), "La imagen excede el tamaño", Toast.LENGTH_SHORT).show();
                return;
            }

            userImage.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            final StorageReference riversRef = storageRef.child("ProfileImages/"+imageUri.getLastPathSegment());


            final UploadTask uploadTask = riversRef.putFile(imageUri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getContext(), "Fallo al subir la foto", Toast.LENGTH_SHORT).show();
                    userImage.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                    userImage.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Log.i("Upload Image", riversRef.getName());

                    userImage.setMaxHeight(150);
                    userImage.setMaxWidth(150);

                    Bitmap bits = Util.fixSize(bit);

                    userImage.setImageBitmap(bits);
                    DataBase.getDataBase().currentUser.setImage(bit);

                    if(DataBase.getDataBase().currentUser.getImageReference() != null)
                        FirebaseStorage.getInstance().getReference().child("ProfileImages/" + DataBase.getDataBase().currentUser.getImageReference()).delete();

                    DataBase.getDataBase().currentUser.setImageReference(riversRef.getName());
                    DataBase.getDataBase().setCampo("imageReference", riversRef.getName());
                }
            });
        }
    }
}