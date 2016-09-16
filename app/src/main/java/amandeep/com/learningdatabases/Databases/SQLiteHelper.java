package amandeep.com.learningdatabases.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by amandeepsingh on 15/09/16.
 */
public class SQLiteHelper extends SQLiteOpenHelper implements BaseColumns {

    public static final String TABLE_NAME = "info";
    public static final String COLUMN_NAME = "name";

    private static final String DATABASE_NAME = "class_info";
    private static final int DATABSE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table " + TABLE_NAME + "(" + _ID +
            " integer primary key autoincrement, " + COLUMN_NAME + " text not null);";


    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABSE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
