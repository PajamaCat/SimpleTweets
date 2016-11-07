package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.activeandroid.query.Select;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.fasterxml.jackson.core.type.TypeReference;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class UserTimelineFragment extends TweetsListFragment {

    public static UserTimelineFragment newInstance(long userId) {
        Bundle args = new Bundle();
        args.putLong(ARG_USER_TIMELINE, userId);
        UserTimelineFragment fragment = new UserTimelineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private static final String ARG_USER_TIMELINE = "user_timeline";

    private long userId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getLong(ARG_USER_TIMELINE);
    }

    @Override
    public void populateTimeLine(boolean pullLatest) {
        client.getUserTimeline(sinceId, maxId, pullLatest, userId, new BaseJsonHttpResponseHandler<List<Tweet>>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, List<Tweet> response) {

                for (Tweet tweet : response) {
                    tweet.setOrigin(UserTimelineFragment.class.getSimpleName());
                }

                if (pullLatest) {
                    insert(response);
                } else {
                    addAll(response);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, List<Tweet> errorResponse) {
                if (swipeContainer.isRefreshing()) {
                    swipeContainer.setRefreshing(false);
                }
            }

            @Override
            protected List<Tweet> parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return mapper.readValue(rawJsonData, new TypeReference<List<Tweet>>(){});
            }
        });
    }

    @Override
    public void populateTimeLineWithDB() {
        List<Tweet> result = new Select()
                .all()
                .from(Tweet.class)
                .where("origin=?", UserTimelineFragment.class.getSimpleName())
                .orderBy("tweet_id DESC")
                .execute();

        addAll(result);
    }
}
