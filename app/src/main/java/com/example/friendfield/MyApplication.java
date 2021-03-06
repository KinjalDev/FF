package com.example.friendfield;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.bokecc.camerafilter.LocalVideoFilter;
import com.example.friendfield.Activity.ChatingActivity;
import com.example.friendfield.Utils.Constans;
import com.example.friendfield.status.utility.Constants;
import com.google.android.exoplayer2.database.DatabaseProvider;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.upstream.cache.CacheEvictor;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import org.jetbrains.annotations.Nullable;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import kotlin.jvm.internal.DefaultConstructorMarker;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class MyApplication extends Application {

    public static final String CHANNEL_ID = "exampleChannel";
    private Socket mSocket;
    public static SimpleCache simpleCache;
    public static final MyApplication.Companion Companion = new MyApplication.Companion((DefaultConstructorMarker) null);
    public static Context context;
    public static WebSocket webSocket;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        try {
            mSocket = IO.socket(Constans.CHAT_SERVER_URL);
//            mSocket=new Socket("http://192.168.29.105:","8080");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        LocalVideoFilter.init(this);

        LeastRecentlyUsedCacheEvictor leastRecentlyUsedCacheEvictor = new LeastRecentlyUsedCacheEvictor(90 * 1024 * 1024);
        DatabaseProvider databaseProvider = (DatabaseProvider) (new ExoDatabaseProvider((Context) this));
        if (simpleCache == null) {
            simpleCache = new SimpleCache(this.getCacheDir(), (CacheEvictor) leastRecentlyUsedCacheEvictor, databaseProvider);
        }


        createNotificationChannel();
    }

//    public static void initiateSocketConnection() {
//
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder().url(Constans.CHAT_SERVER_URL).build();
//        webSocket = client.newWebSocket(request, new ChatingActivity.SocketListener());
//
//    }

    public static Context getContext() {
        return context;
    }


    public static final class Companion {
        @Nullable
        public final SimpleCache getSimpleCache() {
            return MyApplication.simpleCache;
        }

        public final void setSimpleCache(@Nullable SimpleCache var1) {
            MyApplication.simpleCache = var1;
        }

        private Companion() {
        }

        // $FF: synthetic method
        public Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }

    public Socket getSocket() {
        return mSocket;
    }

    public static void setCountryCode(Context context, String code) {
        if (code == null) {
            return;
        }

        // Save in the preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        if (null == sharedPreferences) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("CountryCode", code);
        editor.commit();
    }

    public static String getCountryCode(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String cookie = sharedPreferences.getString("CountryCode", "");

        return cookie;
    }

    public static void setuserActive(Context context, Boolean useractive) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        if (null == sharedPreferences) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isUserActive", useractive);
        editor.commit();
    }

    public static Boolean isUserActive(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Boolean useractive = sharedPreferences.getBoolean("isUserActive", false);

        return useractive;
    }

    public static void setcontactNo(Context context, String contactNo) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        if (null == sharedPreferences) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("contactNo", contactNo);
        editor.commit();
    }

    public static String getcontactNo(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String cookie = sharedPreferences.getString("contactNo", "");
        return cookie;
    }

    public static void setuserName(Context context, String contactNo) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        if (null == sharedPreferences) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName", contactNo);
        editor.commit();
    }

    public static String getuserName(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String cookie = sharedPreferences.getString("userName", "");
        return cookie;
    }

    public static void setAuthToken(Context context, String authToken) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        if (null == sharedPreferences) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("AuthToken", authToken);
        editor.commit();
    }

    public static String getAuthToken(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String cookie = sharedPreferences.getString("AuthToken", "");
        return cookie;
    }

    public static void setAccountType(Context context, String accountType) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        if (null == sharedPreferences) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("AccountType", accountType);
        editor.commit();
    }

    public static String getAccountType(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String cookie = sharedPreferences.getString("AccountType", "");
        return cookie;
    }

    public static void setBusinessProfileRegistered(Context context, Boolean useractive) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        if (null == sharedPreferences) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isBusinessProfileRegistered", useractive);
        editor.commit();
    }

    public static Boolean isBusinessProfileRegistered(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Boolean useractive = sharedPreferences.getBoolean("isBusinessProfileRegistered", false);

        return useractive;
    }

    public static void setPersonalProfileRegistered(Context context, Boolean useractive) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        if (null == sharedPreferences) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isPersonalProfileRegistered", useractive);
        editor.commit();
    }

    public static Boolean isPersonalProfileRegistered(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Boolean useractive = sharedPreferences.getBoolean("isPersonalProfileRegistered", false);

        return useractive;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }


}
