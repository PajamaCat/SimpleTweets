package com.codepath.apps.mysimpletweets;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1";
	public static final String REST_CONSUMER_KEY = "jhsAiLQnajjpEKhoVBxrWsA8n";
	public static final String REST_CONSUMER_SECRET = "tfV3EDHEg9Enop5ivDPLvpGJ2A5ye0x4io955qfEYbUVyb3yxi";
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; //

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// GET statuses/home_timeline.json
	public void getHomeTimeline(long sinceId, long maxId, boolean pullLatest, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		if (pullLatest) {
			params.put("since_id", sinceId);
		} else {
			params.put("max_id", maxId);
		}

		client.get(apiUrl, params, handler);

	}

	// GET statuses/mentions_timeline.json
	public void getMentionsTimeline(long sinceId, long maxId, boolean pullLatest, AsyncHttpResponseHandler handler) {

		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		if (pullLatest) {
			params.put("since_id", sinceId);
		} else {
			params.put("max_id", maxId);
		}

		client.get(apiUrl, params, handler);

	}

	// GET statuses/user_timeline.json
	public void getUserTimeline(long sinceId, long maxId, boolean pullLatest, long userId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		if (pullLatest) {
			params.put("since_id", sinceId);
		} else {
			params.put("max_id", maxId);
		}
		params.put("user_id", userId);

		client.get(apiUrl, params, handler);

	}

	// GET account/verify_credentials.json
	public void getSelfUserInfo(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		// execute request
		client.get(apiUrl, handler);

	}

	public void getUserInfo(long userId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("users/show.json");
		RequestParams params = new RequestParams();
		params.put("user_id", userId);

		// execute request
		client.get(apiUrl, params, handler);
	}

	// POST https://api.twitter.com/1.1/statuses/update.json
	public void publishTweet(String tweet, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", tweet);
		// execute request
		client.post(apiUrl, params, handler);
	}


	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}
