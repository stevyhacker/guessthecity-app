package me.montecode.pmcg.kvizoprirodi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import info.hoang8f.widget.FButton;


public class StartActivity extends Activity implements View.OnClickListener {

    private FButton startQuizButton, highScoreButton, aboutProjectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.start_activity_layout);

        startQuizButton = (FButton) findViewById(R.id.startQuizButton);
        highScoreButton = (FButton) findViewById(R.id.highScoreButton);
        aboutProjectButton = (FButton) findViewById(R.id.aboutProjectButton);

        startQuizButton.setOnClickListener(this);
        highScoreButton.setOnClickListener(this);
        aboutProjectButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startQuizButton:
                Intent intent1 = new Intent(this, MainQuizActivity.class);
                startActivity(intent1);
                break;
            case R.id.highScoreButton:
                Intent intent2 = new Intent(this, HighScoreActivity.class);
                startActivity(intent2);
                break;
            case R.id.aboutProjectButton:
                Intent intent3 = new Intent(this, AboutProjectActivity.class);
                startActivity(intent3);
                break;
        }
    }

//TODO EVERYTHING


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
