package com.apemans.custom.review;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.MediaController;

public class QMediaController extends MediaController {
    public QMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QMediaController(Context context, boolean useFastForward) {
        super(context, useFastForward);
    }

    public QMediaController(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.i("test", "dispatchKeyEvent: " + event.getKeyCode());
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            super.hide();
            ((Activity) getContext()).finish();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
