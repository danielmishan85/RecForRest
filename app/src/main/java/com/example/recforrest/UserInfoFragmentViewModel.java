package com.example.recforrest;

import androidx.lifecycle.ViewModel;

import com.example.recforrest.model.Model;
import com.example.recforrest.model.User;

import java.util.LinkedList;
import java.util.List;

public class UserInfoFragmentViewModel extends ViewModel {

    private List<User> data = new LinkedList<>();

    public User getUserByEmail(String email)
    {
       return Model.instance().getUserByEmail(getData(),email);
    }

    List<User> getData() {
        Model.instance().getAllUsers(list->{
            data=list;
        });

        return data;

    }
}
