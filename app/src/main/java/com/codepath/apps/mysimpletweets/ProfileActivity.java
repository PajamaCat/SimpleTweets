package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.fragments.UserTimelineFragment;
import com.codepath.apps.mysimpletweets.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import icepick.State;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ProfileActivity extends AppCompatActivity {

    private static final String EXTRA_USER_ID = "user_id";

    public static Intent intentForSelf(Context context) {
        return new Intent(context, ProfileActivity.class);
    }

    public static Intent intentForUserId(Context context, long userId) {
        return new Intent(context, ProfileActivity.class).putExtra(EXTRA_USER_ID, userId);
    }

    @State User currentUser;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindDimen(R.dimen.navigation_padding) int navPadding;
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvTagLine) TextView tvTagLine;
    @BindView(R.id.tvFollowers) TextView tvFollowers;
    @BindView(R.id.tvFollowing) TextView tvFollowing;
    @BindView(R.id.ivProfileImageView) ImageView ivProfileImageView;

    private TwitterClient client;
    private ObjectMapper mapper;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Drawable upArrow = getResources().getDrawable(R.drawable.ic_keyboard_backspace_white_24px);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        client = TwitterApplication.getRestClient();
        mapper = new ObjectMapper();

        if (getIntent().hasExtra(EXTRA_USER_ID)) {
            userId = getIntent().getLongExtra(EXTRA_USER_ID, -1);
            getUserInfo(userId);
        } else {
            getUserInfo(null);
        }


    }

    private void getUserInfo(@Nullable Long userId) {
        AsyncHttpResponseHandler handler = new BaseJsonHttpResponseHandler<User>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, User response) {
                currentUser = response;
                populateProfileHeader();

                UserTimelineFragment fragment = UserTimelineFragment.newInstance(currentUser.getUserId());

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, fragment);
                ft.commit();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, User errorResponse) {
                Log.e("[NETWORK]", "Unable to receive user info.");
            }

            @Override
            protected User parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return mapper.readValue(rawJsonData, User.class);
            }
        };

        if (userId == null) {
            client.getSelfUserInfo(handler);
        } else {
            client.getUserInfo(userId, handler);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // This is the up button
            case android.R.id.home:
                finish();
                // overridePendingTransition(R.animator.anim_left, R.animator.anim_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void populateProfileHeader() {
        toolbar.setTitle("@" + currentUser.getScreen_name());
        toolbar.setTitleTextColor(Color.WHITE);

        tvName.setText(currentUser.getName());
        tvTagLine.setText(currentUser.getDescription());
        tvFollowers.setText(String.valueOf(currentUser.getFollowers_count()));
        tvFollowing.setText(String.valueOf(currentUser.getFriends_count()));
        Glide.with(this).load(currentUser.getProfile_image_url())
                .bitmapTransform(new RoundedCornersTransformation(this, 8, 0)).into(ivProfileImageView);
    }
}
