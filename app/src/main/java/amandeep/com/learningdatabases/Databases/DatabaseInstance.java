package amandeep.com.learningdatabases.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by amandeepsingh on 15/09/16.
 */
public class DatabaseInstance {

    private static SQLiteDatabase database;
    private static SQLiteHelper helper;

    public static void init(Context context) {
        if (helper == null) {
            helper = new SQLiteHelper(context);
        }
    }

    public static SQLiteDatabase getDatabaseInstance() {
        if (database == null) {
            database = helper.getWritableDatabase();
        }
        return database;
    }
}
