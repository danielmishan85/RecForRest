package com.example.recforrest;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
    ActivityResultLauncher<Void> cameraLauncher;
    ActivityResultLauncher<String> galleryLauncher;
    Boolean isAvatarSelected  = false;

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
                if (isAvatarSelected){
                    binding.imageView.setDrawingCacheEnabled(true);
                    binding.imageView.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) binding.imageView.getDrawable()).getBitmap();
                    Model.instance().uploadImage(String.valueOf(p.getPostId()), bitmap, url->{
                        if (url != null){
                            p.setImg(url);
                        }
                        Model.instance().addPost(p,()->{
//                            pb.setVisibility(View.GONE);
                            Navigation.findNavController(view).popBackStack();

                        });
                    });
                }else {
                    Model.instance().addPost(p,()->{
//                        pb.setVisibility(View.GONE);
                        Navigation.findNavController(view).popBackStack();

                    });
                }

            }



        });

        binding.chooseFromCamera.setOnClickListener(view1->{
            cameraLauncher.launch(null);
        });

        binding.chooseFromGallery.setOnClickListener(view1->{
            galleryLauncher.launch("media/*");
        });
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
                if (result != null) {
                    binding.imageView.setImageBitmap(result);
                    isAvatarSelected = true;
                }
            }
        });
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null){
                    binding.imageView.setImageURI(result);
                    isAvatarSelected = true;
                }
            }
        });
        return view;
    }
}