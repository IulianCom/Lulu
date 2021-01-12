package com.example.lulu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.example.lulu.FirebaseHelper.mSingersImagesRef;

public class PremiumFragment extends Fragment {
    TextView textView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_premium,container,false);
        textView = view.findViewById(R.id.test);
        textView.setText(mSingersImagesRef.getName());
        return view;

    }
}
