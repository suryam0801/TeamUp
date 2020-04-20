package com.example.teamup.ControlPanel.chat;

import android.os.Parcel;
import android.os.Parcelable;

public class UsersModel implements Parcelable {
    private String name;
    private String userId;

    public UsersModel(String name, String userId) {
        this.name = name;
        this.userId = userId;
    }

    protected UsersModel(Parcel in) {
        name = in.readString();
        userId = in.readString();
    }

    public static final Creator<UsersModel> CREATOR = new Creator<UsersModel>() {
        @Override
        public UsersModel createFromParcel(Parcel in) {
            return new UsersModel(in);
        }

        @Override
        public UsersModel[] newArray(int size) {
            return new UsersModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UsersModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(userId);
    }

    @Override
    public String toString() {
        return "UsersModel{" +
                "name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.userId != null ? this.userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object v) {
        boolean retVal = false;

        if (v instanceof UsersModel){
            UsersModel ptr = (UsersModel) v;
            retVal = ptr.userId.equals(this.userId);
        }

        return retVal;
    }
}