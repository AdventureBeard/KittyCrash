package com.uidesign.braden.kittycrash;

/**
 * Created by braden on 11/29/15.
 */
public class LevelManager {

    Block[] level;

    public LevelManager() {
        this.level = new Block[16];

        level[0] = new Block(0, 0, 100, 5);
        level[0] = new Block(1, 0, 100, 4);
        level[0] = new Block(2, 0, 100, 3);
        level[0] = new Block(3, 0, 100, 2);
        level[0] = new Block(4, 0, 100, 1);


    }

    public Block[] getCurrentLevel() {
        return level;
    }
}
