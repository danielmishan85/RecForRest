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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

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
                //to remove some icons from the top menu
                menu.removeItem(R.id.chooseSignInOrUpFragment);
                menu.removeItem(R.id.postsFragment);
                menu.removeItem(R.id.myPostFragment1);
                menu.removeItem(R.id.userInfoFragment);

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
        binding.progressBar.setVisibility(View.GONE);

        //set the headline of the page
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Edit recommendation info");


        binding.myPostEditFragmentCancelBtn.setOnClickListener(newView ->{
            Navigation.findNavController(newView).popBackStack();
        } );


        binding.myPostEditFragmentSaveBtn.setOnClickListener(newView ->{
            binding.progressBar.setVisibility(View.VISIBLE);

            if(!binding.myPostEditFragmentRestaurantNameEditText.getText().toString().equals(""))
                viewModel.getPostById(id).setRestaurantName(binding.myPostEditFragmentRestaurantNameEditText.getText().toString());

            if(!binding.myPostEditFragmentRestaurantCityEditText.getText().toString().equals(""))
                viewModel.getPostById(id).setCity(binding.myPostEditFragmentRestaurantCityEditText.getText().toString());

            if(!binding.myPostEditFragmentRestaurantDescriptionEditText.getText().toString().equals(""))
                viewModel.getPostById(id).setDescription(binding.myPostEditFragmentRestaurantDescriptionEditText.getText().toString());

            if (isAvatarSelected){
                binding.imageView.setDrawingCacheEnabled(true);
                binding.imageView.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) binding.imageView.getDrawable()).getBitmap();
                Model.instance().uploadImage(String.valueOf(id), bitmap, url->{
                    if (url != null){
                        viewModel.getPostById(id).setImg(url);
                    }
                    Model.instance().addPost(viewModel.getPostById(id),()->{
                        binding.progressBar.setVisibility(View.GONE);

                    });
                Navigation.findNavController(newView).popBackStack();

                });
            }else {
                Model.instance().addPost( viewModel.getPostById(id),()->{
                    binding.progressBar.setVisibility(View.GONE);
                });
            Navigation.findNavController(newView).popBackStack();

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

        binding.chooseFromCamera.setOnClickListener(newView->{
            cameraLauncher.launch(null);
        });

        binding.chooseFromGallery.setOnClickListener(newView->{
            galleryLauncher.launch("media/*");
        });

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        if ( viewModel.getPostById(id).getImg()  != null && ! viewModel.getPostById(id).getImg().isEmpty()) {
            Picasso.get().load( viewModel.getPostById(id).getImg()).placeholder(R.drawable.no_img2).into(binding.imageView);
        }else{
            binding.imageView.setImageResource(R.drawable.no_img2);
        }
    }
}