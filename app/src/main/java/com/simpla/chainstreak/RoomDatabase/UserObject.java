package com.simpla.chainstreak.RoomDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "user")
public class UserObject implements Serializable {

    @PrimaryKey
    @NonNull
    private String userID;

    @ColumnInfo(name = "lastDay")
    private int lastDay;

    @ColumnInfo(name = "counter")
    private int counter;

    @ColumnInfo(name = "best")
    private int best;

    @ColumnInfo(name = "startDay")
    private int startDay;

    @ColumnInfo(name = "rate")
    private int rate;

    @ColumnInfo(name = "totalNumber")
    private int totalNumber;

    @ColumnInfo(name = "monday")
    private int monday;

    @ColumnInfo(name = "tuesday")
    private int tuesday;

    @ColumnInfo(name = "wednesday")
    private int wednesday;

    @ColumnInfo(name = "thursday")
    private int thursday;

    @ColumnInfo(name = "friday")
    private int friday;

    @ColumnInfo(name = "saturday")
    private int saturday;

    public UserObject(@NonNull String userID, int lastDay, int counter, int best, int startDay
            , int rate, int totalNumber, int monday, int tuesday, int wednesday, int thursday
            , int friday, int saturday) {
        this.userID = userID;
        this.lastDay = lastDay;
        this.counter = counter;
        this.best = best;
        this.startDay = startDay;
        this.rate = rate;
        this.totalNumber = totalNumber;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
    }

    @NonNull
    public String getUserID() {
        return userID;
    }

    public void setUserID(@NonNull String userID) {
        this.userID = userID;
    }

    public int getLastDay() {
        return lastDay;
    }

    public void setLastDay(int lastDay) {
        this.lastDay = lastDay;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getBest() {
        return best;
    }

    public void setBest(int best) {
        this.best = best;
    }

    public int getStartDay() {
        return startDay;
    }

    public void setStartDay(int startDay) {
        this.startDay = startDay;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public int getMonday() {
        return monday;
    }

    public void setMonday(int monday) {
        this.monday = monday;
    }

    public int getTuesday() {
        return tuesday;
    }

    public void setTuesday(int tuesday) {
        this.tuesday = tuesday;
    }

    public int getWednesday() {
        return wednesday;
    }

    public void setWednesday(int wednesday) {
        this.wednesday = wednesday;
    }

    public int getThursday() {
        return thursday;
    }

    public void setThursday(int thursday) {
        this.thursday = thursday;
    }

    public int getFriday() {
        return friday;
    }

    public void setFriday(int friday) {
        this.friday = friday;
    }

    public int getSaturday() {
        return saturday;
    }

    public void setSaturday(int saturday) {
        this.saturday = saturday;
    }
}

