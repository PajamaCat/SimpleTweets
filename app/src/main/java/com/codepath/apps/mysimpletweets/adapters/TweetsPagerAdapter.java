package com.codepath.apps.mysimpletweets.adapters;

import android.support.v4.app.FragmentManager;

import com.codepath.apps.mysimpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.TweetsListFragment;

public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter {

    public static final int PAGE_HOME_TIMELINE = 0;
    public static final int PAGE_MENTIONS_TIMELINE = 1;

    private final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Home", "Mentions"};

    public TweetsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    public TweetsListFragment getItem(int position) {
        if (position == PAGE_HOME_TIMELINE) {
            return HomeTimelineFragment.newInstance();
        } else {
            return MentionsTimelineFragment.newInstance();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
