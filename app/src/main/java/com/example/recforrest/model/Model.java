package com.example.recforrest.model;

import java.util.LinkedList;
import java.util.List;

public class Model {
    private static final Model _instance = new Model();

    public static Model instance(){
        return _instance;
    }
    private Model(){
        for (int i = 0; i < 20; i++){
            addRecommendation(new Recommendation("name " + i,"id " + i,"",""));
        }
    }

    List<Recommendation> recommendationsData = new LinkedList<>();
    List<User> usersData = new LinkedList<>();

    public List<Recommendation> getAllRecommendations(){
        return recommendationsData;
    }
    public void addRecommendation(Recommendation r){
        recommendationsData.add(r);
    }

    public List<User> getUsersData() {
        return usersData;
    }
    public void addUser(User u){
        usersData.add(u);
    }
}
