package com.nh.ballsample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button mResumePauseButton;
    private boolean shouldPause = true;
    private MyFrameLayout mContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContainer = (MyFrameLayout)findViewById(R.id.container);
        mResumePauseButton = (Button)findViewById(R.id.pauseButton);
        mResumePauseButton.setEnabled(false);
        mResumePauseButton.setOnClickListener(this);
        mContainer.setOnFirstChildAddListener(new MyFrameLayout.OnFirstChildAddListener() {
            @Override
            public void onFirstChildAdd() {
                //enable the pause button only when first child added
                mResumePauseButton.setEnabled(true);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (mContainer.getChildCount() > 0) {
            if (shouldPause) {
                //pause the animation
                mContainer.pauseChildAnimation();
                //change the label to resume
                mResumePauseButton.setText(getString(R.string.resume));
                shouldPause = false;
            } else {
                //resume the animation
                mContainer.resumeChildAnimation();
                //change the label to pause
                mResumePauseButton.setText(getString(R.string.pause));
                shouldPause = true;
            }
        }
    }
}
