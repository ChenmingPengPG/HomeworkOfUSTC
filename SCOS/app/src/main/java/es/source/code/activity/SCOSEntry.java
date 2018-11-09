package es.source.code.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.view.GestureDetector;

import com.google.gson.Gson;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import es.source.code.model.User;

public class SCOSEntry extends AppCompatActivity{
    private GestureDetector gestureDetector;
    private SharedPreferences myPreferences;
    final int RIGHT = 0;
    final int LEFT = 1;
    private User user, user1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.entry);
        //使用Gson将user对象转换为string数据写入sharedpreference
        /*user = new User();
        user1 = new User("abc","123",true);
        myPreferences = getSharedPreferences("myPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myPreferences.edit();
        Gson gson = new Gson();
        String jsonStr = gson.toJson(user);
        String jsonStr1 = gson.toJson(user1);
        Set<String> userSet = new HashSet<String>();
        userSet.add(jsonStr);
        userSet.add(jsonStr1);
        editor.putStringSet("userIfo", userSet);
        editor.apply();*/

        /*Intent intent = new Intent(SCOSEntry.this, MainScreen.class);
        startActivity(intent);*/
        gestureDetector = new GestureDetector(SCOSEntry.this, onGestureListener);
    }

    private GestureDetector.OnGestureListener onGestureListener =
        new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                   float velocityY) {
                float x = e2.getX() - e1.getX();
                float y = e2.getY() - e1.getY();
                if (x > 0) {
                    doResult(RIGHT);
                } else if (x < 0) {
                    doResult(LEFT);
                }
                return true;
            }
        };

    public boolean onTouchEvent(MotionEvent event){
        return gestureDetector.onTouchEvent(event);
    }

    public void doResult(int action){
        switch(action){
            case LEFT:
                System.out.println("go right");
                Intent intent = new Intent(SCOSEntry.this, MainScreen.class);
                String data = "From Entry";
                intent.putExtra("From Entry", data);
                startActivity(intent);
                break;
            case RIGHT:
                System.out.println("go left");
                break;
        }
    }
}



