package com.example.recforrest;

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
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.recforrest.Model.Model;
import com.example.recforrest.Model.Post;
import com.example.recforrest.databinding.FragmentNewPostBinding;


public class NewPostFragment extends Fragment {

    @NonNull FragmentNewPostBinding binding;
    ActivityResultLauncher<Void> cameraLauncher;
    ActivityResultLauncher<String> galleryLauncher;
    Boolean isAvatarSelected  = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity parentActivity = getActivity();
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                //remove some icons from top menu
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentNewPostBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        String email = NewPostFragmentArgs.fromBundle(getArguments()).getEmail();
        binding.progressBar.setVisibility(View.GONE);

        //change the headline of the application
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("New recommendation");

        binding.newPostFragCancelBtn.setOnClickListener(newView->{
            Navigation.findNavController(newView).popBackStack();
        });

        //when pressing on save button
        binding.newPostFragSaveBtn.setOnClickListener(newView -> {
            binding.progressBar.setVisibility(View.VISIBLE);

            //change the name,city anf description according to the new input
            String restaurantName= binding.newPostFragmentRestaurantNameEditText.getText().toString();
            String city= binding.newPostFragmentRestaurantCityEditText.getText().toString();
            String description= binding.newPostFragmentRestaurantDescriptionEditText.getText().toString();

            //in case of some of the input is empty the user will get a notification
            if(restaurantName.equals("")||city.equals("")||description.equals("")){
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity().getApplicationContext(),"Please fill all the inputs",Toast.LENGTH_LONG).show();
            }
            else {
                //of the user fill all the input
                Post p=new Post(restaurantName,city,"",description,email);
                p.randomId();
                if (isAvatarSelected){
                    binding.imageView.setDrawingCacheEnabled(true);
                    binding.imageView.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) binding.imageView.getDrawable()).getBitmap();

                    //if the user choose to upload an image to the new post
                    Model.instance().uploadImage(String.valueOf(p.getPostId()), bitmap, url->{
                        if (url != null){
                            p.setImg(url);
                        }
                        Model.instance().addPost(p,()->{
                            binding.progressBar.setVisibility(View.GONE);
                            Navigation.findNavController(view).popBackStack();

                        });
                    });
                }else {
                    Model.instance().addPost(p,()->{
                        binding.progressBar.setVisibility(View.GONE);
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