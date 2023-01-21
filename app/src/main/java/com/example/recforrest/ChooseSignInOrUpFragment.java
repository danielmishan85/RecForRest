package com.example.recforrest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.recforrest.databinding.FragmentChooseSignInOrUpBinding;
import com.example.recforrest.databinding.FragmentSignUpBinding;


public class ChooseSignInOrUpFragment extends Fragment {

    @NonNull
    FragmentChooseSignInOrUpBinding binding;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity parentActivity = getActivity();
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.removeItem(R.id.chooseSignInOrUpFragment);
                menu.removeItem(R.id.postsFragment);

            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        },this, Lifecycle.State.RESUMED);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentChooseSignInOrUpBinding.inflate(inflater, container, false);
        View view=binding.getRoot();

        binding.chooseSignInBtn.setOnClickListener(view1 -> {
            NavDirections action = ChooseSignInOrUpFragmentDirections.actionChooseSignInOrUpFragmentToSignInFragment();
            Navigation.findNavController(view1).navigate(action);
        });
        binding.chooseSignUpBtn.setOnClickListener(view1 -> {
            NavDirections action = ChooseSignInOrUpFragmentDirections.actionChooseSignInOrUpFragmentToSignUpFragment();
            Navigation.findNavController(view1).navigate(action);
        });

        return view;
    }
}