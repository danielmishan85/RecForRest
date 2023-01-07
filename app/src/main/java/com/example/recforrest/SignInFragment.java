package com.example.recforrest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.recforrest.Model.Model;
import com.example.recforrest.Model.User;
import com.example.recforrest.databinding.FragmentSignInBinding;
import com.example.recforrest.databinding.FragmentSignUpBinding;


public class SignInFragment extends Fragment {


    @NonNull
    FragmentSignInBinding binding;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSignInBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.SignInFragmentSignInBtn.setOnClickListener((view1) -> {

            String email=binding.SignInFragmentEmailEditText.getText().toString();
            String password=binding.SignInFragmentPasswordEditText.getText().toString();



            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            User user= Model.instance().getUserByEmail(email);
            if(user!=null){
                if(!user.getPassword().equals(password)){
                    Toast.makeText(getActivity().getApplicationContext(),"Wrong password",Toast.LENGTH_LONG).show();
                }
                else{
                    Intent i = new Intent(getActivity(), UserActivity.class);
                    i.putExtra("email",email);
                    startActivity(i);
                }
            }
            else {
                Toast.makeText(getActivity().getApplicationContext(),"Wrong email",Toast.LENGTH_LONG).show();
            }

        });


        return view;
    }
}