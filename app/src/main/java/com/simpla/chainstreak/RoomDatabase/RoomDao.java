package com.simpla.chainstreak.RoomDatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RoomDao {

    @Query("Select * from user")
    LiveData<List<UserObject>> getUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(Iterable<UserObject> userTable);

    @Update
    void updateUser(UserObject userTable);

    @Delete
    void deleteUser(UserObject userTable);

}

