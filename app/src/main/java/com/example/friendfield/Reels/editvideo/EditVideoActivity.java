package com.example.friendfield.Reels.editvideo;

import static android.opengl.GLSurfaceView.RENDERMODE_WHEN_DIRTY;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bokecc.camerafilter.drawer.VideoDrawer;
import com.bokecc.shortvideo.model.StickerSet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.friendfield.R;
import com.example.friendfield.Reels.CreateReelsActivity;
import com.example.friendfield.Reels.cutvideo.TimeSliderView;
import com.example.friendfield.Reels.cutvideo.VideoBean;
import com.example.friendfield.Reels.model.SpecialEffectInfo;
import com.example.friendfield.Reels.model.StickerRes;
import com.example.friendfield.Reels.util.FileUtils;
import com.example.friendfield.Reels.util.MultiUtils;
import com.example.friendfield.Reels.widget.BeautyDialog;
import com.example.friendfield.Reels.widget.BubbleTextDialog;
import com.example.friendfield.Reels.widget.DurView;
import com.example.friendfield.Reels.widget.EditBubbleTextDialog;
import com.example.friendfield.Reels.widget.LocalFilterDialog;
import com.example.friendfield.Reels.widget.MusicDialog;
import com.example.friendfield.Reels.widget.StickerDialog;
import com.example.friendfield.Reels.widget.onEditBubbleText;
import com.example.friendfield.Reels.widget.sticker.BitmapStickerIcon;
import com.example.friendfield.Reels.widget.sticker.DeleteIconEvent;
import com.example.friendfield.Reels.widget.sticker.DrawableSticker;
import com.example.friendfield.Reels.widget.sticker.Sticker;
import com.example.friendfield.Reels.widget.sticker.StickerIconEvent;
import com.example.friendfield.Reels.widget.sticker.StickerView;
import com.example.friendfield.Reels.widget.sticker.TextSticker;
import com.example.friendfield.Reels.widget.sticker.TimeIconEvent;
import com.example.friendfield.Reels.widget.sticker.ZoomIconEvent;

