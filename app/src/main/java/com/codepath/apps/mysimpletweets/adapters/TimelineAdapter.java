package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TimelineActivity;
import com.codepath.apps.mysimpletweets.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder> {

    private Context context;
    private List<Tweet> tweets;

    public TimelineAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    @Override
    public TimelineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View articleView = inflater.inflate(R.layout.item_tweet, parent, false);
        TimelineViewHolder viewHolder = new TimelineViewHolder(articleView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TimelineViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);

        holder.tvUserName.setText(tweet.getUser().getName());
        holder.tvTimeStamp.setText(getRelativeTimeAgo(tweet.getCreated_at()));
        holder.tvTweetBody.setText(tweet.getText());

        Glide.with(context)
                .load(tweet.getUser().getProfile_image_url())
                .bitmapTransform(new RoundedCornersTransformation(context, 5, 0))
                .into(holder.ivProfile);

        if (context instanceof TimelineActivity) {
            holder.ivProfile.setOnClickListener(
                    v -> ((TimelineActivity) context).onClickProfileImage(tweet.getUser()));
        }
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public class TimelineViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivProfile) ImageView ivProfile;
        @BindView(R.id.tvUserName) TextView tvUserName;
        @BindView(R.id.tvTimeStamp) TextView tvTimeStamp;
        @BindView(R.id.tvTweetBody) TextView tvTweetBody;

        public TimelineViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
