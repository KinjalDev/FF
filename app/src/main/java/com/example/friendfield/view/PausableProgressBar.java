package com.example.friendfield.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;

import com.example.friendfield.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

import kotlin.Metadata;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public class PausableProgressBar extends FrameLayout {
    private View frontProgressView;
    private View maxProgressView;
    private PausableScaleAnimation animation;
    private long duration = 4000L;
    private PausableProgressBar.Callback callback;
    private boolean isStarted;
    private static long DEFAULT_PROGRESS_DURATION = 4000L;
    @NotNull
    public static PausableProgressBar.Companion Companion = new PausableProgressBar.Companion();

    public void setDuration(long duration) {
        this.duration = duration;
        if (this.animation != null) {
            this.animation = (PausableScaleAnimation) null;
            this.startProgress();
        }
    }

    public void setCallback(@NotNull PausableProgressBar.Callback callback) {
        Intrinsics.checkParameterIsNotNull(callback, "callback");
        this.callback = callback;
    }

    public void setMax() {
        this.finishProgress(true);
    }

    public void setMin() {
        this.finishProgress(false);
    }

    public void setMinWithoutCallback() {
        this.maxProgressView.setBackgroundResource(R.color.progress_secondary);

        if (this.maxProgressView == null) {
            Intrinsics.throwNpe();
        }

        this.maxProgressView.setVisibility(VISIBLE);
        if (this.animation != null) {
            this.animation.setAnimationListener((Animation.AnimationListener) null);
            this.animation.cancel();
        }

    }

    public void setMaxWithoutCallback() {

        this.maxProgressView.setBackgroundResource(R.color.progress_max_active);

        this.maxProgressView.setVisibility(VISIBLE);
        if (this.animation != null) {
            if (this.animation == null) {
                Intrinsics.throwNpe();
            }

            this.animation.setAnimationListener((Animation.AnimationListener) null);
            if (this.animation == null) {
                Intrinsics.throwNpe();
            }

            this.animation.cancel();
        }

    }

    private void finishProgress(boolean isMax) {
        if (isMax) {
            if (this.maxProgressView == null) {
                Intrinsics.throwNpe();
            }
            this.maxProgressView.setBackgroundResource(R.color.progress_max_active);
        }

        if (this.maxProgressView == null) {
            Intrinsics.throwNpe();
        }

        this.maxProgressView.setVisibility(isMax ? VISIBLE : GONE);
        if (this.animation != null) {

            this.animation.setAnimationListener((Animation.AnimationListener) null);

            this.animation.cancel();
            if (this.callback != null) {

                this.callback.onFinishProgress();
            }
        }

    }

    public void startProgress() {
        if (maxProgressView == null) {
            Intrinsics.throwNpe();
        }

        maxProgressView.setVisibility(GONE);

        if (duration <= 0) duration = DEFAULT_PROGRESS_DURATION;

        this.animation = new PausableScaleAnimation(0.0F, 1.0F, 1.0F, 1.0F, 0, 0.0F, 1, 0.0F);

        this.animation.setDuration(this.duration);

        this.animation.setInterpolator((Interpolator) (new LinearInterpolator()));

        this.animation.setAnimationListener((Animation.AnimationListener) (new Animation.AnimationListener() {
            public void onAnimationStart(@NotNull Animation animation) {
                Intrinsics.checkParameterIsNotNull(animation, "animation");
                if (!PausableProgressBar.this.isStarted) {
                    PausableProgressBar.this.isStarted = true;

                    PausableProgressBar.this.frontProgressView.setVisibility(VISIBLE);
                    if (PausableProgressBar.this.callback != null) {

                        PausableProgressBar.this.callback.onStartProgress();
                    }

                }
            }

            public void onAnimationEnd(@NotNull Animation animation) {
                Intrinsics.checkParameterIsNotNull(animation, "animation");
                PausableProgressBar.this.isStarted = false;
                if (PausableProgressBar.this.callback != null) {

                    PausableProgressBar.this.callback.onFinishProgress();
                }

            }

            public void onAnimationRepeat(@NotNull Animation animation) {
                Intrinsics.checkParameterIsNotNull(animation, "animation");
            }
        }));

        this.animation.setFillAfter(true);

        if (frontProgressView == null) {
            Intrinsics.throwNpe();
        }

        frontProgressView.startAnimation((Animation) this.animation);
    }

    public void pauseProgress() {
        if (this.animation != null) {
            this.animation.pause();
        }

    }

    public void resumeProgress() {
        if (this.animation != null) {
            this.animation.resume();
        }

    }

    public void clear() {
        if (this.animation != null) {
            this.animation.setAnimationListener((Animation.AnimationListener) null);
            this.animation.cancel();
            this.animation = (PausableScaleAnimation) null;
        }

    }

    @JvmOverloads
    public PausableProgressBar(@NotNull Context context, @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.pausable_progress, (ViewGroup) this);
        this.frontProgressView = this.findViewById(R.id.front_progress);
        this.maxProgressView = this.findViewById(R.id.max_progress);
    }

    public PausableProgressBar(Context var1, AttributeSet var2, int var3, int var4, DefaultConstructorMarker var5) {
        this(var1, var2, var3);
        if ((var4 & 2) != 0) {
            var2 = (AttributeSet) null;
        }

        if ((var4 & 4) != 0) {
            var3 = 0;
        }

    }

    @JvmOverloads
    public PausableProgressBar(@NotNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0, 4, (DefaultConstructorMarker) null);
    }

    @JvmOverloads
    public PausableProgressBar(@NotNull Context context) {
        this(context, (AttributeSet) null, 0, 6, (DefaultConstructorMarker) null);
    }

    public interface Callback {
        void onStartProgress();

        void onFinishProgress();
    }

    public static class Companion {
        private Companion() {
        }

    }
}
