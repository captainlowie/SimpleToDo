package com.app.captainlowie.simpletodo;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.security.KeyStore;
import java.util.jar.Attributes;

/**
 * Created by eduard on 24.11.17.
 */

public class Task_TextView extends TextView {

    public Task_TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets()
        ,"fonts/Roboto-Regular.tff"));
    }

}
