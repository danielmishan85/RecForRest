package com.example.recforrest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class UserActivity extends AppCompatActivity {

    NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.user_navhost);
        navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this,navController);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        Log.d("TAG", email + " user ");
//        Bundle args = new Bundle();
//        args.putString("email", email);
//        MyPostFragment fragment = new MyPostFragment();
//        fragment.setArguments(args);

        NavDirections action= MyPostFragmentDirections.actionGlobalMyPostFragment2(email);
        Navigation.findNavController(this, R.id.user_navhost).navigate(action);
    }
}