public class EditVideoActivity extends Activity implements MediaPlayer.OnPreparedListener, GLSurfaceView.Renderer, View.OnClickListener {
    private String videoPath, originalPath, bacMusicPath, recordPath, effectPath;
    private GLSurfaceView gl_view;
    private MediaPlayer mediaPlayer;
    private VideoDrawer mDrawer;
    private Activity activity;
    private int videoWidth, videoHeight, windowWidth, windowHeight, afterChangeVideoHeight;
    private LinearLayout ll_edit_operation, ll_edit_filter, ll_edit_beauty, ll_edit_music, ll_edit_volume,
            ll_origin_volume, ll_music_volume, ll_cut_music, ll_sticker, ll_text, ll_set_sticker_time, ll_special_effect;
    private int currentBeautyWhiteValue = 0, currentBeautySkinValue = 0, filterPosition = 0;
    private int videoDuration, musicDuration;
    private boolean isRecord = false, isClickNext = false;
    private Timer timer;
    private VideoTask videoTask;
    //Video angle information
    private int rotation = 0, originalRotation = 0;
    private TextView tv_next, tv_music_volume_value, tv_origin_volume_value, tv_music_start_time, tv_music_time,
            tv_cut_music, tv_music_volume, tv_sticker_start_show_time, tv_sticker_end_show_time,
            tv_sticker_show_time, tv_play_time, tv_total_time;
    private boolean isPrepared = false, isMusicPrepared = false;
    //soundtrack player
    private MediaPlayer musicPlayer;
    private SeekBar sb_music_volume, sb_origin_volume, sb_cut_music, sb_play_progress;
    private int defaultOriginVolume = 50, defaultMusicVolume = 50, startCutMusicPosition = 0, cutMusicDuration = 0,
            tempOriginVolume, tempMusicVolume, tempStartCutMusicPosition, tempCutMusicDuration;
    private float musicVolume, originVolume, tempOriginValue, tempMusicValue;
    private RelativeLayout rl_edit_video;
    private long currentPosition = 0;
    private double zoomRate = 1.0;
    private DurView dv_sticker_show_time;
    private VideoBean videoBean;
    private int stickerStartShowTime = 0, stickerEndShowTime = 0, defaultEndShowTime = 0;
    private ImageView iv_close_set_sticker_time, iv_confirm_set_sticker_time, iv_back, iv_close_volume;
    private List<Sticker> stickers;
    private List<StickerView> stickerViews;
    private List<StickerSet> stickerSets;
    private int stickerTag = 0;
    private Sticker currentSticker;
    private FrameLayout fl_sticker;
    private Surface surface;
    private boolean isFromCombineImages = false;
    private SurfaceTexture surfaceTexture;
    private ArrayList<SpecialEffectInfo> effects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video);
        activity = this;
        initView();
        initPlay();
        initGl();

    }

    private void initPlay() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setScreenOnWhilePlaying(true);
        mediaPlayer.setLooping(true);
        try {
            mediaPlayer.setDataSource(videoPath);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        videoPath = getIntent().getStringExtra("videoPath");
        recordPath = videoPath;

        isRecord = getIntent().getBooleanExtra("isRecord", false);
        originalPath = getIntent().getStringExtra("originalPath");
        rotation = getIntent().getIntExtra("rotation", 0);
        originalRotation = getIntent().getIntExtra("originalRotation", 0);
        isFromCombineImages = getIntent().getBooleanExtra("combineImages", false);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        stickers = new ArrayList<>();
        stickerViews = new ArrayList<>();
        stickerSets = new ArrayList<>();

        gl_view = findViewById(R.id.gl_view);
        fl_sticker = findViewById(R.id.fl_sticker);
        ll_edit_operation = findViewById(R.id.ll_edit_operation);

        ll_edit_filter = findViewById(R.id.ll_edit_filter);
        ll_edit_filter.setOnClickListener(this);

        ll_edit_beauty = findViewById(R.id.ll_edit_beauty);
        ll_edit_beauty.setOnClickListener(this);

        windowWidth = MultiUtils.getScreenWidth(activity);
        windowHeight = MultiUtils.getScreenHeight(activity);

        tv_next = findViewById(R.id.tv_next);
        tv_next.setOnClickListener(this);

        ll_edit_music = findViewById(R.id.ll_edit_music);
        ll_edit_music.setOnClickListener(this);

        ll_edit_volume = findViewById(R.id.ll_edit_volume);
        ll_edit_volume.setOnClickListener(this);

        ll_special_effect = findViewById(R.id.ll_special_effect);
        ll_special_effect.setOnClickListener(this);

        tv_music_volume_value = findViewById(R.id.tv_music_volume_value);
        tv_origin_volume_value = findViewById(R.id.tv_origin_volume_value);
        sb_music_volume = findViewById(R.id.sb_music_volume);
        sb_origin_volume = findViewById(R.id.sb_origin_volume);
        sb_cut_music = findViewById(R.id.sb_cut_music);
        sb_play_progress = findViewById(R.id.sb_play_progress);
        tv_music_start_time = findViewById(R.id.tv_music_start_time);
        tv_music_time = findViewById(R.id.tv_music_time);
        adjustMusic();
        tv_play_time = findViewById(R.id.tv_play_time);
        tv_total_time = findViewById(R.id.tv_total_time);
        tv_cut_music = findViewById(R.id.tv_cut_music);
        tv_cut_music.setOnClickListener(this);
        tv_music_volume = findViewById(R.id.tv_music_volume);
        tv_music_volume.setOnClickListener(this);
        ll_origin_volume = findViewById(R.id.ll_origin_volume);
        ll_music_volume = findViewById(R.id.ll_music_volume);
        ll_cut_music = findViewById(R.id.ll_cut_music);
        rl_edit_video = findViewById(R.id.rl_edit_video);
        rl_edit_video.setOnClickListener(this);

        ll_sticker = findViewById(R.id.ll_sticker);
        ll_sticker.setOnClickListener(this);
        ll_text = findViewById(R.id.ll_text);
        ll_text.setOnClickListener(this);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        iv_close_volume = findViewById(R.id.iv_confirm_volume);
        iv_close_volume.setOnClickListener(this);

        ll_set_sticker_time = findViewById(R.id.ll_set_sticker_time);
        dv_sticker_show_time = findViewById(R.id.dv_sticker_show_time);
        videoBean = MultiUtils.getLocalVideoInfo(videoPath);

        dv_sticker_show_time.setMediaFileInfo(videoBean);
        tv_sticker_start_show_time = findViewById(R.id.tv_sticker_start_show_time);
        tv_sticker_end_show_time = findViewById(R.id.tv_sticker_end_show_time);
        tv_sticker_show_time = findViewById(R.id.tv_sticker_show_time);
        iv_close_set_sticker_time = findViewById(R.id.iv_close_set_sticker_time);
        iv_close_set_sticker_time.setOnClickListener(this);

        iv_confirm_set_sticker_time = findViewById(R.id.iv_confirm_set_sticker_time);
        iv_confirm_set_sticker_time.setOnClickListener(this);

        editStickerShowTime();

        sb_play_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                if (isPrepared) {
                    mediaPlayer.seekTo(progress);
                }
            }
        });

    }

    private void editStickerShowTime() {
        dv_sticker_show_time.setRangeChangeListener(new DurView.IOnRangeChangeListener() {
            @Override
            public void onCutViewDown() {

            }

            @Override
            public void onCutViewUp(int startTime, int endTime) {
                stickerStartShowTime = startTime / 1000;
                stickerEndShowTime = endTime / 1000;
                tv_sticker_start_show_time.setText(stickerStartShowTime + "s");
                tv_sticker_end_show_time.setText(stickerEndShowTime + "s");
                mediaPlayer.seekTo(startTime);
                int selectedTime = stickerEndShowTime - stickerStartShowTime;
                tv_sticker_show_time.setText("Sticker duration selected " + selectedTime + "s");

            }

            @Override
            public void onCutViewPreview(int previewTime) {
                mediaPlayer.seekTo(previewTime);
            }
        });
    }

    private void adjustMusic() {
        sb_music_volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_music_volume_value.setText(progress + "");
                tempMusicVolume = progress;
                tempMusicValue = MultiUtils.calFloat(1, tempMusicVolume, 100);
                musicPlayer.setVolume(tempMusicValue, tempMusicValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sb_origin_volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_origin_volume_value.setText(progress + "");
                tempOriginVolume = progress;
                tempOriginValue = MultiUtils.calFloat(1, tempOriginVolume, 100);
                mediaPlayer.setVolume(tempOriginValue, tempOriginValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sb_cut_music.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int maxStartCutMusicPosition = musicDuration - videoDuration;
                if (maxStartCutMusicPosition < 0) {
                    maxStartCutMusicPosition = 0;
                }
                if (progress > maxStartCutMusicPosition) {
                    tempCutMusicDuration = musicDuration - progress;
                }
                tv_music_start_time.setText(MultiUtils.millsecondsToMinuteSecondStr(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tempStartCutMusicPosition = seekBar.getProgress();
                musicPlayer.seekTo(seekBar.getProgress());
            }
        });
    }

    private void initGl() {
        gl_view.setEGLContextClientVersion(2);
        gl_view.setRenderer(this);
        gl_view.setRenderMode(RENDERMODE_WHEN_DIRTY);
        gl_view.setPreserveEGLContextOnPause(false);
        gl_view.setCameraDistance(100);
        mDrawer = new VideoDrawer(activity, getResources());
        mDrawer.setRotation(rotation);

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isPrepared = true;
        videoDuration = mediaPlayer.getDuration();
        startVideoTimer();
        stickerEndShowTime = videoDuration / 1000;
        defaultEndShowTime = videoDuration / 1000;
        cutMusicDuration = videoDuration;
        tempCutMusicDuration = videoDuration;
        videoWidth = mediaPlayer.getVideoWidth();
        videoHeight = mediaPlayer.getVideoHeight();
        zoomRate = MultiUtils.div(4, videoWidth, windowWidth);
        afterChangeVideoHeight = videoHeight * windowWidth / videoWidth;
        mediaPlayer.start();
        //Set the width and height of gl_view
        ViewGroup.LayoutParams videoParams = gl_view.getLayoutParams();
        videoParams.width = windowWidth;
        videoParams.height = afterChangeVideoHeight;
        gl_view.setLayoutParams(videoParams);

        //Set the width and height of fl_sticker
        ViewGroup.LayoutParams flParams = fl_sticker.getLayoutParams();
        flParams.width = windowWidth;
        flParams.height = afterChangeVideoHeight;
        fl_sticker.setLayoutParams(flParams);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mDrawer.onSurfaceCreated(gl, config);
        surfaceTexture = mDrawer.getSurfaceTexture();
        surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                gl_view.requestRender();
            }
        });

        surface = new Surface(surfaceTexture);
        mediaPlayer.setSurface(surface);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mDrawer.onSurfaceChanged(gl, windowWidth, afterChangeVideoHeight);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        mDrawer.onDrawFrame(gl);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.tv_next:
                next();
                break;

            case R.id.iv_back:
                back();
                finish();
                break;
            case R.id.iv_confirm_volume:
                defaultOriginVolume = tempOriginVolume;
                setOriginVolume();
                defaultMusicVolume = tempMusicVolume;
                setMusicVolume();
                startCutMusicPosition = tempStartCutMusicPosition;
                cutMusicDuration = tempCutMusicDuration;
                ll_edit_volume.setVisibility(View.GONE);
                ll_edit_operation.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_edit_beauty:
                editBeauty();
                break;
            case R.id.ll_edit_filter:
                editFilter();
                break;
            case R.id.ll_edit_music:
                editMusic();
                break;
            case R.id.tv_cut_music:
                tv_cut_music.setTextColor(getResources().getColor(R.color.white));
                tv_music_volume.setTextColor(getResources().getColor(R.color.sixtyWhite));
                ll_origin_volume.setVisibility(View.GONE);
                ll_music_volume.setVisibility(View.GONE);
                ll_cut_music.setVisibility(View.VISIBLE);
                break;

            case R.id.tv_music_volume:
                tv_music_volume.setTextColor(getResources().getColor(R.color.white));
                tv_cut_music.setTextColor(getResources().getColor(R.color.sixtyWhite));
                ll_origin_volume.setVisibility(View.VISIBLE);
                ll_music_volume.setVisibility(View.VISIBLE);
                ll_cut_music.setVisibility(View.GONE);
                break;
            case R.id.rl_edit_video:
                hideEditMusic();
                break;
            case R.id.ll_sticker:
                editSticker();
                break;
            case R.id.ll_text:
                addBubbleText();
                break;
            case R.id.iv_close_set_sticker_time:
                ll_set_sticker_time.setVisibility(View.GONE);
                ll_edit_operation.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_confirm_set_sticker_time:
                confirmSetStickerTime();
                break;

            case R.id.ll_special_effect:
                Intent intent = new Intent(activity, EditSpecialEffectActivity.class);
                intent.putExtra("videoPath", recordPath);
                if (effects != null) {
                    intent.putParcelableArrayListExtra("effects", effects);
                    effectPath = videoPath;
                }
                startActivityForResult(intent, 1);
                break;
        }
    }

    //Click Next
    private void next() {
        isClickNext = true;
        float beautySkinValue = mDrawer.getBeautySkinValue();
        float beautyWhiteValue = mDrawer.getBeautyWhiteValue();

        if (stickerViews.size() > 0) {
            for (int i = 0; i < stickerViews.size(); i++) {
                StickerView stickerView = stickerViews.get(i);
                int stickerTag = stickerView.getStickerTag();
                Sticker sticker = getStickerByTag(stickerTag);

                StickerSet stickerSet = new StickerSet();
                stickerSet.setStartShowTime(sticker.getStartShowTime());
                stickerSet.setEndShowTime(sticker.getEndShowTime());
                int xOffSet = (int) (stickerView.getMinXOffSet() * zoomRate);
                int yOffSet = (int) (stickerView.getMinYOffSet() * zoomRate);
                if (xOffSet < 0) {
                    xOffSet = 0;
                }
                if (yOffSet < 0) {
                    yOffSet = 0;
                }
                stickerSet.setxOffSet(xOffSet);
                stickerSet.setyOffSet(yOffSet);

                File stickerFile = MultiUtils.getNewFile(activity, "sticker", stickerTag + "");
                if (stickerFile != null) {
                    stickerView.save(stickerFile, zoomRate);
                    String stickerPath = stickerFile.getAbsolutePath();
                    if (FileUtils.isFileExist(stickerPath)) {
                        stickerSet.setStickerPath(stickerPath);
                        stickerSets.add(stickerSet);
                    }
                }

            }
        }

        Intent uploadIntent = new Intent(activity, UploadActivity.class);
        uploadIntent.putExtra("videoPath", videoPath);
        uploadIntent.putExtra("filterPosition", filterPosition);
        uploadIntent.putExtra("skinValue", currentBeautySkinValue);
        uploadIntent.putExtra("whiteValue", currentBeautyWhiteValue);
        uploadIntent.putExtra("beautySkinValue", beautySkinValue);
        uploadIntent.putExtra("beautyWhiteValue", beautyWhiteValue);
        uploadIntent.putExtra("videoDuration", videoDuration);
        uploadIntent.putExtra("bacMusicPath", bacMusicPath);
        uploadIntent.putExtra("startCutMusicPosition", startCutMusicPosition);
        uploadIntent.putExtra("cutMusicDuration", cutMusicDuration);
        uploadIntent.putExtra("originVolume", originVolume);
        uploadIntent.putExtra("musicVolume", musicVolume);
        uploadIntent.putExtra("stickerSets", (Serializable) stickerSets);
        startActivity(uploadIntent);
        stickerSets.clear();
    }

    //Edit Beauty
    private void editBeauty() {
        BeautyDialog beautyDialog = new BeautyDialog(activity, currentBeautyWhiteValue, currentBeautySkinValue, new BeautyDialog.OnBeauty() {
            @Override
            public void getBeautyWhiteValue(int value) {
                currentBeautyWhiteValue = value;
                mDrawer.setBeautyWhiteLevel(value);
            }

            @Override
            public void getBeautySkinValue(int value) {
                currentBeautySkinValue = value;
                mDrawer.setBeautySkinLevel(value);
            }
        });
        beautyDialog.show();
        ll_edit_operation.setVisibility(View.GONE);
        beautyDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ll_edit_operation.setVisibility(View.VISIBLE);
            }
        });
    }

    //Edit video filters
    private void editFilter() {

        LocalFilterDialog localFilterDialog = new LocalFilterDialog(activity, filterPosition, new LocalFilterDialog.OnSelectLocalFilter() {
            @Override
            public void selectLocalFilter(int filterPos) {
                filterPosition = filterPos;
                mDrawer.switchDrawerFilter(filterPos);
            }
        });

        localFilterDialog.show();
        ll_edit_operation.setVisibility(View.GONE);
        localFilterDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ll_edit_operation.setVisibility(View.VISIBLE);
            }
        });
    }

    //edit music
    private void editMusic() {
        mediaPlayer.pause();
        MusicDialog musicDialog = new MusicDialog(activity, bacMusicPath, new MusicDialog.OnSelectMusic() {
            @Override
            public void selectMusic(String musicPath) {
                bacMusicPath = musicPath;
                ll_edit_operation.setVisibility(View.GONE);
                ll_edit_volume.setVisibility(View.VISIBLE);

                if (musicPlayer == null) {
                    musicPlayer = new MediaPlayer();
                }

                try {

                    musicPlayer.reset();
                    musicPlayer.setDataSource(musicPath);
                    musicPlayer.setLooping(true);
                    musicPlayer.prepareAsync();
                    musicPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            isMusicPrepared = true;
                            musicPlayer.start();
                            tv_music_volume_value.setText(defaultMusicVolume + "");
                            sb_music_volume.setProgress(defaultMusicVolume);
                            tv_origin_volume_value.setText(defaultOriginVolume + "");
                            sb_origin_volume.setProgress(defaultOriginVolume);
                            setMusicVolume();
                            setOriginVolume();
                            musicDuration = musicPlayer.getDuration();
                            tv_music_start_time.setText(MultiUtils.millsecondsToMinuteSecondStr(0));
                            tv_music_time.setText(MultiUtils.millsecondsToMinuteSecondStr(musicDuration));
                            startCutMusicPosition = 0;
                            cutMusicDuration = videoDuration;
                            sb_cut_music.setMax(musicDuration);
                            sb_cut_music.setProgress(startCutMusicPosition);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void cancelMusic() {
                bacMusicPath = null;
                isMusicPrepared = false;
                mediaPlayer.setVolume(1.0f, 1.0f);
                if (musicPlayer != null) {
                    musicPlayer.pause();
                }
            }
        });
        musicDialog.show();

        musicDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mediaPlayer.start();
                if (isMusicPrepared && !TextUtils.isEmpty(bacMusicPath)) {
                    musicPlayer.start();
                }
            }
        });
    }

    //Set the soundtrack volume
    private void setMusicVolume() {
        musicVolume = MultiUtils.calFloat(1, defaultMusicVolume, 100);
        musicPlayer.setVolume(musicVolume, musicVolume);
    }

    //Set the video sound volume
    private void setOriginVolume() {
        originVolume = MultiUtils.calFloat(1, defaultOriginVolume, 100);
        mediaPlayer.setVolume(originVolume, originVolume);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == 1) {
            videoPath = data.getStringExtra("path");
            effects = data.getParcelableArrayListExtra("effects");
            if (mediaPlayer != null) {
                try {
                    mediaPlayer.pause();
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.setLooping(true);
                    mediaPlayer.setDataSource(videoPath);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    //Edit stickers
    private void editSticker() {
        ll_edit_operation.setVisibility(View.GONE);
        StickerDialog stickerDialog = new StickerDialog(activity, new StickerDialog.SelectSticker() {
            @Override
            public void selectSticker(StickerRes sticker) {
                BitmapStickerIcon deleteIcon = new BitmapStickerIcon(ContextCompat.getDrawable(activity,
                        R.mipmap.iv_delete_sticker),
                        BitmapStickerIcon.LEFT_TOP);
                deleteIcon.setIconEvent(new DeleteIconEvent());

                BitmapStickerIcon zoomIcon = new BitmapStickerIcon(ContextCompat.getDrawable(activity,
                        R.mipmap.iv_zoom_sticker),
                        BitmapStickerIcon.RIGHT_BOTOM);
                zoomIcon.setIconEvent(new ZoomIconEvent());

                BitmapStickerIcon timeIcon = new BitmapStickerIcon(ContextCompat.getDrawable(activity,
                        R.mipmap.iv_time_sticker),
                        BitmapStickerIcon.RIGHT_TOP);
                timeIcon.setIconEvent(new TimeIconEvent());
                Drawable drawable = ContextCompat.getDrawable(activity, sticker.getImgRes());

                StickerView stickerView = new StickerView(activity);
                stickerView.setIcons(Arrays.asList(deleteIcon, zoomIcon, timeIcon));

                //Set the width and height of the sticker layout
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.width = windowWidth;
                layoutParams.height = afterChangeVideoHeight;
                stickerView.setLayoutParams(layoutParams);
                stickerView.addSticker(new DrawableSticker(drawable), Sticker.Position.CENTER);
                stickerView.setOnStickerOperationListener(onStickerOperationListener);
                hideAllStickerBorders();
                fl_sticker.addView(stickerView);
                stickerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Sticker currentSticker = stickerView.getCurrentSticker();
                        currentSticker.setStickerTag(stickerTag);
                        currentSticker.setStartShowTime(0);
                        currentSticker.setEndShowTime(defaultEndShowTime);
                        stickerView.setStickerTag(stickerTag);
                        stickers.add(currentSticker);
                        stickerTag++;
                        stickerViews.add(stickerView);
                    }
                }, 100);
            }
        });
        stickerDialog.show();
        stickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ll_edit_operation.setVisibility(View.VISIBLE);
            }
        });
    }

    StickerView.OnStickerOperationListener onStickerOperationListener = new StickerView.OnStickerOperationListener() {
        @Override
        public void onStickerAdded(@NonNull Sticker sticker) {

        }

        @Override
        public void onStickerClicked(@NonNull Sticker sticker) {

        }

        @Override
        public void onStickerDeleted(@NonNull Sticker sticker) {
            int stickerTag = sticker.getStickerTag();
            StickerView stickerViewByTag = getStickerViewByTag(stickerTag);
            stickers.remove(sticker);
            stickerViews.remove(stickerViewByTag);
            ll_set_sticker_time.setVisibility(View.GONE);
            ll_edit_operation.setVisibility(View.VISIBLE);
        }

        @Override
        public void onStickerDragFinished(@NonNull Sticker sticker) {

        }

        @Override
        public void onStickerTouchedDown(@NonNull Sticker sticker) {
            hideAllStickerBorders();
            int stickerTag = sticker.getStickerTag();
            StickerView stickerViewByTag = getStickerViewByTag(stickerTag);
            stickerViewByTag.setIsShowBorderAndIcons(true);
            currentSticker = sticker;
            if (ll_set_sticker_time.getVisibility() == View.VISIBLE) {
                selectStickerShowTime();
            }
        }

        @Override
        public void onStickerZoomFinished(@NonNull Sticker sticker) {

        }

        @Override
        public void onStickerSetTime(@NonNull Sticker sticker) {
            currentSticker = sticker;
            selectStickerShowTime();

        }

        @Override
        public void onStickerDoubleTapped(@NonNull Sticker sticker) {

        }
    };

    private void hideAllStickerBorders() {
        for (int i = 0; i < stickerViews.size(); i++) {
            stickerViews.get(i).setIsShowBorderAndIcons(false);
        }
    }

    private StickerView getStickerViewByTag(int stickerTag) {
        for (int i = 0; i < stickerViews.size(); i++) {
            StickerView stickerView = stickerViews.get(i);
            if (stickerView.getStickerTag() == stickerTag) {
                return stickerView;
            }
        }
        return null;
    }

    private Sticker getStickerByTag(int stickerTag) {
        for (int i = 0; i < stickers.size(); i++) {
            Sticker sticker = stickers.get(i);
            if (sticker.getStickerTag() == stickerTag) {
                return sticker;
            }
        }
        return null;
    }

    //Add bubble text
    private void addBubbleText() {
        ll_edit_operation.setVisibility(View.GONE);
        BubbleTextDialog bubbleTextDialog = new BubbleTextDialog(activity, new BubbleTextDialog.SelectSticker() {
            @Override
            public void selectSticker(StickerRes sticker, int position) {
                //Defining Bubble Control Icons and Events
                BitmapStickerIcon deleteIcon = new BitmapStickerIcon(ContextCompat.getDrawable(activity,
                        R.mipmap.iv_delete_sticker),
                        BitmapStickerIcon.LEFT_TOP);
                deleteIcon.setIconEvent(new DeleteIconEvent());

                BitmapStickerIcon zoomIcon = new BitmapStickerIcon(ContextCompat.getDrawable(activity,
                        R.mipmap.iv_zoom_sticker),
                        BitmapStickerIcon.RIGHT_BOTOM);
                zoomIcon.setIconEvent(new ZoomIconEvent());

                BitmapStickerIcon timeIcon = new BitmapStickerIcon(ContextCompat.getDrawable(activity,
                        R.mipmap.iv_time_sticker),
                        BitmapStickerIcon.RIGHT_TOP);
                timeIcon.setIconEvent(new TimeIconEvent());

                BitmapStickerIcon bubbleIcon =
                        new BitmapStickerIcon(ContextCompat.getDrawable(activity, R.mipmap.iv_edit_bubble_text),
                                BitmapStickerIcon.LEFT_BOTTOM);
                bubbleIcon.setIconEvent(new EditBubbleTextEvent());

                StickerView stickerView = new StickerView(activity);
                stickerView.setIcons(Arrays.asList(deleteIcon, zoomIcon, timeIcon, bubbleIcon));

                //Set the width and height of the sticker layout
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.width = windowWidth;
                layoutParams.height = afterChangeVideoHeight;
                stickerView.setLayoutParams(layoutParams);

                Drawable drawable = ContextCompat.getDrawable(activity, sticker.getImgRes());
                int intrinsicWidth = drawable.getIntrinsicWidth();
                int intrinsicHeight = drawable.getIntrinsicHeight();
                int left = (int) (0.2 * intrinsicWidth);
                int top = (int) (0.4 * intrinsicHeight);
                int right = intrinsicWidth - left;
                int bottom = (int) (intrinsicHeight - 0.05 * intrinsicHeight);
                Rect rect = new Rect(left, top, right, bottom);
                stickerView.addSticker(
                        new TextSticker(getApplicationContext())
                                .setDrawable(drawable, rect)
                                .setText("please enter text")
                                .setMaxTextSize(20)
                                .setMinTextSize(14)
                                .setTextColor(getResources().getColor(R.color.black))
                                .setTextAlign(Layout.Alignment.ALIGN_CENTER)
                                .resizeText()
                        , Sticker.Position.TOP);

                stickerView.setOnStickerOperationListener(onStickerOperationListener);
                hideAllStickerBorders();
                fl_sticker.addView(stickerView);
                stickerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Sticker currentSticker = stickerView.getCurrentSticker();
                        currentSticker.setStickerTag(stickerTag);
                        currentSticker.setStartShowTime(0);
                        currentSticker.setEndShowTime(defaultEndShowTime);
                        stickerView.setStickerTag(stickerTag);
                        stickers.add(currentSticker);
                        stickerTag++;
                        stickerViews.add(stickerView);
                        showInputBubbleText(stickerView);
                    }
                }, 100);
            }

        });
        bubbleTextDialog.show();
        bubbleTextDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ll_edit_operation.setVisibility(View.VISIBLE);
            }
        });
    }

    class EditBubbleTextEvent implements StickerIconEvent {

        @Override
        public void onActionDown(StickerView stickerView, MotionEvent event) {

        }

        @Override
        public void onActionMove(StickerView stickerView, MotionEvent event) {

        }

        @Override
        public void onActionUp(StickerView stickerView, MotionEvent event) {
            showInputBubbleText(stickerView);
        }
    }

    //Enter text content
    private void showInputBubbleText(StickerView stickerView) {
        String oldBubbleText = "";
        final TextSticker currentSticker = (TextSticker) stickerView.getCurrentSticker();
        if (currentSticker != null) {
            oldBubbleText = currentSticker.getText();
            if (!TextUtils.isEmpty(oldBubbleText) && oldBubbleText.startsWith("please enter text")) {
                oldBubbleText = oldBubbleText.replace("please enter text", "");
            }
        }
        EditBubbleTextDialog editBubbleTextDialog = new EditBubbleTextDialog(activity, oldBubbleText, new onEditBubbleText() {
            @Override
            public void getBubbleText(String bubbleText) {
                if (currentSticker != null) {
                    currentSticker.setText(bubbleText);
                    currentSticker.resizeText();
                }
            }
        });
        editBubbleTextDialog.show();
    }

    private void selectStickerShowTime() {
        ll_edit_operation.setVisibility(View.GONE);
        ll_set_sticker_time.setVisibility(View.VISIBLE);
        stickerStartShowTime = currentSticker.getStartShowTime();
        tv_sticker_start_show_time.setText(stickerStartShowTime + "s");
        stickerEndShowTime = currentSticker.getEndShowTime();
        tv_sticker_end_show_time.setText(stickerEndShowTime + "s");
        int selectedTime = stickerEndShowTime - stickerStartShowTime;
        tv_sticker_show_time.setText("Sticker duration selected " + selectedTime + "s");
        TimeSliderView timeSliderView = dv_sticker_show_time.getmRangeSlider();

        int left = (int) (MultiUtils.calFloat(2, stickerStartShowTime * 1000, videoDuration) * 100);
        int right = (int) (MultiUtils.calFloat(2, stickerEndShowTime * 1000, videoDuration) * 100);
        timeSliderView.moveThumbPosition(left, right);
    }

    private void confirmSetStickerTime() {
        if (currentSticker != null) {
            stickers.remove(currentSticker);
            currentSticker.setStartShowTime(stickerStartShowTime);
            currentSticker.setEndShowTime(stickerEndShowTime);
            stickers.add(currentSticker);
            ll_edit_operation.setVisibility(View.VISIBLE);
            ll_set_sticker_time.setVisibility(View.GONE);
        }
    }

    //Start the update playback progress task
    private void startVideoTimer() {
        sb_play_progress.setMax(videoDuration);
        tv_total_time.setText(MultiUtils.millsecondsToMinuteSecondStr(videoDuration));
        cancelVideoTimer();
        timer = new Timer();
        videoTask = new VideoTask();
        timer.schedule(videoTask, 0, 1000);
    }

    //Cancel the update playback progress task
    private void cancelVideoTimer() {
        if (timer != null) {
            timer.cancel();
        }
        if (videoTask != null) {
            videoTask.cancel();
        }
    }

    // Playback progress timer
    class VideoTask extends TimerTask {
        @Override
        public void run() {
            if (MultiUtils.isActivityAlive(activity)) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currentPosition = mediaPlayer.getCurrentPosition();
                        sb_play_progress.setProgress((int) currentPosition);
                        tv_play_time.setText(MultiUtils.millsecondsToMinuteSecondStr(currentPosition));
                    }
                });
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
        if (isMusicPrepared) {
            musicPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPrepared) {
            mediaPlayer.start();
        }

        if (isMusicPrepared) {
            musicPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!isClickNext && isRecord) {
            MultiUtils.insertMp4ToGallery(activity, videoPath);
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.release();
            musicPlayer = null;
        }

        if (gl_view != null) {
            gl_view.onPause();
        }

        cancelVideoTimer();
    }

    @Override
    public void onBackPressed() {
        if (back()) return;
        super.onBackPressed();
    }

    private boolean back() {
        if (hideEditMusic()) return true;
        if (isRecord) {
            startActivity(new Intent(activity, CreateReelsActivity.class));
        } else if (isFromCombineImages) {

        } else {
            startActivity(new Intent(activity, CutVideoActivity.class).putExtra("videoPath", originalPath).putExtra("rotation", originalRotation));
        }
        return false;
    }

    private boolean hideEditMusic() {
        if (ll_edit_volume.getVisibility() == View.VISIBLE) {
            setOriginVolume();
            setMusicVolume();
            ll_edit_volume.setVisibility(View.GONE);
            ll_edit_operation.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }

    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration configuration = new Configuration();
        configuration.setToDefaults();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return resources;
    }

}
