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
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    NavController navController;
    NavController navController2;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth= FirebaseAuth.getInstance();

        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.main_nav_host);
        navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this,navController);


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
           // String email = UserInfoFragment.email;
            NavDirections action= MyPostFragmentDirections.actionGlobalMyPostFragment(firebaseAuth.getCurrentUser().getEmail());
            Navigation.findNavController(this, R.id.main_nav_host).navigate(action);
        }
        if (item.getItemId() == R.id.chooseSignInOrUpFragment){

            if(firebaseAuth.getCurrentUser().getEmail()!=null) {
                //user is already sign in
                NavDirections action= MyPostFragmentDirections.actionGlobalMyPostFragment(firebaseAuth.getCurrentUser().getEmail());
                Navigation.findNavController(this, R.id.main_nav_host).navigate(action);

            }
            else{
                NavDirections action= ChooseSignInOrUpFragmentDirections.actionGlobalChooseSignInOrUpFragment();
                Navigation.findNavController(this, R.id.main_nav_host).navigate(action);
            }
        }

        if (item.getItemId() == R.id.postsFragment) {
            firebaseAuth.signOut();
            NavDirections action= PostsFragmentDirections.actionGlobalPostsFragment();
            Navigation.findNavController(this, R.id.main_nav_host).navigate(action);

        }



        else{
            return NavigationUI.onNavDestinationSelected(item,navController);

        }
        return super.onOptionsItemSelected(item);
    }
}