package com.codepath.apps.mysimpletweets.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Automatically generated Parcelable implementation for GenTweet.
 *    DO NOT MODIFY THIS FILE MANUALLY! IT WILL BE OVERWRITTEN THE NEXT TIME
 *    GenTweet's PARCELABLE DESCRIPTION IS CHANGED.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class GenTweet implements Parcelable {

    @JsonProperty("text") protected String mText;
    @JsonProperty("created_at") protected String mCreated_at;
    @JsonProperty("user") protected User mUser;
    @JsonProperty("id") protected long mId;

    protected GenTweet(
            String text,
            String created_at,
            User user,
            long id) {
        this();
        mText = text;
        mCreated_at = created_at;
        mUser = user;
        mId = id;
    }

    protected GenTweet() {
    }

    public String getText() { return mText; }

    @JsonProperty("text")
    public void setText(String value) { mText = value; }

    public String getCreated_at() { return mCreated_at; }

    @JsonProperty("created_at")
    public void setCreated_at(String value) { mCreated_at = value; }

    public User getUser() { return mUser; }

    @JsonProperty("user")
    public void setUser(User value) { mUser = value; }

    public long getId() { return mId; }

    @JsonProperty("id")
    public void setId(long value) { mId = value; }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mText);
        parcel.writeString(mCreated_at);
        parcel.writeParcelable(mUser, 0);
        parcel.writeLong(mId);
    }

    public void readFromParcel(Parcel source) {
        mText = source.readString();
        mCreated_at = source.readString();
        mUser = source.readParcelable(User.class.getClassLoader());
        mId = source.readLong();
    }

}
