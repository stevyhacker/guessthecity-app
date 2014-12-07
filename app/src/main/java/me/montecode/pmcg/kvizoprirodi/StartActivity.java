package me.montecode.pmcg.kvizoprirodi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import info.hoang8f.widget.FButton;


public class StartActivity extends Activity implements View.OnClickListener {

    private FButton startQuizButton, highScoreButton, aboutProjectButton;
    DatabaseHelper db;
    Functions functions = new Functions();
    private String offlineQuestionsJsonString;
    private static final int OFFLINE_DATA = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.start_activity_layout);

        db = new DatabaseHelper(getApplicationContext());

        startQuizButton = (FButton) findViewById(R.id.startQuizButton);
        highScoreButton = (FButton) findViewById(R.id.highScoreButton);
        aboutProjectButton = (FButton) findViewById(R.id.aboutProjectButton);

        startQuizButton.setOnClickListener(this);
        highScoreButton.setOnClickListener(this);
        aboutProjectButton.setOnClickListener(this);

        startOfflineMod();


    }

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
                overridePendingTransition(R.anim.slide_in_from_left_animation , R.anim.slide_out_from_right_animation );

                break;
            case R.id.highScoreButton:
                Intent intent2 = new Intent(this, HighScoreActivity.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in_from_left_animation , R.anim.slide_out_from_right_animation );

                break;
            case R.id.aboutProjectButton:
                Intent intent3 = new Intent(this, AboutProjectActivity.class);
                startActivity(intent3);
                overridePendingTransition(R.anim.slide_in_from_left_animation, R.anim.slide_out_from_right_animation);

                break;
        }
    }


    //MENU CODE (not needed atm)

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.start_activity_menu, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

    //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

//        return super.onOptionsItemSelected(item);
//    }


}
