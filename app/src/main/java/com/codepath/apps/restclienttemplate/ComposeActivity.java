package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import java.io.CharArrayWriter;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public static final int MAX_TWEET_LENGTH = 280;
    public static final String TAG = "ComposeActivity";

    EditText etCompose;
    Button btnTweet;
    String tweetContent;
    TextView tvCharCount;

    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApp.getRestClient(this);

        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);
        tvCharCount = findViewById(R.id.tvCharCount);

        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Fires right before text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Fires right after the text has changed

                int charsLeft = MAX_TWEET_LENGTH - s.toString().length();
                if (charsLeft < 0) {
                    tvCharCount.setTextColor(Color.RED);
                }
                tvCharCount.setText(String.valueOf(charsLeft));
            }

        });


        //add click listener on button:
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //error check:
                tweetContent = etCompose.getText().toString();
                if(tweetContent.isEmpty()){
                    Toast.makeText(ComposeActivity.this,"Sorry, tweet can't be empty!",Toast.LENGTH_LONG).show();
                    return;
                }
                if(tweetContent.length() >MAX_TWEET_LENGTH){
                    Toast.makeText(ComposeActivity.this,"Sorry, tweet is too long!",Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(ComposeActivity.this,tweetContent,Toast.LENGTH_LONG).show();
                //make an API call
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG,"onSuccess publish tweet");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG,"Publish tweet says: " + tweet);
                            Intent intent = new Intent();
                            intent.putExtra("tweet", Parcels.wrap(tweet));
                            setResult(RESULT_OK,intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG,"onFailure publish tweet",throwable);
                    }
                });

            }
        });

    }
}