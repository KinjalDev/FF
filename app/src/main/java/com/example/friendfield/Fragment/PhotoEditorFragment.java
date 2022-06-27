package com.example.friendfield.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.http.HttpClient;
import com.amazonaws.http.HttpResponse;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.friendfield.Activity.AddProductActivity;
import com.example.friendfield.Activity.ImageEditActivity;
import com.example.friendfield.Crop.CropImage;
import com.example.friendfield.Crop.CropImageView;
import com.example.friendfield.MainActivity;
import com.example.friendfield.Model.Product.ProductModel;
import com.example.friendfield.Model.Story.StoryModel;
import com.example.friendfield.MyApplication;
import com.example.friendfield.R;
import com.example.friendfield.Utils.AnimationHelper;
import com.example.friendfield.Utils.BitmapUtil;
import com.example.friendfield.Utils.Constans;
import com.example.friendfield.Utils.DBHelper;
import com.example.friendfield.Utils.DimensionData;
import com.example.friendfield.Utils.FileUtils;
import com.example.friendfield.Utils.ImageEditor;
import com.example.friendfield.Utils.Matrix3;
import com.example.friendfield.Utils.ProcessingImage;
import com.example.friendfield.Utils.ProcessingVideo;
import com.example.friendfield.Utils.SaveSettings;
import com.example.friendfield.Utils.TaskCallback;
import com.example.friendfield.Utils.Utility;
import com.example.friendfield.view.PhotoEditorView;
import com.example.friendfield.view.VerticalSlideColorPicker;
import com.example.friendfield.view.ViewTouchListener;
import com.example.friendfield.view.imagezoom.ImageViewTouch;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import life.knowledge4.videotrimmer.K4LVideoTrimmer;
import life.knowledge4.videotrimmer.interfaces.OnTrimVideoListener;
import nl.bravobit.ffmpeg.FFcommandExecuteResponseHandler;
import nl.bravobit.ffmpeg.FFmpeg;

