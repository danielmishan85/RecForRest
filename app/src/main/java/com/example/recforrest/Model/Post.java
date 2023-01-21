package com.example.recforrest.Model;

public class Post {

    private int id;
    private String restaurantName;
    private String city;
    private String description;
    private String phone;

    public Post(int id,String restaurantName, String city, String description, String phone) {
        this.id=id;
        this.restaurantName = restaurantName;
        this.city = city;
        this.description = description;
        this.phone = phone;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
