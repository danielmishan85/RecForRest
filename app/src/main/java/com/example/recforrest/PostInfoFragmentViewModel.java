package com.example.recforrest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.recforrest.Model.Model;
import com.example.recforrest.Model.Post;
import java.util.List;

public class PostInfoFragmentViewModel extends ViewModel {

    private LiveData<List<Post>> data = Model.instance().getAllPosts();

    LiveData<List<Post>> getData() {
        return data;
    }
}
