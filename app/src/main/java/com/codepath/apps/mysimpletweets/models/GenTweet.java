package com.codepath.apps.mysimpletweets.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Table;
import com.activeandroid.annotation.Column;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "Tweets")
public abstract class GenTweet extends Model implements Parcelable {

    @Column(name="text")
    @JsonProperty("text") protected String mText;

    @Column(name="created_at")
    @JsonProperty("created_at") protected String mCreated_at;

    @Column(name = "user", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    @JsonProperty("user") protected User mUser;

    @Column(name = "tweet_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    @JsonProperty("id") protected long mTweetId;

    @Column(name="origin")
    protected String mOrigin;

    protected GenTweet(
            String text,
            String created_at,
            User user,
            long id) {
        this();
        mText = text;
        mCreated_at = created_at;
        mUser = user;
        mTweetId = id;
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

    public long getTweetId() { return mTweetId; }

    @JsonProperty("id")
    public void setTweetId(long value) { mTweetId = value; }

    public String getOrigin() { return mOrigin; }

    public void setOrigin(String origin) { mOrigin = origin; }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mText);
        parcel.writeString(mCreated_at);
        parcel.writeParcelable(mUser, 0);
        parcel.writeLong(mTweetId);
        parcel.writeString(mOrigin);
    }

    public void readFromParcel(Parcel source) {
        mText = source.readString();
        mCreated_at = source.readString();
        mUser = source.readParcelable(User.class.getClassLoader());
        mTweetId = source.readLong();
        mOrigin = source.readString();
    }

}
