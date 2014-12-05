package me.montecode.pmcg.kvizoprirodi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import info.hoang8f.widget.FButton;


public class MainQuizActivity extends Activity implements View.OnClickListener {
    private DatabaseHelper db;
    private int previousQuestionId;
    TextView questionTextView, option4TextView, option1TextView, option2TextView, option3TextView, questionCounterTextView, timeCounterTextView;
    //    FButton nextQuestionButton;
    private QuestionItem currentQuestionItem;
    private String currentQuestionAnswer;
    private int currentQuestionAnswerPosition;
    private boolean answerOptionClicked = false;
    private Handler timeCounterHandler;
    private int secondsCounter = 0;
    int questionsCounter = 0;
    int correctAnswers = 0;
    private Runnable secondsRunnable;
    AlertDialog newGameDialog,enterNameDialog;
    View newGameDialogView,enterNameDialogView;
    TextView scoreTimeTextView, scorePointsTextView;
    FButton newGameButton, highScoresButton, confirmEnterNameDialogButton, cancelEnterNameDialogButton;
    String dateTimeFormat = "dd.MM.yyyy HH:mm";
    private FButton surveyButton;
    private EditText enterNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_quiz_activity_layout);
        db = new DatabaseHelper(getApplicationContext());
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        questionTextView = (TextView) findViewById(R.id.questionTextView);
        option4TextView = (TextView) findViewById(R.id.option4TextView);
        option1TextView = (TextView) findViewById(R.id.option1TextView);
        option2TextView = (TextView) findViewById(R.id.option2TextView);
        option3TextView = (TextView) findViewById(R.id.option3TextView);
        questionCounterTextView = (TextView) findViewById(R.id.questionCounterTextView);
        timeCounterTextView = (TextView) findViewById(R.id.timeCounterTextView);
//        nextQuestionButton = (FButton) findViewById(R.id.nextQuestionButton);
        option1TextView.setOnClickListener(this);
        option2TextView.setOnClickListener(this);
        option3TextView.setOnClickListener(this);
        option4TextView.setOnClickListener(this);
//        nextQuestionButton.setOnClickListener(this);

        questionCounterTextView.setText(String.valueOf(questionsCounter) + "/10");
        timeCounterTextView.setText(String.valueOf(secondsCounter));

        newGameDialogView = inflater.inflate(R.layout.new_game_dialog_layout, null);
        enterNameDialogView = inflater.inflate(R.layout.enter_name_dialog_layout,null);

        scorePointsTextView = (TextView) newGameDialogView.findViewById(R.id.scorePointsTextView);
        scoreTimeTextView = (TextView) newGameDialogView.findViewById(R.id.scoreTimeTextView);
        newGameButton = (FButton) newGameDialogView.findViewById(R.id.newGameButton);
        highScoresButton = (FButton) newGameDialogView.findViewById(R.id.highScoresButton);
        surveyButton = (FButton) newGameDialogView.findViewById(R.id.surveyButton);

        confirmEnterNameDialogButton = (FButton) enterNameDialogView.findViewById(R.id.confirmEnterNameDialogButton);
        cancelEnterNameDialogButton = (FButton) enterNameDialogView.findViewById(R.id.cancelEnterNameDialogButton);
        enterNameEditText = (EditText) enterNameDialogView.findViewById(R.id.enterNameEditText);

        newGameButton.setOnClickListener(this);
        highScoresButton.setOnClickListener(this);
        surveyButton.setOnClickListener(this);
        confirmEnterNameDialogButton.setOnClickListener(this);
        cancelEnterNameDialogButton.setOnClickListener(this);

        newGameDialog = new AlertDialog.Builder(this).create();
        newGameDialog.setView(newGameDialogView, 0, 0, 0, 0);

        enterNameDialog = new AlertDialog.Builder(this).create();
        enterNameDialog.setView(enterNameDialogView,0,0,0,0);

        setNewQuestion();
        startTimer();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.option1TextView:
                if (!answerOptionClicked) {
                    answerOptionClicked = true;
                    option1TextView.setBackgroundResource(R.drawable.answeroption_blue_drawable);
                    checkAnswer(option1TextView.getText(), option1TextView);
                }
                break;
            case R.id.option2TextView:
                if (!answerOptionClicked) {
                    answerOptionClicked = true;
                    option2TextView.setBackgroundResource(R.drawable.answeroption_blue_drawable);
                    checkAnswer(option2TextView.getText(), option2TextView);
                }
                break;
            case R.id.option3TextView:
                if (!answerOptionClicked) {
                    answerOptionClicked = true;
                    option3TextView.setBackgroundResource(R.drawable.answeroption_blue_drawable);
                    checkAnswer(option3TextView.getText(), option3TextView);
                }
                break;
            case R.id.option4TextView:
                if (!answerOptionClicked) {
                    answerOptionClicked = true;
                    option4TextView.setBackgroundResource(R.drawable.answeroption_blue_drawable);
                    checkAnswer(option4TextView.getText(), option4TextView);
                }
                break;
            case R.id.newGameButton:
                resetGame();
                break;
            case R.id.highScoresButton:
                newGameDialog.cancel();
                enterNameDialog.show();
                break;
            case R.id.confirmEnterNameDialogButton:
                addNewScore();
                enterNameDialog.cancel();
                Intent intent2 = new Intent(this, HighScoreActivity.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in_from_left_animation, R.anim.slide_out_from_right_animation);
                break;
            case R.id.cancelEnterNameDialogButton:
                enterNameDialog.cancel();
                newGameDialog.show();                break;
            case R.id.surveyButton:
                Intent intent3 = new Intent(this, SurveyActivity.class);
                startActivity(intent3);
                overridePendingTransition(R.anim.slide_in_from_left_animation, R.anim.slide_out_from_right_animation);
                break;
