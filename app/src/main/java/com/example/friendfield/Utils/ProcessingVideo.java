package com.example.friendfield.Utils;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class ProcessingVideo extends AsyncTask<String, String, String> {
    private final String srcBitmap;
    private final TaskCallback<String> callback;
    private final File imagePath;

    public ProcessingVideo(String srcBitmap, File imagePath, TaskCallback<String> taskCallback) {
        this.srcBitmap = srcBitmap;
        this.callback = taskCallback;
        this.imagePath = imagePath;
    }

    @Override
    protected String doInBackground(String... voids) {
//        return Utility.downloadFile(srcBitmap, imagePath);
//        Utility.downloadFile(srcBitmap, imagePath);
//        return null;

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
        if (callback != null) {
            callback.onTaskDone(s);
        }
    }
}