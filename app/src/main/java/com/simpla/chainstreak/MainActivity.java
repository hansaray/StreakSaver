package com.simpla.chainstreak;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.simpla.chainstreak.RoomDatabase.AppExecutors;
import com.simpla.chainstreak.RoomDatabase.DatabaseClient;
import com.simpla.chainstreak.RoomDatabase.UserObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private ImageView m,t,w,th,f,s,sn;
    private CardView card;
    private TextView info,total_s,total_d,rate,best_s;
    private Button resData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findIds();
    }

    private void findIds() {
        m = findViewById(R.id.s_monday);
        t = findViewById(R.id.s_tuesday);
        w = findViewById(R.id.s_wednesday);
        th = findViewById(R.id.s_thursday);
        f = findViewById(R.id.s_friday);
        s = findViewById(R.id.s_saturday);
        sn = findViewById(R.id.s_sunday);
        card = findViewById(R.id.s_card);
        info = findViewById(R.id.t_info);
        total_s = findViewById(R.id.total_s);
        total_d = findViewById(R.id.total_d);
        rate = findViewById(R.id.rate);
        best_s = findViewById(R.id.best_s);
        resData = findViewById(R.id.restoreData);
        setData();
        setListeners();
    }

    private void setListeners() {
        card.setOnLongClickListener(view -> {
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
            View promptView = layoutInflater.inflate(R.layout.alert_layout, null);
            final AlertDialog alertD = new AlertDialog.Builder(MainActivity.this).create();
            Button completed = promptView.findViewById(R.id.btn_completed);
            Button close = promptView.findViewById(R.id.btn_close);
            completed.setOnClickListener(v -> {
                SharedPreferences sharedPreferences = getSharedPreferences("chainStreak", Context.MODE_PRIVATE);
                int lastDay = sharedPreferences.getInt("lastDay", 0); //If we don't have a saved value, use 0.
                Calendar c = Calendar.getInstance(TimeZone.getDefault());
                int thisDay = c.get(Calendar.DAY_OF_YEAR); // GET THE CURRENT DAY OF THE YEAR
                if (lastDay != thisDay) {
                    int dayWeek = c.get(Calendar.DAY_OF_WEEK);
                    int best = sharedPreferences.getInt("best", 0);
                    int counterOfConsecutiveDays = sharedPreferences.getInt("counter", 0); //If we don't have a saved value, use 0.
                    if (lastDay == thisDay - 1) {
                        counterOfConsecutiveDays = counterOfConsecutiveDays + 1;
                        sharedPreferences.edit().putInt("lastDay", thisDay).apply();
                        sharedPreferences.edit().putInt("counter", counterOfConsecutiveDays).apply();
                    } else {
                        counterOfConsecutiveDays = 1;
                        sharedPreferences.edit().putInt("lastDay", thisDay).apply();
                        sharedPreferences.edit().putInt("counter", 1).apply();
                    }
                    int totalNumber = sharedPreferences.getInt("totalNumber", 0);
                    totalNumber = totalNumber + 1;
                    sharedPreferences.edit().putInt("totalNumber", totalNumber).apply();
                    switch (dayWeek) {
                        case 1:
                            sn.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_streak_full,getTheme()));
                            break;
                        case 2:
                            m.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_streak_full,getTheme()));
                            sharedPreferences.edit().putInt("m", thisDay).apply();
                            break;
                        case 3:
                            t.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_streak_full,getTheme()));
                            sharedPreferences.edit().putInt("t", thisDay).apply();
                            break;
                        case 4:
                            w.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_streak_full,getTheme()));
                            sharedPreferences.edit().putInt("w", thisDay).apply();
                            break;
                        case 5:
                            th.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_streak_full,getTheme()));
                            sharedPreferences.edit().putInt("th", thisDay).apply();
                            break;
                        case 6:
                            f.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_streak_full,getTheme()));
                            sharedPreferences.edit().putInt("f", thisDay).apply();
                            break;
                        case 7:
                            s.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_streak_full,getTheme()));
                            sharedPreferences.edit().putInt("s", thisDay).apply();
                            break;
                    }
                    info.setText(getResources().getString(R.string.info_f));
                    info.setTextColor(getResources().getColor(R.color.background));
                    total_s.setText(String.valueOf(counterOfConsecutiveDays));
                    if (best < counterOfConsecutiveDays) {
                        best = best + 1;
                        sharedPreferences.edit().putInt("best", counterOfConsecutiveDays).apply();
                        best_s.setText(String.valueOf(counterOfConsecutiveDays));
                    }
                    int startDay = sharedPreferences.getInt("startDay", 0);
                    if (best == 1 && counterOfConsecutiveDays == 1) {
                        //First Start
                        startDay = thisDay;
                        sharedPreferences.edit().putInt("startDay", thisDay).apply();
                    }
                    int t_day = (thisDay - startDay) + 1;
                    total_d.setText(String.valueOf(t_day));
                    int c_rate = (totalNumber / t_day) * 100;
                    String finalTxt = "%" + c_rate;
                    rate.setText(finalTxt);
                    sharedPreferences.edit().putInt("rate", c_rate).apply();
                    saveToDB();
                }
                alertD.cancel();
            });
            close.setOnClickListener(v -> alertD.cancel());
            alertD.setView(promptView);
            alertD.show();
            return false;
        });
        resData.setOnLongClickListener(view -> {
            restoreData();
            return false;
        });
    }

    private void saveToDB() {
        SharedPreferences sharedPreferences = getSharedPreferences("chainStreak", Context.MODE_PRIVATE);
        UserObject object = new UserObject("dummy123"
                ,sharedPreferences.getInt("lastDay", 0),sharedPreferences.getInt("counter", 0)
                ,sharedPreferences.getInt("best", 0),sharedPreferences.getInt("startDay", 0)
                ,sharedPreferences.getInt("rate", 0),sharedPreferences.getInt("totalNumber", 0)
                ,sharedPreferences.getInt("m", 0),sharedPreferences.getInt("t", 0)
                ,sharedPreferences.getInt("w", 0),sharedPreferences.getInt("th", 0)
                ,sharedPreferences.getInt("f", 0),sharedPreferences.getInt("s", 0));
        List<UserObject> list = new ArrayList<>();
        list.add(object);
        AppExecutors.getInstance().diskIO().execute(() -> DatabaseClient
                .getInstance(getApplicationContext()).getAppDatabase().roomDao().insertUser(list));
    }

    private void restoreData() {
        final SharedPreferences sharedPreferences = getSharedPreferences("chainStreak", Context.MODE_PRIVATE);
        DatabaseClient.getInstance(getApplicationContext())
                .getAppDatabase().roomDao().getUser().observe(this, userObjects -> {
                    if(!userObjects.isEmpty()){
                        for(UserObject o : userObjects){
                            sharedPreferences.edit().putInt("lastDay", o.getLastDay()).apply();
                            sharedPreferences.edit().putInt("counter", o.getCounter()).apply();
                            sharedPreferences.edit().putInt("best", o.getBest()).apply();
                            sharedPreferences.edit().putInt("startDay", o.getStartDay()).apply();
                            sharedPreferences.edit().putInt("rate", o.getRate()).apply();
                            sharedPreferences.edit().putInt("totalNumber", o.getTotalNumber()).apply();
                            sharedPreferences.edit().putInt("m", o.getMonday()).apply();
                            sharedPreferences.edit().putInt("t", o.getTuesday()).apply();
                            sharedPreferences.edit().putInt("w", o.getWednesday()).apply();
                            sharedPreferences.edit().putInt("th", o.getThursday()).apply();
                            sharedPreferences.edit().putInt("f", o.getFriday()).apply();
                            sharedPreferences.edit().putInt("s", o.getSaturday()).apply();
                            setData();
                        }
                    }else Toast.makeText(MainActivity.this,getResources().getString(R.string.noData),Toast.LENGTH_SHORT).show();
        });
    }

    @SuppressLint("SetTextI18n")
    private void setData(){
        SharedPreferences sharedPreferences = getSharedPreferences("chainStreak", Context.MODE_PRIVATE);
        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
        int this_day = localCalendar.get(Calendar.DAY_OF_YEAR);
        int dayWeek = localCalendar.get(Calendar.DAY_OF_WEEK);
        int lastDay = sharedPreferences.getInt("lastDay", 0);
        int counter = sharedPreferences.getInt("counter", 0);
        int best = sharedPreferences.getInt("best", 0);
        int start = sharedPreferences.getInt("startDay", 0);
        int spent;
        if (start != 0) {
            spent = (this_day - start) + 1;
        } else {
            spent = start;
        }
        int rate_sp = sharedPreferences.getInt("rate", 0);
        total_s.setText(String.valueOf(counter));
        total_d.setText(String.valueOf(spent));
        rate.setText("% "+rate_sp);
        best_s.setText(String.valueOf(best));
        if(lastDay == this_day) {
            info.setText(getResources().getString(R.string.info_f));
            info.setTextColor(getResources().getColor(R.color.background));
            streakFire(dayWeek,counter,true);
        } else {
            info.setText(getResources().getString(R.string.info_e));
            info.setTextColor(getResources().getColor(R.color.number_color));
            streakFire(dayWeek,counter,false);
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void streakFire(int num, int s_num,boolean check){
        //monday-2,tuesday-3,wednesday-4,thursday-5,friday-6,saturday-7,sunday-1
        if(num >= 2) {
            if((num-1) <= s_num){
                switch(num) {
                    case 2:
                        if(check){
                            m.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        }
                        break;
                    case 3:
                        if(check){
                            t.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        }
                        m.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        break;
                    case 4:
                        if(check){
                            w.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        }
                        m.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        t.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        break;
                    case 5:
                        if(check){
                            th.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        }
                        m.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        t.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        w.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        break;
                    case 6:
                        if(check){
                            f.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        }
                        m.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        t.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        w.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        th.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        break;
                    case 7:
                        if(check){
                            s.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        }
                        m.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        t.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        w.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        th.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        f.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        break;
                }
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences("chainStreak", Context.MODE_PRIVATE);
                Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
                int thisDay = localCalendar.get(Calendar.DAY_OF_YEAR);
                int mon = sharedPreferences.getInt("m", 0);
                int tue = sharedPreferences.getInt("t", 0);
                int wed = sharedPreferences.getInt("w", 0);
                int thu = sharedPreferences.getInt("th", 0);
                int fri = sharedPreferences.getInt("f", 0);
                switch(num) {
                    case 2:
                        if(check){
                            m.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        }
                        break;
                    case 3:
                        if(check){
                            t.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        }
                        if(thisDay-1 == mon) {
                            m.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        }
                        break;
                    case 4:
                        if(check){
                            w.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        }
                        if(thisDay-1 == tue) {
                            t.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        }else if(thisDay-2 == mon){
                            m.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        }
                        break;
                    case 5:
                        if(check){
                            th.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        }
                        if(thisDay-1 == wed) {
                            w.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        }else if(thisDay-2 == tue){
                            t.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        }else if(thisDay-3 == mon){
                            m.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        }
                        break;
                    case 6:
                        if(check){
                            f.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        }
                        if(thisDay-1 == thu) {
                            th.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        }else if(thisDay-2 == wed){
                            w.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        }else if(thisDay-3 == tue){
                            t.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        }else if(thisDay-4 == mon){
                            m.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        }
                        break;
                    case 7:
                        if(check){
                            s.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        }
                        if(thisDay-1 == fri) {
                            f.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        }else if(thisDay-2 == thu){
                            th.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        }else if(thisDay-3 == wed){
                            w.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        }else if(thisDay-4 == tue){
                            t.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        }else if(thisDay-5 == mon){
                            m.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                        }
                        break;
                }
            }
        }else {
            if(num+6 <= s_num){
                if(check){
                    sn.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                }
                m.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                t.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                w.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                th.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                f.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                s.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
            }else {
                SharedPreferences sharedPreferences = getSharedPreferences("chainStreak", Context.MODE_PRIVATE);
                Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
                int thisDay = localCalendar.get(Calendar.DAY_OF_YEAR);
                int mon = sharedPreferences.getInt("m", 0);
                int tue = sharedPreferences.getInt("t", 0);
                int wed = sharedPreferences.getInt("w", 0);
                int thu = sharedPreferences.getInt("th", 0);
                int fri = sharedPreferences.getInt("f", 0);
                int sat = sharedPreferences.getInt("s", 0);
                if(check){
                    sn.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                }
                if(thisDay-1 == sat) {
                    s.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                }else if(thisDay-2 == fri){
                    f.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                }else if(thisDay-3 == thu){
                    th.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                }else if(thisDay-4 == wed){
                    w.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                }else if(thisDay-5 == tue){
                    t.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                }else if(thisDay-6 == mon){
                    m.setImageDrawable(getResources().getDrawable(R.drawable.ic_streak_full));
                }
            }
        }
    }
}