//            case R.id.nextQuestionButton:
//                setNewQuestion();
//                break;

        }
    }

    private void resetGame() {
        secondsCounter = 0;
        questionsCounter = 0;
        correctAnswers = 0;
        answerOptionClicked = false;
        setNewQuestion();
        timeCounterHandler.removeCallbacks(secondsRunnable);
        startTimer();
        newGameDialog.cancel();
    }

    private void setNewQuestion() {
        questionsCounter++;
        questionCounterTextView.setText(String.valueOf(questionsCounter) + "/10");

        currentQuestionItem = getRandomQuestion();
        currentQuestionAnswer = currentQuestionItem.answer;

        questionTextView.setText(currentQuestionItem.question);
        randomSetTextViews();

        option1TextView.setBackgroundResource(R.drawable.answeroption_drawable);
        option2TextView.setBackgroundResource(R.drawable.answeroption_drawable);
        option3TextView.setBackgroundResource(R.drawable.answeroption_drawable);
        option4TextView.setBackgroundResource(R.drawable.answeroption_drawable);
        answerOptionClicked = false;
    }

    private void startTimer() {

        secondsRunnable = new Runnable() {
            @Override
            public void run() {
                timeCounterTextView.setText(String.valueOf(secondsCounter));
                secondsCounter++;
                timeCounterHandler.postDelayed(secondsRunnable, 1000);
            }
        };

        timeCounterHandler = new Handler();
        timeCounterHandler.postDelayed(secondsRunnable, 1000);

    }

    private void randomSetTextViews() {
        Random r = new Random();
        currentQuestionAnswerPosition = r.nextInt(4) + 1;
        switch (currentQuestionAnswerPosition) {
            case 1:
                option1TextView.setText(currentQuestionAnswer);
                option2TextView.setText(currentQuestionItem.option1);
                option3TextView.setText(currentQuestionItem.option3);
                option4TextView.setText(currentQuestionItem.option2);
                break;
            case 2:
                option2TextView.setText(currentQuestionAnswer);
                option1TextView.setText(currentQuestionItem.option1);
                option3TextView.setText(currentQuestionItem.option3);
                option4TextView.setText(currentQuestionItem.option2);
                break;
            case 3:
                option3TextView.setText(currentQuestionAnswer);
                option2TextView.setText(currentQuestionItem.option1);
                option1TextView.setText(currentQuestionItem.option2);
                option4TextView.setText(currentQuestionItem.option3);
                break;
            case 4:
                option4TextView.setText(currentQuestionAnswer);
                option2TextView.setText(currentQuestionItem.option3);
                option3TextView.setText(currentQuestionItem.option2);
                option1TextView.setText(currentQuestionItem.option1);
                break;
        }
    }

    private boolean checkAnswer(CharSequence text, final TextView answerOption) {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//            }
//        }, 800);
        if (text.equals(currentQuestionAnswer)) {
            Toast.makeText(this, "Tačan odgovor", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    answerOption.setBackgroundResource(R.drawable.answeroption_green_drawable);
                    YoYo.with(Techniques.Flash)
                            .duration(500)
                            .playOn(answerOption);


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            correctAnswers++;
                            if (questionsCounter < 10) {
                                setNewQuestion();
                            } else {
                                timeCounterHandler.removeCallbacks(secondsRunnable);
                                showNewGameDialog();
                            }
                        }
                    }, 1700);
                }
            }, 1000);
            return true;
        } else {
            Toast.makeText(this, "Netačan odgovor", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    answerOption.setBackgroundResource(R.drawable.answeroption_red_drawable);
                    YoYo.with(Techniques.Shake)
                            .duration(500)
                            .playOn(answerOption);
                    highlightCorrectAnswer();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (questionsCounter < 10) {
                                setNewQuestion();
                            } else {
                                timeCounterHandler.removeCallbacks(secondsRunnable);
                                showNewGameDialog();
                            }
                        }
                    }, 1700);
                }
            }, 1000);

            return false;
        }
    }

    private void showNewGameDialog() {
        scoreTimeTextView.setText("Vrijeme za koje ste odgovorili na pitanja je: " + String.valueOf(secondsCounter) + " sekundi.");
        scorePointsTextView.setText("Odgovorili ste tačno na " + String.valueOf(correctAnswers) + "/10" + " pitanja.");
        newGameDialog.show();
    }

    private void highlightCorrectAnswer() {
        switch (currentQuestionAnswerPosition) {
            case 1:
                option1TextView.setBackgroundResource(R.drawable.answeroption_green_drawable);
                break;
            case 2:
                option2TextView.setBackgroundResource(R.drawable.answeroption_green_drawable);
                break;
            case 3:
                option3TextView.setBackgroundResource(R.drawable.answeroption_green_drawable);
                break;
            case 4:
                option4TextView.setBackgroundResource(R.drawable.answeroption_green_drawable);
                break;
        }

    }

    private QuestionItem getRandomQuestion() {
        int lastId = db.getLastQuestionItemId();

        Random randomForQuestion = new Random();
        int randomQuestionId = randomForQuestion.nextInt(lastId) + 1;
        while (randomQuestionId == previousQuestionId) {
            randomQuestionId = randomForQuestion.nextInt(lastId) + 1;
        }
        previousQuestionId = randomQuestionId;

        return db.getQuestion(randomQuestionId);
    }

    private void addNewScore() {
        String allInfo = PMCGKvizZnanjaApp.preferencesHelper.getString("scores", "");

        Date dateTime = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateTimeFormat);


        if (allInfo.equalsIgnoreCase("") || allInfo == null) {
            allInfo = enterNameEditText.getText() + ",:," + String.valueOf((correctAnswers * 10000) / secondsCounter) + ",:," + simpleDateFormat.format(dateTime);
            Log.e("PREFERENCES", "allInfo " + allInfo);

        } else {
            allInfo += ":;:" + enterNameEditText.getText()+ ",:," + String.valueOf((correctAnswers * 10000) / secondsCounter) + ",:," + simpleDateFormat.format(dateTime);
            Log.e("PREFERENCES", "allInfo " + allInfo);

        }
        PMCGKvizZnanjaApp.preferencesHelper.putString("scores", allInfo);
    }


//  MENU Code
//   @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main_quiz_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

}
