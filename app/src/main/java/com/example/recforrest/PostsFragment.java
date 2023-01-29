package com.example.recforrest;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class PostsFragment extends Fragment {



    List<Post> postsList;
    RecyclerView list;
    ReviewRecyclerAdapter adapter;
    Button add;



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

        View view = inflater.inflate(R.layout.fragment_posts, container, false);
        postsList = Model.instance().getAllPosts();
        list = view.findViewById(R.id.postsFragment_rv);
        list.setHasFixedSize(true);

        list.setLayoutManager(new LinearLayoutManager(getContext())); //define the recycler view to be a list
        adapter = new ReviewRecyclerAdapter();
        list.setAdapter(adapter);


        adapter.setOnItemClickListener((int pos)-> {

                    PostsFragmentDirections.ActionPostsFragmentToPostInfoFragment action = PostsFragmentDirections.actionPostsFragmentToPostInfoFragment(pos);
                    Navigation.findNavController(view).navigate(action);

                }
        );

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();


    }

    //--------------------- view holder ---------------------------
    class PostViewHolder extends RecyclerView.ViewHolder{
        TextView city;
        TextView nameR;
        public PostViewHolder(@NonNull View itemView,OnItemClickListener listener) {
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
    class ReviewRecyclerAdapter extends RecyclerView.Adapter<PostViewHolder>{
        OnItemClickListener listener;
        void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }
        @NonNull
        @Override
        public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.posts_row,parent,false);
            return new PostViewHolder(view,listener);
        }

        @Override
        public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
            Post p = postsList.get(position);
            holder.bind(p,position);
        }

        @Override
        public int getItemCount() {
            return postsList.size();
        }
    }


}