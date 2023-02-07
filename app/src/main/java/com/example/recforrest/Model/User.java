package com.example.recforrest.Model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import java.util.HashMap;
import java.util.Map;

public class User {

    private String fullName="";
    private String email="";
    private String img="";
    public Long lastUpdated;

    public User(){}
    public User(String fullName, String email,String img) {
        this.fullName = fullName;
        this.email = email;
        this.img=img;
    }

    static final String FULLNAME = "fullName";
    static final String EMAIL = "email";
    static final String IMG = "img";
    static final String COLLECTION = "users";
    static final String LAST_UPDATED = "lastUpdated";
    static final String LOCAL_LAST_UPDATED = "users_local_last_update";


    public static User fromJson(Map<String,Object> json){
        String email = (String)json.get(EMAIL);
        String fullName = (String)json.get(FULLNAME);
        String img = (String)json.get(IMG);
        User u = new User(fullName,email,img);
        try{
            Timestamp time = (Timestamp) json.get(LAST_UPDATED);
            u.setLastUpdated(time.getSeconds());
        }catch(Exception e){}
        return u;
    }

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put(FULLNAME, getFullName());
        json.put(EMAIL, getEmail());
        json.put(IMG, getImg());
        json.put(LAST_UPDATED, FieldValue.serverTimestamp());
        return json;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
