package com.example.recforrest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class PostsFragment extends Fragment {



    PostsFragmentViewModel viewModel;
    RecyclerView list;
    ReviewRecyclerAdapter adapter;
    Button add;
    SwipeRefreshLayout swipe;
    FirebaseAuth firebaseAuth;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(PostsFragmentViewModel.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity parentActivity = getActivity();
        firebaseAuth= FirebaseAuth.getInstance();
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.removeItem(R.id.myPostFragment1);
                menu.removeItem(R.id.postsFragment);
                if(firebaseAuth.getCurrentUser() != null){ //if the user is logged in
                    menu.removeItem(R.id.chooseSignInOrUpFragment);
                }else{ //first time he need to sign up+
                    menu.removeItem(R.id.userInfoFragment);
                }
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
        list = view.findViewById(R.id.postsFragment_rv);
        list.setHasFixedSize(true);
        swipe= view.findViewById(R.id.posts_swipeRefresh);
        list.setLayoutManager(new LinearLayoutManager(getContext())); //define the recycler view to be a list
       // LiveData<List<Post>> l =Model.instance().getAllPosts();
        adapter = new ReviewRecyclerAdapter(getLayoutInflater(),viewModel.getData().getValue());
        list.setAdapter(adapter);



        adapter.setOnItemClickListener((int pos)-> {

                    PostsFragmentDirections.ActionPostsFragmentToPostInfoFragment action = PostsFragmentDirections.actionPostsFragmentToPostInfoFragment(pos);
                    Navigation.findNavController(view).navigate(action);

                }
        );
        viewModel.getData().observe(getViewLifecycleOwner(),list->{
            adapter.setData(list);
        });

        Model.instance().EventReviewsListLoadingState.observe(getViewLifecycleOwner(),status->{
            swipe.setRefreshing(status == Model.LoadingState.LOADING);
        });

        swipe.setOnRefreshListener(()->{
            reloadData();
        });
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        reloadData();


    }

    void reloadData() {
        Model.instance().refreshAllPosts();
    }
    //--------------------- view holder ---------------------------
    class PostViewHolder extends RecyclerView.ViewHolder{
        TextView city;
        TextView nameR;
        ImageView avatarImg;
        public PostViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            city = itemView.findViewById(R.id.postsRow_city);
            nameR = itemView.findViewById(R.id.postsRow_RestaurantName_tv);
            avatarImg = itemView.findViewById(R.id.postsRow_iv);

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
            if (!p.getImg().equals("")) {
                Picasso.get().load(p.getImg()).placeholder(R.drawable.cold_icon).into(avatarImg);
            }else{
                avatarImg.setImageResource(R.drawable.cold_icon);
            }
        }
    }


    //---------------------OnItemClickListener ---------------------------
    public interface OnItemClickListener{
        void onItemClick(int pos);
    }



    //---------------------Recycler adapter ---------------------------
    class ReviewRecyclerAdapter extends RecyclerView.Adapter<PostViewHolder>{
        OnItemClickListener listener;
        LayoutInflater inflater;
        List<Post> data;

        public void setData(List<Post> data){
            this.data = data;
            notifyDataSetChanged();
        }
        public ReviewRecyclerAdapter(LayoutInflater inflater, List<Post> data){
            this.inflater = inflater;
            this.data = data;
        }
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
            Post p = data.get(position);
            holder.bind(p,position);
        }

        @Override
        public int getItemCount() {
            if (data == null) return 0;
            return data.size();
        }
    }


//    void reloadData(){
//        binding.progressBar.setVisibility(View.VISIBLE);
//        Model.instance().getAllStudents((stList)->{
//            viewModel.setData(stList);
//            adapter.setData(viewModel.getData());
//            binding.progressBar.setVisibility(View.GONE);
//        });
//    }

}