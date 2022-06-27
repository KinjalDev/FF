package com.example.friendfield.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.friendfield.Model.Story.ViewStoryModel;

import java.util.ArrayList;


public class DBHelper {

    public static final String IMAGE_ID = "id";
    public static final String IMAGE = "image";
    private final Context mContext;

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "Images.db";
    private static final int DATABASE_VERSION = 1;

    private static final String IMAGES_TABLE = "ImagesTable";

    private static final String CREATE_IMAGES_TABLE =
            "CREATE TABLE IF NOT EXISTS " + IMAGES_TABLE + " (" +
                    IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + IMAGE + " TEXT NOT NULL );";
//                    + IMAGE + " BLOB NOT NULL );";


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_IMAGES_TABLE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //db.execSQL("DROP TABLE IF EXISTS " + CREATE_IMAGES_TABLE);
            onCreate(db);
        }
    }


    public DBHelper(Context ctx) {
        mContext = ctx;
        mDbHelper = new DatabaseHelper(mContext);
    }

    public DBHelper open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public void insertImage(String imageBytes) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(IMAGE, imageBytes);
            mDb.insert(IMAGES_TABLE, null, cv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ViewStoryModel> retreiveimagefromdb1() {
//        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<ViewStoryModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//        Cursor cur = mDb.query(false, IMAGES_TABLE, new String[]{IMAGE_ID, IMAGE},
//        Cursor cur = db.query(false, IMAGES_TABLE, new String[]{IMAGE_ID, IMAGE},
//                null, null, null, null,
//                IMAGE_ID + " DESC", "1");

        Cursor cur = db.rawQuery("SELECT * FROM " + IMAGES_TABLE, null);

        if (cur.moveToFirst()) {
//            byte[] blob = cur.getString(cur.getColumnIndex(IMAGE));
            do {
                int img_id = cur.getInt(cur.getColumnIndex(IMAGE_ID));
                String blob = cur.getString(cur.getColumnIndex(IMAGE));

                ViewStoryModel viewStoryModel = new ViewStoryModel();

                viewStoryModel.setId(img_id);
                viewStoryModel.setImage(blob);

//                cur.close();
                arrayList.add(viewStoryModel);
            } while (cur.moveToNext());

//            return blob;
        }

//        cur.moveToFirst();
//        if (cur.getCount() > 0) {
//            do {
//                String blob = cur.getString(cur.getColumnIndex(IMAGE));
////                cur.close();
//                arrayList.add(blob);
//            } while (cur.moveToNext());
//
//        }
        Log.e("LLL_total_im-->", String.valueOf(arrayList.size()));

        cur.close();
        return arrayList;
    }

    public boolean deleteTitle(int id) {
        return mDb.delete(IMAGES_TABLE, IMAGE_ID + "=" + id, null) > 0;
    }
}
