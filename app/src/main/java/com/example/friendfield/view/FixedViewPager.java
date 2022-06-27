package com.example.friendfield.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.Intrinsics;

public final class FixedViewPager extends ViewPager {
    public boolean onInterceptTouchEvent(@NotNull MotionEvent ev) {
        boolean var10000;
        if (this.isFakeDragging()) {
            var10000 = false;
        } else {
            boolean var2;
            try {
                var2 = super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException var4) {
                var2 = false;
            }

            var10000 = var2;
        }

        return var10000;
    }

    @JvmOverloads
    public FixedViewPager(@NotNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    @JvmOverloads
    public FixedViewPager(@NotNull Context context) {
        this(context, (AttributeSet) null);
    }
}

