package com.example.recforrest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.recforrest.Model.Model;
import com.example.recforrest.Model.Post;
import com.example.recforrest.databinding.FragmentMyPostEditBinding;
import com.squareup.picasso.Picasso;


public class MyPostEditFragment extends Fragment {

    FragmentMyPostEditBinding binding;
    int id;
    Post p;
    MyPostEditFragmentViewModel viewModel;
    ActivityResultLauncher<Void> cameraLauncher;
    ActivityResultLauncher<String> galleryLauncher;
    Boolean isAvatarSelected = false;


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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(MyPostEditFragmentViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentMyPostEditBinding.inflate(inflater, container, false);
        id = MyPostEditFragmentArgs.fromBundle(getArguments()).getId();
        p = Model.instance().getPostById(viewModel.getData().getValue(), id);
        binding.progressBar.setVisibility(View.GONE);


//        binding.myPostEditFragmentRestaurantNameEditText.setHint(p.getRestaurantName());
//        binding.myPostEditFragmentRestaurantCityEditText.setHint(p.getCity());
//        binding.myPostEditFragmentRestaurantDescriptionEditText.setHint(p.getDescription());


        binding.myPostEditFragmentCancelBtn.setOnClickListener(view1 ->{
            Navigation.findNavController(view1).popBackStack();
        } );


        binding.myPostEditFragmentSaveBtn.setOnClickListener(view1 ->{
            binding.progressBar.setVisibility(View.VISIBLE);

            if(!binding.myPostEditFragmentRestaurantNameEditText.getText().toString().equals(""))
                p.setRestaurantName(binding.myPostEditFragmentRestaurantNameEditText.getText().toString());

            if(!binding.myPostEditFragmentRestaurantCityEditText.getText().toString().equals(""))
                p.setCity(binding.myPostEditFragmentRestaurantCityEditText.getText().toString());

            if(!binding.myPostEditFragmentRestaurantDescriptionEditText.getText().toString().equals(""))
                p.setDescription(binding.myPostEditFragmentRestaurantDescriptionEditText.getText().toString());

            if (isAvatarSelected){
                binding.imageView.setDrawingCacheEnabled(true);
                binding.imageView.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) binding.imageView.getDrawable()).getBitmap();
                Model.instance().uploadImage(String.valueOf(p.getPostId()), bitmap, url->{
                    if (url != null){
                        p.setImg(url);
                    }
                    Model.instance().addPost(p,()->{
                        binding.progressBar.setVisibility(View.GONE);
                        Log.d("tag","123");

                    });
                Navigation.findNavController(view1).popBackStack();

                });
            }else {
                Model.instance().addPost(p,()->{
                    binding.progressBar.setVisibility(View.GONE);
                    Log.d("tag","123");
                });
            Navigation.findNavController(view1).popBackStack();

            }
        } );

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

        binding.chooseFromCamera.setOnClickListener(view1->{
            cameraLauncher.launch(null);
        });

        binding.chooseFromGallery.setOnClickListener(view1->{
            galleryLauncher.launch("media/*");
        });

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (p.getImg()  != null && !p.getImg().isEmpty()) {
            Picasso.get().load(p.getImg()).placeholder(R.drawable.cold_icon).into(binding.imageView);
        }else{
            binding.imageView.setImageResource(R.drawable.cold_icon);
        }
    }
}