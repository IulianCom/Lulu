package com.example.lulu;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseHelper {
    public static final DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("users");
    public static final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    public static final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static final DatabaseReference singerDatabase = FirebaseDatabase.getInstance().getReference("singers");
    public static final String adminUUID = "eRbdfXYZIQebBDCiU1IVTKPEBEq2"; //Ion
    public static final StorageReference mImagesRef = mStorageRef.child("singers_image");
}
