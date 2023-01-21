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

import com.example.recforrest.Model.Model;
import com.example.recforrest.Model.User;
import com.example.recforrest.databinding.FragmentSignUpBinding;
import com.example.recforrest.databinding.FragmentUserInfoBinding;


public class UserInfoFragment extends Fragment {

    @NonNull
    FragmentUserInfoBinding binding;
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

        binding = FragmentUserInfoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        email = UserInfoFragmentArgs.fromBundle(getArguments()).getEmail();

        User user= Model.instance().getUserByEmail(email);
        binding.MyReviewDetailsFragmentShowEmailTextView.setText(user.getEmail().toString());
        binding.MyReviewDetailsFragmentShowFullNameTextView.setText(user.getFullName().toString());

        binding.UserInfoFragmentEditUserBtn.setOnClickListener((view1 -> {
            UserInfoFragmentDirections.ActionUserInfoFragmentToUserEditFragment action = UserInfoFragmentDirections.actionUserInfoFragmentToUserEditFragment(email);
            Navigation.findNavController(view).navigate(action);
        }));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        User user= Model.instance().getUserByEmail(email);
        binding.MyReviewDetailsFragmentShowEmailTextView.setText(user.getEmail().toString());
        binding.MyReviewDetailsFragmentShowFullNameTextView.setText(user.getFullName().toString());
    }
}