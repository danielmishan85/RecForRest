package com.example.recforrest;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.recforrest.Model.Model;
import com.example.recforrest.Model.Post;
import com.example.recforrest.databinding.FragmentMyPostBinding;

import java.util.List;


public class MyPostFragment extends Fragment {
    @Nullable
    @NonNull FragmentMyPostBinding binding;
    List<Post> myPostsList;
    RecyclerView list;
    MyPostFragment.ReviewRecyclerAdapter adapter;
    Button add;
    String email;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity parentActivity = getActivity();
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                //menu.removeItem(R.id.chooseSignInOrUpFragment);
                menu.removeItem(R.id.postsFragment);
                menu.removeItem(R.id.myPostFragment);


            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        }, this, Lifecycle.State.RESUMED);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //View view = inflater.inflate(R.layout.fragment_my_post, container, false);
        binding = FragmentMyPostBinding.inflate(inflater, container, false);
        View view=binding.getRoot();
        email= MyPostFragmentArgs.fromBundle(getArguments()).getEmail();
        myPostsList = Model.instance().getMyPosts(email);



        list = view.findViewById(R.id.myPostFrag_rv);
        list.setHasFixedSize(true);

        list.setLayoutManager(new LinearLayoutManager(getContext())); //define the recycler view to be a list
        adapter = new MyPostFragment.ReviewRecyclerAdapter();
        list.setAdapter(adapter);


        binding.myPostFragNewPostBtn.setOnClickListener(view1 ->{

            MyPostFragmentDirections.ActionMyPostFragmentToNewPostFragment action = MyPostFragmentDirections.actionMyPostFragmentToNewPostFragment(email);
            Navigation.findNavController(view).navigate(action);

        });

        adapter.setOnItemClickListener((int pos)-> {

                    MyPostFragmentDirections.ActionMyPostFragmentToMyPostInfoFragment action = MyPostFragmentDirections.actionMyPostFragmentToMyPostInfoFragment(pos,email);
                    Navigation.findNavController(view).navigate(action);

                }
        );

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        myPostsList = Model.instance().getMyPosts(email);
        adapter.notifyDataSetChanged();


    }

    //--------------------- view holder ---------------------------
    class PostViewHolder extends RecyclerView.ViewHolder{
        TextView city;
        TextView nameR;
        public PostViewHolder(@NonNull View itemView, MyPostFragment.OnItemClickListener listener) {
            super(itemView);
            city = itemView.findViewById(R.id.postsRow_city);
            nameR = itemView.findViewById(R.id.postsRow_RestaurantName_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    listener.onItemClick(pos);
                }
            });
        }

        public void bind(Post p, int pos) {
            city.setText(p.getCity());
            nameR.setText(p.getRestaurantName());
        }
    }


    //---------------------OnItemClickListener ---------------------------
    public interface OnItemClickListener{
        void onItemClick(int pos);
    }



    //---------------------Recycler adapter ---------------------------
    class ReviewRecyclerAdapter extends RecyclerView.Adapter<MyPostFragment.PostViewHolder>{
        MyPostFragment.OnItemClickListener listener;
        void setOnItemClickListener(MyPostFragment.OnItemClickListener listener){
            this.listener = listener;
        }
        @NonNull
        @Override
        public MyPostFragment.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.posts_row,parent,false);
            return new MyPostFragment.PostViewHolder(view,listener);
        }

        @Override
        public void onBindViewHolder(@NonNull MyPostFragment.PostViewHolder holder, int position) {
            Post p = myPostsList.get(position);
            holder.bind(p,position);
        }

        @Override
        public int getItemCount() {
            return myPostsList.size();
        }
    }



}