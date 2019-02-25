package com.devstories.starball_android.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class KeyDetectableEditText extends EditText {
    /* Must use this constructor in order for the layout files to instantiate the class properly */
    public KeyDetectableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private KeyImeChange keyImeChangeListener;

    public void setKeyImeChangeListener(KeyImeChange listener) {
        keyImeChangeListener = listener;
    }

    public interface KeyImeChange {
        public void onKeyIme(KeyEvent event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (keyImeChangeListener != null) {
            keyImeChangeListener.onKeyIme(event);
        }
        return super.dispatchKeyEvent(event);
    }

}
