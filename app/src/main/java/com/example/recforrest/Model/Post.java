package com.example.recforrest.Model;

public class Post {

    public static int numOfPosts;
    private int id;
    private String restaurantName;
    private String city;
    private String description;
    private String email;

    public Post(int id,String restaurantName, String city, String description, String email) {
        this.id=numOfPosts;
        this.restaurantName = restaurantName;
        this.city = city;
        this.description = description;
        this.email = email;
        numOfPosts++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
