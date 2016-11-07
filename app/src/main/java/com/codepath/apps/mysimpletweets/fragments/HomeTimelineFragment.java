package com.codepath.apps.mysimpletweets.fragments;

import com.activeandroid.query.Select;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.fasterxml.jackson.core.type.TypeReference;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class HomeTimelineFragment extends TweetsListFragment {

    public static HomeTimelineFragment newInstance() {
        return new HomeTimelineFragment();
    }

    @Override
    public void populateTimeLine(boolean pullLatest) {
        client.getHomeTimeline(sinceId, maxId, pullLatest, new BaseJsonHttpResponseHandler<List<Tweet>>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, List<Tweet> response) {

                for (Tweet tweet : response) {
                    tweet.setOrigin(HomeTimelineFragment.class.getSimpleName());
                    tweet.getUser().save();
                    tweet.save();
                }

                if (pullLatest) {
                    insert(response);
                } else {
                    addAll(response);
                }
                swipeContainer.setRefreshing(false);
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
                .where("origin=?", HomeTimelineFragment.class.getSimpleName())
                .orderBy("tweet_id DESC")
                .execute();

        addAll(result);
    }
}
