package com.example.friendfield.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.friendfield.Adapter.StickerAdapter;
import com.example.friendfield.R;
import com.example.friendfield.stickers.BitmapStickerIcon;
import com.example.friendfield.stickers.DeleteIconEvent;
import com.example.friendfield.stickers.DrawableSticker;
import com.example.friendfield.stickers.FlipHorizontallyEvent;
import com.example.friendfield.stickers.HelloIconEvent;
import com.example.friendfield.stickers.Sticker;
import com.example.friendfield.stickers.StickerView;
import com.example.friendfield.stickers.TextSticker;
import com.example.friendfield.stickers.ZoomIconEvent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class StatusPhotoEditingActivity extends AppCompatActivity {
    ImageView iv_display_image;
    ImageView btn_font;
    ImageView btn_sticker;
    ImageView btn_crop;
    ImageView btn_close;

    RelativeLayout rel_type_msg;
    ImageView ic_emoji;
    EditText txt_chating;
    RelativeLayout btn_send;

    LinearLayout lin_icons;
    String FilePath;
    final int PIC_CROP = 1;

    RecyclerView recycler_sticker;
    StickerView sticker_view;
    private TextSticker sticker;
    Sticker mstSticker;
    EditText et_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_photo_editing);

        iv_display_image = findViewById(R.id.iv_display_image);

        btn_font = findViewById(R.id.btn_font);
        btn_sticker = findViewById(R.id.btn_sticker);
        btn_crop = findViewById(R.id.btn_crop);
        btn_close = findViewById(R.id.btn_close);

        rel_type_msg = findViewById(R.id.rel_type_msg);

        ic_emoji = findViewById(R.id.ic_emoji);
        txt_chating = findViewById(R.id.txt_chating);
        btn_send = findViewById(R.id.btn_send);
        lin_icons = findViewById(R.id.lin_icons);
        et_text = findViewById(R.id.et_text);

        et_text.setVisibility(View.GONE);

        recycler_sticker = findViewById(R.id.recycler_sticker);

        sticker_view = findViewById(R.id.sticker_view);
        recycler_sticker.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));

        FilePath = getIntent().getStringExtra("FilePath");

        BitmapStickerIcon deleteIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                R.drawable.sticker_ic_close_white_18dp),
                BitmapStickerIcon.LEFT_TOP);
        deleteIcon.setIconEvent(new DeleteIconEvent());

        BitmapStickerIcon zoomIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                R.drawable.sticker_ic_scale_white_18dp),
                BitmapStickerIcon.RIGHT_BOTOM);
        zoomIcon.setIconEvent(new ZoomIconEvent());

        BitmapStickerIcon flipIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                R.drawable.sticker_ic_flip_white_18dp),
                BitmapStickerIcon.RIGHT_TOP);
        flipIcon.setIconEvent(new FlipHorizontallyEvent());

//        BitmapStickerIcon heartIcon =
//                new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.ic_aa),
//                        BitmapStickerIcon.LEFT_BOTTOM);
//        heartIcon.setIconEvent(new HelloIconEvent());

//        sticker_view.setIcons(Arrays.asList(deleteIcon, zoomIcon, flipIcon, heartIcon));
        sticker_view.setIcons(Arrays.asList(deleteIcon, zoomIcon, flipIcon));

//        sticker_view.setBackgroundColor(Color.WHITE);
        sticker_view.setLocked(false);
        sticker_view.setConstrained(true);

        sticker = new TextSticker(this);

        sticker.setDrawable(ContextCompat.getDrawable(getApplicationContext(),
                R.drawable.sticker_transparent_background));
        sticker.setText(et_text.getText().toString().trim());
        sticker.setTextColor(Color.BLACK);
        sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        sticker.resizeText();

        sticker_view.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {
                Log.d("LLL_TAG", "onStickerAdded");
            }

            @Override
            public void onStickerClicked(@NonNull Sticker sticker) {
                //sticker_view.removeAllSticker();
                if (sticker instanceof TextSticker) {
                    ((TextSticker) sticker).setTextColor(Color.RED);
                    sticker_view.replace(sticker);
                    sticker_view.invalidate();
                }
                Log.d("LLL_TAG", "onStickerClicked");
            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {
                Log.d("LLL_TAG", "onStickerDeleted");
            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {
                Log.d("LLL_TAG", "onStickerDragFinished");
            }

            @Override
            public void onStickerTouchedDown(@NonNull Sticker sticker) {
                Log.d("LLL_TAG", "onStickerTouchedDown");
            }

            @Override
            public void onStickerZoomFinished(@NonNull Sticker sticker) {
                Log.d("LLL_TAG", "onStickerZoomFinished");
            }

            @Override
            public void onStickerFlipped(@NonNull Sticker sticker) {
                Log.d("LLL_TAG", "onStickerFlipped");
            }

            @Override
            public void onStickerDoubleTapped(@NonNull Sticker sticker) {
                Log.d("LLL_TAG", "onDoubleTapped: double tap will be with two click");
            }
        });


        recycler_sticker.setVisibility(View.GONE);

        iv_display_image.setImageBitmap(TakePhotoActivity.bitmap);
//        iv_display_image.setImageBitmap(flip(TakePhotoActivity.bitmap ,FLIP_HORIZONTAL));

//        Glide.with(getApplicationContext()).load(FilePath).into(iv_display_image);

        StickerAdapter stickerAdapter = new StickerAdapter(getApplicationContext(), addAssetsImages(getApplicationContext(), "stickers"), new StickerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(String imgsticker) {
                recycler_sticker.setVisibility(View.GONE);
                InputStream inputstream = null;
                try {
                    inputstream = getAssets().open("stickers/"
                            + imgsticker);
                    Drawable drawable = Drawable.createFromStream(inputstream, null);

                    sticker_view.addSticker(new DrawableSticker(drawable));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        recycler_sticker.setAdapter(stickerAdapter);

        btn_font.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                et_text.setVisibility(View.VISIBLE);
            }
        });

        btn_sticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycler_sticker.setVisibility(View.VISIBLE);
            }
        });

        btn_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performCrop(getImageUri(getApplicationContext(), TakePhotoActivity.bitmap));
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ic_emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ArrayList<String> listsize = addAssetsImages(getApplicationContext(), "stickers");
        Log.e("LLL_list_size-->", String.valueOf(listsize.size()));
        Log.e("LLL_list_emoji-->", String.valueOf(listsize.get(0)));


    }

    public static ArrayList<String> addAssetsImages(Context mContext, String folderPath) {
        ArrayList<String> pathList = new ArrayList<>();
        try {
            String[] files = mContext.getAssets().list(folderPath);
            for (String name : files) {
//                pathList.add(folderPath + File.separator + name);
                pathList.add(name);
                Log.e("pathList item", folderPath + File.separator + name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pathList;
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties here
            cropIntent.putExtra("crop", true);
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PIC_CROP) {
            if (data != null) {

                Uri imageUri = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageUri);
                    iv_display_image.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }

//                 get the returned data
//                Bundle extras = data.getExtras();
//                // get the cropped bitmap
//                Bitmap selectedBitmap = extras.getParcelable("data");

            }
        }
    }

}