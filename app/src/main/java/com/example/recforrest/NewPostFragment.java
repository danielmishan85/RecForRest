package com.example.recforrest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.recforrest.model.Model;
import com.example.recforrest.model.Post;
import com.example.recforrest.databinding.FragmentNewPostBinding;


public class NewPostFragment extends Fragment {

    @NonNull FragmentNewPostBinding binding;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentNewPostBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        String email = NewPostFragmentArgs.fromBundle(getArguments()).getEmail();


        binding.newPostFragCancelBtn.setOnClickListener(view1->{
            Navigation.findNavController(view1).popBackStack();
        });

        binding.newPostFragSaveBtn.setOnClickListener(view1 -> {
            String restaurantName= binding.newPostFragmentRestaurantNameEditText.getText().toString();
            String city= binding.newPostFragmentRestaurantCityEditText.getText().toString();
            String description= binding.newPostFragmentRestaurantDescriptionEditText.getText().toString();

            if(restaurantName.equals("")||city.equals("")||description.equals("")){
                Toast.makeText(getActivity().getApplicationContext(),"Please fill all the inputs",Toast.LENGTH_LONG).show();
            }
            else {

                Post p=new Post(restaurantName,city,"",description,email);
                p.generateID();
                Model.instance().addPost(p,()->{
                    Navigation.findNavController(view1).popBackStack();
                });

            }


        });
        return view;
    }
}