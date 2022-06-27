package com.example.friendfield.Adapter;

import android.app.Activity;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.friendfield.Activity.OtherStoryViewActivity;
import com.example.friendfield.Activity.ViewStoryActivity;
import com.example.friendfield.Model.Story.ViewStoryModel;
import com.example.friendfield.R;
import com.example.friendfield.Utils.DBHelper;
import com.example.friendfield.Utils.FileUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.shts.android.storiesprogressview.StoriesProgressView;

public class ViewStoryAdapter extends BaseAdapter {
    Context context;
//    ArrayList<ViewStoryModel> arrayList;

    int[][] arrayList;

    StoriesProgressView storiesProgressView;
    ImageView image;
    ImageView blur_image;
    CircleImageView img_user;
    TextView txt_user_name;
    TextView txt_time;
    EditText txt_chating;
    ImageView iv_send;
    ImageView iv_close;

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

    DBHelper dbHelper;
    VideoView video_view;
    String duration;
    long timeInMillisec;
    String username;

    //    public ViewStoryAdapter(Context context, ArrayList<ViewStoryModel> arrayList) {
    public ViewStoryAdapter(Context context, int[][] arrayList, String username) {
        this.context = context;
        this.arrayList = arrayList;
        this.username = username;
    }

    @Override
    public int getCount() {
        return arrayList.length;
    }

    @Override
    public Object getItem(int position) {
        return arrayList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_view_story, parent, false);

        storiesProgressView = view.findViewById(R.id.storiesProgressView);
        image = view.findViewById(R.id.image);
        blur_image = view.findViewById(R.id.blur_image);
        reverse = view.findViewById(R.id.reverse);
        skip = view.findViewById(R.id.skip);
        video_view = view.findViewById(R.id.video_view);

        img_user = view.findViewById(R.id.img_user);
        txt_user_name = view.findViewById(R.id.txt_user_name);
        txt_time = view.findViewById(R.id.txt_time);
        txt_chating = view.findViewById(R.id.txt_chating);
        iv_send = view.findViewById(R.id.iv_send);
        iv_close = view.findViewById(R.id.iv_close);

        txt_user_name.setText(username);

        blur_image.setBackgroundColor(-16777216);

//        dbHelper = new DBHelper(getApplicationContext());
//        dbHelper.open();
//
        for (int i = 0; i < arrayList.length; i++) {
            PROGRESS_COUNT++;
        }
//
//        arrayList.addAll(dbHelper.retreiveimagefromdb1());
//
//        Log.e("LLL_story-->", String.valueOf(arrayList.size()));

        storiesProgressView.setStoriesCount(PROGRESS_COUNT);

//        storiesProgressView.setStoryDuration(3000L);
//        storiesProgressView.startStories(counter);
        storiesProgressView.startStories(position);

//        String select_file = arrayList.get(counter).getImage();
//        String select_file = String.valueOf(arrayList[counter]);
//
//        String lastWord = select_file.substring(select_file.lastIndexOf(".") + 1);
//        Log.e("LLL_v_image_path-->", lastWord);
//
//
//        try {
//            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
////                retriever.setDataSource(select_file, new HashMap<String, String>());
//            retriever.setDataSource(select_file);
//            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//            timeInMillisec = Long.parseLong(time);
//            retriever.release();
//            duration = FileUtils.convertMillieToHMmSs(timeInMillisec); //use this duration
//            Log.e("LLL_n_duration", String.valueOf(duration));
//            Log.e("LLL_ne_duration", String.valueOf(timeInMillisec));
//
//        } catch (RuntimeException e) {
//            e.printStackTrace();
//        }
//
//        if (duration == null) {
        storiesProgressView.setStoryDuration(3000L);
//        } else {
//            storiesProgressView.setStoryDuration(timeInMillisec);
//        }

//        if (lastWord.equalsIgnoreCase("jpg") || lastWord.equalsIgnoreCase("jpeg") || lastWord.equalsIgnoreCase("png") || lastWord.equalsIgnoreCase("svg") || lastWord.equalsIgnoreCase("webp")) {
//            image.setVisibility(View.VISIBLE);
//            video_view.setVisibility(View.GONE);

//            image.setImageURI(Uri.parse(arrayList.get(counter)));
//            image.setImageURI(Uri.parse(select_file));

