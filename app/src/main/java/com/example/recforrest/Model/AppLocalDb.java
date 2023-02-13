package com.example.recforrest.Model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.recforrest.MyApplication;


@Database(entities = {Post.class}, version = 6)
//return the Daos
    abstract class AppLocalDbRepository extends RoomDatabase {
        public abstract PostDao postDao();
    }

    //just for one static function, none objects of this class
    public class AppLocalDb{
        //fallbackToDestructiveMigration = to handle updates,removes old versions related to the current version number
        //build = create DB object
        public static AppLocalDbRepository getAppDb() {
            return Room.databaseBuilder(MyApplication.getMyContext(),
                            AppLocalDbRepository.class,
                            "dbRecForRest.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        private AppLocalDb(){}
}
