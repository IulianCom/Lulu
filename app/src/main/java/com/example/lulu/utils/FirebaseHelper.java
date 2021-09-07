package com.example.lulu.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseHelper {

    public static final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static final FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    public static final DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("users");
    public static final DatabaseReference artistSongsDatabase = FirebaseDatabase.getInstance().getReference("songs");
    public static final DatabaseReference postsDatabase = FirebaseDatabase.getInstance().getReference("posts");
    public static final DatabaseReference likedSongsDatabase = FirebaseDatabase.getInstance().getReference("likedSongs");

    public static final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    public static final StorageReference mSingersImagesRef = mStorageRef.child("singers_image");
    public static final StorageReference mSingersSongsRef = mStorageRef.child("songs");

    public static final String adminUUID = "eRbdfXYZIQebBDCiU1IVTKPEBEq2"; //Ion
}
