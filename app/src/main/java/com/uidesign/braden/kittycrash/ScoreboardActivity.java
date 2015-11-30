package com.uidesign.braden.kittycrash;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ScoreboardActivity extends AppCompatActivity {

    ArrayList<Score> scoreboard;
    ArrayAdapter<String> nameAdapter;
    ArrayAdapter<String> scoreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadScoreBoard();

        Intent newScoreIntent = getIntent();
        final int newScore = newScoreIntent.getIntExtra("playerScore", 0);

        if (scoreboard.size() < 10 || newScore > scoreboard.get(9).score) {
            AlertDialog.Builder enterHiScore = new AlertDialog.Builder(this);
            enterHiScore.setTitle("New High Score!");
            enterHiScore.setMessage("Enter your name:");
            final EditText nameInput = new EditText(this);
            enterHiScore.setView(nameInput);
            enterHiScore.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String name = nameInput.getText().toString();
                    scoreboard.add(new Score(name, newScore));

                    try {
                        ScoreboardLoader sl = new ScoreboardLoader(getApplicationContext());
                        sl.writeScores(scoreboard);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    loadScoreBoard();
                }
            });
            enterHiScore.show();
        }



    }

    private void loadScoreBoard() {
        try {
            ScoreboardLoader sl = new ScoreboardLoader(getApplicationContext());
            scoreboard = sl.readScores();
            Collections.sort(scoreboard);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (scoreboard.size() > 10) {
            scoreboard = (ArrayList) scoreboard.subList(0, 9);
        }

        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> scores = new ArrayList<>();
        for (int i = 0; i < scoreboard.size(); i++) {
            Score score = scoreboard.get(i);
            names.add(score.getName());
            scores.add(score.getScore() + "");
        }
        ListView nameList = (ListView) findViewById(R.id.nameList);
        ListView scoreList = (ListView) findViewById(R.id.scoreList);
        if (scores != null) {
            nameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
            nameList.setAdapter(nameAdapter);
            scoreAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,scores);
            scoreList.setAdapter(scoreAdapter);
        }
    }

    @Override
    public void onBackPressed() {

    }

}
