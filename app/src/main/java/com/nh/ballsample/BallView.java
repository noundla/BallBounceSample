package com.nh.ballsample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import java.util.Random;

/**
 * Created by sknoundla on 7/12/2015.
 */
public class BallView extends View {
    private float radius = 30;      // Ball radius
    private float x = radius;
    private float y = radius;
    private float speedX = 5;       // Ball speed for X
    private float speedY = 7;       // Ball speed for Y

    private RectF mBallBounds;
    private Paint mBallPaint;
    private Paint mBoxPaint;

    private Box mBox;
    private Random mRandom = new Random();

    public BallView(Context context, float startX, float startY) {
        super(context);
        //get the random color for ball
        int randomColor = Color.argb(255, mRandom.nextInt(256), mRandom.nextInt(256), mRandom.nextInt(256));
        mBox = new Box();

        mBallBounds = new RectF();
        mBallPaint = new Paint();
        mBallPaint.setColor(randomColor);
        x = startX ; //set the touch x
        y = startY ; //set the touch y

        mBoxPaint = new Paint();
        mBoxPaint.setColor(Color.TRANSPARENT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(mBox.getBoxBounds(), mBoxPaint);

        mBallBounds.set(x - radius, y - radius, x + radius, y + radius);
        canvas.drawOval(mBallBounds, mBallPaint);

        //animate the ball position.
        moveWithCollisionDetection(mBox);
    }

    @Override
    public void onSizeChanged(int width, int height, int oldW, int oldH) {
        // set the ball moment area
        mBox.set(0, 0, width, height);
    }

    public void moveWithCollisionDetection (Box box) {
        // create new x,y position
        x += speedX;
        y += speedY;

        // Detect collision and react to traverse in other direction
        if (x + radius > box.xMax) {
            speedX = -speedX;
            x = box.xMax-radius;
        } else if (x - radius < box.xMin) {
            speedX = -speedX;
            x = box.xMin+radius;
        }
        if (y + radius > box.yMax) {
            speedY = -speedY;
            y = box.yMax - radius;
        } else if (y - radius < box.yMin) {
            speedY = -speedY;
            y = box.yMin + radius;
        }
    }
}
