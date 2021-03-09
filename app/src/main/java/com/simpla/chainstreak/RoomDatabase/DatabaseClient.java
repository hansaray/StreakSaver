package com.simpla.chainstreak.RoomDatabase;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {

    private Context mContext;
    private static DatabaseClient mInstance;

    private AppDatabase appDatabase;

    private DatabaseClient(Context mContext){
        this.mContext = mContext;
        //Creating a room database named "StreakSaverRoom"
        appDatabase = Room.databaseBuilder(mContext,AppDatabase.class,"StreakSaverRoom").build();
    }

    public static synchronized DatabaseClient getInstance(Context mContext){
        if(mInstance==null){
            mInstance = new DatabaseClient(mContext);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase(){
        return appDatabase;
    }
}

