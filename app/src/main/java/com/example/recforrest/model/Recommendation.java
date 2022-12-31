package com.example.recforrest.model;

import java.util.ArrayList;
import java.util.List;

public class Recommendation {

    String nameOfRes;
    String address;
    String imgUrl;
    String description;
    List<String> photosUrls;

    public Recommendation(String nameOfRes, String address, String imgUrl, String description) {
        this.nameOfRes = nameOfRes;
        this.address = address;
        this.imgUrl = imgUrl;
        this.description = description;
        this.photosUrls = new ArrayList<>();
    }

    public String getNameOfRes() {
        return nameOfRes;
    }

    public String getAddress() {
        return address;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
