package com.example.lulu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.example.lulu.FirebaseHelper.adminUUID;
import static com.example.lulu.FirebaseHelper.mAuth;
import static com.example.lulu.FirebaseHelper.mSingersImagesRef;
import static com.example.lulu.FirebaseHelper.singerDatabase;

public class HomeFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private LinearLayout adminLl;
    private EditText newSingerEt;
    private Button addBtn;
    private UUID randomUUID;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.fragment_home,container,false);
        view = itemView;
        initializeViews();
        return itemView;
    }


    private void initializeViews() {
        recyclerView = view.findViewById(R.id.rv);
        adminLl = view.findViewById(R.id.ll_add_singer);
        if(mAuth.getCurrentUser().getUid().equals(adminUUID)) {
            adminLl.setVisibility(View.VISIBLE);
            newSingerEt = view.findViewById(R.id.et_singer_name);
            addBtn = view.findViewById(R.id.add_singer_btn);

            addBtn.setOnClickListener(view1 -> {
                String singerName = newSingerEt.getText().toString();
                if(!singerName.isEmpty()) {
                    randomUUID = UUID.randomUUID();
                    singerDatabase.child(randomUUID.toString()).setValue(new Singer(singerName, randomUUID.toString()));
                    addPhotoToDatabase();
                }
                else{
                    Toast.makeText(getContext(), "Field should not be empty", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void addPhotoToDatabase() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uploadImage(data.getData());
        }
    }

    private void uploadImage(Uri imageUri) {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Uploading image");
        pd.show();

        StorageReference riversRef = mSingersImagesRef.child(randomUUID.toString());

        riversRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Snackbar.make(view.findViewById(android.R.id.content), "Image upload", Snackbar.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getContext(), "Failed image", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage("Percentage: " + (int) progress + "%");
            }
        });
    }
}
