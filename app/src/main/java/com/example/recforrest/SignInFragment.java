package com.example.recforrest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.example.recforrest.databinding.FragmentSignInBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignInFragment extends Fragment {

    @NonNull
    FragmentSignInBinding binding;
    FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity parentActivity = getActivity();
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                //remove icons from the app
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

        binding = FragmentSignInBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        firebaseAuth= FirebaseAuth.getInstance();
        binding.progressBar.setVisibility(View.GONE);

        //changing the headline
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Sign in");

        binding.SignInFragmentSignInBtn.setOnClickListener((view1) -> {
            binding.progressBar.setVisibility(View.VISIBLE);

            String email=binding.SignInFragmentEmailEditText.getText().toString();
            String password=binding.SignInFragmentPasswordEditText.getText().toString();

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });

            //if the user filled all the fields
            if((!(password.equals("")) && !(email.equals("")))) {

                firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        SignInFragmentDirections.ActionSignInFragmentToUserInfoFragment action = SignInFragmentDirections.actionSignInFragmentToUserInfoFragment(email);
                        binding.progressBar.setVisibility(View.GONE);
                        Navigation.findNavController(view).navigate(action);
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.progressBar.setVisibility(View.GONE);

                    }
                });

            }

            else { //if he does not fill all the fields
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity().getApplicationContext(), "fill all the fields", Toast.LENGTH_LONG).show();

            }
        });


        return view;
    }
}