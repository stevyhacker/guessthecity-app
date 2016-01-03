package me.montecode.simplequizapp.gradovi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;

import info.hoang8f.widget.FButton;


public class MainQuizActivity extends Activity implements View.OnClickListener {
    private DatabaseHelper db;
    TextView questionTextView, option4TextView, option1TextView, option2TextView, option3TextView, questionCounterTextView, timeCounterTextView;
    private ImageView baseImgView;
    private QuestionItem currentQuestionItem;
    private String currentQuestionAnswer;
    private int currentQuestionAnswerPosition;
    private boolean answerOptionClicked = false;
    private Handler timeCounterHandler;
    private int secondsCounter = 0;
    int questionsCounter = 0;
    int correctAnswers = 0;
    private Runnable secondsRunnable;
    AlertDialog newGameDialog, enterNameDialog, nextLevelDialog;
    View newGameDialogView, enterNameDialogView, nextLevelDialogView;
    TextView scoreTimeTextView, scorePointsTextView;
    FButton newGameButton, highScoresButton, confirmEnterNameDialogButton, cancelEnterNameDialogButton;
    String dateTimeFormat = "dd.MM.yyyy HH:mm";
    private EditText enterNameEditText;
    QuestionItem[] firstLevelQuestionArray;
    QuestionItem[] secondLevelQuestionArray;
    QuestionItem[] thirdLevelQuestionArray;
    int currentLevel = 1;
    TextView correctAnswersTextView;
    private Button nextLevelButton;
    private TextView scorePointsThisLevelTextView;

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
        baseImgView = (ImageView) findViewById(R.id.quizBaseImageView);

        correctAnswersTextView = (TextView) findViewById(R.id.firstLevelCorrectAnswersTextView);
        correctAnswersTextView.setText("Tačnih odgovora: 0");
        option1TextView.setOnClickListener(this);
        option2TextView.setOnClickListener(this);
        option3TextView.setOnClickListener(this);
        option4TextView.setOnClickListener(this);

        questionCounterTextView.setText(String.valueOf(questionsCounter) + "/5");
        timeCounterTextView.setText(String.valueOf(secondsCounter));

        newGameDialogView = inflater.inflate(R.layout.new_game_dialog_layout, null);
        enterNameDialogView = inflater.inflate(R.layout.enter_name_dialog_layout, null);
        nextLevelDialogView = inflater.inflate(R.layout.next_level_dialog_layout, null);

        scorePointsTextView = (TextView) newGameDialogView.findViewById(R.id.scorePointsTextView);
        scoreTimeTextView = (TextView) newGameDialogView.findViewById(R.id.scoreTimeTextView);
        newGameButton = (FButton) newGameDialogView.findViewById(R.id.newGameButton);
        highScoresButton = (FButton) newGameDialogView.findViewById(R.id.highScoresButton);

        confirmEnterNameDialogButton = (FButton) enterNameDialogView.findViewById(R.id.confirmEnterNameDialogButton);
        cancelEnterNameDialogButton = (FButton) enterNameDialogView.findViewById(R.id.cancelEnterNameDialogButton);
        enterNameEditText = (EditText) enterNameDialogView.findViewById(R.id.enterNameEditText);

        nextLevelButton = (Button) nextLevelDialogView.findViewById(R.id.continueLevelButton);
        scorePointsThisLevelTextView = (TextView) nextLevelDialogView.findViewById(R.id.scorePointsThisLevelTextView);

        newGameButton.setOnClickListener(this);
        highScoresButton.setOnClickListener(this);
        confirmEnterNameDialogButton.setOnClickListener(this);
        cancelEnterNameDialogButton.setOnClickListener(this);
        nextLevelButton.setOnClickListener(this);

        newGameDialog = new AlertDialog.Builder(this).create();
        newGameDialog.setView(newGameDialogView, 0, 0, 0, 0);

        enterNameDialog = new AlertDialog.Builder(this).create();
        enterNameDialog.setView(enterNameDialogView, 0, 0, 0, 0);

        nextLevelDialog = new AlertDialog.Builder(this).create();
        nextLevelDialog.setView(nextLevelDialogView, 0, 0, 0, 0);

        AdView mAdView = (AdView) findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        firstLevelQuestionArray = getLevelQuestions(1);
        secondLevelQuestionArray = getLevelQuestions(2);
        thirdLevelQuestionArray = getLevelQuestions(3);

