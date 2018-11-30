package es.source.code.model;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import es.source.code.activity.R;

public class food_list{
    private String askfor;
    //菜名
    public static ArrayList<String> coldfood_name = new ArrayList<>();
    //private String[] coldfood_name = {"毛豆","黄瓜","木耳"};
    private String[] hotfood_name = {"酸辣土豆丝","西红柿炒鸡蛋", "鱼香肉丝"};
    private String[] seafood_name={"扇贝", "龙虾", "生蚝"};
    private String[] drink_name ={"可乐","雪碧","啤酒"};

    //图片
    private int[] ResourceID = {R.drawable.order_item_logo,
            R.drawable.order_item_logo, R.drawable.order_item_logo};

    //价格
    public static ArrayList<Integer> coldfood_price = new ArrayList<>();
    //private int[] coldfood_price = {12, 13, 14};
    private int[] hotfood_price = {12, 13, 14};
    private int[] seafood_price = {12, 13, 14};
    private int[] drink_price = {11,12,13};

    //库存
    public static ArrayList<Integer> coldfood_store = new ArrayList<>();
    //private int[] coldfood_store = {5,5,5};
    private int[] hotfood_store = {5,5,5};
    private int[] seafood_store = {5,5,5};
    private int[] drink_store = {5,5,5};
    static{
        coldfood_name.add("毛豆"); coldfood_price.add(12); coldfood_store.add(5);
        coldfood_name.add("黄瓜"); coldfood_price.add(13); coldfood_store.add(5);
        coldfood_name.add("木耳"); coldfood_price.add(14); coldfood_store.add(5);
    }
    private ArrayList<HashMap<String, Food>> cfl = new ArrayList<HashMap<String, Food>>();
    private ArrayList<HashMap<String, Food>> hfl = new ArrayList<HashMap<String, Food>>();
    private ArrayList<HashMap<String, Food>> sfl = new ArrayList<HashMap<String, Food>>();
    private ArrayList<HashMap<String, Food>> dfl = new ArrayList<HashMap<String, Food>>();
    public void add_food(String name, int price, int storage){
        coldfood_name.add(name);
        coldfood_price.add(price);
        coldfood_store.add(storage);
    }
    public food_list(String name){
        askfor = name;
        if(name.equals("coldfood")){
            for(int i = 0; i < coldfood_name.size(); i++){
                Food cold = new Food(coldfood_name.get(i), coldfood_price.get(i),
                        coldfood_store.get(i), R.drawable.order_item_logo, false);
                HashMap<String, Food> map = new HashMap<String, Food>();
                map.put("name", cold);
                cfl.add(map);
            }
        }else if(name.equals("hotfood")){
            for(int i = 0; i < hotfood_name.length; i++){
                Food hot = new Food(hotfood_name[i], hotfood_price[i],
                        hotfood_store[i], ResourceID[i], false);
                HashMap<String, Food> map = new HashMap<String, Food>();
                map.put("name", hot);
                hfl.add(map);
            }
        }else if(name.equals("seafood")){
            for(int i = 0; i < seafood_name.length; i++){
                Food sea = new Food(seafood_name[i], seafood_price[i],
                        seafood_store[i], ResourceID[i], false);
                HashMap<String, Food> map = new HashMap<String, Food>();
                map.put("name", sea);
                sfl.add(map);
            }
        }else if(name.equals("drink")) {
            for (int i = 0; i < drink_name.length; i++) {
                Food drink = new Food(drink_name[i], drink_price[i],
                        drink_store[i], ResourceID[i], false);
                HashMap<String, Food> map = new HashMap<String, Food>();
                map.put("name", drink);
                dfl.add(map);
            }
        }
    }



    public  List<HashMap<String, Food>> getData(){
        if(askfor.equals("coldfood")){
            return cfl;
        }else if(askfor.equals("hotfood")){
            return hfl;
        }else if(askfor.equals("seafood")){
            return sfl;
        }else if(askfor.equals("drink")){
            return dfl;
        }else return cfl;
    }
}
