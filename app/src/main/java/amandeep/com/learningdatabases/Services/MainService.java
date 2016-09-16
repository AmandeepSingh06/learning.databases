package amandeep.com.learningdatabases.Services;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import amandeep.com.learningdatabases.Constants.Constants;
import amandeep.com.learningdatabases.Databases.DatabaseInstance;
import amandeep.com.learningdatabases.Databases.SQLiteHelper;

/**
 * Created by amandeepsingh on 15/09/16.
 */
public class MainService extends Service {

    private Handler handler;

    @Override
    public void onCreate() {
        HandlerThread handlerThread = new HandlerThread("thread");
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        handler = new Handler(looper) {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                String action = (String) bundle.get(Constants.ACTION);
                Intent intent = new Intent();
                intent.setAction(Constants.INTENT_ACTION);
                try {
                    switch (action) {
                        case Constants.ACTION_ADD:
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(SQLiteHelper.COLUMN_NAME, Constants.STRING_TO_BE_ADDED);
                            DatabaseInstance.getDatabaseInstance().insert(SQLiteHelper.TABLE_NAME, null, contentValues);
                            break;
                        case Constants.ACTION_UPDATE_COUNT:
                            String[] allColumns = {SQLiteHelper._ID, SQLiteHelper.COLUMN_NAME};
                            Cursor cursor = DatabaseInstance.getDatabaseInstance().query(SQLiteHelper.TABLE_NAME, allColumns, null, null, null, null, null);
                            bundle.putInt(Constants.COUNT, cursor.getCount());
                            cursor.close();
                            break;
                        case Constants.ACTION_DELETE:
                            DatabaseInstance.getDatabaseInstance().delete(SQLiteHelper.TABLE_NAME,
                                    SQLiteHelper.COLUMN_NAME
                                            + " = '" + Constants.STRING_TO_BE_ADDED + "'", null);
                            break;
                    }
                } catch (SQLException e) {
                    bundle = new Bundle();
                    bundle.putString(Constants.ACTION, Constants.ACTION_EXCEPTION);
                    bundle.putString(Constants.MESSAGE, e.getMessage());
                }
                intent.putExtras(bundle);
                LocalBroadcastManager.getInstance(MainService.this).sendBroadcast(intent);
            }
        };
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message message = new Message();
        message.setData(intent.getExtras());
        handler.sendMessage(message);
        return 1;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
