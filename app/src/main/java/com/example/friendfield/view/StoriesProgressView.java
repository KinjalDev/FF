package com.example.friendfield.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.example.friendfield.R;
import com.example.friendfield.view.PausableProgressBar.Callback;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import kotlin.Metadata;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.Intrinsics;

public class StoriesProgressView extends LinearLayout {
    private final List progressBars;
    private StoriesProgressView.StoriesListener storiesListener;
    private int storiesCount = -1;
    private int current = -1;
    private boolean isSkipStart;
    private boolean isReverseStart;
    private int position = -1;
    private boolean isComplete;
    private static LinearLayout.LayoutParams PROGRESS_BAR_LAYOUT_PARAM = new LayoutParams(0, -2, 1.0F);
    private static LayoutParams SPACE_LAYOUT_PARAM = new LayoutParams(5, -2);
    @NotNull
    public static StoriesProgressView.Companion Companion = new StoriesProgressView.Companion();
    private HashMap _$_findViewCache;

    private void bindViews() {
        progressBars.clear();
        removeAllViews();

        for (int i = 0; i < storiesCount; i++) {
            final PausableProgressBar p = createProgressBar();
            progressBars.add(p);
            addView(p);
            if ((i + 1) < storiesCount) {
                addView(createSpace());
            }
        }

    }

    private PausableProgressBar createProgressBar() {
        PausableProgressBar var1 = new PausableProgressBar(getContext());
        var1.setLayoutParams((android.view.ViewGroup.LayoutParams) PROGRESS_BAR_LAYOUT_PARAM);
        return var1;
    }

    private View createSpace() {
        View var1 = new View(this.getContext());
        var1.setLayoutParams((android.view.ViewGroup.LayoutParams) SPACE_LAYOUT_PARAM);
        return var1;
    }

    private Callback callback(final int index) {
        return (Callback) (new Callback() {
            public void onStartProgress() {
                StoriesProgressView.this.current = index;
            }

            public void onFinishProgress() {
                if (isReverseStart) {
                    if (storiesListener != null) storiesListener.onPrev();
                    if (0 <= (current - 1)) {
                        PausableProgressBar p = (PausableProgressBar) progressBars.get(current - 1);
                        p.setMinWithoutCallback();
                        ((PausableProgressBar) progressBars.get(--current)).startProgress();
                    } else {
                        ((PausableProgressBar) progressBars.get(current)).startProgress();
                    }
                    isReverseStart = false;
                    return;
                }
                int next = current + 1;
                if (next <= (progressBars.size() - 1)) {
                    if (storiesListener != null) storiesListener.onNext();
                    ((PausableProgressBar) progressBars.get(next)).startProgress();
                } else {
                    isComplete = true;
                    if (storiesListener != null) storiesListener.onComplete();
                }
                isSkipStart = false;
            }
        });
    }

    public void setStoriesCountDebug(int storiesCount, int position) {
        this.storiesCount = storiesCount;
        this.position = position;
        this.bindViews();
    }

    public void setStoriesListener(@Nullable StoriesProgressView.StoriesListener storiesListener) {
        this.storiesListener = storiesListener;
    }

    public void skip() {
        if (!this.isSkipStart && !this.isReverseStart) {
            if (!this.isComplete) {
                if (this.current >= 0) {
                    PausableProgressBar p = (PausableProgressBar) this.progressBars.get(this.current);
                    this.isSkipStart = true;
                    p.setMax();
                }
            }
        }
    }

    public void reverse() {
        if (!this.isSkipStart && !this.isReverseStart) {
            if (!this.isComplete) {
                if (this.current >= 0) {
                    PausableProgressBar p = (PausableProgressBar) this.progressBars.get(this.current);
                    this.isReverseStart = true;
                    p.setMin();
                }
            }
        }
    }

    public void setAllStoryDuration(long duration) {
        int i = 0;

        for (int var4 = ((Collection) this.progressBars).size(); i < var4; ++i) {
            ((PausableProgressBar) this.progressBars.get(i)).setDuration(duration);
            ((PausableProgressBar) this.progressBars.get(i)).setCallback(this.callback(i));
        }

    }

    public void startStories() {
        if (this.progressBars.size() > 0) {
            ((PausableProgressBar) this.progressBars.get(0)).startProgress();
        }

    }

    public void startStories(int from) {
        for (int i = 0; i < from; i++) {
            ((PausableProgressBar) progressBars.get(i)).setMaxWithoutCallback();
        }
        ((PausableProgressBar) progressBars.get(from)).startProgress();

    }

    public void destroy() {
        Iterator var2 = this.progressBars.iterator();

        while (var2.hasNext()) {
            PausableProgressBar p = (PausableProgressBar) var2.next();
            p.clear();
        }

    }

    public void abandon() {
        if (this.progressBars.size() > this.current && this.current >= 0) {
            ((PausableProgressBar) this.progressBars.get(this.current)).setMinWithoutCallback();
        }

    }

    public void pause() {
        if (this.current >= 0) {
            ((PausableProgressBar) this.progressBars.get(this.current)).pauseProgress();
        }
    }

    public void resume() {
        if (this.current < 0 && this.progressBars.size() > 0) {
            ((PausableProgressBar) this.progressBars.get(0)).startProgress();
        } else {
            ((PausableProgressBar) this.progressBars.get(this.current)).resumeProgress();
        }
    }

    @NotNull
    public PausableProgressBar getProgressWithIndex(int index) {
        return (PausableProgressBar) this.progressBars.get(index);
    }

    @JvmOverloads
    public StoriesProgressView(@NotNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.progressBars = (List) (new ArrayList());
        this.setOrientation(HORIZONTAL);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StoriesProgressView);
        this.storiesCount = typedArray.getInt(R.styleable.StoriesProgressView_progressCount, 0);
        typedArray.recycle();
        this.bindViews();
    }

    public StoriesProgressView(Context var1, AttributeSet var2, int var3, int var4) {
        this(var1, var2, var3);

        if ((var4 & 2) != 0) {
            var2 = (AttributeSet) null;
        }

        if ((var4 & 4) != 0) {
            var3 = 0;
        }

    }

    @JvmOverloads
    public StoriesProgressView(@NotNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0, 4);
    }

    @JvmOverloads
    public StoriesProgressView(@NotNull Context context) {
        this(context, (AttributeSet) null, 0, 6);
    }

    public interface StoriesListener {
        void onNext();

        void onPrev();

        void onComplete();
    }

    public static class Companion {
        private Companion() {
        }
    }
}