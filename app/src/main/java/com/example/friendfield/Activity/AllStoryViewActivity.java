package com.example.friendfield.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.friendfield.Adapter.StoryPagerAdapter;
import com.example.friendfield.Fragment.StoryDisplayFragment;
import com.example.friendfield.Model.Story.Story;
import com.example.friendfield.Model.Story.StoryUser;
import com.example.friendfield.MyApplication;
import com.example.friendfield.R;
import com.example.friendfield.Utils.CubeOutTransformer;


import com.example.friendfield.Utils.PageChangeListener;
import com.example.friendfield.Utils.PageViewOperator;
import com.example.friendfield.view.FixedViewPager;
//import com.example.friendfield.Storyv.utils.StoryGenerator;
import com.example.friendfield.view.StoryGenerator;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheUtil;
import com.google.android.exoplayer2.upstream.cache.CacheUtil.ProgressListener;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import kotlin.TypeCastException;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Deferred;
import kotlinx.coroutines.GlobalScope;

public class AllStoryViewActivity extends AppCompatActivity implements PageViewOperator {
    FixedViewPager fixedViewPager;
    StoryPagerAdapter pagerAdapter;
    int currentPage = 0;
    int prevDragPosition = 0;
    public static SparseIntArray progressState = new SparseIntArray();
    public static AllStoryViewActivity.Companion Companion = new AllStoryViewActivity.Companion();
    SimpleCache simpleCache;
    String[][] total_data;
    String[] user_status_data;
    String[] UserName;
    String[] UserProfile;
    int currentPosition;

    public static class Companion {
        @NotNull
        public SparseIntArray getProgressState() {
            return AllStoryViewActivity.progressState;
        }

        private Companion() {
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_story_view);

        fixedViewPager = findViewById(R.id.viewPager);

        total_data = (String[][]) getIntent().getSerializableExtra("Total_Images");
        UserName = getIntent().getStringArrayExtra("UserName");
        UserProfile = getIntent().getStringArrayExtra("UserProfile");
        currentPosition = getIntent().getIntExtra("currentPosition", 0);

        int totalsize = total_data.length;
        user_status_data = new String[totalsize];

        this.setUpPager();
    }

    public void backPageView() {

        if (fixedViewPager.getCurrentItem() > 0) {
            try {
                this.fakeDrag(false);
            } catch (Exception var2) {
            }
        }
    }

