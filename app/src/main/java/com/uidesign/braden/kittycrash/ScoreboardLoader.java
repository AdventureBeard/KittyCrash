// Braden Herndon
package com.uidesign.braden.kittycrash;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by braden on 11/3/15.
 */
public class ScoreboardLoader {

    Context ctx;

    // The constructor for ContactFileIO takes the context of the main activity, ContactListActivity.
    public ScoreboardLoader(Context context) throws IOException {
        this.ctx = context;
        File file = new File(context.getFilesDir(), "scoreboard.txt");
        // If there is no contacts file, make one with bogus data.
        if (!file.exists()) {
        generateTestFile();
        }
    }

    /**
     * readContacts
     * <p/>
     * Reads a String of contacts from a text file in internal storage and populates an
     * ArrayList with the results.
     *
     * @return an arrayList of contacts
     * @throws IOException
     */
    public ArrayList readScores() throws IOException {
        ArrayList<Score> scores = new ArrayList<>();
        ArrayList<String> contacts = new ArrayList<>();
        try {
            FileInputStream fileIn = ctx.openFileInput("scoreboard.txt");
            InputStreamReader inputReader = new InputStreamReader(fileIn);
            BufferedReader br = new BufferedReader(inputReader);
            String str;
            int count = 0;
            String[] data;
            while ((str = br.readLine()) != null && count < 11) {
                data = str.split("\t\t\t");
                scores.add(new Score(data[0], Integer.parseInt(data[1])));
                count++;
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        } catch (IOException e) {
            System.out.println("Something went wrong. :(");
        }
        return scores;
    }

    /**
     * writeContacts
     * <p/>
     * Receive an arrayList of contacts and serialize them into a string to write to
     * the contacts file in internal storage.
     *
     * @param contacts the contacts we want to write to file
     * @throws IOException
     */
    public void writeScores(ArrayList scores) throws IOException {
        try {
            FileOutputStream fileOut = ctx.openFileOutput("scoreboard.txt", Context.MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileOut);
            String data = "";
            for (int i = 0; i < scores.size(); i++) {
                data = scores.get(i).toString() + "\n";
                outputWriter.write(data);
            }
            outputWriter.close();
        } catch (Exception e) {
            System.out.println("Write failed.");
            e.printStackTrace();
        }
    }

    /**
     * generateTestFile
     * This is only called if there is no contacts file found on internal storage.
     *
     * @throws IOException
     */
    public void generateTestFile() throws IOException {
        ArrayList<Score> scores = new ArrayList<>();
        scores.add(new Score("BPH", 500));
        scores.add(new Score("HUI", 400));
        scores.add(new Score("QUI", 300));
        scores.add(new Score("ASS", 200));
        writeScores(scores);
    }
}