public class PhotoEditorFragment extends BaseFragment
        implements View.OnClickListener, ViewTouchListener {

    ImageViewTouch mainImageView;
    ImageView cropButton;
    ImageView stickerButton;
    ImageView addTextButton;
    PhotoEditorView photoEditorView;
    ImageView paintButton;
    ImageView deleteButton;
    VerticalSlideColorPicker colorPickerView;
    //CustomPaintView paintEditView;
    View toolbarLayout;
    RecyclerView filterRecylerview;
    View filterLayout;
    View filterLabel;
    FloatingActionButton doneBtn;
    private Bitmap mainBitmap;
    private LruCache<Integer, Bitmap> cacheStack;
    private int filterLayoutHeight;
    private OnFragmentInteractionListener mListener;
    public static final int MODE_NONE = 0;
    public static final int MODE_PAINT = 1;
    public static final int MODE_ADD_TEXT = 2;
    public static final int MODE_STICKER = 3;

    protected int currentMode;
    //    private ImageFilter selectedFilter;
    private Bitmap originalBitmap;
    //        VideoView video_view;
//    K4LVideoTrimmer video_view;
    TextureView video_view;
    RelativeLayout btn_send;
    ImageView iv_trans_image;
    Boolean isfromcam;
    String imagePath;
    String finalImagePath;
    Uri uri;
    Uri finalVideo;
    private MediaPlayer mediaPlayer;
    private ArrayList<String> exeCmd;
    FFmpeg fFmpeg;
    private String[] newCommand;

    private int originalDisplayWidth;
    private int originalDisplayHeight;
    private int newCanvasWidth, newCanvasHeight;
    private int DRAW_CANVASW = 0;
    private int DRAW_CANVASH = 0;
    private ProgressDialog progressDialog;
    Boolean isFrontCam;
    RequestQueue queue;
    String image_type;
    String saveVideoPath;
    AmazonS3Client amazonS3Client;
    DBHelper dbHelper;

    public static PhotoEditorFragment newInstance(String imagePath, Boolean isfromcamera, Boolean isfrontcam) {
//    public static PhotoEditorFragment newInstance(String imagePath, Boolean isfromcamera) {
//    public static PhotoEditorFragment newInstance(Bitmap imagePath) {
        Bundle bundle = new Bundle();
        bundle.putString(ImageEditor.EXTRA_IMAGE_PATH, imagePath);
        bundle.putBoolean("isFromCamera", isfromcamera);
        bundle.putBoolean("isFrontCamera", isfrontcam);
//        bundle.putParcelable(ImageEditor.EXTRA_IMAGE_PATH, imagePath);
        PhotoEditorFragment photoEditorFragment = new PhotoEditorFragment();
        photoEditorFragment.setArguments(bundle);
        return photoEditorFragment;
    }

    public PhotoEditorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_editor, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(
                    context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onCropClicked(Bitmap bitmap);

        void onDoneClicked(String imagePath);
    }

    public void setImageBitmap(Bitmap bitmap) {
        mainImageView.setImageBitmap(bitmap);
        mainImageView.post(new Runnable() {
            @Override
            public void run() {
                photoEditorView.setBounds(mainImageView.getBitmapRect());
            }
        });
    }

    public void setImageWithBitmapRect(Bitmap bitmap, Rect rect) {
//        mainImageView.setImageBitmap(bitmap);
        mainBitmap = getScaledBitmap(getCroppedBitmap(getBitmapCache(bitmap), rect));
        mainImageView.setImageBitmap(mainBitmap);
        mainImageView.post(new Runnable() {
            @Override
            public void run() {
                photoEditorView.setBounds(mainImageView.getBitmapRect());
            }
        });
    }

    public void setImageWithRect(Rect rect) {
        mainBitmap = getScaledBitmap(getCroppedBitmap(getBitmapCache(originalBitmap), rect));
        mainImageView.setImageBitmap(mainBitmap);
        mainImageView.post(new Runnable() {
            @Override
            public void run() {
                photoEditorView.setBounds(mainImageView.getBitmapRect());
            }
        });

//    new GetFiltersTask(new TaskCallback<ArrayList<ImageFilter>>() {
//      @Override public void onTaskDone(ArrayList<ImageFilter> data) {
//        FilterImageAdapter filterImageAdapter = (FilterImageAdapter) filterRecylerview.getAdapter();
//        if (filterImageAdapter != null) {
//          filterImageAdapter.setData(data);
//          filterImageAdapter.notifyDataSetChanged();
//        }
//      }
//    }, mainBitmap).execute();

    }

    private Bitmap getScaledBitmap(Bitmap resource) {
        int currentBitmapWidth = resource.getWidth();
        int currentBitmapHeight = resource.getHeight();
        int ivWidth = mainImageView.getWidth();
        int newHeight = (int) Math.floor(
                (double) currentBitmapHeight * ((double) ivWidth / (double) currentBitmapWidth));
        return Bitmap.createScaledBitmap(resource, ivWidth, newHeight, true);
    }

    private Bitmap getCroppedBitmap(Bitmap srcBitmap, Rect rect) {

//        Bitmap bitmap= setImageBitmap(srcBitmap);
        // Crop the subset from the original Bitmap.
        Bitmap bmOverlay = Bitmap.createBitmap(srcBitmap.getWidth(),
                srcBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
//        return Bitmap.createBitmap(srcBitmap, rect.left, rect.top, (rect.right - rect.left), (rect.bottom - rect.top));
//        return Bitmap.createBitmap(srcBitmap, rect.left, rect.top, rect.width(), rect.height());
        return bmOverlay;
    }

    public void reset() {
        photoEditorView.reset();
    }

    protected void initView(View view) {

        mainImageView = view.findViewById(R.id.image_iv);
        cropButton = view.findViewById(R.id.crop_btn);
        stickerButton = view.findViewById(R.id.stickers_btn);
        addTextButton = view.findViewById(R.id.add_text_btn);
        deleteButton = view.findViewById(R.id.delete_view);
        photoEditorView = view.findViewById(R.id.photo_editor_view);
        paintButton = view.findViewById(R.id.paint_btn);
        colorPickerView = view.findViewById(R.id.color_picker_view);
        //paintEditView = findViewById(R.id.paint_edit_view);
        toolbarLayout = view.findViewById(R.id.toolbar_layout);
        filterRecylerview = view.findViewById(R.id.filter_list_rv);
        filterLayout = view.findViewById(R.id.filter_list_layout);
        filterLabel = view.findViewById(R.id.filter_label);
        doneBtn = view.findViewById(R.id.done_btn);
        video_view = view.findViewById(R.id.video_view);
        btn_send = view.findViewById(R.id.btn_send);
        iv_trans_image = view.findViewById(R.id.iv_trans_image);

        fFmpeg = FFmpeg.getInstance(getContext());
        progressDialog = new ProgressDialog(getContext());
        queue = Volley.newRequestQueue(getContext());
        dbHelper = new DBHelper(getContext());
        dbHelper.open();

        if (getArguments() != null && getActivity() != null && getActivity().getIntent() != null) {
            imagePath = getArguments().getString(ImageEditor.EXTRA_IMAGE_PATH);
            Log.e("LLL_image_path-->", imagePath);

            isfromcam = getArguments().getBoolean("isFromCamera");
            isFrontCam = getArguments().getBoolean("isFrontCamera");

//            setImageBitmap(decodedImage);

//            if (isfromcam) {

//                mainImageView.post(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        byte[] imageBytes = Base64.decode(imagePath, Base64.DEFAULT);
//                        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//
////                        mainImageView.setImageBitmap(decodedImage);
//                        originalBitmap = getScaledBitmap(decodedImage);
////                        mainBitmap = Utility.decodeBitmap(imagePath, mainImageView.getWidth(), mainImageView.getHeight());
//                        mainBitmap = originalBitmap;
//
//                        setImageBitmap(mainBitmap);

//                    }
//                });

            if (isfromcam) {

                image_type = "PHOTO";

                mainImageView.setVisibility(View.VISIBLE);
                video_view.setVisibility(View.GONE);

                mainImageView.post(new Runnable() {
                    @Override
                    public void run() {
                        mainBitmap = Utility.decodeBitmap(imagePath, mainImageView.getWidth(), mainImageView.getHeight());
                    }
                });

                Glide.with(getActivity()).asBitmap().load(imagePath).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {

                        int currentBitmapWidth = resource.getWidth();
                        int currentBitmapHeight = resource.getHeight();

                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        int height = displayMetrics.heightPixels;
                        int width = displayMetrics.widthPixels;

//                        int ivWidth = mainImageView.getWidth();

                        int newHeight = (int) Math.floor((double) currentBitmapHeight * ((double) width / (double) currentBitmapWidth));

                        originalBitmap = Bitmap.createScaledBitmap(resource, width, newHeight, true);
//                        originalBitmap = Bitmap.createScaledBitmap(resource, ivWidth, newHeight, true);
                        mainBitmap = originalBitmap;
                        setImageBitmap(mainBitmap);

                    }
                });
            } else {

                image_type = "VIDEO";

                if (isFrontCam) {
                    video_view.setScaleX(-1);
                }

                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(imagePath);
                String metaRotation = retriever.extractMetadata(METADATA_KEY_VIDEO_ROTATION);
                int rotation = metaRotation == null ? 0 : Integer.parseInt(metaRotation);

                if (rotation == 90 || rotation == 270) {
                    DRAW_CANVASH = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                    DRAW_CANVASW = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                } else {
                    DRAW_CANVASW = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                    DRAW_CANVASH = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                }

                mainImageView.setVisibility(View.VISIBLE);
                video_view.setVisibility(View.VISIBLE);

                mainImageView.post(new Runnable() {
                    @Override
                    public void run() {
//                        mainBitmap = Utility.decodeBitmap(imagePath, mainImageView.getWidth(), mainImageView.getHeight());

                        mainBitmap = convertToBitmap(getContext().getResources().getDrawable(R.drawable.trans), mainImageView.getWidth(), mainImageView.getHeight());

                    }
                });

                Glide.with(getActivity()).asBitmap().load(R.drawable.trans).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        try {
                            int currentBitmapWidth = resource.getWidth();
                            int currentBitmapHeight = resource.getHeight();
                            int ivWidth = mainImageView.getWidth();
                            int newHeight = (int) Math.floor(
                                    (double) currentBitmapHeight * ((double) ivWidth / (double) currentBitmapWidth));
                            originalBitmap = Bitmap.createScaledBitmap(resource, ivWidth, newHeight, true);
                            mainBitmap = originalBitmap;
                            setImageBitmap(mainBitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

//                Uri uri = Uri.parse(imagePath);
                uri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".provider", new File(imagePath));
                Log.e("LLL_uri-->", String.valueOf(uri));

                video_view.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                    @Override
                    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
                        Surface surface = new Surface(surfaceTexture);

                        try {
                            mediaPlayer = new MediaPlayer();

                            Log.d("VideoPath>>", imagePath);
                            mediaPlayer.setDataSource(imagePath);
//                            mediaPlayer.setDataSource(uri.getPath());
                            mediaPlayer.setSurface(surface);
                            mediaPlayer.prepare();
                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    mp.start();

                                }
                            });
                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            mediaPlayer.start();
                        } catch (IllegalArgumentException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (SecurityException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IllegalStateException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

                    }

                    @Override
                    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                        return false;
                    }

                    @Override
                    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

                    }
                });

            }

            exeCmd = new ArrayList<>();

            Intent intent = getActivity().getIntent();
            setVisibility(addTextButton, intent.getBooleanExtra(ImageEditor.EXTRA_IS_TEXT_MODE, false));
            setVisibility(cropButton, intent.getBooleanExtra(ImageEditor.EXTRA_IS_CROP_MODE, false));
            setVisibility(stickerButton, intent.getBooleanExtra(ImageEditor.EXTRA_IS_STICKER_MODE, false));
            setVisibility(paintButton, intent.getBooleanExtra(ImageEditor.EXTRA_IS_PAINT_MODE, false));
            setVisibility(filterLayout, intent.getBooleanExtra(ImageEditor.EXTRA_HAS_FILTERS, false));


            photoEditorView.setImageView(mainImageView, deleteButton, this);
            //stickerEditorView.setImageView(mainImageView, deleteButton,this);
            cropButton.setOnClickListener(this);
            stickerButton.setOnClickListener(this);
            addTextButton.setOnClickListener(this);
            paintButton.setOnClickListener(this);
            doneBtn.setOnClickListener(this);
            view.findViewById(R.id.back_iv).setOnClickListener(this);

            colorPickerView.setOnColorChangeListener(
                    new VerticalSlideColorPicker.OnColorChangeListener() {
                        @Override
                        public void onColorChange(int selectedColor) {
                            if (currentMode == MODE_PAINT) {
                                paintButton.setBackground(
                                        Utility.tintDrawable(getContext(), R.drawable.circle, selectedColor));
                                photoEditorView.setColor(selectedColor);
                            } else if (currentMode == MODE_ADD_TEXT) {
                                addTextButton.setBackground(
                                        Utility.tintDrawable(getContext(), R.drawable.circle, selectedColor));
                                photoEditorView.setTextColor(selectedColor);
                            }
                        }
                    });

            photoEditorView.setColor(colorPickerView.getDefaultColor());
            photoEditorView.setTextColor(colorPickerView.getDefaultColor());

        }

        btn_send.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
