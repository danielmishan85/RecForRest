package com.example.recforrest.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PostDao {

    @Query("select * from Post")
    LiveData<List<Post>> getAllPosts();

    @Query("select * from Post where postId = :postId")
    Post getPostById(String postId);

    //onConflict= if the review is already exist, it will update it.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Post... posts);


}