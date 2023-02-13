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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.example.recforrest.Model.Model;
import com.example.recforrest.Model.User;
import com.example.recforrest.databinding.FragmentUserEditBinding;
import com.squareup.picasso.Picasso;


public class UserEditFragment extends Fragment {

    @NonNull
    FragmentUserEditBinding binding;
    String email;
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
                //remove icons from the top menu
                menu.removeItem(R.id.chooseSignInOrUpFragment);
                menu.removeItem(R.id.myPostFragment1);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        }, this, Lifecycle.State.RESUMED);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentUserEditBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        email = UserEditFragmentArgs.fromBundle(getArguments()).getEmail();
        bindPic();
        binding.progressBar.setVisibility(View.GONE);

        //change the headline
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Edit profile");

        binding.UserEditFragmentSaveBtn.setOnClickListener(newView -> {
            String fullName= binding.UserEditFragmentShowFullNameEditText.getText().toString();
            binding.progressBar.setVisibility(View.VISIBLE);

            Model.instance().getAllUsers(list-> {
                User user = Model.instance().getUserByEmail(list,email);
                if(!fullName.equals(""))
                    user.setFullName(fullName);
                if (isAvatarSelected){
                    binding.UserEditFragmentImageview.setDrawingCacheEnabled(true);
                    binding.UserEditFragmentImageview.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) binding.UserEditFragmentImageview.getDrawable()).getBitmap();
                    Model.instance().uploadImage(String.valueOf(user.getEmail()), bitmap, url->{
                        if (url != null){
                            user.setImg(url);
                        }
                        Model.instance().addUser(user,()->{
                            binding.progressBar.setVisibility(View.GONE);
                        });
                        Navigation.findNavController(newView).popBackStack();

                    });
                }else {
                    Model.instance().addUser(user,()->{
                        binding.progressBar.setVisibility(View.GONE);
                    });
                    Navigation.findNavController(newView).popBackStack();

                }

            });

        } );

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
                if (result != null) {
                    binding.UserEditFragmentImageview.setImageBitmap(result);
                    isAvatarSelected = true;
                }
            }
        });
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null){
                    binding.UserEditFragmentImageview.setImageURI(result);
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


        binding.UserEditFragmentCancelBtn.setOnClickListener(newView -> Navigation.findNavController(newView).popBackStack());


        return view;
    }
    public void bindPic(){
        Model.instance().getAllUsers(list-> {
            User user = Model.instance().getUserByEmail(list, email);
            if (user.getImg()  != null && !user.getImg().isEmpty()) {
                Picasso.get().load(user.getImg()).placeholder(R.drawable.no_img2).into(binding.UserEditFragmentImageview);
            } else {
                binding.UserEditFragmentImageview.setImageResource(R.drawable.no_img2);
            }
        });
    }
}