//                File saveimagepath = FileUtils.commonDirPath("edited_" + System.currentTimeMillis() + ".jpg");
                if (isfromcam) {
                    String saveimagepath = FileUtils.commonDocumentDirPath("FriendField") + "/edited_" + System.currentTimeMillis() + ".jpg";
                    new ProcessingImage(getBitmapCache(mainBitmap), saveimagepath,
                            new TaskCallback<String>() {
                                @Override
                                public void onTaskDone(String data) {

                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                                    SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                                    String startDate = df.format(c.getTime());

                                    try {
                                        Date date = df1.parse(startDate);
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.setTime(date);
                                        calendar.add(Calendar.HOUR, 24);
                                        String endDate = df1.format(calendar.getTime());

                                        Log.e("LLL_start_date-->", startDate);
                                        Log.e("LLL_end_date-->", endDate);

                                        CreateStory(startDate, endDate, image_type, data);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    mListener.onDoneClicked(data);

                                    try {
                                        dbHelper.open();
//                                        InputStream iStream = getContext().getContentResolver().openInputStream(Uri.parse(data));
                                        InputStream iStream = getContext().getContentResolver().openInputStream(Uri.fromFile(new File(data)));
                                        byte[] inputData = Utility.getBytes(iStream);
//                                        dbHelper.insertImage(inputData);
                                        dbHelper.insertImage(data);
                                        dbHelper.close();
                                        Log.e("LLL_insert-->", "insert sucessfully");
//                                        return true;
                                    } catch (IOException ioe) {
                                        Log.e("LLL_TAG", "<saveImageInDB> Error : " + ioe.getLocalizedMessage());
                                        dbHelper.close();
//                                        return false;
                                    }
//                                    dbHelper.insertImage(getLogoImage(data));


//                                    String uploadid = UUID.randomUUID().toString();
//
//                                    try {
//                                        new MultipartUploadRequest(getContext(), uploadid, Constans.BASE_URL)
//                                                .addFileToUpload(data, "images")
//                                                .setNotificationConfig(new UploadNotificationConfig())
//                                                .setMaxRetries(2)
//                                                .startUpload();
//                                    } catch (MalformedURLException e) {
//                                        e.printStackTrace();
//                                    } catch (FileNotFoundException e) {
//                                        e.printStackTrace();
//                                    }

                                }
                            }).execute();
                } else {

                    String saveimagepath = FileUtils.commonDocumentDirPath("FriendField") + "/edited_" + System.currentTimeMillis() + ".jpg";

                    SaveSettings saveSettings = new SaveSettings.Builder()
                            .setClearViewsEnabled(true)
                            .setTransparencyEnabled(false)
                            .build();

//                    saveAsFile(saveimagepath, saveSettings, new TaskCallback<String>() {
                    saveAsFile(Utility.getCacheFilePath(v.getContext()), saveSettings, new TaskCallback<String>() {
                        @Override
                        public void onTaskDone(String data) {
                            finalImagePath = data;
//                mListener.onDoneClicked(data);
                            Toast.makeText(getContext(), "Saved successfully...", Toast.LENGTH_SHORT).show();
                            applayWaterMark();
                        }
                    });

//        } catch (IOException e) {
//            e.printStackTrace();
//
//        }
                }
            }
        });
    }

    private byte[] getLogoImage(String url) {
        try {
            URL imageUrl = new URL(url);
            URLConnection ucon = imageUrl.openConnection();

            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            ByteArrayBuffer baf = new ByteArrayBuffer(500);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }

            return baf.toByteArray();
        } catch (Exception e) {
            Log.d("ImageManager", "Error: " + e.toString());
        }
        return null;
    }