        setNewQuestion();
        startTimer();

    }

    public void setAnimations(long duration) {
//        YoYo.with(Techniques.Shake)
//                .duration(duration)
//                .playOn(rightDownQuestionMarkImgView);
//        YoYo.with(Techniques.Landing)
//                .duration(duration)
//                .playOn(rightUpQuestionMarkImgView);
//        YoYo.with(Techniques.Tada)
//                .duration(duration)
//                .playOn(leftDownQuestionMarkImgView);
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
                newGameDialog.show();
                break;
            case R.id.continueLevelButton:
                nextLevelDialog.cancel();
                break;

        }
    }

    private void resetGame() {
        firstLevelQuestionArray = getLevelQuestions(1);
        secondLevelQuestionArray = getLevelQuestions(2);
        thirdLevelQuestionArray = getLevelQuestions(3);
        currentLevel = 1;
        secondsCounter = 0;
        questionsCounter = 0;
        correctAnswers = 0;
        answerOptionClicked = false;
        timeCounterHandler.removeCallbacks(secondsRunnable);
        correctAnswersTextView.setText("Tačnih odgovora: " + String.valueOf(correctAnswers));
        setNewQuestion();
        startTimer();
        newGameDialog.cancel();
    }

    private void setNewQuestion() {
        questionsCounter++;
        switch (currentLevel) {
            default:
                questionCounterTextView.setText(String.valueOf(questionsCounter) + "/15");
                break;
            case 2:
                questionCounterTextView.setText(String.valueOf(questionsCounter + 5) + "/15");
                break;
            case 3:
                questionCounterTextView.setText(String.valueOf(questionsCounter + 10) + "/15");
                break;
        }

        currentQuestionItem = getRandomQuestion();
        currentQuestionAnswer = currentQuestionItem.answer;

        questionTextView.setText(currentQuestionItem.question);
        randomSetTextViews();

        option1TextView.setBackgroundResource(R.drawable.answeroption_drawable);
        option2TextView.setBackgroundResource(R.drawable.answeroption_drawable);
        option3TextView.setBackgroundResource(R.drawable.answeroption_drawable);
        option4TextView.setBackgroundResource(R.drawable.answeroption_drawable);
        answerOptionClicked = false;

        if (!currentQuestionItem.imageName.equalsIgnoreCase("default")) {
            try {
                baseImgView.setVisibility(View.VISIBLE);
                baseImgView.setImageBitmap(loadDataFromAsset(currentQuestionItem.imageName));
            } catch (IOException e) {
                baseImgView.setVisibility(View.GONE);
                e.printStackTrace();
            }
        } else {
            baseImgView.setVisibility(View.GONE);

        }

        setAnimations(5000);

    }

    public Bitmap loadDataFromAsset(String imageName) throws IOException {

        InputStream is = getAssets().open("slike/" + imageName + ".jpg");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        return BitmapFactory.decodeStream(is, new Rect(), options);
    }

    private void startTimer() {

        secondsRunnable = new Runnable() {
            @Override
            public void run() {
                timeCounterTextView.setText(String.valueOf(secondsCounter) + " s");
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

        if (option1TextView.getText().toString().equalsIgnoreCase("")) {
            option1TextView.setVisibility(View.GONE);
        } else {
            option1TextView.setVisibility(View.VISIBLE);
        }

        if (option2TextView.getText().toString().equalsIgnoreCase("")) {
            option2TextView.setVisibility(View.GONE);
        } else {
            option2TextView.setVisibility(View.VISIBLE);
        }

        if (option3TextView.getText().toString().equalsIgnoreCase("")) {
            option3TextView.setVisibility(View.GONE);
        } else {
            option3TextView.setVisibility(View.VISIBLE);
        }
        if (option4TextView.getText().toString().equalsIgnoreCase("")) {
            option4TextView.setVisibility(View.GONE);
        } else {
            option4TextView.setVisibility(View.VISIBLE);
        }

    }

    private boolean checkAnswer(CharSequence text, final TextView answerOption) {
        if (text.equals(currentQuestionAnswer)) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Tačan odgovor", Toast.LENGTH_SHORT).show();
                    answerOption.setBackgroundResource(R.drawable.answeroption_green_drawable);
                    YoYo.with(Techniques.Flash)
                            .duration(500)
                            .playOn(answerOption);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            correctAnswers++;
                            correctAnswersTextView.setText("Tačnih odgovora: " + String.valueOf(correctAnswers));

                            if (questionsCounter < 5) {
                                setNewQuestion();
                            } else if (currentLevel < 3) {
                                currentLevel++;
                                showNextLevelDialog();
                                questionsCounter = 0;
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
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Netačan odgovor", Toast.LENGTH_SHORT).show();
                    answerOption.setBackgroundResource(R.drawable.answeroption_red_drawable);
                    YoYo.with(Techniques.Shake)
                            .duration(500)
                            .playOn(answerOption);
                    highlightCorrectAnswer();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (questionsCounter < 5) {
                                setNewQuestion();
                            } else if (currentLevel < 3) {
                                currentLevel++;
                                showNextLevelDialog();
                                questionsCounter = 0;
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

    private void showNextLevelDialog() {
        switch (currentLevel) {
            default:
                scorePointsThisLevelTextView.setText("Odgovorili ste tačno na " + String.valueOf(correctAnswers) + "/5" + " lakih pitanja.");
                break;
            case 3:
                scorePointsThisLevelTextView.setText("Odgovorili ste tačno na " + String.valueOf(correctAnswers) + "/10" + " srednje teških pitanja.");
                break;
        }
        nextLevelDialog.show();
    }


    private void showNewGameDialog() {
        scoreTimeTextView.setText("Vrijeme za koje ste odgovorili na pitanja je: " + String.valueOf(secondsCounter) + " sekundi.");
        scorePointsTextView.setText("Odgovorili ste tačno na " + String.valueOf(correctAnswers) + "/15" + " pitanja.");
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
//        int lastId;
//        switch (currentLevel) {
//            default:
//                lastId = firstLevelQuestionArray.length;
//                break;
//            case 2:
//                lastId = secondLevelQuestionArray.length;
//                break;
//            case 3:
//                lastId = thirdLevelQuestionArray.length;
//                break;
//        }

//        Random randomForQuestion = new Random();
//        int randomQuestionId = randomForQuestion.nextInt(lastId);
//        while (randomQuestionId == previousQuestionId) {
//            randomQuestionId = randomForQuestion.nextInt(lastId);
//        }
//        previousQuestionId = randomQuestionId;

        switch (currentLevel) {
            default:
                return firstLevelQuestionArray[questionsCounter - 1];
            case 2:
                return secondLevelQuestionArray[questionsCounter - 1];
            case 3:
                return thirdLevelQuestionArray[questionsCounter - 1];
        }

    }

    private void addNewScore() {
        String allInfo = KvizApp.preferencesHelper.getString("scores", "");

        Date dateTime = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateTimeFormat);


        if (allInfo.equalsIgnoreCase("") || allInfo == null) {
            allInfo = enterNameEditText.getText() + ",:," + String.valueOf(correctAnswers + "/" + secondsCounter + "s") + ",:," + simpleDateFormat.format(dateTime);
            Log.e("PREFERENCES", "allInfo " + allInfo);

        } else {
            allInfo += ":;:" + enterNameEditText.getText() + ",:," + String.valueOf(correctAnswers + "/" + secondsCounter + "s") + ",:," + simpleDateFormat.format(dateTime);
            Log.e("PREFERENCES", "allInfo " + allInfo);

        }
        KvizApp.preferencesHelper.putString("scores", allInfo);
    }

    public QuestionItem[] getLevelQuestions(int level) {
        QuestionItem[] allLevelQuestions;

        switch (level) {
            default:
                allLevelQuestions = db.getFirstLevelQuestionArray();
                break;
            case 2:
                allLevelQuestions = db.getSecondLevelQuestionArray();
                break;
            case 3:
                allLevelQuestions = db.getThirdLevelQuestionArray();
                break;
        }

        QuestionItem[] levelQuestionItems = new QuestionItem[5];
        int[] chosenIds = new int[5];

        int lastId = allLevelQuestions.length;
        Random randomForQuestion = new Random();
        //TODO check if it duplicates questions
        for (int i = 0; i < levelQuestionItems.length; i++) {
            int randomQuestionId = randomForQuestion.nextInt(lastId);

            for (int j = 0; j < 5; j++) {
                if (randomQuestionId == chosenIds[j]) {
                    randomQuestionId = randomForQuestion.nextInt(lastId);
                    j = 0;
                }
            }
            chosenIds[i] = randomQuestionId;
            levelQuestionItems[i] = (allLevelQuestions[randomQuestionId]);
        }

        HashSet<Integer> pitanja = new HashSet<>();
        for (int i = 0; i < chosenIds.length; i++) {
            if (pitanja.add(chosenIds[i]) == false) {
                int randomQuestionId = randomForQuestion.nextInt(lastId);
                chosenIds[i] = randomQuestionId;
                levelQuestionItems[i] = (allLevelQuestions[randomQuestionId]);
                i = 0;
                pitanja.clear();
            }
        }


        return levelQuestionItems;
    }


}
