package com.example.recforrest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.recforrest.model.Model;
import com.example.recforrest.model.Post;

import java.util.List;

public class PostsFragmentViewModel extends ViewModel {

    private LiveData<List<Post>> data = Model.instance().getAllPosts();

    LiveData<List<Post>> getData() {
        return data;
    }
}
