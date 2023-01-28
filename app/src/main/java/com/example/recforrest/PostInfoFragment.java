package com.example.recforrest;

import android.media.Image;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recforrest.Model.Model;
import com.example.recforrest.Model.Post;
import com.example.recforrest.databinding.FragmentPostInfoBinding;
import com.example.recforrest.databinding.FragmentSignInBinding;

import java.util.List;


public class PostInfoFragment extends Fragment {

    int pos;
    TextView city, description, name;
    static ImageView icon;
    static TextView temperaturetv;
    Button backBtn,editBtn;
    Post p;
    List<Post> postsList= Model.instance().getAllPosts();
    @NonNull FragmentPostInfoBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity parentActivity = getActivity();
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.removeItem(R.id.chooseSignInOrUpFragment);
                menu.removeItem(R.id.postsFragment);
                menu.removeItem(R.id.myPostFragment);

            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        }, this, Lifecycle.State.RESUMED);

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPostInfoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        pos = PostInfoFragmentArgs.fromBundle(getArguments()).getPos();


        icon=view.findViewById(R.id.postInfoFragment_weather_icon);
        temperaturetv=view.findViewById(R.id.postInfoFragment_temp);

        p=postsList.get(pos);
        this.bind(p,pos);


        binding.postInfoFragmentBackBtn.setOnClickListener(view1 ->{
            Navigation.findNavController(view1).popBackStack();
        } );
        return view;
    }

    public void bind(Post post, int pos) {
        binding.postInfoFragmentEditCity.setText(post.getCity());
        binding.postInfoFragmentEditRestaurantName.setText(post.getRestaurantName());
        binding.postInfoFragmentEditDescription.setText(post.getDescription());
        WeatherAPI.GetWeatherTask task = new WeatherAPI.GetWeatherTask();
        task.execute(binding.postInfoFragmentEditCity.getText().toString());
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
        bind(p,pos);
    }
}