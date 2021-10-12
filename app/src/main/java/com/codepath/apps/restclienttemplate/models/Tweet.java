package com.codepath.apps.restclienttemplate.models;

import android.provider.ContactsContract;
import android.text.format.DateUtils;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.codepath.apps.restclienttemplate.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Parcel
@Entity(foreignKeys = @ForeignKey(entity= User.class, parentColumns="id", childColumns="userId"))
public class Tweet {

    @PrimaryKey
    @ColumnInfo
    public long id;

    @ColumnInfo
    public  String body;

    @ColumnInfo
    public String createdAt;

    @ColumnInfo
    public long userId;

    @Ignore
    public User user;
    public String time;


    //empty constructor for parceler library:
    public Tweet(){}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.id = jsonObject.getLong("id");
        User user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.user = user;
        tweet.userId = user.id;
        // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
        tweet.time = TimeFormatter.getTimeDifference(tweet.createdAt);
        return tweet;
    }
    public static List<Tweet> fromJasonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }
}