        for (int i = 0; i < arrayList[position].length; i++) {

            for (int j = 0; j < arrayList[i].length; j++) {

//                image.setImageResource(arrayList[i][j]);
                image.setImageResource(arrayList[counter][j]);
                blur_image.setImageResource(arrayList[counter][j]);
//                blur_image.setImageResource(arrayList[i][j]);

//                int finalI = i;
                int finalJ = j;
                int finalI = i;

                storiesProgressView.setStoriesListener(new StoriesProgressView.StoriesListener() {
                    @Override
                    public void onNext() {
                        try {
//                image.setImageResource(images[++counter]);
//                blur_image.setImageResource(images[++counter]);
//                String select_file = arrayList.get(++counter).getImage();
                            int pos = finalJ + 1;
                            int select_file = arrayList[finalI][pos];
//                        int select_file = arrayList[++counter][finalJ];
//                    int pos = arrayList[position][i]finalI + 1;
//                    int select_file = pos;

//                String lastWord = select_file.substring(select_file.lastIndexOf(".") + 1);
//                Log.e("LLL_next_image-->", lastWord);

//                long duration = FileUtils.getDuration(getApplicationContext(), Uri.parse(select_file));

//                try {
//                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                    retriever.setDataSource(select_file);
//                    String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//                    timeInMillisec = Long.parseLong(time);
//                    retriever.release();
//                    duration = FileUtils.convertMillieToHMmSs(timeInMillisec); //use this duration
//                    Log.e("LLL_n_duration", String.valueOf(duration));
//                    Log.e("LLL_ne_duration", String.valueOf(timeInMillisec));
//
//                } catch (RuntimeException e) {
//                    e.printStackTrace();
//                }
//
//
//                if (duration == null) {
//                storiesProgressView.setStoryDuration(3000L);
//                } else {
//                    storiesProgressView.setStoryDuration(timeInMillisec);
//                }

//                if (lastWord.equalsIgnoreCase("jpg") || lastWord.equalsIgnoreCase("jpeg") || lastWord.equalsIgnoreCase("png") || lastWord.equalsIgnoreCase("svg") || lastWord.equalsIgnoreCase("webp")) {
//                    image.setVisibility(View.VISIBLE);
//                    video_view.setVisibility(View.GONE);

//                image.setImageURI(Uri.parse(select_file));
//                blur_image.setImageURI(Uri.parse(select_file));

                            image.setImageResource(select_file);
                            blur_image.setImageResource(select_file);

//                } else {
//                    image.setVisibility(View.GONE);
//                    video_view.setVisibility(View.VISIBLE);
//
//                    video_view.setVideoURI(Uri.parse(select_file));
//                    video_view.start();
//                }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onPrev() {
                        try {
                            if ((finalJ - 1) < 0) return;
//                image.setImageResource(images[--counter]);
//                blur_image.setImageResource(images[--counter]);
//                String select_file = arrayList.get(--counter).getImage();
//                    int pos = 1 - finalI;
                            int pos = finalJ - 1;
//                    int select_file = pos;
//                        int select_file = arrayList[finalI][pos];
//                        int select_file = arrayList[finalI][pos];
                            int select_file = arrayList[finalI][pos];

//                    int select_file = arrayList[--counter];

//                String lastWord = select_file.substring(select_file.lastIndexOf(".") + 1);
//                Log.e("LLL_prev_image-->", lastWord);
//
//                try {
//                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
////                retriever.setDataSource(select_file, new HashMap<String, String>());
//                    retriever.setDataSource(select_file);
//                    String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//                    timeInMillisec = Long.parseLong(time);
//                    retriever.release();
//                    duration = FileUtils.convertMillieToHMmSs(timeInMillisec); //use this duration
//                    Log.e("LLL_n_duration", String.valueOf(duration));
//                    Log.e("LLL_ne_duration", String.valueOf(timeInMillisec));
//
//                } catch (RuntimeException e) {
//                    e.printStackTrace();
//                }
//
//                if (duration == null) {
//                storiesProgressView.setStoryDuration(3000L);
//                } else {
//                    storiesProgressView.setStoryDuration(timeInMillisec);
//                }

//                if (lastWord.equalsIgnoreCase("jpg") || lastWord.equalsIgnoreCase("jpeg") || lastWord.equalsIgnoreCase("png") || lastWord.equalsIgnoreCase("svg") || lastWord.equalsIgnoreCase("webp")) {
//                    image.setVisibility(View.VISIBLE);
//                    video_view.setVisibility(View.GONE);

//                image.setImageURI(Uri.parse(select_file));
//                blur_image.setImageURI(Uri.parse(select_file));

                            image.setImageResource(select_file);
                            blur_image.setImageResource(select_file);

//                } else {
//                    image.setVisibility(View.GONE);
//                    video_view.setVisibility(View.VISIBLE);
//
//                    video_view.setVideoURI(Uri.parse(select_file));
//                    video_view.start();
//                }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onComplete() {
//                image.setImageResource(images[counter++]);
//                finish();

//                        ((OtherStoryViewActivity) context).finish();

//                int current_pos = position + 1;
//                int arr = arrayList[current_pos];
//                for (int i = 0; i < arr; i++) {
//                    PROGRESS_COUNT++;
//                }
////
//                Log.e("LLL_t_co-->", String.valueOf(PROGRESS_COUNT));
////        arrayList.addAll(dbHelper.retreiveimagefromdb1());
////
////        Log.e("LLL_story-->", String.valueOf(arrayList.size()));
//
//                storiesProgressView.setStoriesCount(PROGRESS_COUNT);
//
////        storiesProgressView.setStoryDuration(3000L);
//                storiesProgressView.startStories(counter);


                    }
                });

            }

        }


//            blur_image.setImageURI(Uri.parse(select_file));
//        } else {
//            image.setVisibility(View.GONE);
//            video_view.setVisibility(View.VISIBLE);
//
////            video_view.setVideoURI(Uri.parse(arrayList.get(counter)));
//            video_view.setVideoURI(Uri.parse(select_file));
//            video_view.start();
//
//        }

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewStoryActivity) context).finish();
//                onBackPressed();
            }
        });

//        blur_image.setImageResource(images[counter]);

//        image.setImageResource(arrayList.get(counter));


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

        return view;
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

}
