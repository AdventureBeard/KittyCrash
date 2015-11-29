package com.uidesign.braden.kittycrash;

import java.util.ArrayList;

/**
 * Created by braden on 11/29/15.
 */
public class LevelManager {

    ArrayList<Block> level;

    public LevelManager(int w, int h) {
        level = new ArrayList<Block>();

        level.add(new Block(0, 1, 4, w, h));
        level.add(new Block(1, 1, 4, w, h));
        level.add(new Block(2, 1, 4, w, h));
        level.add(new Block(4, 1, 3, w, h));
        level.add(new Block(5, 1, 3, w, h));
        level.add(new Block(6, 1, 4, w, h));
        level.add(new Block(7, 1, 4, w, h));
        level.add(new Block(8, 1, 4, w, h));




        level.add(new Block(3, 3, 2, w, h));
        level.add(new Block(4, 3, 2, w, h));
        level.add(new Block(5, 3, 2, w, h));

        level.add(new Block(4, 4, 2, w, h));
    }

    public ArrayList getCurrentLevel() {
        return level;
    }
}
