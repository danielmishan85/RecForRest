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
    private Model(){
        for(int i=0;i<20;i++){
            addPost(new Post( i,"resturant "+i,"dubai ","des ", "0235643"));
        }
    }
    List<User> allUsers = new LinkedList<>();
    List<Post> allPosts = new LinkedList<>();


//*******************************User************************************
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


//*******************************post************************************
    public void addPost(Post p){allPosts.add(p);}
    public List<Post> getAllPosts(){
        return allPosts;
    }
    public Post getPostById(int id){
        for(Post p:allPosts){
            if (id==p.getId())
                return p;
        }
        return null;
    }

    public void deletePostById(int id){
        for(int i=0;i<allPosts.size();i++){
            if (id==allPosts.get(i).getId())
                allPosts.remove(i);
        }
    }
    public void printPost(int id){
        Log.d("TAG","post id : "+ getPostById(id).getId() +"post city : "+ getPostById(id).getCity().toString()+"post description : "+ getPostById(id).getDescription().toString()+"post phone : "+ getPostById(id).getPhone().toString());
    }

}
