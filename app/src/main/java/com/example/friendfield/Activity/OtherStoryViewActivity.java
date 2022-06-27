package com.example.friendfield.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.friendfield.Adapter.ViewStoryAdapter;
import com.example.friendfield.R;
import com.example.friendfield.Utils.Utility;

public class OtherStoryViewActivity extends AppCompatActivity {
    AdapterViewFlipper adapterViewFlipper;

    int[][] total_data;
    int[] user_status_data;
    String UserName;

    private ProgressBar[] mProgressBar;
    private int flipperCount;
    private ObjectAnimator animation;

    int[] IMAGES = {
            R.drawable.img_8,
            R.drawable.img_9,
            R.drawable.img_11,
            R.drawable.img_12
    };

    String[] NAMES = {
            "Deosai National Park",
            "Lake Dudipatsar",
            "Rama Meadows",
            "Lower Kachura Lake"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_story_view);

        adapterViewFlipper = findViewById(R.id.adapterViewFlipper);

        total_data = (int[][]) getIntent().getSerializableExtra("Total_Images");
        UserName = getIntent().getStringExtra("UserName");

        int totalsize = total_data.length;
        user_status_data = new int[totalsize];

        for (int i = 0; i < totalsize; i++) {
//            user_status_data[i];
//            user_status_data = total_data[i];
            Log.e("LLL_img-->", String.valueOf(total_data[i]));

//            for (int val : total_data) {
//                Log.e("LLL_img1-->", String.valueOf(val));
//            }
        }

//        for (int i = 0; i < total_data.length; i++) {
//
//        }

        ViewStoryAdapter viewStoryAdapter = new ViewStoryAdapter(OtherStoryViewActivity.this, total_data, UserName);
        adapterViewFlipper.setAdapter(viewStoryAdapter);
//        adapterViewFlipper.setFlipInterval(2600);

        flipperCount = adapterViewFlipper.getCount();
        adapterViewFlipper.startFlipping();

//        adapterViewFlipper.setAutoStart(true);

//        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), NAMES, IMAGES);
//        adapterViewFlipper.setAdapter(customAdapter);
//        adapterViewFlipper.setFlipInterval(2600);
//        adapterViewFlipper.startFlipping();
//        adapterViewFlipper.setAutoStart(true);

//        mProgressBar = new ProgressBar[flipperCount];
//        for (int i = 0; i < flipperCount; i++) {
//            mProgressBar[i] = new ProgressBar(getApplicationContext(), null, android.R.attr.progressBarStyleHorizontal);
//            mProgressBar[i].setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
//            Utility.setMargins(mProgressBar[i]);
//            Utility.setProgressMax(mProgressBar[i]);
//            mProgressBar[i].setMax(100 * 100);
//            mProgressBar[i].getProgress();
//            ViewGroup mViewGroup = findViewById(R.id.my_status_parent_progressbar);
//            mViewGroup.addView(mProgressBar[i]);
//            adapterViewFlipper.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//                @Override
//                public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
//                    setProgressAnimate(mProgressBar[adapterViewFlipper.getDisplayedChild()]);
////                    StatusItem statusItem = statusItemList.get(adapterViewFlipper.getDisplayedChild());
//                    if (adapterViewFlipper.getDisplayedChild() == adapterViewFlipper.getCount() - 1) {
//                        adapterViewFlipper.stopFlipping();
//                        animation.addListener(new Animator.AnimatorListener() {
//                            @Override
//                            public void onAnimationStart(Animator animator) {
//
//                            }
//
//                            @Override
//                            public void onAnimationEnd(Animator animator) {
//                                finish();
//                            }
//
//                            @Override
//                            public void onAnimationCancel(Animator animator) {
//
//                            }
//
//                            @Override
//                            public void onAnimationRepeat(Animator animator) {
//
//                            }
//                        });
//                    }
//                    animation.start();
//                }
//            });
//        }

    }

    private void setProgressAnimate(ProgressBar pb) {
        animation = ObjectAnimator.ofInt(pb, "progress", pb.getProgress(), 100 * 100);
        animation.setDuration(2500);
        animation.setInterpolator(new LinearInterpolator());
    }

    class CustomAdapter extends BaseAdapter {
        Context context;
        int[] images;
        String[] names;
        LayoutInflater inflater;

        public CustomAdapter(Context applicationContext, String[] names, int[] images) {
            this.context = applicationContext;
            this.images = images;
            this.names = names;
            inflater = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            view = inflater.inflate(R.layout.custom_adapter_layout, null);

            // Link those objects with their respective id's
            // that we have given in .XML file
            TextView name = (TextView) view.findViewById(R.id.name);
            ImageView image = (ImageView) view.findViewById(R.id.image);

            // Set the data in text view
            name.setText(names[position]);

            // Set the image in Image view
            image.setImageResource(images[position]);
            return view;
        }
    }
}