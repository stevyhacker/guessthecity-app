package me.montecode.pmcg.kvizoprirodi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class MainQuizActivity extends Activity implements View.OnClickListener {
    private DatabaseHelper db;
    private int previousQuestionId;
    TextView questionTextView, option4TextView, option1TextView, option2TextView, option3TextView;
    private QuestionItem currentQuestionItem;
    private String currentQuestionAnswer;
    private int currentQuestionAnswerPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_quiz_activity_layout);
        db = new DatabaseHelper(getApplicationContext());

        questionTextView = (TextView) findViewById(R.id.questionTextView);
        option4TextView = (TextView) findViewById(R.id.option4TextView);
        option1TextView = (TextView) findViewById(R.id.option1TextView);
        option2TextView = (TextView) findViewById(R.id.option2TextView);
        option3TextView = (TextView) findViewById(R.id.option3TextView);

        currentQuestionItem = getRandomQuestion();
        currentQuestionAnswer = currentQuestionItem.answer;

        questionTextView.setText(currentQuestionItem.question);

        option1TextView.setBackgroundResource(R.drawable.answeroption_drawable);
        option2TextView.setBackgroundResource(R.drawable.answeroption_drawable);
        option3TextView.setBackgroundResource(R.drawable.answeroption_drawable);
        option4TextView.setBackgroundResource(R.drawable.answeroption_drawable);

        option1TextView.setOnClickListener(this);
        option2TextView.setOnClickListener(this);
        option3TextView.setOnClickListener(this);
        option4TextView.setOnClickListener(this);

        randomSetTextViews();

    }

    private void randomSetTextViews() {
        Random r = new Random();
        currentQuestionAnswerPosition = r.nextInt(4) + 1;
        switch (currentQuestionAnswerPosition){
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.option1TextView:
                option1TextView.setBackgroundResource(R.drawable.answeroption_blue_drawable);
                checkAnswer(option1TextView.getText());
                break;
            case R.id.option2TextView:
                option2TextView.setBackgroundResource(R.drawable.answeroption_blue_drawable);
                checkAnswer(option2TextView.getText());
                break;
            case R.id.option3TextView:
                option3TextView.setBackgroundResource(R.drawable.answeroption_blue_drawable);
                checkAnswer(option3TextView.getText());
                break;
            case R.id.option4TextView:
                option4TextView.setBackgroundResource(R.drawable.answeroption_blue_drawable);
                checkAnswer(option4TextView.getText());
                break;

        }
    }

    private void checkAnswer(CharSequence text) {
        if (text.equals(currentQuestionAnswer)) {
            Toast.makeText(this, "Tačan odgovor", Toast.LENGTH_SHORT).show();
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


//    @Override
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
