package me.montecode.simplequizapp.gradovi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class HighScoreActivity extends Activity {


    ArrayList<ScoreItem> highScoreValues;
    ListView highScoreListView;
    AdapterForHighScoreList adapter;
    private String name, result, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.high_score_activity_layout);

        highScoreListView = (ListView) findViewById(R.id.scoresListView);
        highScoreValues = new ArrayList<ScoreItem>();

        adapter = new AdapterForHighScoreList(this, highScoreValues);
        loadScores();
        highScoreListView.setAdapter(adapter);

        TextView titleScoresTextView = (TextView) findViewById(R.id.titleScoresTextView);
        TextView emptyText = new TextView(this);
        emptyText.setText("Još uvjek nema rezultata.");
        emptyText.setGravity(Gravity.CENTER);
        emptyText.setTextColor(getResources().getColor(R.color.white));
        emptyText.setTextSize(25);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, titleScoresTextView.getId());
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.setMargins(0,25,0,0);
        emptyText.setLayoutParams(params);
        emptyText.setPadding(10, 10, 10, 10);
        ((ViewGroup) highScoreListView.getParent()).addView(emptyText);
        highScoreListView.setEmptyView(emptyText);

    }

    private void loadScores() {
        highScoreValues.clear();

        String allInformation = KvizApp.preferencesHelper.getString("scores", "");
        if (allInformation == null || allInformation.equalsIgnoreCase("")) {

        } else {

            for (String returnvalue : allInformation.split(":;:")) {

                Log.e("PREFERENCES", "returnvalue  " + returnvalue);

                name = "";
                result = "";
                date = "";

                String[] token = returnvalue.split(",:,");

                name = token[0];
                Log.e("PREFERENCES", "name " + name);

                result = token[1];
                Log.e("PREFERENCES", "result " + result);

                date = token[2];
                Log.e("PREFERENCES", "date " + date);

                ScoreItem item = new ScoreItem();
                item.name = name;
                item.result = result;
                item.date = date;

                highScoreValues.add(item);

            }

            adapter.notifyDataSetChanged();
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.high_score_activity_menu, menu);
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
