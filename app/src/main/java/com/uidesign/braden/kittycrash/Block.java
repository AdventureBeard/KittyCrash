package com.uidesign.braden.kittycrash;

import android.graphics.Color;
import android.os.Handler;

import javax.net.ssl.HandshakeCompletedListener;

/**
 * Created by braden on 11/29/15.
 */
public class Block {

    private int hitpoints;
    int color;
    boolean invincible;

    public int top, left, bottom, right;

    public Block(int position, int layer, int hitpoints, int screenX, int screenY) {
        left = position * (screenX / 9) + 30;
        top = layer * (screenY / 16);
        right = left + (screenX / 9) - 30;
        bottom = top  + (screenY / 32);
//        left = screenX / 2;
//        top = screenY / 2;
//        right = left + 200;
//        bottom = top + 100;
        this.hitpoints = hitpoints;
        setColor(hitpoints);
    }

    private void decreaseHitpoints() {
        if (!invincible) {
            this.hitpoints--;
            invincible = true;
            setColor(hitpoints);
            Handler blockCooldownHandler = new Handler();
            Runnable blockCooldown = new Runnable() {
                @Override
                public void run() {
                    invincible = false;
                }
            };
            blockCooldownHandler.postDelayed(blockCooldown, 500);
        }
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public int getColor() {
        return color;
    }

    private void setColor(int hp) {
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

    public boolean collision(int ballX, int ballY, int ballRadius) {
        if (hitpoints == 0) return false;
        if (((ballX > (this.left - ballRadius)) && (ballX < (this.right + ballRadius))) &&
                (ballY > (this.top - ballRadius)) && (ballY < (this.bottom + ballRadius))) {
            decreaseHitpoints();
            return true;
        } else {
            return false;
        }
    }
}
