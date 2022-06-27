package com.example.friendfield.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.media.CamcorderProfile;
import android.media.Image;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.friendfield.MainActivity;
import com.example.friendfield.R;
import com.example.friendfield.Utils.FileUtils;
import com.example.friendfield.Utils.ImageEditor;
import com.example.friendfield.view.CameraPreview;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TakePhotoActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private Camera mCamera;
    private CameraPreview mPreview;
    private Camera.PictureCallback mPicture;
    private ImageView capture, switchCamera;
    private Context myContext;
    private LinearLayout cameraPreview;
    private boolean cameraFront = false;
    public static Bitmap bitmap = null;
    ImageView iv_display_image;
    ImageView btn_flash;
    ImageView iv_stop;

    private MediaRecorder recorder;

    ImageView btn_close;
    LinearLayout lin_camera;
    ImageView iv_collection;

    LinearLayout lin_icons;
    private boolean flashmode = false;

    private final String[] flashModes = {Camera.Parameters.FLASH_MODE_AUTO, Camera.Parameters.FLASH_MODE_ON, Camera.Parameters.FLASH_MODE_OFF};
    private int fmi = 0; //flash mode index

    final static int FLIP_VERTICAL = 1;
    final static int FLIP_HORIZONTAL = 2;
    public static final int PICK_IMAGE = 1;

    private boolean enabledEditorText = true;
    private boolean enabledEditorPaint = false;
    private boolean enabledEditorSticker = true;
    private boolean enableEditorCrop = true;
    private boolean enableFilters = false;

    private MediaRecorder mMediaRecorder;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private View mToggleButton;
    private boolean mInitSuccesful;
    private Point mWindowSize;

    String video_output;

    public static Bitmap flip(Bitmap src, int type) {
        // create new matrix for transformation
        Matrix matrix = new Matrix();
        // if vertical
        if (type == FLIP_VERTICAL) {
            matrix.preScale(1.0f, -1.0f);
        }
        // if horizonal
        else if (type == FLIP_HORIZONTAL) {
            matrix.preScale(-1.0f, 1.0f);
            // unknown type
        } else {
            return null;
        }

        // return transformed image
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        capture = findViewById(R.id.btnCam);
        iv_display_image = findViewById(R.id.iv_display_image);

        btn_flash = findViewById(R.id.btn_flash);

        btn_close = findViewById(R.id.btn_close);
        lin_camera = findViewById(R.id.lin_camera);
        iv_collection = findViewById(R.id.iv_collection);
        iv_stop = findViewById(R.id.iv_stop);

        lin_icons = findViewById(R.id.lin_icons);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        myContext = this;

        if (mWindowSize == null) mWindowSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(mWindowSize);

        mCamera = Camera.open();

//        Camera.Parameters params = mCamera.getParameters();
//
//// Check what resolutions are supported by your camera
//        List<Size> sizes = params.getSupportedPictureSizes();
//
//// Iterate through all available resolutions and choose one.
//// The chosen resolution will be stored in mSize.
//        Size mSize = null;
//        for (Size size : sizes) {
//            Log.i("TAG", "Available resolution: " + size.width + " " + size.height);
//            mSize = size;
//        }
//
//
//        Log.i("TAG", "Chosen resolution: " + mSize.width + " " + mSize.height);
//        params.setPictureSize(mSize.width, mSize.height);
//        mCamera.setParameters(params);

        cameraPreview = (LinearLayout) findViewById(R.id.cPreview);

        mPreview = new CameraPreview(myContext, mCamera);

        mCamera.setDisplayOrientation(90);

        cameraPreview.addView(mPreview);

//        mPreview.setAspectRatio(mWindowSize.x, mWindowSize.y);

        Camera.Parameters params = mCamera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);

        mCamera.setParameters(params);

