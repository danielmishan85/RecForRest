package com.example.recforrest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.recforrest.Model.Model;
import com.example.recforrest.Model.Post;

import java.util.List;

public class MyPostEditFragmentViewModel extends ViewModel {

    private LiveData<List<Post>> data = Model.instance().getAllPosts();

    Post getPostById(int id)
    {
        return Model.instance().getPostById(data.getValue(),id);
    }


}