//    public class uploadToServer extends AsyncTask<String, String, String> {
//
//        private ProgressDialog pd = new ProgressDialog(getContext());
//
//
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pd.setMessage("Wait image uploading!");
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//            nameValuePairs.add(new BasicNameValuePair("base64", ba1));
//            nameValuePairs.add(new BasicNameValuePair("ImageName", System.currentTimeMillis() + ".jpg"));
//
//            try {
//                HttpClient httpclient = (HttpClient) new DefaultHttpClient();
//                HttpPost httppost = new HttpPost("http://192.168.29.105:8080/");
//                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//                HttpResponse response = httpclient.execute(httppost);
//                String st = EntityUtils.toString(response.getEntity());
//                Log.v("log_tag", "In the try Loop" + st);
//
//            } catch (Exception e) {
//                Log.v("log_tag", "Error in http connection " + e.toString());
//            }
//            return "Success";
//
//        }
//
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            pd.hide();
//            pd.dismiss();
//        }
//    }

    public void CreateStory(String start_time, String end_time, String type, String imagedata) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("startTime", start_time);
        params.put("endTime", end_time);
        params.put("type", type);


        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constans.story_create, new JSONObject(params), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
//                    FileUtils.DismissLoading(getContext());
                    Log.e("LLL_add_pro-->", response.toString());

                    StoryModel storyModel = new Gson().fromJson(response.toString(), StoryModel.class);
                    Log.e("LLL_story_id", storyModel.getStoryData().getId());