//        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
//        mHolder = mSurfaceView.getHolder();
//        mHolder.addCallback(this);
//        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//
//        if(mCamera == null) {
//            mCamera = Camera.open();
//            mCamera.unlock();
//        }
//
//        if(mMediaRecorder == null)  mMediaRecorder = new MediaRecorder();
//        mMediaRecorder.setPreviewDisplay(mHolder.getSurface());
//        mMediaRecorder.setCamera(mCamera);

        recorder = new MediaRecorder();


        capture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                capture.setVisibility(View.GONE);
                iv_stop.setVisibility(View.VISIBLE);
                prepareRecorder();
                return true;
            }
        });

        iv_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    capture.setVisibility(View.VISIBLE);
                    iv_stop.setVisibility(View.GONE);

                    recorder.stop();

//                recording = false;

                    // Let's initRecorder so we can record again
//                initRecorder();

//                    prepareRecorder();

                    Toast.makeText(TakePhotoActivity.this, "Stopped Recording", Toast.LENGTH_SHORT).show();// toast shows a display of little sorts

                    Intent intent = new Intent(getApplicationContext(), ImageEditActivity.class);
//                    Intent intent = new Intent(getApplicationContext(), VideoEditActivity.class);
                    intent.putExtra(ImageEditor.EXTRA_IMAGE_PATH, video_output);
                    intent.putExtra(ImageEditor.EXTRA_IS_PAINT_MODE, enabledEditorPaint);
                    intent.putExtra(ImageEditor.EXTRA_IS_STICKER_MODE, enabledEditorSticker);
                    intent.putExtra(ImageEditor.EXTRA_IS_TEXT_MODE, enabledEditorText);
                    intent.putExtra(ImageEditor.EXTRA_IS_CROP_MODE, false);
                    intent.putExtra(ImageEditor.EXTRA_HAS_FILTERS, enableFilters);
                    startActivity(intent);


                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }

            }
        });

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//
//                startActivityForResult(intent, 7);

//                mCamera.takePicture(null, null, mPicture);

                /*************/

                mCamera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
//                        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                        Bitmap rotated = BitmapFactory.decodeByteArray(data, 0, data.length);

                        if (cameraFront) {
                            Matrix matrix = new Matrix();
                            matrix.postRotate(-90);

                            Bitmap front_bitmap = Bitmap.createBitmap(rotated, 0, 0, rotated.getWidth(), rotated.getHeight(),
                                    matrix, true);

                            bitmap = flip(front_bitmap, FLIP_HORIZONTAL);

//                            String imgString = Base64.encodeToString(getBytesFromBitmap(bitmap),
//                                    Base64.NO_WRAP);

                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            String imgString = String.valueOf(getImageUri(getApplicationContext(), bitmap));

//                            String imgString = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);

                            Intent intent = new Intent(getApplicationContext(), ImageEditActivity.class);
                            intent.putExtra(ImageEditor.EXTRA_IMAGE_PATH, imgString);
                            intent.putExtra("isFromCamera", true);
                            intent.putExtra(ImageEditor.EXTRA_IS_PAINT_MODE, enabledEditorPaint);
                            intent.putExtra(ImageEditor.EXTRA_IS_STICKER_MODE, enabledEditorSticker);
                            intent.putExtra(ImageEditor.EXTRA_IS_TEXT_MODE, enabledEditorText);
                            intent.putExtra(ImageEditor.EXTRA_IS_CROP_MODE, enableEditorCrop);
                            intent.putExtra(ImageEditor.EXTRA_HAS_FILTERS, enableFilters);
                            startActivity(intent);
                        } else {
                            Matrix matrix = new Matrix();
                            matrix.postRotate(90);

                            Bitmap bitmap = Bitmap.createBitmap(rotated, 0, 0, rotated.getWidth(), rotated.getHeight(),
                                    matrix, true);

//                            String imgString = Base64.encodeToString(getBytesFromBitmap(bitmap), Base64.NO_WRAP);

                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            String imgString = String.valueOf(getImageUri(getApplicationContext(), bitmap));
//                            String imgString = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);

                            Intent intent = new Intent(getApplicationContext(), ImageEditActivity.class);
                            intent.putExtra(ImageEditor.EXTRA_IMAGE_PATH, imgString);
                            intent.putExtra("isFromCamera", true);
                            intent.putExtra(ImageEditor.EXTRA_IS_PAINT_MODE, enabledEditorPaint);
                            intent.putExtra(ImageEditor.EXTRA_IS_STICKER_MODE, enabledEditorSticker);
                            intent.putExtra(ImageEditor.EXTRA_IS_TEXT_MODE, enabledEditorText);
                            intent.putExtra(ImageEditor.EXTRA_IS_CROP_MODE, enableEditorCrop);
                            intent.putExtra(ImageEditor.EXTRA_HAS_FILTERS, enableFilters);
                            startActivity(intent);
                        }

                    }
                });
            }
        });

        switchCamera =

                findViewById(R.id.btnSwitch);

        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the number of cameras
                int camerasNumber = Camera.getNumberOfCameras();
                if (camerasNumber > 1) {
                    //release the old camera instance
                    //switch camera, from the front and the back and vice versa

                    releaseCamera();
                    chooseCamera();
                } else {

                }
            }
        });

        mCamera.startPreview();

        btn_flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                flashOnButton();
