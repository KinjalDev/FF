package com.example.friendfield.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterViewFlipper;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.friendfield.Adapter.ViewStoryAdapter;
import com.example.friendfield.Model.Story.ViewStoryModel;
import com.example.friendfield.R;
import com.example.friendfield.Utils.DBHelper;
import com.example.friendfield.Utils.FileUtils;
import com.example.friendfield.Utils.ImageEditor;
import com.example.friendfield.Utils.Utility;
import com.example.friendfield.view.PausableProgressBar;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.shts.android.storiesprogressview.StoriesProgressView;

public class ViewStoryActivity extends AppCompatActivity {

    AdapterViewFlipper adapterViewFlipper;
    StoriesProgressView storiesProgressView;
    ImageView image;
    ImageView blur_image;
    CircleImageView img_user;
    TextView txt_user_name;
    TextView txt_time;
    EditText txt_chating;
    ImageView iv_send;
    ImageView iv_close;
TextView tv_seen_count;

    int counter = 0;
    int[] images = {R.drawable.cupcakes_bg,
            R.drawable.girl_image,
            R.drawable.cupcakes_bg,
            R.drawable.img_7};
    int PROGRESS_COUNT = 0;
    View reverse;
    View skip;
    long pressTime = 0L;
    long limit = 500L;
    byte[] bytes;
    //    ArrayList<byte[]> arrayList = new ArrayList<>();
//    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<ViewStoryModel> arrayList = new ArrayList<>();
    DBHelper dbHelper;
    VideoView video_view;
    String duration;
    long timeInMillisec;
    int image_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_story);

        adapterViewFlipper = findViewById(R.id.adapterViewFlipper);

        storiesProgressView = findViewById(R.id.storiesProgressView);
        image = findViewById(R.id.image);
        blur_image = findViewById(R.id.blur_image);
        reverse = findViewById(R.id.reverse);
        skip = findViewById(R.id.skip);
        video_view = findViewById(R.id.video_view);

        img_user = findViewById(R.id.img_user);
        txt_user_name = findViewById(R.id.txt_user_name);
        txt_time = findViewById(R.id.txt_time);
        txt_chating = findViewById(R.id.txt_chating);
        iv_send = findViewById(R.id.iv_send);
        iv_close = findViewById(R.id.iv_close);
        tv_seen_count = findViewById(R.id.tv_seen_count);

        dbHelper = new DBHelper(getApplicationContext());
        dbHelper.open();

        for (int i = 0; i < dbHelper.retreiveimagefromdb1().size(); i++) {
            PROGRESS_COUNT++;
        }

        arrayList.addAll(dbHelper.retreiveimagefromdb1());

        Log.e("LLL_story-->", String.valueOf(arrayList.size()));


        storiesProgressView.setStoriesCount(PROGRESS_COUNT);

//        storiesProgressView.setStoryDuration(3000L);
        storiesProgressView.startStories(counter);

        String select_file = arrayList.get(counter).getImage();
        image_id = arrayList.get(counter).getId();

        String lastWord = select_file.substring(select_file.lastIndexOf(".") + 1);
        Log.e("LLL_v_image_path-->", lastWord);


        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                retriever.setDataSource(select_file, new HashMap<String, String>());
            retriever.setDataSource(select_file);
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            timeInMillisec = Long.parseLong(time);
            retriever.release();
            duration = FileUtils.convertMillieToHMmSs(timeInMillisec); //use this duration
            Log.e("LLL_n_duration", String.valueOf(duration));
            Log.e("LLL_ne_duration", String.valueOf(timeInMillisec));

        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        if (duration == null) {
            storiesProgressView.setStoryDuration(3000L);
        } else {
            storiesProgressView.setStoryDuration(timeInMillisec);
        }

        if (lastWord.equalsIgnoreCase("jpg") || lastWord.equalsIgnoreCase("jpeg") || lastWord.equalsIgnoreCase("png") || lastWord.equalsIgnoreCase("svg") || lastWord.equalsIgnoreCase("webp")) {
            image.setVisibility(View.VISIBLE);
            video_view.setVisibility(View.GONE);

//            image.setImageURI(Uri.parse(arrayList.get(counter)));
            image.setImageURI(Uri.parse(select_file));
            blur_image.setImageURI(Uri.parse(select_file));
        } else {
            image.setVisibility(View.GONE);
            video_view.setVisibility(View.VISIBLE);

//            video_view.setVideoURI(Uri.parse(arrayList.get(counter)));
            video_view.setVideoURI(Uri.parse(select_file));
            video_view.start();

        }

