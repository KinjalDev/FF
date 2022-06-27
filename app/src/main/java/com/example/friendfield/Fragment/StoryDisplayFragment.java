package com.example.friendfield.Fragment;

import static com.google.android.exoplayer2.upstream.DataSource.*;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.format.DateFormat;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.example.friendfield.Activity.AllStoryViewActivity;
import com.example.friendfield.Model.Story.Story;
import com.example.friendfield.Model.Story.StoryUser;
import com.example.friendfield.MyApplication;
import com.example.friendfield.R;
import com.example.friendfield.Utils.PageViewOperator;
import com.example.friendfield.view.OnSwipeTouchListener;
import com.example.friendfield.view.PausableProgressBar;
import com.example.friendfield.view.StoriesProgressView;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.database.DatabaseProvider;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheEvictor;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public class StoryDisplayFragment extends Fragment implements StoriesProgressView.StoriesListener {

    private SimpleExoPlayer simpleExoPlayer;
    private Factory mediaDataSourceFactory;
    private PageViewOperator pageViewOperator;
    private int counter;
    private long pressTime;
    private long limit = 500L;
    private boolean onResumeCalled;
    private boolean onVideoPrepared;

    PlayerView storyDisplayVideo;
    AppCompatImageView storyDisplayImage;
    ProgressBar storyDisplayVideoProgress;
    View previous;
    View next;
    StoriesProgressView storiesProgressView;
    LinearLayout storyDisplayProfile;
    CircleImageView storyDisplayProfilePicture;
    TextView storyDisplayNick;
    TextView storyDisplayTime;
    ConstraintLayout storyOverlay;
    EditText txt_chating;
    ImageView iv_send;

    public static StoryDisplayFragment newInstance(int position, @NotNull StoryUser story) {
        Intrinsics.checkParameterIsNotNull(story, "story");
        StoryDisplayFragment var3 = new StoryDisplayFragment();
        Bundle var8 = new Bundle();
        var8.putInt("EXTRA_POSITION", position);
        var8.putParcelable("EXTRA_STORY_USER", (Parcelable) story);
        var3.setArguments(var8);
        return var3;
    }

    private int getPosition() {
        return (getArguments() != null ? StoryDisplayFragment.this.getArguments().getInt("EXTRA_POSITION") : 0);
    }

    private StoryUser getStoryUser() {
        return StoryDisplayFragment.this.getArguments() != null ? (StoryUser) StoryDisplayFragment.this.getArguments().getParcelable("EXTRA_STORY_USER") : null;
    }

    private ArrayList getStories() {
        return StoryDisplayFragment.this.getStoryUser().getStories();
    }

    @Nullable
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_story_display, container, false);
        storyDisplayVideo = view.findViewById(R.id.storyDisplayVideo);
        storyDisplayImage = view.findViewById(R.id.storyDisplayImage);
        storyDisplayVideoProgress = view.findViewById(R.id.storyDisplayVideoProgress);
        previous = view.findViewById(R.id.previous);
        next = view.findViewById(R.id.next);
        storiesProgressView = view.findViewById(R.id.storiesProgressView);
        storyDisplayProfile = view.findViewById(R.id.storyDisplayProfile);
        storyDisplayProfilePicture = view.findViewById(R.id.storyDisplayProfilePicture);
        storyDisplayNick = view.findViewById(R.id.storyDisplayNick);
        storyDisplayTime = view.findViewById(R.id.storyDisplayTime);
        storyOverlay = view.findViewById(R.id.storyOverlay);
        txt_chating = view.findViewById(R.id.txt_chating);
        iv_send = view.findViewById(R.id.iv_send);

        return view;
    }

    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        super.onViewCreated(view, savedInstanceState);
        storyDisplayVideo.setUseController(false);
        this.updateStory();
        this.setUpUi();
    }

    public void onAttach(@NotNull Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        super.onAttach(context);
        this.pageViewOperator = (PageViewOperator) context;
    }

    public void onStart() {
        super.onStart();
        this.counter = this.restorePosition();
    }

    public void onResume() {
        super.onResume();
        try {
            this.onResumeCalled = true;
            if (((Story) this.getStories().get(this.counter)).isVideo() && !this.onVideoPrepared) {
                if (simpleExoPlayer != null) {
                    simpleExoPlayer.setPlayWhenReady(false);
                }

            } else {
                if (simpleExoPlayer != null) {
                    simpleExoPlayer.seekTo(5L);
                }

                if (simpleExoPlayer != null) {
                    simpleExoPlayer.setPlayWhenReady(true);
                }

                if (this.counter == 0) {
                    if (storiesProgressView != null) {
                        storiesProgressView.startStories();
                    }
                } else {
                    SparseIntArray var10001 = AllStoryViewActivity.Companion.getProgressState();
                    Bundle var10002 = this.getArguments();
                    this.counter = var10001.get(var10002 != null ? var10002.getInt("EXTRA_POSITION") : 0);
                    if (storiesProgressView != null) {
                        storiesProgressView.startStories(this.counter);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onPause() {
        super.onPause();
        try {
            SimpleExoPlayer var10000 = this.simpleExoPlayer;
            if (var10000 != null) {
                var10000.setPlayWhenReady(false);
            }

            if (storiesProgressView != null) {
                storiesProgressView.abandon();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onComplete() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
        }

        if (pageViewOperator != null) {
            pageViewOperator.nextPageView();
        }
    }

    public void onPrev() {
        if (counter - 1 < 0) return;
        --counter;
        savePosition(counter);
        updateStory();
    }

    public void onNext() {
        if (this.getStories().size() <= counter + 1) {
            return;
        }
        ++counter;
        savePosition(counter);
        updateStory();
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (this.simpleExoPlayer != null) {
            this.simpleExoPlayer.release();
        }

    }

    private void updateStory() {
        if (this.simpleExoPlayer != null) {
            this.simpleExoPlayer.stop();
        }

        if (((Story) this.getStories().get(this.counter)).isVideo()) {
            storyDisplayVideo.setVisibility(View.VISIBLE);
            storyDisplayImage.setVisibility(View.GONE);
            storyDisplayVideoProgress.setVisibility(View.VISIBLE);

            this.initializePlayer();

        } else {
            storyDisplayVideo.setVisibility(View.GONE);
            storyDisplayVideoProgress.setVisibility(View.GONE);
            storyDisplayImage.setVisibility(View.VISIBLE);

            Glide.with(getContext()).load(((Story) this.getStories().get(this.counter)).getUrl()).into(storyDisplayImage);
        }

        Calendar var2 = Calendar.getInstance(Locale.ENGLISH);
        var2.setTimeInMillis(((Story) this.getStories().get(this.counter)).getStoryDate());
        storyDisplayTime.setText((CharSequence) DateFormat.format((CharSequence) "MM-dd-yyyy HH:mm:ss", var2).toString());
    }


    private void initializePlayer() {
        if (this.simpleExoPlayer == null) {
            this.simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this.requireContext());
        } else {
            if (this.simpleExoPlayer != null) {
                this.simpleExoPlayer.release();
            }
            this.simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this.requireContext());
        }

        this.mediaDataSourceFactory = (Factory) (new CacheDataSourceFactory(MyApplication.Companion.getSimpleCache(), (Factory) (new DefaultHttpDataSourceFactory(Util.getUserAgent(this.getContext(), Util.getUserAgent(this.requireContext(), this.getString(R.string.app_name)))))));
        com.google.android.exoplayer2.source.ProgressiveMediaSource.Factory var2 = new com.google.android.exoplayer2.source.ProgressiveMediaSource.Factory(mediaDataSourceFactory);

        if (this.mediaDataSourceFactory == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mediaDataSourceFactory");
        }

        ProgressiveMediaSource mediaSource = var2.createMediaSource(Uri.parse(((Story) this.getStories().get(this.counter)).getUrl()));
        if (this.simpleExoPlayer != null) {
            this.simpleExoPlayer.prepare((MediaSource) mediaSource, false, false);
        }

        if (this.onResumeCalled) {
            if (this.simpleExoPlayer != null) {
                this.simpleExoPlayer.setPlayWhenReady(true);
            }
        }

        storyDisplayVideo.setShutterBackgroundColor(Color.BLACK);
        storyDisplayVideo.setPlayer((Player) this.simpleExoPlayer);
        if (this.simpleExoPlayer != null) {
            this.simpleExoPlayer.addListener((Player.EventListener) (new Player.EventListener() {

                public void onPlayerError(@Nullable ExoPlaybackException error) {
                    Player.EventListener.super.onPlayerError(error);
                    storyDisplayVideoProgress.setVisibility(View.GONE);
                    if (StoryDisplayFragment.this.counter == StoryDisplayFragment.this.getStories().size() - 1) {
                        PageViewOperator var2 = StoryDisplayFragment.this.pageViewOperator;
                        if (var2 != null) {
                            var2.nextPageView();
                        }
                    } else {
                        if (storiesProgressView != null) {
                            storiesProgressView.skip();
                        }
                    }
                }

                public void onLoadingChanged(boolean isLoading) {
                    Player.EventListener.super.onLoadingChanged(isLoading);
                    if (isLoading) {
                        storyDisplayVideoProgress.setVisibility(View.VISIBLE);
                        StoryDisplayFragment.this.pressTime = System.currentTimeMillis();
                        StoryDisplayFragment.this.pauseCurrentStory();

                    } else {
                        storyDisplayVideoProgress.setVisibility(View.GONE);
                        if (storiesProgressView != null) {
                            PausableProgressBar var3 = storiesProgressView.getProgressWithIndex(StoryDisplayFragment.this.counter);
                            if (var3 != null) {
                                SimpleExoPlayer var10001 = StoryDisplayFragment.this.simpleExoPlayer;
                                var3.setDuration(var10001 != null ? var10001.getDuration() : 8000L);
                            }
                        }

                        StoryDisplayFragment.this.onVideoPrepared = true;
                        StoryDisplayFragment.this.resumeCurrentStory();
                    }
                }
            }));
        }

    }

    private void setUpUi() {
        OnSwipeTouchListener swipeTouchListener = new OnSwipeTouchListener(getActivity()) {
            public void onSwipeTop() {
                Toast.makeText((Context) StoryDisplayFragment.this.getActivity(), (CharSequence) "onSwipeTop", Toast.LENGTH_LONG).show();
            }

            public void onSwipeBottom() {
                Toast.makeText((Context) StoryDisplayFragment.this.getActivity(), (CharSequence) "onSwipeBottom", Toast.LENGTH_LONG).show();
            }

            public void onClick(@NotNull View view) {
                Intrinsics.checkParameterIsNotNull(view, "view");
                StoriesProgressView var3;
                if (Intrinsics.areEqual(view, next)) {
                    if (StoryDisplayFragment.this.counter == StoryDisplayFragment.this.getStories().size() - 1) {

                        if (pageViewOperator != null) {
                            pageViewOperator.nextPageView();
                        }
                    } else {
                        if (storiesProgressView != null) {
                            storiesProgressView.skip();
                        }
                    }
                } else if (Intrinsics.areEqual(view, previous)) {
                    if (StoryDisplayFragment.this.counter == 0) {

                        if (pageViewOperator != null) {
                            pageViewOperator.backPageView();
                        }
                    } else {
                        if (storiesProgressView != null) {
                            storiesProgressView.reverse();
                        }
                    }
                }

            }

            public void onLongClick() {
                StoryDisplayFragment.this.hideStoryOverlay();
            }

            public boolean onTouchView(@NotNull View view, @NotNull MotionEvent event) {
                super.onTouchView(view, event);
                Intrinsics.checkParameterIsNotNull(view, "view");
                Intrinsics.checkParameterIsNotNull(event, "event");
                switch (event.getAction()) {
                    case 0:
                        StoryDisplayFragment.this.pressTime = System.currentTimeMillis();
                        StoryDisplayFragment.this.pauseCurrentStory();
                        return false;
                    case 1:
                        StoryDisplayFragment.this.showStoryOverlay();
                        StoryDisplayFragment.this.resumeCurrentStory();
                        return StoryDisplayFragment.this.limit < System.currentTimeMillis() - StoryDisplayFragment.this.pressTime;
                    default:
                        return false;
                }
            }
        };
        FragmentActivity var10003 = this.getActivity();
        if (var10003 == null) {
            Intrinsics.throwNpe();
        }

        previous.setOnTouchListener((View.OnTouchListener) swipeTouchListener);
        next.setOnTouchListener((View.OnTouchListener) swipeTouchListener);

        if (storiesProgressView != null) {
            int var10001 = this.getStories().size();
            Bundle var10002 = this.getArguments();
            storiesProgressView.setStoriesCountDebug(var10001, var10002 != null ? var10002.getInt("EXTRA_POSITION") : -1);
        }

        if (storiesProgressView != null) {
            storiesProgressView.setAllStoryDuration(4000L);
        }

        if (storiesProgressView != null) {
            storiesProgressView.setStoriesListener((StoriesProgressView.StoriesListener) this);
        }

        ((RequestBuilder) Glide.with(getContext()).load(this.getStoryUser().getProfilePicUrl())).into(storyDisplayProfilePicture);
        storyDisplayNick.setText((CharSequence) this.getStoryUser().getUsername());
    }

    private void showStoryOverlay() {
        if (storyOverlay == null || storyOverlay.getAlpha() != 0F) return;

        storyOverlay.animate()
                .setDuration(100)
                .alpha(1F)
                .start();

    }

    private void hideStoryOverlay() {
        if (storyOverlay == null || storyOverlay.getAlpha() != 1F) return;

        storyOverlay.animate()
                .setDuration(200)
                .alpha(0F)
                .start();

    }

    private void savePosition(int pos) {
        AllStoryViewActivity.Companion.getProgressState().put(this.getPosition(), pos);
    }

    private int restorePosition() {
        return AllStoryViewActivity.Companion.getProgressState().get(this.getPosition());
    }

    public void pauseCurrentStory() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.setPlayWhenReady(false);
        }

        if (storiesProgressView != null) {
            storiesProgressView.pause();
        }
    }

    public void resumeCurrentStory() {
        if (this.onResumeCalled) {
            SimpleExoPlayer var10000 = this.simpleExoPlayer;
            if (var10000 != null) {
                var10000.setPlayWhenReady(true);
            }

            this.showStoryOverlay();
            if (storiesProgressView != null) {
                storiesProgressView.resume();
            }
        }
    }
}