//                    uploadImage(storyModel.getStoryData().getId(), imagedata);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    FileUtils.DismissLoading(getContext());
                    Log.e("LLL_add_pro_err-->", error.toString());
                    error.printStackTrace();

                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("Content-Type", "application/json");
                    map.put("auth-token", MyApplication.getAuthToken(getContext()));
                    return map;
                }

            };
            queue.add(jsonObjectRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }

    @SuppressLint("MissingPermission")
    private void saveImage() {

        String saveimagepath = FileUtils.commonDocumentDirPath("FriendField") + "/edited_" + System.currentTimeMillis() + ".jpg";

        SaveSettings saveSettings = new SaveSettings.Builder()
                .setClearViewsEnabled(true)
                .setTransparencyEnabled(false)
                .build();

        saveAsFile(saveimagepath, saveSettings, new TaskCallback<String>() {
            @Override
            public void onTaskDone(String data) {
                finalImagePath = data;
//                mListener.onDoneClicked(data);
                Toast.makeText(getContext(), "Saved successfully...", Toast.LENGTH_SHORT).show();
                applayWaterMark();
            }
        });

//        } catch (IOException e) {
//            e.printStackTrace();
//
//        }

    }

    @SuppressLint("StaticFieldLeak")
    @RequiresPermission(allOf = {Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void saveAsFile(@NonNull final String imagePath1,
                           @NonNull final SaveSettings saveSettings,
                           @NonNull final TaskCallback<String> taskCallback) {
        Log.d("TAG", "Image Path: " + imagePath1);
//        parentView.saveFilter(new OnSaveBitmap() {
//            @Override
//            public void onBitmapReady(Bitmap saveBitmap) {
        new AsyncTask<String, String, Exception>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                        clearHelperBox();
                photoEditorView.setDrawingCacheEnabled(false);
            }

            @SuppressLint("MissingPermission")
            @Override
            protected Exception doInBackground(String... strings) {
                // Create a media file name
                File file = new File(imagePath1);
                try {
                    FileOutputStream out = new FileOutputStream(file, false);
                    if (photoEditorView != null) {
                        photoEditorView.setDrawingCacheEnabled(true);
                        Bitmap drawingCache = saveSettings.isTransparencyEnabled()
                                ? BitmapUtil.removeTransparency(photoEditorView.getDrawingCache())
                                : photoEditorView.getDrawingCache();
                        drawingCache.compress(saveSettings.getCompressFormat(), saveSettings.getCompressQuality(), out);
                    }
                    out.flush();
                    out.close();
                    Log.d("TAG", "Filed Saved Successfully");
//                    return file.getAbsolutePath();
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("TAG", "Failed to save File");
                    return e;
                }
            }

            @Override
            protected void onPostExecute(Exception e) {
                super.onPostExecute(e);
                if (e == null) {
                    //Clear all views if its enabled in save settings


//                    if (saveSettings.isClearViewsEnabled()) {
                    if (taskCallback != null) {
                        taskCallback.onTaskDone(imagePath1);
                    }
//                    }
//                        else {
//                            onSaveListener.onFailure(e);
//                        }
                }
            }

        }.execute();
//            }

//            @Override
//            public void onFailure(Exception e) {
//                onSaveListener.onFailure(e);
//            }
//        });
    }

    private void applayWaterMark() {
        saveVideoPath = FileUtils.commonDocumentDirPath("FriendField") + "/edited_" + System.currentTimeMillis() + ".mp4";

        try {

            FileDescriptor fd = getContext().getContentResolver().openFileDescriptor(uri, "r").getFileDescriptor();

            exeCmd.add("-y");
            exeCmd.add("-i");
            exeCmd.add(imagePath);
//            exeCmd.add(uri.getPath());
//            exeCmd.add(fd.toString());
//            exeCmd.add("-framerate 30000/1001 -loop 1");
            exeCmd.add("-i");
            exeCmd.add(finalImagePath);
            exeCmd.add("-filter_complex");
//            exeCmd.add("-strict");
//            exeCmd.add("-2");
//            exeCmd.add("-map");
//            exeCmd.add("[1:v]scale=(iw+(iw/2)):(ih+(ih/2))[ovrl];[0:v][ovrl]overlay=x=(main_w-overlay_w)/2:y=(main_h-overlay_h)/2");
//            exeCmd.add("[1:v]scale=720:1280:1823[ovrl];[0:v][ovrl]overlay=x=0:y=0");
            exeCmd.add("[1:v]scale=" + DRAW_CANVASW + ":" + DRAW_CANVASH + "[ovrl];[0:v][ovrl]overlay=x=0:y=0");
            exeCmd.add("-c:v");
            exeCmd.add("libx264");
            exeCmd.add("-preset");
            exeCmd.add("ultrafast");
//            exeCmd.add(output.getAbsolutePath());
            exeCmd.add(saveVideoPath);


            newCommand = new String[exeCmd.size()];
            for (int j = 0; j < exeCmd.size(); j++) {
                newCommand[j] = exeCmd.get(j);
            }


            for (int k = 0; k < newCommand.length; k++) {
                Log.d("CMD==>>", newCommand[k] + "");
            }

//            newCommand = new String[]{"-i", videoPath, "-i", imagePath, "-preset", "ultrafast", "-filter_complex", "[1:v]scale=2*trunc(" + (width / 2) + "):2*trunc(" + (height/ 2) + ") [ovrl], [0:v][ovrl]overlay=0:0" , output.getAbsolutePath()};
//            executeCommand(newCommand, output.getAbsolutePath());
            executeCommand(newCommand, saveVideoPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void executeCommand(String[] command, final String absolutePath) {
//        try {
        try {
            fFmpeg.execute(command, new FFcommandExecuteResponseHandler() {
                @Override
                public void onSuccess(String message) {
                    Log.d("CommandExecute", "onSuccess" + "  " + message);
                    //                    Toast.makeText(getContext(), "Sucess", Toast.LENGTH_SHORT).show();

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                    SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                    String startDate = df.format(c.getTime());

                    try {
                        Date date = df1.parse(startDate);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.add(Calendar.HOUR, 24);
                        String endDate = df1.format(calendar.getTime());

                        Log.e("LLL_start_date-->", startDate);
                        Log.e("LLL_end_date-->", endDate);

                        CreateStory(startDate, endDate, image_type, saveVideoPath);


                        try {
                            dbHelper.open();
//                                        InputStream iStream = getContext().getContentResolver().openInputStream(Uri.parse(data));
                            InputStream iStream = getContext().getContentResolver().openInputStream(Uri.fromFile(new File(absolutePath)));
                            byte[] inputData = Utility.getBytes(iStream);
//                                        dbHelper.insertImage(inputData);
                            dbHelper.insertImage(absolutePath);
                            dbHelper.close();
                            Log.e("LLL_insert-->", "insert sucessfully");
//                                        return true;
                        } catch (IOException ioe) {
                            Log.e("LLL_TAG", "<saveImageInDB> Error : " + ioe.getLocalizedMessage());
                            dbHelper.close();
//                                        return false;
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onProgress(String message) {
                    Log.d("CommandExecute", "onFailure" + "  " + message);
                    progressDialog.setMessage(message);
                }

                @Override
                public void onFailure(String message) {
                    Log.d("CommandExecute", "onFailure" + "  " + message);
//                    progressDialog.hide();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }


                }

                @Override
                public void onStart() {
                    try {
                        progressDialog.setTitle("Preccesing");
                        progressDialog.setMessage("Starting");
                        progressDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFinish() {
                    Log.d("CommandExecute", "onFinish");
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
//                    progressDialog.hide();
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer = null;
                    }
                    getActivity().finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uploadImage(String imgId, String imagepath) {

//        AmazonS3 s3Client = new AmazonS3Client(new BasicSessionCredentials(awsAccessKey, awsSecretKey, sessionToken));
//        s3Client.setRegion(Region.getRegion(Regions.US_WEST_2));

        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getContext(),
                imgId,
                Regions.US_EAST_1);
        AmazonS3Client s3Client = new AmazonS3Client(credentialsProvider);
        TransferUtility transferUtility = new TransferUtility(s3Client, getContext());
//        TransferObserver transferObserver = transferUtility.upload(imgId, new File(imagepath));
        String uniqueID = UUID.randomUUID().toString();
        TransferObserver transferObserver = transferUtility.upload("http://192.168.29.105:8080/", imgId, new File(imagepath));
        transferObserver.setTransferListener(new TransferListener() {

            @Override

            public void onStateChanged(int id, TransferState state) {

//Implement the code for handle the file status changed.
                Log.e("LLL_state_change-->", state.toString());

            }

            @Override

            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

                //Implement the code to handle the file uploaded progress.
                Log.e("LLL_p_change-->", String.valueOf(bytesCurrent));

            }

            @Override

            public void onError(int id, Exception exception) {
                exception.printStackTrace();
                Log.e("LLL_p_Error-->", exception.getMessage());
//Implement the code to handle the file upload error.

            }

        });

    }

    protected void onModeChanged(int currentMode) {
        Log.i(ImageEditActivity.class.getSimpleName(), "CM: " + currentMode);
        onStickerMode(currentMode == MODE_STICKER);
        onAddTextMode(currentMode == MODE_ADD_TEXT);
        onPaintMode(currentMode == MODE_PAINT);

        if (currentMode == MODE_PAINT || currentMode == MODE_ADD_TEXT) {
            AnimationHelper.animate(getContext(), colorPickerView, R.anim.slide_in_right, View.VISIBLE,
                    null);
        } else {
            AnimationHelper.animate(getContext(), colorPickerView, R.anim.slide_out_right, View.INVISIBLE,
                    null);
        }
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data != null) {

                Uri selectedImageUri = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageUri);

                    setImageBitmap(bitmap);
//                    mainImageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
//                ((ImageView) findViewById(R.id.quick_start_cropped_image)).setImageURI(result.getUri());
                Uri selectedImageUri = result.getUri();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageUri);

                    mainImageView.post(new Runnable() {
                        @Override
                        public void run() {
                            mainBitmap = Utility.decodeBitmap(selectedImageUri.toString(), mainImageView.getWidth(), mainImageView.getHeight());

                        }
                    });

                    Glide.with(getActivity()).asBitmap().load(selectedImageUri.toString()).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource,
                                                    @Nullable Transition<? super Bitmap> transition) {
                            int currentBitmapWidth = resource.getWidth();
                            int currentBitmapHeight = resource.getHeight();
                            int ivWidth = mainImageView.getWidth();
                            int newHeight = (int) Math.floor(
                                    (double) currentBitmapHeight * ((double) ivWidth / (double) currentBitmapWidth));
                            Bitmap originalBitmap = Bitmap.createScaledBitmap(resource, ivWidth, newHeight, true);
                            mainBitmap = originalBitmap;
                            setImageBitmap(mainBitmap);

                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getContext(), "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setFixAspectRatio(true)
                .start(getContext(), this);
//                .getIntent(getActivity());
//        getActivity().startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onClick(final View view) {
        int id = view.getId();
        if (id == R.id.crop_btn) {
//            if (selectedFilter != null) {
//                new ApplyFilterTask(new TaskCallback<Bitmap>() {
//                    @Override
//                    public void onTaskDone(Bitmap data) {
//                        if (data != null) {
//                            mListener.onCropClicked(getBitmapCache(data));
//                            photoEditorView.hidePaintView();
//                        }
//                    }
//                }, Bitmap.createBitmap(originalBitmap)).execute(selectedFilter);
//            } else {

//            ImageCropFunction(getImageUri(getContext(), originalBitmap));

            startCropImageActivity(getImageUri(getContext(), originalBitmap));
//            mListener.onCropClicked(getBitmapCache(originalBitmap));
            photoEditorView.hidePaintView();

//            startActivity(new Intent(getActivity(), CropImageActivity.class));

//            }
        } else if (id == R.id.stickers_btn) {
            setMode(MODE_STICKER);

        } else if (id == R.id.add_text_btn) {
            setMode(MODE_ADD_TEXT);

        } else if (id == R.id.paint_btn) {
            setMode(MODE_PAINT);
        } else if (id == R.id.back_iv) {

            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer = null;
            }
            getActivity().onBackPressed();
        } else if (id == R.id.done_btn) {

//            if (selectedFilter != null) {
//                new ApplyFilterTask(new TaskCallback<Bitmap>() {
//                    @Override
//                    public void onTaskDone(Bitmap data) {
//                        if (data != null) {
//                            new ProcessingImage(getBitmapCache(data), Utility.getCacheFilePath(view.getContext()),
//                                    new TaskCallback<String>() {
//                                        @Override
//                                        public void onTaskDone(String data) {
//                                            mListener.onDoneClicked(data);
//                                        }
//                                    }).execute();
//                        }
//                    }
//                }, Bitmap.createBitmap(mainBitmap)).execute(selectedFilter);
//            } else {

//            new ProcessingImage(getBitmapCache(mainBitmap), Utility.getCacheFilePath(view.getContext()),

            if (isfromcam) {
                String saveimagepath = FileUtils.commonDocumentDirPath("FriendField") + "/edited_" + System.currentTimeMillis() + ".jpg";

                new ProcessingImage(getBitmapCache(mainBitmap), saveimagepath,
                        new TaskCallback<String>() {
                            @Override
                            public void onTaskDone(String data) {
                                mListener.onDoneClicked(data);
                            }
                        }).execute();
            } else {
                String saveVideoPath = FileUtils.commonDocumentDirPath("FriendField") + "/edited_" + System.currentTimeMillis() + ".mp4";
                new ProcessingImage(getBitmapCache(mainBitmap), saveVideoPath,
                        new TaskCallback<String>() {
                            @Override
                            public void onTaskDone(String data) {
                                mListener.onDoneClicked(data);
                            }
                        }).execute();
            }
//            }
        }

        if (currentMode != MODE_NONE) {
            filterLabel.setAlpha(0f);
            mainImageView.animate().scaleX(1f);
            photoEditorView.animate().scaleX(1f);
            mainImageView.animate().scaleY(1f);
            photoEditorView.animate().scaleY(1f);
            filterLayout.animate().translationY(filterLayoutHeight);
            //touchView.setVisibility(View.GONE);
        } else {
            filterLabel.setAlpha(1f);
            //touchView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
//            mediaPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
//            mediaPlayer = null;
        }
    }

    private void onAddTextMode(boolean status) {
        if (status) {
            addTextButton.setBackground(
                    Utility.tintDrawable(getContext(), R.drawable.circle, photoEditorView.getColor()));
            //photoEditorView.setTextColor(photoEditorView.getColor());
            photoEditorView.addText();
        } else {
            addTextButton.setBackground(null);
            photoEditorView.hideTextMode();
        }
    }

    private void onPaintMode(boolean status) {
        if (status) {
            paintButton.setBackground(
                    Utility.tintDrawable(getContext(), R.drawable.circle, photoEditorView.getColor()));
            photoEditorView.showPaintView();
            //paintEditView.setVisibility(View.VISIBLE);
        } else {
            paintButton.setBackground(null);
            photoEditorView.hidePaintView();

            //photoEditorView.enableTouch(true);
            //paintEditView.setVisibility(View.GONE);
        }
    }

    private void onStickerMode(boolean status) {
        if (status) {
            stickerButton.setBackground(
                    Utility.tintDrawable(getContext(), R.drawable.circle, photoEditorView.getColor()));
            if (getActivity() != null && getActivity().getIntent() != null) {
                String folderName = getActivity().getIntent().getStringExtra(ImageEditor.EXTRA_STICKER_FOLDER_NAME);
//                photoEditorView.showStickers(folderName);
                photoEditorView.showStickers("stickers");
            }
        } else {
            stickerButton.setBackground(null);
            photoEditorView.hideStickers();
        }
    }

    @Override
    public void onStartViewChangeListener(final View view) {
        Log.i(ImageEditActivity.class.getSimpleName(), "onStartViewChangeListener" + "" + view.getId());
        toolbarLayout.setVisibility(View.GONE);
        AnimationHelper.animate(getContext(), deleteButton, R.anim.fade_in_medium, View.VISIBLE, null);
    }

    @Override
    public void onStopViewChangeListener(View view) {
        Log.i(ImageEditActivity.class.getSimpleName(), "onStopViewChangeListener" + "" + view.getId());
        deleteButton.setVisibility(View.GONE);
        AnimationHelper.animate(getContext(), toolbarLayout, R.anim.fade_in_medium, View.VISIBLE, null);
    }

    private Bitmap getBitmapCache(Bitmap bitmap) {
        Matrix touchMatrix = mainImageView.getImageViewMatrix();

        Bitmap resultBit = Bitmap.createBitmap(bitmap).copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(resultBit);

        float[] data = new float[9];
        touchMatrix.getValues(data);
        Matrix3 cal = new Matrix3(data);
        Matrix3 inverseMatrix = cal.inverseMatrix();
        Matrix m = new Matrix();
        m.setValues(inverseMatrix.getValues());

        float[] f = new float[9];
        m.getValues(f);
        int dx = (int) f[Matrix.MTRANS_X];
        int dy = (int) f[Matrix.MTRANS_Y];
        float scale_x = f[Matrix.MSCALE_X];
        float scale_y = f[Matrix.MSCALE_Y];
        canvas.save();
        canvas.translate(dx, dy);
        canvas.scale(scale_x, scale_y);

        photoEditorView.setDrawingCacheEnabled(true);

        if (photoEditorView.getDrawingCache() != null) {
            canvas.drawBitmap(photoEditorView.getDrawingCache(), 0, 0, null);
//            canvas.drawBitmap(resultBit, 0, 0, null);
        }

        if (photoEditorView.getPaintBit() != null) {
            canvas.drawBitmap(photoEditorView.getPaintBit(), 0, 0, null);
//            canvas.drawBitmap(resultBit, 0, 0, null);
        }

        canvas.restore();
        return resultBit;
    }

//    @Override
//    public void onFilterSelected(ImageFilter imageFilter) {
//        selectedFilter = imageFilter;
//        new ApplyFilterTask(new TaskCallback<Bitmap>() {
//            @Override
//            public void onTaskDone(Bitmap data) {
//                if (data != null) {
//                    setImageBitmap(data);
//                }
//            }
//        }, Bitmap.createBitmap(mainBitmap)).execute(imageFilter);
//    }

    protected void setMode(int mode) {
        if (currentMode != mode) {
            onModeChanged(mode);
        } else {
            mode = MODE_NONE;
            onModeChanged(mode);
        }
        this.currentMode = mode;
    }
}
