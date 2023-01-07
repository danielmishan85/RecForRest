package com.example.recforrest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recforrest.Model.Model;
import com.example.recforrest.Model.User;
import com.example.recforrest.databinding.FragmentChooseSignInOrUpBinding;
import com.example.recforrest.databinding.FragmentSignUpBinding;


public class SignUpFragment extends Fragment {


    @NonNull FragmentSignUpBinding binding;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        binding.SignUpFragmentSignUpBtn.setOnClickListener((view1 -> {
            Model.instance().addUser(new User(binding.SignUpFragmentFullNameEditText.getText().toString(),binding.SignUpFragmentEmailEditText.getText().toString(),binding.SignUpFragmentPasswordEditText.getText().toString()));

        }));


        return view;
    }
}