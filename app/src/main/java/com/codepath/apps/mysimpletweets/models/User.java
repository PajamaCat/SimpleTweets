package com.codepath.apps.mysimpletweets.models;

import android.os.Parcel;
import android.os.Parcelable;


public class User extends GenUser {

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

        public User[] newArray(int size) {
            return new User[size];
        }

        public User createFromParcel(Parcel source) {
            User object = new User();
            object.readFromParcel(source);
            return object;
        }
    };

}
