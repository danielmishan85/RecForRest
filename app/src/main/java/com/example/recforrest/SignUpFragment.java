package com.example.recforrest;

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
import android.widget.Toast;

import com.example.recforrest.model.Model;
import com.example.recforrest.model.User;
import com.example.recforrest.databinding.FragmentSignUpBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignUpFragment extends Fragment {

    FirebaseAuth firebaseAuth;
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
        firebaseAuth= FirebaseAuth.getInstance();


        binding.SignUpFragmentSignUpBtn.setOnClickListener((view1 -> {

            String name= binding.SignUpFragmentFullNameEditText.getText().toString();
            String email= binding.SignUpFragmentEmailEditText.getText().toString();
            String pass= binding.SignUpFragmentPasswordEditText.getText().toString();

            if(!(pass.equals("")) && !(email.equals("")) && !(name.equals("")) ) {


                Model.instance().addUser(new User(name,email,""),()->{
                            firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    SignUpFragmentDirections.ActionSignUpFragmentToUserInfoFragment action = SignUpFragmentDirections.actionSignUpFragmentToUserInfoFragment(binding.SignUpFragmentEmailEditText.getText().toString());
                                    Navigation.findNavController(view).navigate(action);
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    SignUpFragmentDirections.ActionSignUpFragmentToUserInfoFragment action = SignUpFragmentDirections.actionSignUpFragmentToUserInfoFragment(binding.SignUpFragmentEmailEditText.getText().toString());
                                    Navigation.findNavController(view).navigate(action);
                                }
                            });

                });

            }else{
                Toast.makeText(getActivity().getApplicationContext(), "fill all the fields", Toast.LENGTH_LONG).show();

            }
        }));



//
//            Model.instance().addUser(new User(binding.SignUpFragmentFullNameEditText.getText().toString(),binding.SignUpFragmentEmailEditText.getText().toString(),binding.SignUpFragmentPasswordEditText.getText().toString()));
//
//            SignUpFragmentDirections.ActionSignUpFragmentToUserInfoFragment action = SignUpFragmentDirections.actionSignUpFragmentToUserInfoFragment(binding.SignUpFragmentEmailEditText.getText().toString());
//            Navigation.findNavController(view).navigate(action);


        return view;
    }
}