//                changeFlashMode();
                setFlashMode();
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(TakePhotoActivity.this)
//                        .crop()
                        .galleryOnly()
//                        .maxResultSize(1080, 1080)
                        .start(PICK_IMAGE);
            }
        });

    }

    private void initRecorder(Surface surface) throws IOException {
        // It is very important to unlock the camera before doing setCamera
        // or it will results in a black preview
//        if(mCamera == null) {
//            mCamera = Camera.open();
//            mCamera.unlock();
//        }
//
//        if(mMediaRecorder == null)  mMediaRecorder = new MediaRecorder();
//        mMediaRecorder.setPreviewDisplay(surface);
//        mMediaRecorder.setCamera(mCamera);

        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
        //       mMediaRecorder.setOutputFormat(8);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setVideoEncodingBitRate(512 * 1000);
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoSize(640, 480);
        mMediaRecorder.setOutputFile(commonDocumentDirPath("FriendField") + "/"
                + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date())
                + ".mp4");

        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            // This is thrown if the previous calls are not called with the
            // proper order
            e.printStackTrace();
        }

        mInitSuccesful = true;
    }

    private void prepareRecorder() {
        try {
//            recorder = new MediaRecorder();
//        recorder.setPreviewDisplay(mPreview);
//        recorder.setPreviewDisplay(surfaceHolder.getSurface());
//        if (usecamera) {
            mCamera.unlock();
            recorder.setCamera(mCamera);
//        }
//            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
//            recorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//            recorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
//            CamcorderProfile camcorderProfile_HQ = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
//            recorder.setProfile(camcorderProfile_HQ);

//            recorder.setVideoEncodingBitRate(512 * 1000);
            recorder.setVideoEncodingBitRate(10000000);
            recorder.setVideoFrameRate(30);
//            recorder.setVideoSize(640, 480);
            recorder.setVideoSize(3840, 2160);

            video_output = commonDocumentDirPath("FriendField") + "/"
                    + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date())
                    + ".mp4";

            recorder.setOutputFile(video_output);

            recorder.setMaxDuration(15000);
            recorder.setOrientationHint(90);

            if (recorder != null) {
                try {
                    recorder.prepare();
//                    mCamera.startPreview();
                    recorder.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
//                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public static File commonDocumentDirPath(String FolderName) {
        File dir = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + FolderName);
        } else {
            dir = new File(Environment.getExternalStorageDirectory() + "/" + FolderName);
        }

        // Make sure the path directory exists.
        if (!dir.exists()) {
            // Make it, if it doesn't exit
            boolean success = dir.mkdirs();
            if (!success) {
                dir = null;
            }
        }
        return dir;
    }


    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
