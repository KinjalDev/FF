package com.example.friendfield.Reels.editvideo;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bokecc.camerafilter.gpufilter.filter.BeautySkinAdjust;
import com.bokecc.camerafilter.gpufilter.filter.BeautyWhiteAdjust;
import com.bokecc.camerafilter.gpufilter.helper.MagicFilterType;
import com.bokecc.camerafilter.mediacodec.VideoClipper;
import com.bokecc.shortvideo.model.MusicSet;
import com.bokecc.shortvideo.model.StickerSet;
import com.bokecc.shortvideo.videoedit.HandleProcessListener;
import com.bokecc.shortvideo.videoedit.ShortVideoHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

import com.coremedia.iso.boxes.Container;
import com.example.friendfield.R;
import com.example.friendfield.Reels.util.MultiUtils;
import com.example.friendfield.Reels.widget.CustomProgressDialog;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AACTrackImpl;
import com.googlecode.mp4parser.authoring.tracks.h264.H264TrackImpl;

import nl.bravobit.ffmpeg.FFcommandExecuteResponseHandler;
import nl.bravobit.ffmpeg.FFmpeg;

public class UploadActivity extends Activity {

    private String videoPath, bacMusicPath;
    private int filterPosition = 0, skinValue = 0, whiteValue = 0, handleTimes = 0,
            startCutMusicPosition;
    private float beautySkinValue, beautyWhiteValue, originVolume, musicVolume;
    private int videoDuration;
    private CustomProgressDialog handleProgressDialog;
    private MagicFilterType filterType = MagicFilterType.NONE;
    private Activity activity;
    private boolean isAddFilter = false, isBeautySkin = false, isBeautyWhite = false, isAddMusic = false, isAddSticker = false;
    private TextView tv_handle_success;
    private List<StickerSet> stickerSets;
    private ImageView iv_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        activity = this;
        iv_back = findViewById(R.id.iv_back);
        tv_handle_success = findViewById(R.id.tv_handle_success);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        videoPath = getIntent().getStringExtra("videoPath");
        filterPosition = getIntent().getIntExtra("filterPosition", 0);
        skinValue = getIntent().getIntExtra("skinValue", 0);
        whiteValue = getIntent().getIntExtra("whiteValue", 0);
        beautySkinValue = getIntent().getFloatExtra("beautySkinValue", 0.0f);
        beautyWhiteValue = getIntent().getFloatExtra("beautyWhiteValue", 0.0f);
        videoDuration = getIntent().getIntExtra("videoDuration", 0);
        bacMusicPath = getIntent().getStringExtra("bacMusicPath");
        startCutMusicPosition = getIntent().getIntExtra("startCutMusicPosition", 0);
        originVolume = getIntent().getFloatExtra("originVolume", 0.0f);
        musicVolume = getIntent().getFloatExtra("musicVolume", 0.0f);
        stickerSets = (List<StickerSet>) getIntent().getSerializableExtra("stickerSets");
        videoDuration = getIntent().getIntExtra("videoDuration", 0);

        if (filterPosition > 0) {
            isAddFilter = true;
        }

        if (skinValue > 0) {
            isBeautySkin = true;
        }

        if (whiteValue > 0) {
            isBeautyWhite = true;
        }

        if (!TextUtils.isEmpty(bacMusicPath)) {
            isAddMusic = true;
        }

        if (stickerSets.size() > 0) {
            isAddSticker = true;
        }

        handleProgressDialog = new CustomProgressDialog(UploadActivity.this);
        makeVideo();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void makeVideo() {
//        handleProgressDialog.show();

        if (isBeautyWhite || isBeautySkin || isAddFilter) {
            addFilter();
        } else {
            if (isAddMusic || isAddSticker) {
                addMusicAndSticker();
            } else {
                handleVideoSuccess();
            }
        }
    }

