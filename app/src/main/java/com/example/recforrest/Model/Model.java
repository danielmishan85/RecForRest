package com.example.recforrest.Model;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {

    private static final Model _instance = new Model();

    public static Model instance(){
        return _instance;
    }
    private Model(){

    }
    AppLocalDbRepository localDb=AppLocalDb.getAppDb();
    Executor executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    private FireBaseModel firebaseModel = new FireBaseModel();
    private LiveData<List<Post>> postsList;

    public interface Listener<T>{
        void onComplete(T data);
    }
    public interface Listener2<Void>{
        void onComplete();
    }
    public enum LoadingState{
        LOADING,
        NOT_LOADING
    }
    final public MutableLiveData<LoadingState> EventReviewsListLoadingState = new MutableLiveData<LoadingState>(LoadingState.NOT_LOADING);


    public void addUser(User u, Listener2<Void> listener) { firebaseModel.addUser(u,listener);}
    public void getAllUsers(Listener<List<User>> callback){
        firebaseModel.getAllUsers(callback);
    }
    public User getUserByEmail(List<User> users, String email){
        for(User u:users)
        {
            if(u.getEmail().equals(email))
                return u;
        }
        return new User();
    }

//POSTS

    public LiveData<List<Post>> getAllReviews() {
        if(postsList == null){
            postsList = localDb.postDao().getAllPosts();
            refreshAllPosts();
        }
        return postsList;
    }

    public void refreshAllPosts(){
        EventReviewsListLoadingState.setValue(LoadingState.LOADING);
        // get local last update
        Long localLastUpdate = Post.getLocalLastUpdate();
        // get all updated recorde from firebase since local last update
        firebaseModel.getAllReviewsSince(localLastUpdate,list->{
            executor.execute(()->{
                Log.d("TAG", " firebase return : " + list.size());
                Long time = localLastUpdate;
                for(Post st:list){
                    // insert new records into ROOM
                    localDb.postDao().insertAll(st);
                    if (time < st.getLastUpdated()){
                        time = st.getLastUpdated();
                    }
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // update local last update
                Post.setLocalLastUpdate(time);
                Log.d("TAG", " loding");
                EventReviewsListLoadingState.postValue(LoadingState.NOT_LOADING);
            });
        });
    }


    public void addPost(Post post, Listener2<Void> listener) {
        firebaseModel.addPost(post,()->{
            refreshAllPosts();
            listener.onComplete();
        });
    }

    public void getAllPosts(Listener<List<Post>> callback){
        firebaseModel.getAllPosts(callback);
    }


    public List<Post> getMyReviews(List<Post> all, String email){

        List<Post> mine=new LinkedList<>();
        for(Post post: all)
        {
            if(post.getEmail().equals(email))
            {
                mine.add(post);
            }
        }
        return mine;
    }

    public void uploadImage(String name, Bitmap bitmap, Listener<String> listener) {
        firebaseModel.uploadImage(name,bitmap,listener);
    }


//
////*******************************User************************************
//    public void addUser(User u){allUsers.add(u);}
//    public List<User> getAllUsers(){
//        return allUsers;
//    }
//    public User getUserByEmail(String email){
//        for(User user:allUsers){
//            if (email.equals(user.getEmail()))
//                return user;
//        }
//        return null;
//    }
//
//    public void deleteUserByEmail(String email){
//        for(int i=0;i<allUsers.size();i++){
//            if (email.equals(allUsers.get(i).getEmail()))
//                allUsers.remove(i);
//        }
//    }
//    public void printUser(String email){
//        Log.d("TAG","user name : "+ getUserByEmail(email).getFullName().toString() +" user email : "+ getUserByEmail(email).getEmail().toString()+" user password : "+ getUserByEmail(email).getPassword().toString());
//    }
//
//
////*******************************post************************************
//    public void addPost(Post p){allPosts.add(p);}
//    public List<Post> getAllPosts(){
//        return allPosts;
//    }
//    public Post getPostById(int id){
//        for(Post p:allPosts){
//            if (id==p.getPostId())
//                return p;
//        }
//        return null;
//    }
//    public List<Post> getMyPosts(String email){
//        List<Post> myPosts=new LinkedList<>();
//        for(Post p:allPosts){
//            if (email.equals(p.getEmail()))
//                myPosts.add(p);
//        }
//        return myPosts;
//    }
//    public void deletePostById(int id){
//        for(int i=0;i<allPosts.size();i++){
//            if (id==allPosts.get(i).getPostId())
//                allPosts.remove(i);
//        }
//    }
//    public void printPost(int id){
//        Log.d("TAG","post id : "+ getPostById(id).getPostId() +"post city : "+ getPostById(id).getCity().toString()+"post description : "+ getPostById(id).getDescription().toString()+"post phone : "+ getPostById(id).getEmail().toString());
//    }

}
