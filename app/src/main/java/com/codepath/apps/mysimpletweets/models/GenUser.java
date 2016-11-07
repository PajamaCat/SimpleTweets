package com.codepath.apps.mysimpletweets.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Automatically generated Parcelable implementation for GenUser.
 *    DO NOT MODIFY THIS FILE MANUALLY! IT WILL BE OVERWRITTEN THE NEXT TIME
 *    GenUser's PARCELABLE DESCRIPTION IS CHANGED.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class GenUser implements Parcelable {

    @JsonProperty("name") protected String mName;
    @JsonProperty("screen_name") protected String mScreen_name;
    @JsonProperty("profile_image_url") protected String mProfile_image_url;
    @JsonProperty("description") protected String mDescription;
    @JsonProperty("followers_count") protected int mFollowers_count;
    @JsonProperty("friends_count") protected int mFriends_count;
    @JsonProperty("id") protected long mId;

    protected GenUser(
            String name,
            String screen_name,
            String profile_image_url,
            String description,
            int followers_count,
            int friends_count,
            long id) {
        this();
        mName = name;
        mScreen_name = screen_name;
        mProfile_image_url = profile_image_url;
        mDescription = description;
        mFollowers_count = followers_count;
        mFriends_count = friends_count;
        mId = id;
    }

    protected GenUser() {
    }

    public String getName() { return mName; }

    @JsonProperty("name")
    public void setName(String value) { mName = value; }

    public String getScreen_name() { return mScreen_name; }

    @JsonProperty("screen_name")
    public void setScreen_name(String value) { mScreen_name = value; }

    public String getProfile_image_url() { return mProfile_image_url; }

    @JsonProperty("profile_image_url")
    public void setProfile_image_url(String value) { mProfile_image_url = value; }

    public String getDescription() { return mDescription; }

    @JsonProperty("description")
    public void setDescription(String value) { mDescription = value; }

    public int getFollowers_count() { return mFollowers_count; }

    @JsonProperty("followers_count")
    public void setFollowers_count(int value) { mFollowers_count = value; }

    public int getFriends_count() { return mFriends_count; }

    @JsonProperty("friends_count")
    public void setFriends_count(int value) { mFriends_count = value; }

    public long getId() { return mId; }

    @JsonProperty("id")
    public void setId(long value) { mId = value; }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mName);
        parcel.writeString(mScreen_name);
        parcel.writeString(mProfile_image_url);
        parcel.writeString(mDescription);
        parcel.writeInt(mFollowers_count);
        parcel.writeInt(mFriends_count);
        parcel.writeLong(mId);
    }

    public void readFromParcel(Parcel source) {
        mName = source.readString();
        mScreen_name = source.readString();
        mProfile_image_url = source.readString();
        mDescription = source.readString();
        mFollowers_count = source.readInt();
        mFriends_count = source.readInt();
        mId = source.readLong();
    }

}
