package com.example.recforrest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.recforrest.model.Model;
import com.example.recforrest.model.Post;

import java.util.List;

public class MyPostFragmentViewModel extends ViewModel {

    private LiveData<List<Post>> data = Model.instance().getAllPosts();

    List<Post> getMyData(String email) {
        return Model.instance().getMyPosts(data.getValue(),email);
    }

    List<Post> getMyData(List<Post> l, String email) {
        return Model.instance().getMyPosts(l,email);
    }
    LiveData<List<Post>> getData() {
        return data;
    }

}
