package com.example.recforrest;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.recforrest.Model.Model;
import com.example.recforrest.Model.User;
import com.example.recforrest.databinding.FragmentUserInfoBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

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
                //remove icons from the top menu
                menu.removeItem(R.id.chooseSignInOrUpFragment);
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

        binding = FragmentUserInfoBinding.inflate(inflater, container, false);
        //changing the headline of the current page
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Your information");
        View view = binding.getRoot();
        email=UserInfoFragmentArgs.fromBundle(getArguments()).getEmail();
        bind(email);
        binding.UserInfoFragmentEditUserBtn.setOnClickListener((newView -> {
            UserInfoFragmentDirections.ActionUserInfoFragmentToUserEditFragment action = UserInfoFragmentDirections.actionUserInfoFragmentToUserEditFragment(email);
            Navigation.findNavController(view).navigate(action);
        }));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        bind(email);
    }

    public void bind(String email){
        binding.UserInfoFragmentProgressBar.setVisibility(View.VISIBLE);
        Model.instance().getAllUsers(list->
        {
            User user = Model.instance().getUserByEmail(list,email);
            binding.MyReviewDetailsFragmentShowEmailTextView.setText(email);
            binding.MyReviewDetailsFragmentShowFullNameTextView.setText(user.getFullName().toString());
            if (user.getImg()  != null && !user.getImg().isEmpty()) {
                Picasso.get().load(user.getImg()).placeholder(R.drawable.no_img2).into(binding.UserInfoFragmentImageview);
            }else{
                binding.UserInfoFragmentImageview.setImageResource(R.drawable.no_img2);

            }
            binding.UserInfoFragmentProgressBar.setVisibility(View.GONE);
        });


    }
}