package edu.hebut215054.bighomework1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private static SQLiteOpenHelper mInstance;

    private MySQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table postPic(pic text)";
        db.execSQL(sql);

    }

    public static synchronized SQLiteOpenHelper getInstance(Context context){
        if(mInstance == null){
            mInstance = new MySQLiteOpenHelper(context, "temp.db", null, 1);
        }

        return mInstance;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
