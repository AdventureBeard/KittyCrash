package com.uidesign.braden.kittycrash;

import android.graphics.Color;

/**
 * Created by braden on 11/29/15.
 */
public class Block {

    public int xPos;
    int yPos;
    int width;
    int height;
    int layer;
    int hitpoints;
    int color;

    public Block(int xPos, int layer, int width, int hitpoints) {
        this.xPos = xPos;
        this.layer = layer;
        this.width = 80;
        this.hitpoints = hitpoints;
        setColor(hitpoints);

    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public void decreaseHitpoints() {
        hitpoints--;
        setColor(hitpoints);
    }

    public void setHitpoints(int hitpoints) {
        this.hitpoints = hitpoints;
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int hp) {
        switch (hp) {
            case 1:
                color = Color.WHITE;
                break;
            case 2:
                color = Color.BLUE;
                break;
            case 3:
                color = Color.GREEN;
                break;
            case 4:
                color = Color.RED;
                break;
            case 5:
                color = Color.BLACK;
        }
    }
}
