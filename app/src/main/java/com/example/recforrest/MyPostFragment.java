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

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recforrest.model.Model;
import com.example.recforrest.model.Post;
import com.example.recforrest.databinding.FragmentMyPostBinding;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MyPostFragment extends Fragment {
    @Nullable
    @NonNull FragmentMyPostBinding binding;
    RecyclerView list;
    MyPostFragment.ReviewRecyclerAdapter adapter;
    String email;
    MyPostFragmentViewModel viewModel;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(MyPostFragmentViewModel.class);
    }
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



        list = view.findViewById(R.id.myPostFrag_rv);
        list.setHasFixedSize(true);

        list.setLayoutManager(new LinearLayoutManager(getContext())); //define the recycler view to be a list
        adapter = new MyPostFragment.ReviewRecyclerAdapter(getLayoutInflater(),viewModel.getData().getValue());
        list.setAdapter(adapter);


        binding.myPostFragNewPostBtn.setOnClickListener(view1 ->{

            MyPostFragmentDirections.ActionMyPostFragmentToNewPostFragment action = MyPostFragmentDirections.actionMyPostFragmentToNewPostFragment(email);
            Navigation.findNavController(view).navigate(action);

        });

        viewModel.getData().observe(getViewLifecycleOwner(),list->{
            adapter.setData(viewModel.getMyData(list,email));
        });

//        Model.instance().EventReviewsListLoadingState.observe(getViewLifecycleOwner(),status->{
//            sw.setRefreshing(status == Model.LoadingState.LOADING);
//        });
//
//        sw.setOnRefreshListener(()->{
//            reloadData();
//            Log.d("TAG", "refresh");
//
//        });
        adapter.setOnItemClickListener((int pos)-> {

                    MyPostFragmentDirections.ActionMyPostFragmentToMyPostInfoFragment action = MyPostFragmentDirections.actionMyPostFragmentToMyPostInfoFragment(pos,email);
                    Navigation.findNavController(view).navigate(action);

                }
        );

        return view;

    }

    void reloadData() {
        Model.instance().refreshAllPosts();
    }

    @Override
    public void onStart() {
        super.onStart();
        reloadData();
        adapter.notifyDataSetChanged();
    }

    //--------------------- view holder ---------------------------
    class PostViewHolder extends RecyclerView.ViewHolder{
        TextView city;
        TextView nameR;
        ImageView avatarImg;
        public PostViewHolder(@NonNull View itemView, MyPostFragment.OnItemClickListener listener) {
            super(itemView);
            city = itemView.findViewById(R.id.postsRow_city);
            nameR = itemView.findViewById(R.id.postsRow_RestaurantName_tv);
            avatarImg= itemView.findViewById(R.id.postsRow_iv);

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
            if (p.getImg()  != null && !p.getImg().isEmpty()) {
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
    class ReviewRecyclerAdapter extends RecyclerView.Adapter<MyPostFragment.PostViewHolder>{
        MyPostFragment.OnItemClickListener listener;
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
            Post p = data.get(position);
            holder.bind(p,position);
        }

        @Override
        public int getItemCount() {
            if (data == null) return 0;
            return data.size();
        }
    }



}