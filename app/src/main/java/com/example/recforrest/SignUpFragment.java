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
import com.example.recforrest.Model.User;
import com.example.recforrest.databinding.FragmentSignUpBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignUpFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    @NonNull FragmentSignUpBinding binding;
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
                //remove icons from the menu
                menu.removeItem(R.id.chooseSignInOrUpFragment);
                menu.removeItem(R.id.postsFragment);
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

        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        firebaseAuth= FirebaseAuth.getInstance();
        binding.progressBar.setVisibility(View.GONE);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Sign up");

        binding.SignUpFragmentSignUpBtn.setOnClickListener((newView -> {
            binding.progressBar.setVisibility(View.VISIBLE);

            String name= binding.SignUpFragmentFullNameEditText.getText().toString();
            String email= binding.SignUpFragmentEmailEditText.getText().toString();
            String pass= binding.SignUpFragmentPasswordEditText.getText().toString();

            if(!(pass.equals("")) && !(email.equals("")) && !(name.equals("")) ) {
                    User user=new User(name,email,"");
                    if (isAvatarSelected){
                        binding.SignUpFragmentImage.setDrawingCacheEnabled(true);
                        binding.SignUpFragmentImage.buildDrawingCache();
                        Bitmap bitmap = ((BitmapDrawable) binding.SignUpFragmentImage.getDrawable()).getBitmap();
                        Model.instance().uploadImage(String.valueOf(user.getEmail()), bitmap, url->{
                            if (url != null){
                                user.setImg(url);
                            }
                            Model.instance().addUser(user,()->{
                                firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        SignUpFragmentDirections.ActionSignUpFragmentToUserInfoFragment action = SignUpFragmentDirections.actionSignUpFragmentToUserInfoFragment(binding.SignUpFragmentEmailEditText.getText().toString());
                                        binding.progressBar.setVisibility(View.GONE);
                                        Navigation.findNavController(view).navigate(action);
                                    }

                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        binding.progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();


                                    }
                                });

                            });
                            });

                    }else {
                        Model.instance().addUser(user,()->{
                            firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    binding.progressBar.setVisibility(View.GONE);
                                    SignUpFragmentDirections.ActionSignUpFragmentToUserInfoFragment action = SignUpFragmentDirections.actionSignUpFragmentToUserInfoFragment(binding.SignUpFragmentEmailEditText.getText().toString());
                                    Navigation.findNavController(view).navigate(action);
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    binding.progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            });
                        });
                    }
            }else{
                Toast.makeText(getActivity().getApplicationContext(), "fill all the fields", Toast.LENGTH_LONG).show();
            }
        }));



        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
                if (result != null) {
                    binding.SignUpFragmentImage.setImageBitmap(result);
                    isAvatarSelected = true;
                }
            }
        });
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null){
                    binding.SignUpFragmentImage.setImageURI(result);
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

        return view;
    }
}