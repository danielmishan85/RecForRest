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

    public Post getPostById(List<Post> allPosts,int id){
        for(Post p:allPosts){
            if (id==p.getPostId())
                return p;
        }
        return null;
    }

//POSTS

    public LiveData<List<Post>> getAllPosts() {
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
        firebaseModel.getAllPostsSince(localLastUpdate,list->{
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

    public List<Post> getMyPosts(List<Post> all, String email){

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
}
