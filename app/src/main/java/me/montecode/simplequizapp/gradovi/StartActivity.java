package me.montecode.simplequizapp.gradovi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import info.hoang8f.widget.FButton;


public class StartActivity extends Activity implements View.OnClickListener {

    private FButton startQuizButton, highScoreButton;
    DatabaseHelper db;
    Functions functions = new Functions();
    private String offlineQuestionsJsonString;
    private static final int OFFLINE_DATA = 100;
    private TextView quizTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.start_activity_layout);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        ImageView logoImgView = (ImageView) findViewById(R.id.logoBanerImgView);
        logoImgView.setOnClickListener(this);


        db = new DatabaseHelper(getApplicationContext());

        startQuizButton = (FButton) findViewById(R.id.startQuizButton);
        highScoreButton = (FButton) findViewById(R.id.highScoreButton);
//        aboutProjectButton = (FButton) findViewById(R.id.aboutProjectButton);
        quizTitleTextView = (TextView) findViewById(R.id.quizTitleTextView);

        startQuizButton.setOnClickListener(this);
        highScoreButton.setOnClickListener(this);
//        aboutProjectButton.setOnClickListener(this);
//        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/Courgette-Regular.ttf");


       Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/IndieFlower.ttf");
        quizTitleTextView.setTypeface(myTypeface);

        startOfflineMod();


    }
//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
//    }
    private void startOfflineMod() {
        try {
            if (db.doesDatabaseExist(getApplicationContext(), "quizDB")) {
                Log.w("DATABASE_STATE", "EXISTS");
                if (db.isQuestionsTableEmpty()) {
                    Log.w("DATABASE_STATE", "Empty");
                    offlineQuestionsJsonString = functions.jsonToStringFromAssetFolder("questions_data.json", getApplicationContext());
                    loadingHandler.sendEmptyMessage(OFFLINE_DATA);
                }
            } else if (!db.doesDatabaseExist(getApplicationContext(), "quizDB")) {
                Log.w("DATABASE_STATE", "DOESN'T EXIST");
                offlineQuestionsJsonString = functions.jsonToStringFromAssetFolder("questions_data.json", getApplicationContext());
                loadingHandler.sendEmptyMessage(OFFLINE_DATA);
            }
        } catch (IOException e) {
            Log.e("IOException", String.valueOf(e));
        }
    }

    Handler loadingHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == OFFLINE_DATA) {
                try {
                    JSONObject offlineQuestionsJSON = new JSONObject(offlineQuestionsJsonString);
                    JSONArray questionsJsonArray = offlineQuestionsJSON.getJSONArray("data");
                    for (int i = 0; i < questionsJsonArray.length(); i++) {
                        JSONObject questionJsonObject = questionsJsonArray.getJSONObject(i);
                        QuestionItem questionItem = new QuestionItem();

                        questionItem.question = questionJsonObject.getString("question");
                        questionItem.level = questionJsonObject.getInt("level");
                        questionItem.answer = questionJsonObject.getString("answer");
                        questionItem.option1 = questionJsonObject.getString("option1");
                        questionItem.option2 = questionJsonObject.getString("option2");
                        questionItem.option3 = questionJsonObject.getString("option3");
                        questionItem.imageName = questionJsonObject.getString("image_name");


                        db.addQuestion(questionItem);
                    }

                } catch (JSONException e) {
                    Log.e("JSONException", String.valueOf(e));
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startQuizButton:
                Intent intent1 = new Intent(this, MainQuizActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_from_left_animation, R.anim.slide_out_from_right_animation);
                break;

            case R.id.highScoreButton:
                Intent intent2 = new Intent(this, HighScoreActivity.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in_from_left_animation, R.anim.slide_out_from_right_animation);
                break;
            case R.id.logoBanerImgView:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.montecode.me")));
                break;

        }
    }




}
