package com.example.friendfield.Reels.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bokecc.shortvideo.combineimages.model.SelectImageInfo;
import com.bokecc.shortvideo.combineimages.model.SelectVideoInfo;
import com.bumptech.glide.Glide;
import com.example.friendfield.MyApplication;
import com.example.friendfield.R;
import com.example.friendfield.Reels.cutvideo.VideoBean;
import com.example.friendfield.Reels.model.AnimRes;
import com.example.friendfield.Reels.model.MusicInfo;
import com.example.friendfield.Reels.model.SpecialEffectRes;
import com.example.friendfield.Reels.model.TransitionRes;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MultiUtils {

    public static String APP_FILE_PATH = "AHuodeShortVideo";
//    public static String APP_FILE_PATH = "ShortVideo";

    public static void showToast(final Activity activity, final String content) {
        if (activity != null && !activity.isFinishing()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, content, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //display image
    public static void loadUriImage(Uri uri, ImageView imageView) {
        if (uri != null) {
            Glide.with(MyApplication.getContext()).load(uri).into(imageView);
        }
    }

    //display image
    public static void loadPathImage(String path, ImageView imageView) {
        if (!TextUtils.isEmpty(path)) {
            Glide.with(MyApplication.getContext()).load(path).into(imageView);
        }
    }

    /**
     * Get local video information
     */
    public static VideoBean getLocalVideoInfo(String path) {
        VideoBean info = new VideoBean();
        info.src_path = path;
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        try {
            mmr.setDataSource(path);
            info.src_path = path;
            info.duration = Integer.valueOf(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            info.rate = Integer.valueOf(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));
            info.width = Integer.valueOf(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            info.height = Integer.valueOf(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            info.rotation = Integer.valueOf(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mmr.release();
        }
        return info;
    }


    //set to full screen
    public static void setFullScreen(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    //convert dp to sp
    public static int dipToPx(Context context, float dip) {
        return (int) (dip * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    //get file
    public static File getNewFile(Context context, String folderName, String stickerName) {
        String path;
        if (isSDAvailable()) {
            path = getFolderName(folderName) + File.separator + stickerName + ".png";
        } else {
            path = context.getFilesDir().getPath() + File.separator + stickerName + ".png";
        }

        if (TextUtils.isEmpty(path)) {
            return null;
        }

        File saveFile = new File(path);
        if (saveFile.exists()) {
            saveFile.delete();
        }

        return saveFile;
    }


    public static double div(int scale, int num1, int num2) {
        BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(num1));
        BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(num2));
        return bigDecimal1.divide(bigDecimal2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static float calFloat(int scale, int num1, int num2) {
        BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(num1));
        BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(num2));
        return bigDecimal1.divide(bigDecimal2, scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public static float calTimeRatio(int scale, float num1, float num2) {
        BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(num1));
        BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(num2));
        return bigDecimal1.divide(bigDecimal2, scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * Check if sd card is usable
     */
    private static boolean isSDAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String getFolderName(String name) {
//        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + APP_FILE_PATH, name);

        File mediaStorageDir = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
//            mPath= getActivity().getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/" + now + ".jpeg";
//            mediaStorageDir = new File(MyApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_DCIM) + File.separator +  APP_FILE_PATH, name);
            mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + APP_FILE_PATH, name);
        } else {
//            mPath= Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg";
            mediaStorageDir = new File(Environment.getExternalStorageDirectory() + File.separator + APP_FILE_PATH, name);
        }


        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return "";
            }
        }
        return mediaStorageDir.getAbsolutePath();
    }

    //Get the path of the output video
    public static String getOutPutVideoPath() {
//        String outPath = Environment.getExternalStorageDirectory().getPath() + File.separator + APP_FILE_PATH;
        String outPath = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
//            file = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM) + File.separator + dirName, "RecordVideo");
//            outPath = MyApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_DCIM) + File.separator + APP_FILE_PATH;
            outPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + APP_FILE_PATH;
        } else {
//            mPath= Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg";
            outPath = Environment.getExternalStorageDirectory().getPath() + File.separator + APP_FILE_PATH;
        }


        File file = new File(outPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        File outPutVideo = new File(outPath, System.currentTimeMillis() + ".mp4");
        if (outPutVideo.exists()) {
            outPutVideo.delete();
        }

        return outPutVideo.getAbsolutePath();
    }

    public static String getOutPutCutVideoPath() {
//        String outPath = Environment.getExternalStorageDirectory().getPath() + File.separator + APP_FILE_PATH;
        String outPath = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
//            file = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM) + File.separator + dirName, "RecordVideo");
//            outPath = MyApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_DCIM) + File.separator + APP_FILE_PATH;
            outPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + APP_FILE_PATH + File.separator;
        } else {
//            mPath= Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg";
            outPath = Environment.getExternalStorageDirectory().getPath() + File.separator + APP_FILE_PATH + File.separator;
        }


        File file = new File(outPath);
        if (!file.exists()) {
            file.mkdirs();
        }

//        File outPutVideo = new File(outPath, System.currentTimeMillis() + ".mp4");
//        if (outPutVideo.exists()) {
//            outPutVideo.delete();
//        }

//        return outPutVideo.getAbsolutePath();
        return file.getAbsolutePath();
    }

    //Get the path of the output video
    public static String getEffectOutputVideoPath() {
//        String outPath = Environment.getExternalStorageDirectory().getPath() + File.separator + APP_FILE_PATH;

        String outPath = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
//            file = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM) + File.separator + dirName, "RecordVideo");
//            outPath = MyApplication.context.getExternalFilesDir(Environment.DIRECTORY_DCIM) + File.separator + APP_FILE_PATH;
            outPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + APP_FILE_PATH;
        } else {
//            mPath= Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg";
            outPath = Environment.getExternalStorageDirectory().getPath() + File.separator + APP_FILE_PATH;
        }

        File file = new File(outPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        File outPutVideo = new File(outPath, "effect.mp4");
        return outPutVideo.getAbsolutePath();
    }

    //Get the available width of the screen
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        int height = dm.widthPixels;
        return height;
    }

    //get the width of the image
    public static int getImageWidth(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);
        int width = options.outWidth;
        return width;
    }

    //get the height of the image
    public static int getImageHeight(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);
        int height = options.outHeight;
        return height;
    }

    //Get the available height of the screen
    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        int height = dm.heightPixels;
        return height;
    }

    public static void insertMp4ToGallery(Context context, String filePath) {
        final ContentValues values = new ContentValues(2);
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Video.Media.DATA, filePath);
        context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                values);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + filePath)));
    }

    //Delete Files
    public static void deleteFile(String path) {
        File file = new File(path);
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    //Get local music information
    public static List getMusicInfos(Context context) {
        List list = new ArrayList();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MusicInfo musicInfo = new MusicInfo();
                musicInfo.musicName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                musicInfo.singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                musicInfo.musicPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                musicInfo.musicTime = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                musicInfo.isSelected = false;
                list.add(musicInfo);
            }
            cursor.close();
        }

        return list;
    }

    //get local video data
    public static List<SelectVideoInfo> getVideoDatas(Context context) {
        List<SelectVideoInfo> datas = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Video.VideoColumns.DATE_MODIFIED);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                SelectVideoInfo selectVideoInfo = new SelectVideoInfo();
                String id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                Uri cover = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
                selectVideoInfo.setCover(cover);
                int videoTime = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                selectVideoInfo.setVideoTime(videoTime);
                int videoSize = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                selectVideoInfo.setVideoSize(videoSize);
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                selectVideoInfo.setPath(path);
                long date = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED));
                selectVideoInfo.setDate(date);
                selectVideoInfo.setSelected(false);
                datas.add(selectVideoInfo);
            }
            cursor.close();
        }
        if (datas.size() > 0) {
            Collections.sort(datas, new Comparator<SelectVideoInfo>() {
                @Override
                public int compare(SelectVideoInfo o1, SelectVideoInfo o2) {
                    int rank = (int) (o2.getDate() - o1.getDate());
                    return rank;
                }
            });
        }
        return datas;
    }

    //Get local image data
    public static List<SelectImageInfo> getImageDatas(Context context) {
        List<SelectImageInfo> datas = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Images.ImageColumns.DATE_MODIFIED);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                SelectImageInfo selectImageInfo = new SelectImageInfo();
                int imageSize = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));
                selectImageInfo.setSize(imageSize);
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                selectImageInfo.setPath(path);
                long date = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED));
                selectImageInfo.setDate(date);
                selectImageInfo.setSelected(false);
                datas.add(selectImageInfo);
            }
            cursor.close();
        }
        if (datas.size() > 0) {
            Collections.sort(datas, new Comparator<SelectImageInfo>() {
                @Override
                public int compare(SelectImageInfo o1, SelectImageInfo o2) {
                    int rank = (int) (o2.getDate() - o1.getDate());
                    return rank;
                }
            });
        }
        return datas;
    }

    public static List<TransitionRes> getTransitionResDatas() {
        List<TransitionRes> datas = new ArrayList<>();
        TransitionRes transitionRes0 = new TransitionRes();
        transitionRes0.setNormalImgRes(R.mipmap.iv_no_transition_normal);
        transitionRes0.setSelectdImgRes(R.mipmap.iv_no_transition_selected);
        transitionRes0.setSelected(true);
        transitionRes0.setTransitionName("none");
        transitionRes0.setTransitionType(0);
        datas.add(transitionRes0);

        TransitionRes transitionRes1 = new TransitionRes();
        transitionRes1.setNormalImgRes(R.mipmap.iv_overlap_normal);
        transitionRes1.setSelectdImgRes(R.mipmap.iv_overlap_selected);
        transitionRes1.setSelected(false);
        transitionRes1.setTransitionName("overlapping");
        transitionRes1.setTransitionType(1);
        datas.add(transitionRes1);

        TransitionRes transitionRes2 = new TransitionRes();
        transitionRes2.setNormalImgRes(R.mipmap.iv_flash_black_normal);
        transitionRes2.setSelectdImgRes(R.mipmap.iv_flash_black_selected);
        transitionRes2.setSelected(false);
        transitionRes2.setTransitionName("flash black");
        transitionRes2.setTransitionType(2);
        datas.add(transitionRes2);

        TransitionRes transitionRes3 = new TransitionRes();
        transitionRes3.setNormalImgRes(R.mipmap.iv_flash_white_normal);
        transitionRes3.setSelectdImgRes(R.mipmap.iv_flash_white_selected);
        transitionRes3.setSelected(false);
        transitionRes3.setTransitionName("flash white");
        transitionRes3.setTransitionType(3);
        datas.add(transitionRes3);

        TransitionRes transitionRes4 = new TransitionRes();
        transitionRes4.setNormalImgRes(R.mipmap.iv_circle_normal);
        transitionRes4.setSelectdImgRes(R.mipmap.iv_circle_selected);
        transitionRes4.setSelected(false);
        transitionRes4.setTransitionName("round");
        transitionRes4.setTransitionType(4);
        datas.add(transitionRes4);
        return datas;
    }

    public static List<AnimRes> getAnimResDatas() {
        List<AnimRes> datas = new ArrayList<>();
        AnimRes animRes0 = new AnimRes();
        animRes0.setNormalImgRes(R.mipmap.iv_no_transition_normal);
        animRes0.setSelectdImgRes(R.mipmap.iv_no_transition_selected);
        animRes0.setSelected(true);
        animRes0.setAnimName("none");
        animRes0.setAnimType(0);
        datas.add(animRes0);

        AnimRes animRes1 = new AnimRes();
        animRes1.setNormalImgRes(R.mipmap.iv_zoom_big_normal);
        animRes1.setSelectdImgRes(R.mipmap.iv_zoom_big_selected);
        animRes1.setSelected(false);
        animRes1.setAnimName("enlarge");
        animRes1.setAnimType(1);
        datas.add(animRes1);

        AnimRes animRes2 = new AnimRes();
        animRes2.setNormalImgRes(R.mipmap.iv_zoom_small_normal);
        animRes2.setSelectdImgRes(R.mipmap.iv_zoom_small_selected);
        animRes2.setSelected(false);
        animRes2.setAnimName("zoom out");
        animRes2.setAnimType(2);
        datas.add(animRes2);

        AnimRes animRes3 = new AnimRes();
        animRes3.setNormalImgRes(R.mipmap.iv_to_left_normal);
        animRes3.setSelectdImgRes(R.mipmap.iv_to_left_selected);
        animRes3.setSelected(false);
        animRes3.setAnimName("Swipe left");
        animRes3.setAnimType(3);
        datas.add(animRes3);

        AnimRes animRes4 = new AnimRes();
        animRes4.setNormalImgRes(R.mipmap.iv_to_right_normal);
        animRes4.setSelectdImgRes(R.mipmap.iv_to_right_selected);
        animRes4.setSelected(false);
        animRes4.setAnimName("Swipe right");
        animRes4.setAnimType(4);
        datas.add(animRes4);
        return datas;
    }

    public static List<SpecialEffectRes> getSpecialEffectResDatas() {
        List<SpecialEffectRes> datas = new ArrayList<>();

        SpecialEffectRes SpecialEffectRes1 = new SpecialEffectRes();
        SpecialEffectRes1.setNormalImgRes(R.mipmap.iv_shake_normal);
        SpecialEffectRes1.setSelectdImgRes(R.mipmap.iv_shake_selected);
        SpecialEffectRes1.setSelected(false);
        SpecialEffectRes1.setSpEffectName("jitter");
        datas.add(SpecialEffectRes1);

        SpecialEffectRes SpecialEffectRes2 = new SpecialEffectRes();
        SpecialEffectRes2.setNormalImgRes(R.mipmap.iv_splash_screen_normal);
        SpecialEffectRes2.setSelectdImgRes(R.mipmap.iv_splash_screen_selected);
        SpecialEffectRes2.setSelected(false);
        SpecialEffectRes2.setSpEffectName("splash screen");
        datas.add(SpecialEffectRes2);

        SpecialEffectRes SpecialEffectRes3 = new SpecialEffectRes();
        SpecialEffectRes3.setNormalImgRes(R.mipmap.iv_sprinkle_gold_normal);
        SpecialEffectRes3.setSelectdImgRes(R.mipmap.iv_sprinkle_gold_selected);
        SpecialEffectRes3.setSelected(false);
        SpecialEffectRes3.setSpEffectName("hallucination");
        datas.add(SpecialEffectRes3);

        SpecialEffectRes SpecialEffectRes4 = new SpecialEffectRes();
        SpecialEffectRes4.setNormalImgRes(R.mipmap.iv_fly_flower_normal);
        SpecialEffectRes4.setSelectdImgRes(R.mipmap.iv_fly_flower_selected);
        SpecialEffectRes4.setSelected(false);
        SpecialEffectRes4.setSpEffectName("glitch");
        datas.add(SpecialEffectRes4);

        SpecialEffectRes SpecialEffectRes5 = new SpecialEffectRes();
        SpecialEffectRes5.setNormalImgRes(R.mipmap.iv_light_spot_normal);
        SpecialEffectRes5.setSelectdImgRes(R.mipmap.iv_light_spot_selected);
        SpecialEffectRes5.setSelected(false);
        SpecialEffectRes5.setSpEffectName("zoom");
        datas.add(SpecialEffectRes5);

        return datas;
    }

    public static boolean isActivityAlive(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            return true;
        }
        return false;
    }

    // Convert milliseconds to minutes: seconds
    public static String millsecondsToMinuteSecondStr(long ms) {
        int seconds = (int) (ms / 1000);
        String result = "";
        int min = 0, second = 0;
        min = seconds / 60;
        second = seconds - min * 60;

        if (min < 10) {
            result += "0" + min + ":";
        } else {
            result += min + ":";
        }
        if (second < 10) {
            result += "0" + second;
        } else {
            result += second;
        }
        return result;
    }

    /**
     * ?????????????????????
     *
     * @param path
     * @param count    ????????????
     * @param width    ?????????????????????
     * @param height   ?????????????????????
     * @param listener
     */
    public static void getLocalVideoBitmap(final String path, final int count, final int width, final int height, final OnBitmapGetListener listener) {
        AsyncTask<Object, Object, Object> task = new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                try {
                    mmr.setDataSource(path);
                    long duration = (Long.valueOf(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))) * 1000;
                    long inv = (duration / count);

                    for (long i = 0; i < duration; i += inv) {
                        //??????getFrameAtTime?????????timeUs ???????????? 1us * 1000 * 1000 = 1s
                        Bitmap bitmap = mmr.getFrameAtTime(i, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                        Bitmap destBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
                        bitmap.recycle();

                        publishProgress(destBitmap);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                } finally {
                    mmr.release();
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Object... values) {
                if (listener != null) {
                    listener.onBitmapGet((Bitmap) values[0]);
                }
            }

            @Override
            protected void onPostExecute(Object result) {

            }
        };
        task.execute();
    }

}
