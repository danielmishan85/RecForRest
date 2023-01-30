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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recforrest.Model.Model;
import com.example.recforrest.Model.Post;
import com.example.recforrest.databinding.FragmentMyPostInfoBinding;
import com.example.recforrest.databinding.FragmentSignUpBinding;


public class MyPostInfoFragment extends Fragment {

    int pos;
    Post p;
    static ImageView icon;
    static TextView temperaturetv;
    String email;
    @NonNull FragmentMyPostInfoBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity parentActivity = getActivity();
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.removeItem(R.id.chooseSignInOrUpFragment);
//                menu.removeItem(R.id.postsFragment);
//                menu.removeItem(R.id.myPostFragment);

            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        }, this, Lifecycle.State.RESUMED);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentMyPostInfoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        pos= MyPostInfoFragmentArgs.fromBundle(getArguments()).getPos();
        email = MyPostInfoFragmentArgs.fromBundle(getArguments()).getEmail();

        icon = view.findViewById(R.id.myPostInfoFragment_weather_icon);
        temperaturetv=view.findViewById(R.id.myPostInfoFragment_temp);
        p= Model.instance().getMyPosts(email).get(pos);
        bind(p);

        binding.myPostInfoFragmentBackBtn.setOnClickListener(view1 ->{
            Navigation.findNavController(view1).popBackStack();
        } );

        binding.myPostInfoFragmentEditBtn.setOnClickListener(view1 ->{
            NavDirections action = MyPostInfoFragmentDirections.actionMyPostInfoFragmentToMyPostEditFragment(p.getId());
            Navigation.findNavController(view1).navigate(action);
        } );
        return view;
    }

    public void bind(Post post) {
        binding.myPostInfoFragmentEditCity.setText(post.getCity());
        binding.myPostInfoFragmentEditRestaurantName.setText(post.getRestaurantName());
        binding.myPostInfoFragmentEditDescription.setText(post.getDescription());
        WeatherAPI.GetWeatherTask task = new WeatherAPI.GetWeatherTask();
        task.execute(binding.myPostInfoFragmentEditCity.getText().toString(),"my posts");
    }

    public static void changeIconAccordingToTemp(double temperature)
    {
        int roundTemp=(int)Math.round(temperature-273.27);
        temperaturetv.setText(roundTemp+" °C");
        if (temperature < 273.15) {
            // Cold
            icon.setImageResource(R.drawable.cold_icon);
        } else if (temperature < 283.15) {
            // Cool
            icon.setImageResource(R.drawable.cool_icon);
        } else if (temperature < 293.15) {
            // Mild
            icon.setImageResource(R.drawable.mild_icon);
        } else {
            // Hot
            icon.setImageResource(R.drawable.hot_icon);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        bind(p);
    }
}