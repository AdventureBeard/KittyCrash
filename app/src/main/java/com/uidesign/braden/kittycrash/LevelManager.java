package com.uidesign.braden.kittycrash;

import java.util.ArrayList;

/**
 * Created by braden on 11/29/15.
 */
public class LevelManager {

    ArrayList<Block> level;

    public LevelManager(int w, int h) {
        level = new ArrayList<Block>();

        level.add(new Block(1, 1, 3, w, h));
        level.add(new Block(2, 1, 3, w, h));
        level.add(new Block(3, 1, 4, w, h));
        level.add(new Block(4, 1, 5, w, h));
        level.add(new Block(5, 1, 4, w, h));
        level.add(new Block(6, 1, 3, w, h));
        level.add(new Block(7, 1, 3, w, h));

        level.add(new Block(1, 2, 3, w, h));
        level.add(new Block(2, 2, 2, w, h));
        level.add(new Block(3, 2, 1, w, h));
        level.add(new Block(4, 2, 1, w, h));
        level.add(new Block(5, 2, 2, w, h));
        level.add(new Block(6, 2, 3, w, h));

        level.add(new Block(1, 3, 1, w, h));
        level.add(new Block(2, 3, 1, w, h));
        level.add(new Block(3, 3, 1, w, h));
        level.add(new Block(4, 3, 1, w, h));
        level.add(new Block(5, 3, 1, w, h));

    }

    public ArrayList getCurrentLevel() {
        return level;
    }
}
