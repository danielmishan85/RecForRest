package com.example.recforrest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recforrest.Model.Model;
import com.example.recforrest.Model.Post;
import com.example.recforrest.databinding.FragmentMyPostEditBinding;
import com.example.recforrest.databinding.FragmentMyPostInfoBinding;



public class MyPostEditFragment extends Fragment {

    FragmentMyPostEditBinding binding;
    int id;
    Post p;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentMyPostEditBinding.inflate(inflater, container, false);
        id = MyPostEditFragmentArgs.fromBundle(getArguments()).getId();
        p = Model.instance().getPostById(id);

        View view = binding.getRoot();
        binding.myPostEditFragmentRestaurantNameEditText.setHint(p.getRestaurantName());
        binding.myPostEditFragmentRestaurantCityEditText.setHint(p.getCity());
        binding.myPostEditFragmentRestaurantDescriptionEditText.setHint(p.getDescription());

        binding.myPostEditFragmentCancelBtn.setOnClickListener(view1 ->{
            Navigation.findNavController(view1).popBackStack();
        } );

        binding.myPostEditFragmentSaveBtn.setOnClickListener(view1 ->{
            if(!binding.myPostEditFragmentRestaurantNameEditText.getText().toString().equals(""))
                p.setRestaurantName(binding.myPostEditFragmentRestaurantNameEditText.getText().toString());

            if(!binding.myPostEditFragmentRestaurantCityEditText.getText().toString().equals(""))
                p.setCity(binding.myPostEditFragmentRestaurantCityEditText.getText().toString());

            if(!binding.myPostEditFragmentRestaurantDescriptionEditText.getText().toString().equals(""))
                p.setDescription(binding.myPostEditFragmentRestaurantDescriptionEditText.getText().toString());

            Navigation.findNavController(view1).popBackStack();
        } );
        return view;
    }
}