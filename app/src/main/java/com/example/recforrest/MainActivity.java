package com.example.recforrest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    NavController navController;
    NavController navController2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.main_nav_host);
        navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this,navController);

//        NavHostFragment navHostFragment2 = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.user_navhost);
//        navController2 = navHostFragment.getNavController();
//        NavigationUI.setupActionBarWithNavController(this, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            navController.popBackStack();
        }
        if (item.getItemId() == R.id.myPostFragment){
            Intent intent = new Intent(this, UserActivity.class);
            String email = UserInfoFragment.email;
            intent.putExtra("email", email);
            startActivity(intent);


        }
        else{
            return NavigationUI.onNavDestinationSelected(item,navController);

        }
        return super.onOptionsItemSelected(item);
    }
}