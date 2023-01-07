package com.example.recforrest.Model;

import android.util.Log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Model {

    private static final Model _instance = new Model();

    public static Model instance(){
        return _instance;
    }
    private Model(){}
    List<User> allUsers = new LinkedList<>();


    public void addUser(User u){allUsers.add(u);}
    public List<User> getAllUsers(){
        return allUsers;
    }
    public User getUserByEmail(String email){
        for(User user:allUsers){
            if (email.equals(user.getEmail()))
                return user;
        }
        return null;
    }

    public void deleteUserByEmail(String email){
        for(int i=0;i<allUsers.size();i++){
            if (email.equals(allUsers.get(i).getEmail()))
                allUsers.remove(i);
        }
    }
    public void printUser(String email){
        Log.d("TAG","user name : "+ getUserByEmail(email).getFullName().toString() +" user email : "+ getUserByEmail(email).getEmail().toString()+" user password : "+ getUserByEmail(email).getPassword().toString());
    }


}
