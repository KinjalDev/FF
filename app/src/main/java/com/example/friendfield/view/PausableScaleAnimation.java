package com.example.friendfield.view;

import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;

import org.jetbrains.annotations.NotNull;

import kotlin.jvm.internal.Intrinsics;

public class PausableScaleAnimation extends ScaleAnimation {
    private long elapsedAtPause;
    private boolean isPaused;

    public boolean getTransformation(long currentTime, @NotNull Transformation outTransformation, float scale) {
        Intrinsics.checkParameterIsNotNull(outTransformation, "outTransformation");
        if (this.isPaused && this.elapsedAtPause == 0L) {
            this.elapsedAtPause = currentTime - this.getStartTime();
        }

        if (this.isPaused) {
            this.setStartTime(currentTime - this.elapsedAtPause);
        }

        return super.getTransformation(currentTime, outTransformation, scale);
    }

    public final void pause() {
        if (!this.isPaused) {
            this.elapsedAtPause = 0L;
            this.isPaused = true;
        }
    }

    public final void resume() {
        this.isPaused = false;
    }

    public PausableScaleAnimation(float fromX, float toX, float fromY, float toY, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue) {
        super(fromX, toX, fromY, toY, pivotXType, pivotXValue, pivotYType, pivotYValue);
    }
}