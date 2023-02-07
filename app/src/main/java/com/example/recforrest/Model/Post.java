package com.example.recforrest.Model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.recforrest.MyApplication;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Entity
public class Post {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int postId;
    private String restaurantName="";
    private String city="";
    private String img="";
    private String description="";
    private String email="";
    public Long lastUpdated;

    public Post(String restaurantName, String city, String img, String description, String email) {
        this.restaurantName = restaurantName;
        this.city = city;
        this.img = img;
        this.description = description;
        this.email = email;
    }


    public Post(String postId, String restaurantName, String city, String description, String email, String img) {
        this.postId=Integer.parseInt(postId);
        this.restaurantName = restaurantName;
        this.city = city;
        this.description = description;
        this.email = email;
        this.img=img;
    }

    public void generateID(){
        Random rand = new Random();
        this.postId = rand.nextInt((200000 - 100000) + 1) + 100000;
    }

    static final String POSTID = "postid";
    static final String RESTAURANTNAME = "restaurantName";
    static final String DESCRIPTION = "description";
    static final String CITY = "city";
    static final String EMAIL = "email";
    static final String IMG = "img";
    static final String COLLECTION = "posts";
    static final String LAST_UPDATED = "lastUpdated";
    static final String LOCAL_LAST_UPDATED = "posts_local_last_update";


    public static Post fromJson(Map<String,Object> json){
        String postid=String.valueOf(json.get(POSTID));
        String email = (String)json.get(EMAIL);
        String city = (String)json.get(CITY);
        String restaurantName = (String)json.get(RESTAURANTNAME);
        String img = (String)json.get(IMG);
        String description = (String)json.get(DESCRIPTION);
        Post post = new Post(postid,restaurantName,city,description,email,img);
        try{
            Timestamp time = (Timestamp) json.get(LAST_UPDATED);
            post.setLastUpdated(time.getSeconds());
        }catch(Exception e){

        }
        return post;
    }

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put(POSTID, getPostId());
        json.put(EMAIL, getEmail());
        json.put(DESCRIPTION, getDescription());
        json.put(CITY, getCity());
        json.put(RESTAURANTNAME, getRestaurantName());
        json.put(IMG, getImg());
        json.put(LAST_UPDATED, FieldValue.serverTimestamp());

        return json;
    }

    public static Long getLocalLastUpdate() {
        SharedPreferences sharedPref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        return sharedPref.getLong(LOCAL_LAST_UPDATED, 0);
    }

    public static void setLocalLastUpdate(Long time) {
        SharedPreferences sharedPref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(LOCAL_LAST_UPDATED,time);
        editor.commit();
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
