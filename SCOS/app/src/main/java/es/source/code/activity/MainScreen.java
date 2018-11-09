package es.source.code.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.support.v7.app.*;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import es.source.code.model.User;

public class MainScreen extends AppCompatActivity
        implements AdapterView.OnItemClickListener{
    private Button button_log, button_look, button_help, button_order;
    private GridView gridView;
    private User user;
    private SharedPreferences myPreference;
    Integer[] icon = {
            R.drawable.log,
            R.drawable.help,
            R.drawable.order,
            R.drawable.look,
    };
    private String[] iconName = {"登录","帮助","点餐","订单",};
    //private ArrayList<Map<String,Object>> dataList;
    private SimpleAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_layout);
        Intent intent = getIntent();
        String data = intent.getStringExtra("From Entry");
        String data1 = intent.getStringExtra("From Login");
        user = intent.getParcelableExtra("loginUser");
        //int need = icon.length;

        //判断是否成功登陆逻辑
        myPreference = getSharedPreferences("myPreference", Context.MODE_PRIVATE);
        int state = myPreference.getInt("state", 0);
        System.out.println(state);

        /*if( data1 != null){
            if(data1.equals("LoginSuccess")){
                need = 2;
            }else if(data1.equals("return")){
                need = icon.length;
            }else if(data1.equals("RegisterSuccess")){
                need = 2;
                Toast.makeText(this, "欢迎您成为SCOS新用户", Toast.LENGTH_SHORT).show();
            }
        }*/
        gridView = (GridView) findViewById(R.id.gv_function);
        /*1.准备数据源； 2.新建适配器；
        3.GridView加载适配器；4.GridView配置事件监听器
        */
        ArrayList<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();

        if(state == 0 ){
            for(int i = 0; i < 2; i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("image", icon[i]);
                map.put("text", iconName[i]);
                dataList.add(map);
            }
        }else if(state == 1){
            for (int i = 0; i < icon.length; i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("image", icon[i]);
                map.put("text", iconName[i]);
                dataList.add(map);
            }
        }

        // 使用simpleAdapter封装数据，将图片显示出来
        // 参数一是当前上下文Context对象
        // 参数二是图片数据列表，要显示数据都在其中
        // 参数三是界面的XML文件，注意，不是整体界面，而是要显示在GridView中的单个Item的界面XML
        // 参数四是动态数组中与map中图片对应的项，也就是map中存储进去的相对应于图片value的key
        // 参数五是单个Item界面XML中的图片ID
        adapter=new SimpleAdapter(this, dataList, R.layout.items,
                new String[]{"image","text"},
                new int[]{R.id.itemImage, R.id.itemText});
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(position){
            case 2:
                Intent trUser = new Intent(MainScreen.this, FoodView.class);
                trUser.putExtra("FromMain", user);
                startActivity(trUser);
                break;
            case 3:
                Intent trUser2 = new Intent(MainScreen.this, FoodOrderView.class);
                trUser2.putExtra("FromMain", user);
                startActivity(trUser2);
                break;
            case 0:
                Intent intent = new Intent(MainScreen.this, LoginOrRegister.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent1 = new Intent(MainScreen.this, SCOSHelper.class);
                startActivity(intent1);
                break;
            default:
        }
    }

}

