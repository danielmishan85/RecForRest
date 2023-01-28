package com.example.recforrest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recforrest.databinding.FragmentMyPostBinding;
import com.example.recforrest.databinding.FragmentSignUpBinding;


public class MyPostFragment extends Fragment {
    @Nullable
    @NonNull FragmentMyPostBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentMyPostBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
//        Bundle args = getArguments();
//        String email="dg";
//        if (args != null) {
//            email = args.getString("email");
//            Log.d("TAG",args.toString());
//            // Use the email as needed
//        }
       String email = MyPostFragmentArgs.fromBundle(getArguments()).getEmail();
        Log.d("TAG", email + "myPost");
        return view;
    }

}