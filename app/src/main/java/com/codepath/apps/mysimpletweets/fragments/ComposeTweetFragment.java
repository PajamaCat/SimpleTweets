package com.codepath.apps.mysimpletweets.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import icepick.Icepick;
import icepick.State;

public class ComposeTweetFragment extends DialogFragment {

    private static final String ARG_USER_INFO = "user_info";
    private static final int MAX_CHAR_COUNT = 140;

    private User userInfo;
    private TwitterClient client;
    private ObjectMapper mapper;

    private Listener listener;

    public ComposeTweetFragment() {
        // Required empty public constructor
    }

    public static ComposeTweetFragment newInstance(User user) {
        ComposeTweetFragment fragment = new ComposeTweetFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER_INFO, user);
        fragment.setArguments(args);
        return fragment;
    }

    @State int curCount;

    @BindView(R.id.etCompose) EditText tweetComposer;
    @BindView(R.id.tvCharCount) TextView charCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);

        if (getArguments() != null) {
            userInfo = getArguments().getParcelable(ARG_USER_INFO);
        }
        client = TwitterApplication.getRestClient();
        mapper = new ObjectMapper();

        if (savedInstanceState == null) {
            curCount = MAX_CHAR_COUNT;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compose_tweet, container, false);
        ButterKnife.bind(this, view);

        charCount.setText(String.valueOf(MAX_CHAR_COUNT));
        tweetComposer.setFilters(new InputFilter[] {new InputFilter.LengthFilter(MAX_CHAR_COUNT)});
        tweetComposer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                curCount = s.toString().length();
                charCount.setText(String.valueOf(MAX_CHAR_COUNT - curCount));
            }
        });
        return view;
    }

    @OnClick(R.id.btnClose)
    public void onClickCloseButton() {
        dismiss();
    }

    @OnClick(R.id.btnComposeTweet)
    public void onClickComposeButton() {
        String tweet = tweetComposer.getText().toString();
        client.publishTweet(tweet, new BaseJsonHttpResponseHandler<Tweet>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Tweet response) {
                dismiss();
                listener.onFinishedPublishTweet(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Tweet errorResponse) {
                Log.d("failure", throwable.getMessage());
            }

            @Override
            protected Tweet parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return mapper.readValue(rawJsonData, Tweet.class);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            listener = (Listener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }

    public interface Listener {
        void onFinishedPublishTweet(Tweet tweet);
    }
}
