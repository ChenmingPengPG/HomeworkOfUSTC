package es.source.code.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Food implements Parcelable {

    private String foodName; //菜名
    private int price; //
    private int num;// 点的数量
    private int store; //库存
    private int imgId; //图片id
    private boolean ordered; //是否点过

    public Food(){
    }
    public Food(String foodName, int price, int store, int imgId, boolean oredered){
        this.foodName = foodName;
        this.price = price;
        this.store = store;
        this.imgId = imgId;
        this.ordered = oredered;
    }
    public String getFoodName(){return foodName;}
    public int getPrice(){return price;}
    public int getStore(){return store;}
    public int getImgId(){return imgId;}
    public boolean getOredered(){return ordered;}

    @Override
    public int describeContents(){return 0;}

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(foodName);
        dest.writeInt(price);
        dest.writeInt(store);
        dest.writeByte(ordered ? (byte)1 : (byte)0);
        dest.writeInt(imgId);
    }

    protected Food(Parcel in){
        this.foodName = in. readString();
        this.price = in.readInt();
        this.store = in.readInt();
        this.ordered = in.readByte() != 0;
        this.imgId = in.readInt();
    }


    public static final Parcelable.Creator<Food> CREATOR =  new Parcelable.Creator<Food>(){
        @Override
        public Food createFromParcel(Parcel source){
            return new Food(source);
        }
        @Override
        public Food[] newArray(int size){
            return new Food[size];
        }
    };

}
