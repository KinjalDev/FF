package com.example.friendfield.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.friendfield.R;
import com.example.friendfield.Utils.FileUtils;
import com.example.friendfield.Utils.ImageEditor;
import com.example.friendfield.Utils.ProcessingImage;
import com.example.friendfield.Utils.ProcessingVideo;
import com.example.friendfield.Utils.TaskCallback;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import life.knowledge4.videotrimmer.K4LVideoTrimmer;
import life.knowledge4.videotrimmer.interfaces.OnTrimVideoListener;

public class VideoEditActivity extends AppCompatActivity {
    String videoPath;
    TextureView video_view;
    Uri finalVideo;
    RelativeLayout btn_send;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_edit);

        videoPath = getIntent().getStringExtra(ImageEditor.EXTRA_IMAGE_PATH);

        Uri uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", new File(videoPath));
        Log.e("LLL_uri-->", String.valueOf(uri));

        video_view = findViewById(R.id.video_view);
        btn_send = findViewById(R.id.btn_send);

//        video_view.setVideoURI(uri);
//
//        video_view.setOnTrimVideoListener(new OnTrimVideoListener() {
//            @Override
//            public void getResult(Uri uri) {
//                finalVideo = uri;
//                if (video_view != null) {
//                    video_view = null;
//                }
//                video_view.setVideoURI(uri);
//            }
//
//            @Override
//            public void cancelAction() {
//
//            }
//        });
//
//        video_view.setMaxDuration(15);

        video_view.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
//                activityHomeBinding.videoSurface.getLayoutParams().height=640;
//                activityHomeBinding.videoSurface.getLayoutParams().width=720;
                Surface surface = new Surface(surfaceTexture);

                try {
                    mediaPlayer = new MediaPlayer();
//                    mediaPlayer.setDataSource("http://daily3gp.com/vids/747.3gp");

                    Log.d("VideoPath>>", videoPath);
                    mediaPlayer.setDataSource(videoPath);
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

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String saveVideoPath = FileUtils.commonDocumentDirPath("FriendField") + "/edited_" + System.currentTimeMillis() + ".mp4";
                new ProcessingVideo(finalVideo.toString(), new File(saveVideoPath)).execute();
            }
        });

    }

    public class ProcessingVideo extends AsyncTask<String, String, String> {
        private final String srcBitmap;
        private final File imagePath;

        public ProcessingVideo(String srcBitmap, File imagePath) {
            this.srcBitmap = srcBitmap;
            this.imagePath = imagePath;
        }

        @Override
        protected String doInBackground(String... voids) {

            int count;
            try {
                String root = Environment.getExternalStorageDirectory().toString();

                System.out.println("Downloading");
                URL url = new URL(srcBitmap);

                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file
//            OutputStream output = new FileOutputStream(root+"/downloadedfile.jpg");
                OutputStream output = new FileOutputStream(imagePath);
                byte data[] = new byte[1024];

                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;

                    // writing data to file
                    output.write(data, 0, count);

                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            finish();
        }
    }
}