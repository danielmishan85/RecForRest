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

import com.example.recforrest.model.Model;
import com.example.recforrest.model.User;
import com.example.recforrest.databinding.FragmentUserEditBinding;


public class UserEditFragment extends Fragment {

    @NonNull
    FragmentUserEditBinding binding;
    String email;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity parentActivity = getActivity();
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.removeItem(R.id.chooseSignInOrUpFragment);

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


        //binding.UserEditFragmentShowFullNameEditText.setHint(Model.instance().getUserByEmail(email).getFullName().toString());



        binding.UserEditFragmentSaveBtn.setOnClickListener(view1 -> {
            String fullName= binding.UserEditFragmentShowFullNameEditText.getText().toString();
          //  String password= binding.UserEditFragmentShowPasswordEditText.getText().toString();

            Model.instance().getAllUsers(list-> {
                User user = Model.instance().getUserByEmail(list,email);
                if(!fullName.equals(""))
                    user.setFullName(fullName);
                Model.instance().addUser(user,()->{
                    Navigation.findNavController(view).popBackStack();
                });

            });

//            if(!password.equals(""))
//                user.setPassword(password);


        });

        binding.UserEditFragmentCancelBtn.setOnClickListener(view1 -> Navigation.findNavController(view1).popBackStack());


        return view;
    }
}