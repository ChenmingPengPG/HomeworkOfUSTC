package es.source.code.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class User implements Parcelable { //为了实现传递对象
    private String userName;
    private String password;
    private Boolean isoldUser;
    private List<Food> ordered;


    public User(String userName, String password, boolean isoldUser) {
        this.userName = userName;
        this.password = password;
        this.isoldUser = isoldUser;
    }
    public User() {
        this.userName = "new";
        this.password="";
        this.isoldUser= false;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isOldUser() {
        return isoldUser;
    }

    public void setOldUser(boolean oldUser) {
        this.isoldUser = oldUser;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userName);
        dest.writeString(this.password);
        dest.writeByte(this.isoldUser ? (byte) 1 : (byte) 0);
    }

    protected User(Parcel in) {
        this.userName = in.readString();
        this.password = in.readString();
        this.isoldUser = in.readByte() != 0;
    }

    public static final Parcelable.Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {

            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

}
