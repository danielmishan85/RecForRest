package com.example.recforrest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    NavController navController;
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
        if (item.getItemId() == R.id.postsFragment) {
            NavDirections action= PostsFragmentDirections.actionGlobalPostsFragment();
            Navigation.findNavController(this, R.id.main_nav_host).navigate(action);
            //when user wants to see the list of all posts he must sign up from the application
            firebaseAuth.signOut();
        }
        if (item.getItemId() == R.id.myPostFragment1){
            //if the user wants to see his reviews
            NavDirections action= MyPostFragmentDirections.actionGlobalMyPostFragment(firebaseAuth.getCurrentUser().getEmail());
            Navigation.findNavController(this, R.id.main_nav_host).navigate(action);
        }
        if (item.getItemId() == R.id.chooseSignInOrUpFragment){
            //if the user wants to get into the application, its depends:
            //if he already logged in -> he will redirect to userInfoFragment
            //if the user is not logged in -> he will be forward to a page in which he will need to choose if sign in or sign up
            NavDirections action= ChooseSignInOrUpFragmentDirections.actionGlobalChooseSignInOrUpFragment();
            Navigation.findNavController(this, R.id.main_nav_host).navigate(action);
        }

        if (item.getItemId() == R.id.userInfoFragment){
            //if the user wants to change his details / log out from the application
            NavDirections action= UserInfoFragmentDirections.actionGlobalUserInfoFragment(firebaseAuth.getCurrentUser().getEmail());
            Navigation.findNavController(this, R.id.main_nav_host).navigate(action);
        }


        else{
            return NavigationUI.onNavDestinationSelected(item,navController);

        }
        return super.onOptionsItemSelected(item);
    }
}