    public void nextPageView() {
        int var3 = fixedViewPager.getCurrentItem() + 1;
        PagerAdapter var4 = fixedViewPager.getAdapter();
        if (var3 < (var4 != null ? var4.getCount() : 0)) {
            try {
                this.fakeDrag(true);
            } catch (Exception var2) {
            }
        } else {
            Toast.makeText(this, (CharSequence) "All stories displayed.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void setUpPager() {
//        ArrayList<StoryUser> storyUserList = StoryGenerator.INSTANCE.generateStories();
        ArrayList<StoryUser> storyUserList = StoryGenerator.INSTANCE.generateStories(total_data, UserName, UserProfile, currentPosition);

        this.preLoadStories(storyUserList);
        FragmentManager var10003 = this.getSupportFragmentManager();
        Intrinsics.checkExpressionValueIsNotNull(var10003, "supportFragmentManager");
        this.pagerAdapter = new StoryPagerAdapter(var10003, storyUserList);
        if (pagerAdapter == null) {
            Intrinsics.throwUninitializedPropertyAccessException("pagerAdapter");
        }

        fixedViewPager.setAdapter((PagerAdapter) pagerAdapter);
        fixedViewPager.setCurrentItem(this.currentPage);
        fixedViewPager.setPageTransformer(true, new CubeOutTransformer());

        fixedViewPager.addOnPageChangeListener((ViewPager.OnPageChangeListener) (new PageChangeListener() {
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPage = position;
            }

            public void onPageScrollCanceled() {
                StoryDisplayFragment var10000 = currentFragment();
                if (var10000 != null) {
                    var10000.resumeCurrentStory();
                }
            }
        }));
    }

    private void preLoadStories(ArrayList storyUserList) {
        List imageList = (List) (new ArrayList());
        List videoList = (List) (new ArrayList());
        Iterable $this$forEach$iv = (Iterable) storyUserList;
        Iterator var6 = $this$forEach$iv.iterator();

        while (var6.hasNext()) {
            Object element$iv = var6.next();
            StoryUser storyUser = (StoryUser) element$iv;
            Iterable $this$forEach$iv1 = (Iterable) storyUser.getStories();
            Iterator var12 = $this$forEach$iv1.iterator();

            while (var12.hasNext()) {
                Object element$iv1 = var12.next();
                Story story = (Story) element$iv1;
                if (story.isVideo()) {
                    videoList.add(story.getUrl());
                } else {
                    imageList.add(story.getUrl());
                }
            }
        }

        this.preLoadVideos(videoList);
        this.preLoadImages(imageList);
    }


    private void preLoadVideos(List videoList) {
        Iterable $this$map$iv = (Iterable) videoList;
        Collection destination$iv$iv = (Collection) (new ArrayList(Collections.singleton($this$map$iv)));
        Iterator var7 = $this$map$iv.iterator();

        for (int i = 0; i < videoList.size(); i++) {
            int finalI = i;
            Deferred var12 = BuildersKt.async((CoroutineScope) GlobalScope.INSTANCE, EmptyCoroutineContext.INSTANCE, CoroutineStart.DEFAULT, new Function2<CoroutineScope, Continuation<? super Object>, Object>() {

                @Override
                public Object invoke(CoroutineScope coroutineScope, Continuation<? super Object> continuation) {
//                        Uri dataUri = Uri.parse(data);
                    Uri dataUri = Uri.parse(videoList.get(finalI).toString());
                    DataSpec dataSpec = new DataSpec(dataUri, 0, 500 * 1024, (String) null);
                    DefaultDataSource var10000 = (new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(getApplicationContext(), getString(R.string.app_name)))).createDataSource();
                    Intrinsics.checkExpressionValueIsNotNull(var10000, "DefaultDataSourceFactory…     ).createDataSource()");
                    DataSource dataSource = (DataSource) var10000;
                    ProgressListener listener = new ProgressListener() {
                        @Override
                        public void onProgress(long requestLength, long bytesCached, long newBytesCached) {
                            Double downloadPercentage = (bytesCached * 100.0 / requestLength);
                            Log.e("LLL_preLoadVideos", "downloadPercentage: " + downloadPercentage);
                        }
                    };

                    try {
                        CacheUtil.cache(dataSpec, MyApplication.Companion.getSimpleCache(), CacheUtil.DEFAULT_CACHE_KEY_FACTORY, dataSource, listener, (AtomicBoolean) null);
                    } catch (Exception var8) {
                        var8.printStackTrace();
                    }
                    return null;
                }
            });
            destination$iv$iv.add(var12);
        }

    }

    private void preLoadImages(List imageList) {
        Iterable $this$forEach$iv = (Iterable) imageList;
        Iterator var4 = $this$forEach$iv.iterator();

        while (var4.hasNext()) {
            Object element$iv = var4.next();
            String imageStory = (String) element$iv;
            Glide.with((FragmentActivity) this).load(imageStory).preload();
        }

    }

    private StoryDisplayFragment currentFragment() {
        StoryPagerAdapter var10000 = this.pagerAdapter;
        if (var10000 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("pagerAdapter");
        }

        FixedViewPager var10001 = fixedViewPager;
        Intrinsics.checkExpressionValueIsNotNull(var10001, "viewPager");
        Fragment var1 = var10000.findFragmentByPosition((ViewPager) var10001, this.currentPage);
        if (var1 == null) {
            throw new TypeCastException("null cannot be cast to non-null type com.c2m.storyviewer.screen.StoryFragment");
        } else {
            return (StoryDisplayFragment) var1;
        }
    }

    private void fakeDrag(boolean forward) {
        if (this.prevDragPosition == 0 && fixedViewPager.beginFakeDrag()) {

            int[] var10000 = new int[]{0, 0};
            var10000[1] = fixedViewPager.getWidth();
            ValueAnimator var2 = ValueAnimator.ofInt(var10000);
            var2.setDuration(400L);
            var2.setInterpolator((TimeInterpolator) (new FastOutSlowInInterpolator()));
            var2.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    var2.removeAllUpdateListeners();
                    if (fixedViewPager.isFakeDragging()) {
                        fixedViewPager.endFakeDrag();
                    }
                    prevDragPosition = 0;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    var2.removeAllUpdateListeners();
                    if (fixedViewPager.isFakeDragging()) {
                        fixedViewPager.endFakeDrag();
                    }
                    prevDragPosition = 0;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            var2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (fixedViewPager.isFakeDragging()) {
                        Object var4 = var2.getAnimatedValue();
                        if (var4 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type kotlin.Int");
                        } else {
                            int dragPosition = (Integer) var4;
                            float dragOffset = (float) ((dragPosition - prevDragPosition) * (forward ? -1 : 1));
                            prevDragPosition = dragPosition;
                            fixedViewPager.fakeDragBy(dragOffset);
                        }
                    }
                }
            });
            var2.start();
        }
    }
}