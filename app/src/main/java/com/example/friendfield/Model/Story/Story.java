package com.example.friendfield.Model.Story;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;

import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

public class Story implements Parcelable {

    private  String url;
    private  long storyDate;

    protected Story(Parcel in) {
        url = in.readString();
        storyDate = in.readLong();
    }
    public Story(@NotNull String url, long storyDate) {
//        Intrinsics.checkParameterIsNotNull(url, "url");
        super();
        this.url = url;
        this.storyDate = storyDate;
    }
    public static final Creator<Story> CREATOR = new Creator<Story>() {
        @Override
        public Story createFromParcel(Parcel in) {
//            return new Story(in);
//            return new Story(in);
            return new Story(in.readString(), in.readLong());

        }

        @Override
        public Story[] newArray(int size) {
            return new Story[size];
        }
    };

    public final boolean isVideo() {
        return StringsKt.contains((CharSequence)this.url, (CharSequence)".mp4", false);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getStoryDate() {
        return storyDate;
    }

    public void setStoryDate(long storyDate) {
        this.storyDate = storyDate;
    }

    public static Creator<Story> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeLong(storyDate);
    }
}
