package com.example.friendfield.Utils;

import android.graphics.Bitmap;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.example.friendfield.view.PhotoEditorView;


/**
 * @author <a href="https://github.com/burhanrashid52">Burhanuddin Rashid</a>
 * @since 8/8/2018
 * Builder Class to apply multiple save options
 */
public class SaveSettings {
    private boolean isTransparencyEnabled;
    private boolean isClearViewsEnabled;
    private Bitmap.CompressFormat compressFormat;
    private int compressQuality;

    public boolean isTransparencyEnabled() {
        return isTransparencyEnabled;
    }

   public boolean isClearViewsEnabled() {
        return isClearViewsEnabled;
    }

    public Bitmap.CompressFormat getCompressFormat() {
        return compressFormat;
    }

   public int getCompressQuality() {
        return compressQuality;
    }

    private SaveSettings(Builder builder) {
        this.isClearViewsEnabled = builder.isClearViewsEnabled;
        this.isTransparencyEnabled = builder.isTransparencyEnabled;
        this.compressFormat = builder.compressFormat;
        this.compressQuality = builder.compressQuality;
    }

    public static class Builder {
        private boolean isTransparencyEnabled = true;
        private boolean isClearViewsEnabled = true;
        private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.PNG;
        private int compressQuality = 100;

        public Builder setTransparencyEnabled(boolean transparencyEnabled) {
            isTransparencyEnabled = transparencyEnabled;
            return this;
        }

        /**
         * Define a flag to clear the view after saving the image
         *
         * @param clearViewsEnabled true if you want to clear all the views on {@link PhotoEditorView}
         * @return Builder
         */
        public Builder setClearViewsEnabled(boolean clearViewsEnabled) {
            isClearViewsEnabled = clearViewsEnabled;
            return this;
        }

        /**
         * Set the compression format for the file to save: JPEG, PNG or WEBP
         *
         * @param compressFormat JPEG, PNG or WEBP
         * @return Builder
         * @see{android.graphics.Bitmap.CompressFormat}
         */
        public Builder setCompressFormat(@NonNull Bitmap.CompressFormat compressFormat) {
            this.compressFormat = compressFormat;
            return this;
        }

        /**
         * Set the expected compression quality for the output, a number between
         * 0 and 100
         *
         * @param compressQuality An integer from 0 to 100
         * @return Builder
         */
        public Builder setCompressQuality(@IntRange(from = 0, to = 100) int compressQuality) {
            this.compressQuality = compressQuality;
            return this;
        }

        public SaveSettings build() {
            return new SaveSettings(this);
        }
    }
}