//        if (requestCode == RESULT_OK) {

            try {
                Uri selectedImageUri = data.getData();

                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                Intent intent = new Intent(getApplicationContext(), ImageEditActivity.class);
                intent.putExtra(ImageEditor.EXTRA_IMAGE_PATH, selectedImageUri.toString());
                intent.putExtra("isFromCamera", true);
                intent.putExtra(ImageEditor.EXTRA_IS_PAINT_MODE, enabledEditorPaint);
                intent.putExtra(ImageEditor.EXTRA_IS_STICKER_MODE, enabledEditorSticker);
                intent.putExtra(ImageEditor.EXTRA_IS_TEXT_MODE, enabledEditorText);
                intent.putExtra(ImageEditor.EXTRA_IS_CROP_MODE, enableEditorCrop);
                intent.putExtra(ImageEditor.EXTRA_HAS_FILTERS, enableFilters);
                startActivity(intent);


            } catch (Exception e) {
                e.printStackTrace();
            }

//        } else if (requestCode == 7) {
//            bitmap = (Bitmap) data.getExtras().get("data");
//            startActivity(new Intent(TakePhotoActivity.this, StatusPhotoEditingActivity.class));

//            imageView.setImageBitmap(bitmap);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void setFlashMode() {
        Camera.Parameters params = mCamera.getParameters();
        switch (fmi) {
            case 0: //IF Flash AUTO
                fmi = 1; //Flash ON
                btn_flash.setImageResource(R.drawable.ic_on);
                break;
            case 1: //IF Flash ON
                fmi = 2; //Flash OFF
                btn_flash.setImageResource(R.drawable.ic_off);
                break;
            case 2: //IF Flash OFF
                fmi = 0; //Flash AUTO
                btn_flash.setImageResource(R.drawable.ic_flashauto);
                break;
            default:
                fmi = 0; //Flash AUTO
                btn_flash.setImageResource(R.drawable.ic_flashauto);
                break;
        }

//        if(status == Mode.VIDEO && fmi == 1) //If in video mode, flash on == torch
//        if (fmi == 1) //If in video mode, flash on == torch
//            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//        else
        params.setFlashMode(flashModes[fmi]);

        Log.d("LLL_flash-->", params.getFlashMode());

        //Set the new parameters to the camera:
        mCamera.setParameters(params);

    }


    private int findFrontFacingCamera() {

        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;

    }

    private int findBackFacingCamera() {
        int cameraId = -1;
        //Search for the back facing camera
        //get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        //for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;

            }

        }
        return cameraId;
    }

    public void onResume() {

        super.onResume();
        if (mCamera == null) {
            mCamera = Camera.open();
            mCamera.setDisplayOrientation(90);
//            mPicture = getPictureCallback();
            mPreview.refreshCamera(mCamera);
            Log.d("nu", "null");
        } else {
            Log.d("nu", "no null");
        }

    }

    public void chooseCamera() {
        //if the camera preview is the front
        if (cameraFront) {
            int cameraId = findBackFacingCamera();
            if (cameraId >= 0) {
                //open the backFacingCamera
                //set a picture callback
                //refresh the preview

                mCamera = Camera.open(cameraId);
                mCamera.setDisplayOrientation(90);
                mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera);
            }
        } else {
            int cameraId = findFrontFacingCamera();
            if (cameraId >= 0) {
                //open the backFacingCamera
                //set a picture callback
                //refresh the preview
                mCamera = Camera.open(cameraId);
                mCamera.setDisplayOrientation(90);
                mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //when on Pause, release camera in order to be used from other applications
        releaseCamera();
    }

    private void releaseCamera() {
        // stop and release camera
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Camera.PictureCallback getPictureCallback() {
        Camera.PictureCallback picture = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                startActivity(new Intent(TakePhotoActivity.this, ImageEditActivity.class).putExtra(ImageEditor.EXTRA_IMAGE_PATH, bitmap));

            }
        };
        return picture;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        try {
            if (!mInitSuccesful)
                initRecorder(mHolder.getSurface());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        shutdown();
    }

    private void shutdown() {
        // Release MediaRecorder and especially the Camera as it's a shared
        // object that can be used by other applications
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mCamera.release();

        // once the objects have been released they can't be reused
        mMediaRecorder = null;
        mCamera = null;
    }
}