package com.example.friendfield.Utils;

import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_DRAGGING;
import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_IDLE;
import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_SETTLING;

import android.os.Handler;
import android.util.Log;

import androidx.viewpager.widget.ViewPager;

public abstract class PageChangeListener implements ViewPager.OnPageChangeListener {
    private int pageBeforeDragging = 0;
    private int currentPage = 0;
    private static long DEBOUNCE_TIMES = 500L;
    private long lastTime = DEBOUNCE_TIMES + 1L;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d("onPageScrollState", "onPageSelected(): position(" + position + ')');
        this.currentPage = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case SCROLL_STATE_IDLE:
                Log.d("onPageScrollState", " SCROLL_STATE_IDLE");
                long now = System.currentTimeMillis();
                if (now - this.lastTime < 500L) {
                    return;
                }

                this.lastTime = now;
                (new Handler()).postDelayed((Runnable) (new Runnable() {
                    public final void run() {
                        if (PageChangeListener.this.pageBeforeDragging == PageChangeListener.this.currentPage) {
                            PageChangeListener.this.onPageScrollCanceled();
                        }

                    }
                }), 300L);
                break;
            case SCROLL_STATE_DRAGGING:
                Log.d("onPageScrollState", " SCROLL_STATE_DRAGGING");
                this.pageBeforeDragging = this.currentPage;
                break;
            case SCROLL_STATE_SETTLING:
                Log.d("onPageScrollState", " SCROLL_STATE_SETTLING");
        }
    }

    public abstract void onPageScrollCanceled();

}
