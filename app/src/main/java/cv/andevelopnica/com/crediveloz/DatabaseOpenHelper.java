package cv.andevelopnica.com.crediveloz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "cv.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseOpenHelper(Context context) {
        // super(context, DATABASE_NAME, null, DATABASE_VERSION);
        super(context, "/sdcard/database/"+DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase.openOrCreateDatabase("/sdcard/database/"+DATABASE_NAME,null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}