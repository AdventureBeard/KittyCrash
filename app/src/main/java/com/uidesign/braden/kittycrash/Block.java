package com.uidesign.braden.kittycrash;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;

import javax.net.ssl.HandshakeCompletedListener;

/**
 * Created by braden on 11/29/15.
 */
public class Block {

    private int hitpoints;
    int color;
    boolean invincible;
    Rect rectangle;

    public int top, left, bottom, right;

    public Block(int position, int layer, int hitpoints, int screenX, int screenY) {

        int xScale = screenX / 9;
        if (layer == 3) {
            xScale = screenX / 7;
        } else if (layer == 2) {
            xScale = screenX / 8;
        }


        left = position * xScale + 30;
        top = layer * (screenY / 16);
        right = left + xScale - 30;
        bottom = top  + (screenY / 32);
        rectangle = new Rect(left, top, right, bottom);
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
            blockCooldownHandler.postDelayed(blockCooldown, 200);
        }
    }

    public Rect getRectangle() {
        return rectangle;
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
                color = Color.parseColor("#ffc2e2");
                break;
            case 3:
                color = Color.parseColor("#3d7f11");
                break;
            case 4:
                color = Color.parseColor("#6E44ff");
                break;
            case 5:
                color = Color.BLACK;
        }
    }

    public int collision(int ballX, int ballY, int ballRadius) {
        if (hitpoints == 0) return 0;
//        if (((ballX > (this.left - ballRadius)) && (ballX < (this.right + ballRadius))) &&
//                (ballY > (this.top - ballRadius)) && (ballY < (this.bottom + ballRadius))) {
//            decreaseHitpoints();
//            return true;
//        } else {
//            return false;
//        }

        float ballWidth = 2 * ballRadius + 3;
        float ballHeight = 2 * ballRadius + 3;
        float blockWidth = right - left;
        float blockHeight = bottom - top;
        float w = (float) 0.5 * (ballWidth + blockWidth);
        float h = (float) 0.5 * (ballHeight + blockHeight);
        float dx = ballX - rectangle.centerX();
        float dy = ballY - rectangle.centerY();

        if (Math.abs(dx) <= w && Math.abs(dy) <= h) {
            float wy = w * dy;
            float hx = h * dx;
            decreaseHitpoints();

            if (wy > hx) {
                if (wy > -hx) {
                    return 3;           // BOTTOM COLLISION
                } else {
                    return 2;           // LEFT COLLISION
                }
            } else {
                if (wy > -hx) {
                    return 4;           // RIGHT COLLISION
                } else {
                    return 1;           // TOP COLLISION
                }
            }
        }
        return 0;
    }
}
