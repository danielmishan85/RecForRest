package com.example.recforrest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.recforrest.Model.Model;
import com.example.recforrest.Model.Post;
import java.util.List;

public class MyPostInfoFragmentViewModel extends ViewModel {

    private LiveData<List<Post>> data = Model.instance().getAllPosts();

    List<Post> getMyData(List<Post> l, String email) {
        return Model.instance().getMyPosts(l,email);
    }
    LiveData<List<Post>> getData() {
        return data;
    }

    Post getPostByEmailAndPos(String email,int pos)
    {
        return getMyData(getData().getValue(),email).get(pos);
    }

}
