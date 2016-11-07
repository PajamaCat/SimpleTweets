package com.codepath.apps.mysimpletweets.fragments;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.fasterxml.jackson.core.type.TypeReference;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MentionsTimelineFragment extends TweetsListFragment {

    public static MentionsTimelineFragment newInstance() {
        return new MentionsTimelineFragment();
    }

    @Override
    public void populateTimeLine(boolean pullLatest) {
        client.getMentionsTimeline(sinceId, maxId, pullLatest, new BaseJsonHttpResponseHandler<List<Tweet>>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, List<Tweet> response) {
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
}
