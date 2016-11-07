package com.codepath.apps.mysimpletweets.models;

import android.os.Parcel;
import android.os.Parcelable;


public class Tweet extends GenTweet {

    public static final Parcelable.Creator<Tweet> CREATOR = new Parcelable.Creator<Tweet>() {

        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }

        public Tweet createFromParcel(Parcel source) {
            Tweet object = new Tweet();
            object.readFromParcel(source);
            return object;
        }
    };
}
