package com.uidesign.braden.kittycrash;

/**
 * Created by braden on 11/29/15.
 */
public class Score implements Comparable<Score> {

    String name;
    int score;

    public Score(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return name + "\t\t\t" + score;
    }

    @Override
    public int compareTo(Score otherScore) {
        if (this.score < otherScore.score) {
            return 1;
        } else {
            return -1;
        }
    }
}
