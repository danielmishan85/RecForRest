package com.example.recforrest;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.recforrest.Model.Model;
import com.example.recforrest.Model.User;
import com.example.recforrest.databinding.FragmentChooseSignInOrUpBinding;
import com.example.recforrest.databinding.FragmentSignUpBinding;


public class SignUpFragment extends Fragment {


    @NonNull FragmentSignUpBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity parentActivity = getActivity();
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.removeItem(R.id.chooseSignInOrUpFragment);
                menu.removeItem(R.id.postsFragment);
                menu.removeItem(R.id.myPostFragment);

            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        }, this, Lifecycle.State.RESUMED);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        binding.SignUpFragmentSignUpBtn.setOnClickListener((view1 -> {
            Model.instance().addUser(new User(binding.SignUpFragmentFullNameEditText.getText().toString(),binding.SignUpFragmentEmailEditText.getText().toString(),binding.SignUpFragmentPasswordEditText.getText().toString()));

            SignUpFragmentDirections.ActionSignUpFragmentToUserInfoFragment action = SignUpFragmentDirections.actionSignUpFragmentToUserInfoFragment(binding.SignUpFragmentEmailEditText.getText().toString());
            Navigation.findNavController(view).navigate(action);
        }));


        return view;
    }
}