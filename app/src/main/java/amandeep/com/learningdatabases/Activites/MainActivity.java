package amandeep.com.learningdatabases.Activites;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import amandeep.com.learningdatabases.Constants.Constants;
import amandeep.com.learningdatabases.Databases.DatabaseInstance;
import amandeep.com.learningdatabases.R;
import amandeep.com.learningdatabases.Services.MainService;

public class MainActivity extends Activity implements View.OnClickListener {

    private TextView textView;
    private Button addButton, removeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseInstance.init(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(serviceEventsReceiver,
                new IntentFilter(Constants.INTENT_ACTION));
        startService(getIntentForServiceForUpdateCount());
        setContentView(R.layout.activity_main);
        setViews();
    }

    private void setViews() {
        textView = (TextView) findViewById(R.id.textView);
        addButton = (Button) findViewById(R.id.add_button);
        removeButton = (Button) findViewById(R.id.delete_button);
        addButton.setOnClickListener(this);
        removeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = getIntentForService();
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.add_button:
                bundle.putString(Constants.ACTION, Constants.ACTION_ADD);
                break;
            case R.id.delete_button:
                bundle.putString(Constants.ACTION, Constants.ACTION_DELETE);
                break;
        }
        intent.putExtras(bundle);
        startService(intent);
    }

    public Intent getIntentForService() {
        return new Intent(this, MainService.class);

    }

    public Intent getIntentForServiceForUpdateCount() {
        Intent intent = getIntentForService();

        Bundle bundle = new Bundle();
        bundle.putString(Constants.ACTION, Constants.ACTION_UPDATE_COUNT);
        intent.putExtras(bundle);
        return intent;
    }

    private BroadcastReceiver serviceEventsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = (String) intent.getExtras().get(Constants.ACTION);
            switch (action) {
                case Constants.ACTION_ADD:
                case Constants.ACTION_DELETE:
                    startService(getIntentForServiceForUpdateCount());
                    break;
                case Constants.ACTION_UPDATE_COUNT:
                    int count = (int) intent.getExtras().get(Constants.COUNT);
                    final String string = context.getResources().getString(R.string.text, count);
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(string);
                        }
                    });
                    break;
                case Constants.ACTION_EXCEPTION:
                    String message = (String) intent.getExtras().get(Constants.MESSAGE);
                    Toast toast = Toast.makeText(context, "Exception Occured " + message, Toast.LENGTH_LONG);
                    toast.show();
                    break;
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(serviceEventsReceiver);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(getIntentForService());
    }
}