//        setstories();

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();
//                dbHelper.deleteTitle(counter);
                dbHelper.deleteTitle(image_id);
//                arrayList.remove(counter);

                arrayList.remove(counter);

//                if (counter == arrayList.size() - 1) {
//                    finish();
//                    return;
//                } else {

                PROGRESS_COUNT = 0;

//                setstories(counter);
                setstories();
//                }
//                for (int i = 0; i < dbHelper.retreiveimagefromdb1().size(); i++) {
//                    PROGRESS_COUNT++;
//                }
//
//                arrayList.addAll(dbHelper.retreiveimagefromdb1());
//                storiesProgressView.setStoriesCount(PROGRESS_COUNT);

            }
        });

        storiesProgressView.setStoriesListener(new StoriesProgressView.StoriesListener() {
            @Override
            public void onNext() {
                int total_count = ++counter;
                String select_file = arrayList.get(total_count).getImage();
                image_id = arrayList.get(total_count).getId();

                String lastWord = select_file.substring(select_file.lastIndexOf(".") + 1);
                Log.e("LLL_next_image-->", lastWord);


                try {
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(select_file);
                    String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    timeInMillisec = Long.parseLong(time);
                    retriever.release();
                    duration = FileUtils.convertMillieToHMmSs(timeInMillisec); //use this duration
                    Log.e("LLL_n_duration", String.valueOf(duration));
                    Log.e("LLL_ne_duration", String.valueOf(timeInMillisec));

                } catch (RuntimeException e) {
                    e.printStackTrace();
                }


                if (duration == null) {
                    storiesProgressView.setStoryDuration(3000L);
                } else {
                    storiesProgressView.setStoryDuration(timeInMillisec);
                }

                if (lastWord.equalsIgnoreCase("jpg") || lastWord.equalsIgnoreCase("jpeg") || lastWord.equalsIgnoreCase("png") || lastWord.equalsIgnoreCase("svg") || lastWord.equalsIgnoreCase("webp")) {
                    image.setVisibility(View.VISIBLE);
                    video_view.setVisibility(View.GONE);

                    image.setImageURI(Uri.parse(select_file));
                    blur_image.setImageURI(Uri.parse(select_file));

                } else {
                    image.setVisibility(View.GONE);
                    video_view.setVisibility(View.VISIBLE);

                    video_view.setVideoURI(Uri.parse(select_file));
                    video_view.start();
                }

            }

            @Override
            public void onPrev() {
                if ((counter - 1) < 0) return;
                int total_count = --counter;
                String select_file = arrayList.get(total_count).getImage();
                image_id = arrayList.get(total_count).getId();

                String lastWord = select_file.substring(select_file.lastIndexOf(".") + 1);
                Log.e("LLL_prev_image-->", lastWord);

                try {
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(select_file);
                    String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    timeInMillisec = Long.parseLong(time);
                    retriever.release();
                    duration = FileUtils.convertMillieToHMmSs(timeInMillisec); //use this duration
                    Log.e("LLL_n_duration", String.valueOf(duration));
                    Log.e("LLL_ne_duration", String.valueOf(timeInMillisec));

                } catch (RuntimeException e) {
                    e.printStackTrace();
                }

                if (duration == null) {
                    storiesProgressView.setStoryDuration(3000L);
                } else {
                    storiesProgressView.setStoryDuration(timeInMillisec);
                }

                if (lastWord.equalsIgnoreCase("jpg") || lastWord.equalsIgnoreCase("jpeg") || lastWord.equalsIgnoreCase("png") || lastWord.equalsIgnoreCase("svg") || lastWord.equalsIgnoreCase("webp")) {
                    image.setVisibility(View.VISIBLE);
                    video_view.setVisibility(View.GONE);

                    image.setImageURI(Uri.parse(select_file));
                    blur_image.setImageURI(Uri.parse(select_file));

                } else {
                    image.setVisibility(View.GONE);
                    video_view.setVisibility(View.VISIBLE);

                    video_view.setVideoURI(Uri.parse(select_file));
                    video_view.start();
                }

            }

            @Override
            public void onComplete() {
                counter = 0;
                finish();

            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });

        skip.setOnTouchListener(onTouchListener);

        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();
            }
        });

        reverse.setOnTouchListener(onTouchListener);

    }

    //    public void setstories(int counter) {
    public void setstories() {
        try {
            for (int i = 0; i < dbHelper.retreiveimagefromdb1().size(); i++) {
                PROGRESS_COUNT++;
            }

            arrayList.addAll(dbHelper.retreiveimagefromdb1());
            storiesProgressView.setStoriesCount(PROGRESS_COUNT);

//        if (counter > 0) {
//            if (PROGRESS_COUNT >= 0) {
//                storiesProgressView.startStories(counter);
//            } else {
//                finish();
//                return;
//            }
//            int lastval = arrayList.get(arrayList.size() - 1);
////            if (counter == arrayList.size() - 1) {
//            if (counter == arrayList.size() - 1) {
//                finish();
//                return;
//            } else {
//
//                storiesProgressView.startStories(counter);
//
//            }


//            int next = counter + 1;
            if (counter <= (arrayList.size() - 1)) {
                storiesProgressView.startStories(counter);
            } else {
                finish();
                return;
            }


            String select_file = arrayList.get(counter).getImage();
            image_id = arrayList.get(counter).getId();

            String lastWord = select_file.substring(select_file.lastIndexOf(".") + 1);
            Log.e("LLL_v_image_path-->", lastWord);

            try {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(select_file);
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                timeInMillisec = Long.parseLong(time);
                retriever.release();
                duration = FileUtils.convertMillieToHMmSs(timeInMillisec); //use this duration
                Log.e("LLL_n_duration", String.valueOf(duration));
                Log.e("LLL_ne_duration", String.valueOf(timeInMillisec));

            } catch (RuntimeException e) {
                e.printStackTrace();
            }

            if (duration == null) {
                storiesProgressView.setStoryDuration(3000L);
            } else {
                storiesProgressView.setStoryDuration(timeInMillisec);
            }

            if (lastWord.equalsIgnoreCase("jpg") || lastWord.equalsIgnoreCase("jpeg") || lastWord.equalsIgnoreCase("png") || lastWord.equalsIgnoreCase("svg") || lastWord.equalsIgnoreCase("webp")) {
                image.setVisibility(View.VISIBLE);
                video_view.setVisibility(View.GONE);

                image.setImageURI(Uri.parse(select_file));
                blur_image.setImageURI(Uri.parse(select_file));
            } else {
                image.setVisibility(View.GONE);
                video_view.setVisibility(View.VISIBLE);

                video_view.setVideoURI(Uri.parse(select_file));
                video_view.start();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    storiesProgressView.pause();

                    if (video_view.getVisibility() == View.VISIBLE) {
                        if (video_view.isPlaying()) {
                            video_view.pause();
                        }
                    }

                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.resume();

                    if (video_view.getVisibility() == View.VISIBLE) {
                        if (!video_view.isPlaying()) {
                            video_view.start();
                        }
                    }
                    return limit < now - pressTime;
            }
            return false;
        }
    };

    @Override
    protected void onDestroy() {
        // Very important !
        storiesProgressView.destroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        storiesProgressView.destroy();
        finish();
    }
}