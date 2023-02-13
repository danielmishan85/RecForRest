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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.recforrest.Model.Post;
import com.example.recforrest.databinding.FragmentPostInfoBinding;
import com.squareup.picasso.Picasso;


public class PostInfoFragment extends Fragment {

    int pos;
    static ImageView icon;
    static TextView temperaturetv;
    PostInfoFragmentViewModel viewModel;
    @NonNull FragmentPostInfoBinding binding;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(PostInfoFragmentViewModel.class);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity parentActivity = getActivity();
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                //remove some icons from the top menu
                menu.removeItem(R.id.chooseSignInOrUpFragment);
                menu.removeItem(R.id.postsFragment);
                menu.removeItem(R.id.myPostFragment1);
                menu.removeItem(R.id.userInfoFragment);
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

        //change the headline of the current page
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Recommendation info");

        icon=view.findViewById(R.id.postInfoFragment_weather_icon);
        temperaturetv=view.findViewById(R.id.postInfoFragment_temp);


        this.bind(viewModel.getData().getValue().get(pos));

        binding.postInfoFragmentBackBtn.setOnClickListener(view1 ->{
            Navigation.findNavController(view1).popBackStack();
        } );
        return view;
    }

    public void bind(Post post) {
        binding.postInfoFragmentEditCity.setText(post.getCity());
        binding.postInfoFragmentEditRestaurantName.setText(post.getRestaurantName());
        binding.postInfoFragmentEditDescription.setText(post.getDescription());
        if (post.getImg()  != null && !post.getImg().isEmpty()) {
            Picasso.get().load(post.getImg()).placeholder(R.drawable.no_img2).into(binding.postInfoFragmentRestaurantImageview);
        }else{
            binding.postInfoFragmentRestaurantImageview.setImageResource(R.drawable.no_img2);
        }
        WeatherAPI.GetWeatherTask task = new WeatherAPI.GetWeatherTask();
        task.execute(binding.postInfoFragmentEditCity.getText().toString(),"all posts");
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
        bind(viewModel.getData().getValue().get(pos));
    }
}