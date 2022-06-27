package com.example.friendfield.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;

import com.example.friendfield.Fragment.BaseFragment;
import com.example.friendfield.Fragment.CropFragment;
import com.example.friendfield.Fragment.PhotoEditorFragment;
import com.example.friendfield.MainActivity;
import com.example.friendfield.R;
import com.example.friendfield.Utils.FragmentUtil;
import com.example.friendfield.Utils.ImageEditor;

public class ImageEditActivity extends AppCompatActivity implements PhotoEditorFragment.OnFragmentInteractionListener,
        CropFragment.OnFragmentInteractionListener {
    private Rect cropRect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit);

        String imagePath = getIntent().getStringExtra(ImageEditor.EXTRA_IMAGE_PATH);
        Boolean isFromCamera = getIntent().getBooleanExtra("isFromCamera", false);
        Boolean isFrontCamera = getIntent().getBooleanExtra("isFrontCamera", false);
//        Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra(ImageEditor.EXTRA_IMAGE_PATH);

//        byte[] byteArray = getIntent().getByteArrayExtra(ImageEditor.EXTRA_IMAGE_PATH);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        if (imagePath != null) {

            FragmentUtil.addFragment(this, R.id.fragment_container,
                    PhotoEditorFragment.newInstance(imagePath, isFromCamera, isFrontCamera));
//                    PhotoEditorFragment.newInstance(imagePath, isFromCamera));
        }
    }

    @Override
    public void onCropClicked(Bitmap bitmap) {
        FragmentUtil.replaceFragment(this, R.id.fragment_container,
                CropFragment.newInstance(bitmap, cropRect));
    }

    @Override
    public void onDoneClicked(String imagePath) {
//        Log.e("LLL_final_path--->", imagePath);
        Intent intent = new Intent();
        intent.putExtra(ImageEditor.EXTRA_EDITED_PATH, imagePath);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onImageCropped(Bitmap bitmap, Rect cropRect) {
        this.cropRect = cropRect;
        PhotoEditorFragment photoEditorFragment =
                (PhotoEditorFragment) FragmentUtil.getFragmentByTag(this,
                        PhotoEditorFragment.class.getSimpleName());
        if (photoEditorFragment != null) {

            photoEditorFragment.setImageWithBitmapRect(bitmap, cropRect);
            photoEditorFragment.reset();

//            getSupportFragmentManager().popBackStack();
            FragmentUtil.removeFragment(this,
                    (BaseFragment) FragmentUtil.getFragmentByTag(this, CropFragment.class.getSimpleName()));
        }
    }

    @Override
    public void onCancelCrop() {
        getSupportFragmentManager().popBackStack();

//        FragmentUtil.removeFragment(this,
//                (BaseFragment) FragmentUtil.getFragmentByTag(this, CropFragment.class.getSimpleName()));
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}