package me.montecode.pmcg.kvizoprirodi;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
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

        TextView emptyText = new TextView(this);
        emptyText.setText("Još uvjek nema rezultata.");
        highScoreListView.setEmptyView(emptyText);

    }

    private void loadScores() {
        highScoreValues.clear();

        String allInformation = PMCGKvizZnanjaApp.preferencesHelper.getString("scores", "");

        if (allInformation != null || !allInformation.equalsIgnoreCase("")) {

            for (String returnvalue : allInformation.split(":;:")) {
                name = "";
                result = "";
                date = "";

                String[] token = returnvalue.split(",:,");

                name = token[0];
                result = token[1];
                date = token[2];

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
