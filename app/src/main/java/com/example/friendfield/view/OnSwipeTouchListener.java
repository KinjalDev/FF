package com.example.friendfield.view;

import static java.lang.Math.abs;

import android.app.Activity;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

public class OnSwipeTouchListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;
    private float startX = 0f;
    private float startY = 0f;
    private long touchDownTime = 0l;

    @NotNull
    public static  OnSwipeTouchListener.Companion Companion = new OnSwipeTouchListener.Companion();

    @Override
    public boolean onTouch(@NotNull View view, @NotNull MotionEvent event) {
        this.onTouchView(view, event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.touchDownTime = this.now();
                this.startX = event.getX();
                this.startY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float endX = event.getX();
                float endY = event.getY();
                if (this.isAClick(this.startX, endX, this.startY, endY)) {
                    this.onClick(view);
                }
        }

        return this.gestureDetector.onTouchEvent(event);
    }

    private  boolean isAClick(float startX, float endX, float startY, float endY) {
        boolean isTouchDuration = this.now() - this.touchDownTime < 200L;
        boolean isTouchLength = abs(endX - startX) + abs(endY - startY) < (float) 150L;
        return isTouchDuration && isTouchLength;
    }

    private  long now() {
        return System.currentTimeMillis();
    }

    public void onClick(@NotNull View view) {
        Intrinsics.checkParameterIsNotNull(view, "view");
    }

    public void onLongClick() {
    }

    public boolean onTouchView(@NotNull View view, @NotNull MotionEvent event) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        Intrinsics.checkParameterIsNotNull(event, "event");
        return false;
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }

    public OnSwipeTouchListener(@NotNull Activity context) {
        super();
        Intrinsics.checkParameterIsNotNull(context, "context");
        this.gestureDetector = new GestureDetector((Context) context, (GestureDetector.OnGestureListener) (new OnSwipeTouchListener.GestureListener()));
    }

    private  class GestureListener extends GestureDetector.SimpleOnGestureListener {
        public boolean onDown(@NotNull MotionEvent e) {
            return true;
        }

        public boolean onFling(@NotNull MotionEvent e1, @NotNull MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;

            try {
                float diffY = e2.getY() - e1.getY();
                if (abs(diffY) > (float) 100 && abs(velocityY) > (float) 100) {
                    if (diffY > (float) 0) {
                        OnSwipeTouchListener.this.onSwipeBottom();
                    } else {
                        OnSwipeTouchListener.this.onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception var8) {
                var8.printStackTrace();
            }

            return result;
        }

        public void onLongPress(@Nullable MotionEvent e) {
            OnSwipeTouchListener.this.onLongClick();
        }

        public GestureListener() {
        }
    }

    public static  class Companion {
        private Companion() {
        }

    }
}
