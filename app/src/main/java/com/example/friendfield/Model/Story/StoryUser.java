package com.example.friendfield.Model.Story;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

public class StoryUser implements Parcelable {

    private  String username;
    @NotNull
    private  String profilePicUrl;
    @NotNull
    private  ArrayList<Story> stories;

    protected StoryUser(Parcel in) {
        username = in.readString();
        profilePicUrl = in.readString();
        stories = in.createTypedArrayList(Story.CREATOR);
    }

    public static final Creator<StoryUser> CREATOR = new Creator<StoryUser>() {
        @Override
        public StoryUser createFromParcel(Parcel in) {
//            return new StoryUser(in);
            return new StoryUser(in.readString(),in.readString(),in.createTypedArrayList(Story.CREATOR));
        }

        @Override
        public StoryUser[] newArray(int size) {
            return new StoryUser[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(profilePicUrl);
//        dest.writeTypedList(stories);
        ArrayList var10002 = this.stories;
        dest.writeInt(var10002.size());
        Iterator var10000 = var10002.iterator();

        while(var10000.hasNext()) {
            ((Story)var10000.next()).writeToParcel(dest, 0);
        }
    }

    public StoryUser(String username, @NotNull String profilePicUrl, @NotNull ArrayList<Story> stories) {
        this.username = username;
        this.profilePicUrl = profilePicUrl;
        this.stories = stories;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotNull
    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(@NotNull String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    @NotNull
    public ArrayList<Story> getStories() {
        return stories;
    }

    public void setStories(@NotNull ArrayList<Story> stories) {
        this.stories = stories;
    }
}
