package com.nh.ballsample;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by sknoundla on 7/12/2015.
 */
public class Box {
    int xMin, xMax, yMin, yMax;
    private Rect bounds;

    public void set(int x, int y, int width, int height) {
        xMin = x;
        xMax = x + width - 1;
        yMin = y;
        yMax = y + height - 1;
        // The box's bounds do not change unless the view's size changes
        bounds.set(xMin, yMin, xMax, yMax);
    }

    public Rect getBoxBounds(){
        return bounds;
    }
}