    private void addFilter() {
        //add filter
        getFilterType();
        VideoClipper addFilter = new VideoClipper();
        addFilter.setInputVideoPath(videoPath);
        String outputFilterPath = MultiUtils.getOutPutVideoPath();
        if (isBeautyWhite) {
            BeautyWhiteAdjust beautyWhiteAdjust = new BeautyWhiteAdjust(beautyWhiteValue);
            addFilter.setBeautyWhiteAdjust(beautyWhiteAdjust);
        }
        if (isBeautySkin) {
            BeautySkinAdjust beautySkinAdjust = new BeautySkinAdjust(beautySkinValue);
            addFilter.setBeautySkinAdjust(beautySkinAdjust);
        }
        if (isAddFilter) {
            addFilter.setFilterType(filterType);
        }
        addFilter.setOutputVideoPath(outputFilterPath);
        try {
            addFilter.clipVideo(0, videoDuration * 1000);
        } catch (IOException e) {
            e.printStackTrace();
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    handleVideoFail();
                }
            });
        }
        addFilter.setOnVideoCutFinishListener(new VideoClipper.OnVideoCutFinishListener() {
            @Override
            public void onFinish() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (handleTimes > 0) {
                            MultiUtils.deleteFile(videoPath);
                        }
                        videoPath = outputFilterPath;
                        handleTimes++;
                        if (isAddMusic || isAddSticker) {
                            addMusicAndSticker();
                        } else {
                            handleVideoSuccess();
                        }

                    }
                });
            }
        });
    }

    //Add music and stickers
    private void addMusicAndSticker() {
        String outPutAddStickerPath = MultiUtils.getOutPutVideoPath();
        MusicSet musicSet = null;
        int startMusicPos = 0;
        if (isAddMusic) {
            musicSet = new MusicSet();
            startMusicPos = startCutMusicPosition / 1000;
            musicSet.setMusicPath(bacMusicPath);
            musicSet.setStartMusicPos(startMusicPos);
            musicSet.setOriginVolume(originVolume);
            musicSet.setMusicVolume(musicVolume);
        }

/**********************/
        try {
            FFmpeg ffmpeg = FFmpeg.getInstance(this);
//            String[] command = new String[] { "-ss", "" + startMusicPos,"-i", bacMusicPath, "-i", videoPath, "-acodec", "copy", "-shortest",
//            String[] command = new String[]{"-ss", "" + startMusicPos, "-i", bacMusicPath, "-i", videoPath, "-i", stickerSets.get(0).getStickerPath(), "-filter_complex [0]crop=720:720[base][1]scale=50:50[overl][base][overl]overlay=274:305", "-preset", "ultrafast", "-c:a", "copy", "-acodec", "copy", "-shortest",
            String[] command = new String[]{"-ss", "" + startMusicPos, "-i", bacMusicPath, "-i", videoPath, "-acodec", "copy", "-shortest",
                    "-vcodec", "copy", outPutAddStickerPath};
//            String cmd = "-i " + videoPath + " -i " + bacMusicPath + " -i " + stickerSets + " -shortest -threads 0 -preset ultrafast -strict -2 " + MultiUtils.getOutPutVideoPath();
//            String cmd = "-i " + videoPath + " -i " + bacMusicPath + " -shortest -threads 0 -preset ultrafast -strict -2 " + MultiUtils.getOutPutVideoPath();
//            String[] command = cmd.split(" ");
            ffmpeg.execute(command, new FFcommandExecuteResponseHandler() {
                @Override
                public void onSuccess(String message) {
                    Log.e("TAG", "Started command success : ffmpeg " + message);

                }

                @Override
                public void onProgress(String message) {
                    Log.e("TAG", "Started command onProgress : ffmpeg " + message);
                }

                @Override
                public void onFailure(String message) {

                }

                @Override
                public void onStart() {
                    handleProgressDialog.show();

                    Log.e("TAG", "Started command onStart : ffmpeg " + "start");
                }

                @Override
                public void onFinish() {
                    handleProgressDialog.dismiss();
                    Log.e("TAG", "Finished command onFinish : ffmpeg " + "finish");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

/*******************/
//        String outputFile = "";
//
//        MediaExtractor audioExtractor = null;
//        MediaMuxer muxer = null;
//        int audioTrack = 0;
//        int frameCount = 0;
//        int offset = 0;
//        ByteBuffer audioBuf = null;
//        MediaCodec.BufferInfo videoBufferInfo = null;
//        MediaCodec.BufferInfo audioBufferInfo = null;
//        try {
////            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "final2.mp4");
////            file.createNewFile();
////            outputFile = file.getAbsolutePath();
//
//            MediaExtractor videoExtractor = new MediaExtractor();
//            videoExtractor.setDataSource(videoPath);
////            AssetFileDescriptor afdd = getAssets().openFd("Produce.MP4");
////            videoExtractor.setDataSource(afdd.getFileDescriptor() ,afdd.getStartOffset(),afdd.getLength());
//
//            audioExtractor = new MediaExtractor();
//            audioExtractor.setDataSource(bacMusicPath);
//
//            Log.e("TAG", "Video Extractor Track Count " + videoExtractor.getTrackCount());
//            Log.e("TAG", "Audio Extractor Track Count " + audioExtractor.getTrackCount());
//
//            muxer = null;
//            try {
//                muxer = new MediaMuxer(MultiUtils.getOutPutVideoPath(), MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
////                muxer = new MediaMuxer(outputFile, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
//            } catch (IOException ioException) {
//                ioException.printStackTrace();
//            }
//
//            videoExtractor.selectTrack(0);
//            MediaFormat videoFormat = videoExtractor.getTrackFormat(0);
//            int videoTrack = muxer.addTrack(videoFormat);
//
//            audioExtractor.selectTrack(0);
//            MediaFormat audioFormat = audioExtractor.getTrackFormat(0);
//            audioTrack = muxer.addTrack(audioFormat);
//
//            Log.e("TAG", "Video Format " + videoFormat.toString());
//            Log.e("TAG", "Audio Format " + audioFormat.toString());
//
//            boolean sawEOS = false;
//            frameCount = 0;
//            offset = 100;
//            int sampleSize = 256 * 1024;
//            ByteBuffer videoBuf = ByteBuffer.allocate(sampleSize);
//            audioBuf = ByteBuffer.allocate(sampleSize);
//            videoBufferInfo = new MediaCodec.BufferInfo();
//            audioBufferInfo = new MediaCodec.BufferInfo();
//
//
//            videoExtractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
//            audioExtractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
//
//            muxer.start();
//
//            while (!sawEOS) {
//                videoBufferInfo.offset = offset;
//                videoBufferInfo.size = videoExtractor.readSampleData(videoBuf, offset);
//
//
//                if (videoBufferInfo.size < 0 || audioBufferInfo.size < 0) {
//                    Log.e("TAG", "saw input EOS.");
//                    sawEOS = true;
//                    videoBufferInfo.size = 0;
//
//                } else {
//                    videoBufferInfo.presentationTimeUs = videoExtractor.getSampleTime();
//                    videoBufferInfo.flags = videoExtractor.getSampleFlags();
//                    muxer.writeSampleData(videoTrack, videoBuf, videoBufferInfo);
//                    videoExtractor.advance();
//
//
//                    frameCount++;
//                    Log.e("TAG", "Frame (" + frameCount + ") Video PresentationTimeUs:" + videoBufferInfo.presentationTimeUs + " Flags:" + videoBufferInfo.flags + " Size(KB) " + videoBufferInfo.size / 1024);
//                    Log.e("TAG", "Frame (" + frameCount + ") Audio PresentationTimeUs:" + audioBufferInfo.presentationTimeUs + " Flags:" + audioBufferInfo.flags + " Size(KB) " + audioBufferInfo.size / 1024);
//
//                }
//            }
//        } catch (IOException ioException) {
//            ioException.printStackTrace();
//        }
//
//        Toast.makeText(getApplicationContext(), "frame:" + frameCount, Toast.LENGTH_SHORT).show();
//
//
//        boolean sawEOS2 = false;
//        int frameCount2 = 0;
//        while (!sawEOS2) {
//            frameCount2++;
//
//            audioBufferInfo.offset = offset;
//            audioBufferInfo.size = audioExtractor.readSampleData(audioBuf, offset);
//
//            if (videoBufferInfo.size < 0 || audioBufferInfo.size < 0) {
//                Log.e("TAG", "saw input EOS.");
//                sawEOS2 = true;
//                audioBufferInfo.size = 0;
//            } else {
//                audioBufferInfo.presentationTimeUs = audioExtractor.getSampleTime();
//                audioBufferInfo.flags = audioExtractor.getSampleFlags();
//                muxer.writeSampleData(audioTrack, audioBuf, audioBufferInfo);
//                audioExtractor.advance();
//
//
//                Log.e("TAG", "Frame (" + frameCount + ") Video PresentationTimeUs:" + videoBufferInfo.presentationTimeUs + " Flags:" + videoBufferInfo.flags + " Size(KB) " + videoBufferInfo.size / 1024);
//                Log.e("TAG", "Frame (" + frameCount + ") Audio PresentationTimeUs:" + audioBufferInfo.presentationTimeUs + " Flags:" + audioBufferInfo.flags + " Size(KB) " + audioBufferInfo.size / 1024);
//
//            }
//        }
//
//        Toast.makeText(getApplicationContext(), "frame:" + frameCount2, Toast.LENGTH_SHORT).show();
//
//        muxer.stop();
//        muxer.release();
//    }
//    catch (IOException e) {
//        Log.d(TAG, "Mixer Error 1 " + e.getMessage());
//    } catch (Exception e) {
//        Log.d(TAG, "Mixer Error 2 " + e.getMessage());
//    }

/****************/
//        ShortVideoHelper.AddMusicAndSticker(activity, videoPath, outPutAddStickerPath, musicSet, stickerSets, new HandleProcessListener() {
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onFinish() {
//
//                if (handleTimes > 0) {
//                    MultiUtils.deleteFile(videoPath);
//                }
//
//                videoPath = outPutAddStickerPath;
//                handleTimes++;
//                handleVideoSuccess();
//
//                if (stickerSets.size() > 0) {
//                    for (int i = 0; i < stickerSets.size(); i++) {
//                        MultiUtils.deleteFile(stickerSets.get(i).getStickerPath());
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFail(int errorCode, String msg) {
//
////                if (handleTimes > 0) {
////                    MultiUtils.deleteFile(videoPath);
////                }
////
////                videoPath = outPutAddStickerPath;
////                handleTimes++;
////                handleVideoSuccess();
////
////                if (stickerSets.size() > 0) {
////                    for (int i = 0; i < stickerSets.size(); i++) {
////                        MultiUtils.deleteFile(stickerSets.get(i).getStickerPath());
////                    }
////                }
//
//                handleVideoFail();
//            }
//        });
    }

    private void handleVideoFail() {
        handleProgressDialog.dismiss();
        MultiUtils.showToast(activity, "Processing failed");
    }

    private void handleVideoSuccess() {

        handleProgressDialog.dismiss();
        MultiUtils.insertMp4ToGallery(activity, videoPath);
        MultiUtils.showToast(activity, "Processed successfully");
        tv_handle_success.setVisibility(View.VISIBLE);
    }

    private void getFilterType() {
        if (filterPosition == 0) {
            filterType = MagicFilterType.NONE;
        } else if (filterPosition == 1) {
            filterType = MagicFilterType.COOL;
        } else if (filterPosition == 2) {
            filterType = MagicFilterType.ANTIQUE;
        } else if (filterPosition == 3) {
            filterType = MagicFilterType.HUDSON;
        } else if (filterPosition == 4) {
            filterType = MagicFilterType.HEFE;
        } else if (filterPosition == 5) {
            filterType = MagicFilterType.BRANNAN;
        }
    }
}
