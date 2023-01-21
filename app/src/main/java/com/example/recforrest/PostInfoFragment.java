package com.example.recforrest;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recforrest.Model.Model;
import com.example.recforrest.Model.Post;

import java.util.List;


public class PostInfoFragment extends Fragment {

    int pos;
    TextView city, description, name;
    ImageView icon;
    TextView temperature;
    Button backBtn,editBtn;
    Post p;
    List<Post> postsList= Model.instance().getAllPosts();
    WeatherAPI weather = new WeatherAPI();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_info, container, false);
        pos = PostInfoFragmentArgs.fromBundle(getArguments()).getPos();

        city = view.findViewById(R.id.postInfoFragment_edit_city);
        description=view.findViewById(R.id.postInfoFragment_edit_description);
        name= view.findViewById(R.id.postInfoFragment_edit_restaurant_name);
        backBtn=view.findViewById(R.id.postInfoFragment_back_btn);
        icon=view.findViewById(R.id.postInfoFragment_weather_icon);
        temperature=view.findViewById(R.id.postInfoFragment_temp);



        p=postsList.get(pos);
        this.bind(p,pos);
        weather=WeatherAPI.getWeatherDataForCity(p.getCity());

        int roundTemp=(int)Math.round(weather.temperature);
        temperature.setText(roundTemp+" °C");
        int drawableResourceId = getResources().getIdentifier(weather.icon, "drawable", getActivity().getPackageName());
        icon.setImageResource(drawableResourceId);

        backBtn.setOnClickListener(view1 ->{
            Navigation.findNavController(view1).popBackStack();
        } );
        return view;
    }

    public void bind(Post post, int pos) {
        city.setText(post.getCity());
        name.setText(post.getRestaurantName());
        description.setText(post.getDescription());
    }


    @Override
    public void onStart() {
        super.onStart();
        bind(p,pos);
    }
}