package com.example.recforrest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recforrest.Model.Model;
import com.example.recforrest.Model.User;
import com.example.recforrest.databinding.FragmentUserEditBinding;
import com.example.recforrest.databinding.FragmentUserInfoBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserEditFragment extends Fragment {

    @NonNull
    FragmentUserEditBinding binding;
    String email;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentUserEditBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        email = UserEditFragmentArgs.fromBundle(getArguments()).getEmail();


        binding.UserEditFragmentShowFullNameEditText.setHint(Model.instance().getUserByEmail(email).getFullName().toString());



        binding.UserEditFragmentSaveBtn.setOnClickListener(view1 -> {
            String fullName= binding.UserEditFragmentShowFullNameEditText.getText().toString();
            String password= binding.UserEditFragmentShowPasswordEditText.getText().toString();

            Model.instance().printUser(email);
            User user= Model.instance().getUserByEmail(email);

            if(!fullName.equals(""))
                user.setFullName(fullName);


            if(!password.equals(""))
                user.setPassword(password);


            Model.instance().deleteUserByEmail(email);
            Model.instance().addUser(user);
            Model.instance().printUser(email);
            Navigation.findNavController(view1).popBackStack();


        });

        binding.UserEditFragmentCancelBtn.setOnClickListener(view1 -> Navigation.findNavController(view1).popBackStack());


        return view;
    }
}