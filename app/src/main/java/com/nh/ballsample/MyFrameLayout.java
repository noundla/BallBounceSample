package com.nh.ballsample;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;

/**
 * Created by sknoundla on 7/12/2015.
 */
public class MyFrameLayout extends FrameLayout {
    private Context mContext;
    private int mBallId = 1000;
    private ArrayList<View> childList = new ArrayList<View>();
    private BallAnimationThread mThread;
    private OnFirstChildAddListener mListener;


    public MyFrameLayout(Context context) {
        super(context);
        mContext = context;
    }

    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public MyFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            //create and add new ball to container
            BallView view = new BallView(mContext,event.getX(),event.getY());
            view.setId(getBallId());
            addView(view);
            startAnimationThread();
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        childList.add(child);
        if(childList.size() == 1 && mListener != null){
            mListener.onFirstChildAdd();
        }
    }

    /**
     * Handler to update child positions on UI
     * */
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            for (View view : childList){
                view.invalidate();
            }
        }
    };

    /**
     * This thread is used to trigger the handler on specific intervals.
     * */
    class BallAnimationThread extends Thread {
        private boolean mIsPaused = false;
        private boolean mIsWaiting = false;
        @Override
        public void run() {
            if (mThread!=null) {
                while (true) {
                    if (mIsPaused) {
                        makeThreadToWait();
                    }

                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = mHandler.obtainMessage();
                    mHandler.sendMessage(message);
                }
            }
        }

        private void makeThreadToWait(){
            if(!mIsWaiting) {
                synchronized (this) {
                    try {
                        wait();
                        mIsWaiting = true;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        /**Pauses the thread*/
        public void pauseThread(){
            mIsPaused = true;
            mIsWaiting = false;
        }

        /**Resumes the thread by calling notify()*/
        public void resumeThread(){
            synchronized (this) {
                mThread.notify();
                mIsWaiting = false;
                mIsPaused = false;
            }
        }

    }

    /**Returns the next id for Ball*/
    private int getBallId(){
        return mBallId++;
    }

    /**Creates and start the thread. This will start the balls animations*/
    private synchronized void startAnimationThread(){
        //create and start thread when first child is added
        if (mThread == null) {
            mThread = new BallAnimationThread();
            mThread.start();
        }
    }

    /**Resumes the balls animation*/
    public synchronized void resumeChildAnimation(){
        if (mThread!=null) {
            mThread.resumeThread();
        }
    }

    /**Pause the balls animation*/
    public synchronized void pauseChildAnimation(){
        if(mThread!=null ){
            mThread.pauseThread();
        }
    }

    public void setOnFirstChildAddListener(OnFirstChildAddListener listener){
        mListener = listener;
    }

    /**Callback to invoke when the first child added to container*/
    public interface OnFirstChildAddListener {
        public void onFirstChildAdd();
    }
}
