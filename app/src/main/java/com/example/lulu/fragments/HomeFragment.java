package com.example.lulu.fragments;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lulu.R;
import com.example.lulu.adapters.SingerAdapter;
import com.example.lulu.classes.Singer;
import com.example.lulu.classes.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.example.lulu.FirebaseHelper.adminUUID;
import static com.example.lulu.FirebaseHelper.mAuth;
import static com.example.lulu.FirebaseHelper.mSingersImagesRef;
import static com.example.lulu.FirebaseHelper.singerDatabase;
import static com.example.lulu.FirebaseHelper.userDatabase;

public class HomeFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private LinearLayout adminLl;
    private EditText newSingerEt;
    private Button addBtn;
    private TextView welcomeTv;

    private UUID randomUUID;
    ArrayList<Singer> singers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.fragment_home,container,false);
        view = itemView;
        initializeViews();
        return itemView;
    }

    @Override
    public void onStart() {
        super.onStart();

        userDatabase.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User currentUser = snapshot.getValue(User.class);
                if(!currentUser.getName().isEmpty())
                    welcomeTv.setText(welcomeTv.getText().toString() + ", " + currentUser.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(singerDatabase!=null){
            singerDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        singers = new ArrayList<>();
                        for(DataSnapshot ds : snapshot.getChildren()){

                            singers.add(ds.getValue(Singer.class));
                        }
                        recyclerView.setAdapter(new SingerAdapter(singers, getContext()));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                     Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initializeViews() {
        welcomeTv = view.findViewById(R.id.welcome_tv);
        recyclerView = view.findViewById(R.id.home_rv);
        if(mAuth.getCurrentUser().getUid().equals(adminUUID)) {
            adminLl = view.findViewById(R.id.ll_add_singer);
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
                    Toast.makeText(getContext(), "Field must not be empty", Toast.LENGTH_SHORT).show();
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
