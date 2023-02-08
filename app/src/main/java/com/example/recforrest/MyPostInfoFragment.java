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
import com.squareup.picasso.Picasso;


public class MyPostInfoFragment extends Fragment {

    int pos;
    static ImageView icon;
    static TextView temperaturetv;
    ImageView avatarImg;
    String email;
    @NonNull FragmentMyPostInfoBinding binding;
    MyPostInfoFragmentViewModel viewModel;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(MyPostInfoFragmentViewModel.class);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity parentActivity = getActivity();
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                //remove some icons from top menu
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentMyPostInfoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //changing the headline of the page
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Post information");

        pos= MyPostInfoFragmentArgs.fromBundle(getArguments()).getPos();
        email = MyPostInfoFragmentArgs.fromBundle(getArguments()).getEmail();
        avatarImg = view.findViewById(R.id.myPostInfoFragment_restaurant_imageview);

        icon = view.findViewById(R.id.myPostInfoFragment_weather_icon);
        temperaturetv=view.findViewById(R.id.myPostInfoFragment_temp);

        bind(viewModel.getPostByEmailAndPos(email,pos));

        binding.myPostInfoFragmentBackBtn.setOnClickListener(newView ->{
            Navigation.findNavController(newView).popBackStack();
        } );

        binding.myPostInfoFragmentEditBtn.setOnClickListener(newView ->{
            NavDirections action = MyPostInfoFragmentDirections.actionMyPostInfoFragmentToMyPostEditFragment(viewModel.getPostByEmailAndPos(email,pos).getPostId());
            Navigation.findNavController(newView).navigate(action);
        } );
        return view;
    }

    public void bind(Post post) {
        //after the information is changed, need to change the text to the updated one
        binding.myPostInfoFragmentEditCity.setText(post.getCity());
        binding.myPostInfoFragmentEditRestaurantName.setText(post.getRestaurantName());
        binding.myPostInfoFragmentEditDescription.setText(post.getDescription());

        //in order to run the weather API function "GetWeatherTask" on another thread
        WeatherAPI.GetWeatherTask task = new WeatherAPI.GetWeatherTask();
        task.execute(binding.myPostInfoFragmentEditCity.getText().toString(),"my posts");
        if (post.getImg()  != null && !post.getImg().isEmpty()) {
            Picasso.get().load(post.getImg()).placeholder(R.drawable.no_img2).into(avatarImg);
        }else{
            avatarImg.setImageResource(R.drawable.no_img2);
        }
    }

    //change the icon of the weather according to the temperature
    public static void changeIconAccordingToTemp(double temperature)
    {
        int roundTemp=(int)Math.round(temperature-273.27); // round the temperature and change it to celsius.
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
        bind(viewModel.getPostByEmailAndPos(email,pos));
    }

    @Override
    public void onResume() {
        super.onResume();
        bind(viewModel.getPostByEmailAndPos(email,pos));

    }
}