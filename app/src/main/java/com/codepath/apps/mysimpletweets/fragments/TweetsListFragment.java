package com.codepath.apps.mysimpletweets.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.mysimpletweets.EndlessRecyclerViewScrollListener;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.adapters.TimelineAdapter;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

public class TweetsListFragment extends Fragment {

    public interface Listener {
        void onClickComposeTweet();
    }

    protected ObjectMapper mapper;
    protected TwitterClient client;

    private TimelineAdapter adapter;
    private Listener listener;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.compose) FloatingActionButton composeButton;

    @State ArrayList<Tweet> tweets = Lists.newArrayList();
    @State long sinceId = 1;
    @State long maxId = Long.MAX_VALUE - 1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(this.getClass().getSimpleName(), "onAttach");

        try {
            this.listener = (Listener) context;
        } catch (ClassCastException e) {
            Log.e("[ERROR CASTING]", context.toString() + " doesn't implement listener interface!");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(this.getClass().getSimpleName(), "onDetach");

        listener = null;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        ButterKnife.bind(this, view);

        swipeContainer.setOnRefreshListener(() -> populateTimeLine(true));

        adapter = new TimelineAdapter(getActivity(), tweets);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                populateTimeLine(false);
            }
        });

        composeButton.setOnClickListener(v -> listener.onClickComposeTweet());

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);

        client = TwitterApplication.getRestClient();
        mapper = new ObjectMapper();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (isNetworkAvailable()) {
            populateTimeLine(false);
        } else {
            populateTimeLineWithDB();
        }
    }

    public void addAll(List<Tweet> tweets) {
        if (!tweets.isEmpty()) {
            maxId = Math.min(maxId, tweets.get(tweets.size() - 1).getTweetId() - 1);
            sinceId = Math.max(sinceId, tweets.get(0).getTweetId());
        } else {
            return;
        }

        this.tweets.addAll(tweets);
        adapter.notifyDataSetChanged();
    }

    public void insert(List<Tweet> tweets) {
        if (!tweets.isEmpty()) {
            maxId = Math.min(maxId, tweets.get(tweets.size() - 1).getTweetId());
            sinceId = Math.max(sinceId, tweets.get(0).getTweetId());
        } else {
            return;
        }

        ArrayList<Tweet> newTweets = Lists.newArrayList(tweets);
        newTweets.addAll(this.tweets);
        this.tweets = newTweets;

        adapter.notifyDataSetChanged();
    }

    public void insert(Tweet tweet) {
        if (tweet == null)
            return;

        maxId = Math.min(maxId, tweet.getTweetId());
        sinceId = Math.max(sinceId, tweet.getTweetId());

        this.tweets.add(0, tweet);
        adapter.notifyDataSetChanged();
    }

    protected void populateTimeLine(boolean pullLatest) {}

    protected void populateTimeLineWithDB() {}

    public Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
