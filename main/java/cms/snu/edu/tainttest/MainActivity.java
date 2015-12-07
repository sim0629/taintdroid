package cms.snu.edu.tainttest;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Browser;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cms.snu.edu.tainttest.sink.NetworkSender;
import cms.snu.edu.tainttest.source.DataSource;


public class MainActivity extends Activity {

    private final String TAG = "MainActivity";

    private TextView content = null;

    private TaintTrackingTest taintTest = null;

    public void onClick_src (View v) {
        String data = null;
        switch(v.getId()) {
            case R.id.src_contact_button:
                data = DataSource.getContact(getBaseContext());
                break;
            case R.id.src_bookmark_button:
                data = DataSource.getBrowserBookmark(this);
                break;
        }

        content.setText(data);
    }

    public void onClick_sink (View v) {
        String target;

        target = content.getText().toString();

        content.setText(target);
        switch (v.getId()) {
            case R.id.sink_button:
                new HttpAsyncTask().execute("http://hmkcode.appspot.com/jsonservlet");
                break;
        }
    }

    public void onClick_test (View v) {
        switch(v.getId()) {
            case R.id.test0_button:
                TaintTrackingTest.localTest();
                break;
            case R.id.test1_button:
                taintTest.ipcTest();
                break;
            case R.id.test2_button2:
                taintTest.fileTest();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taintTest = new TaintTrackingTest(getApplicationContext());

        content = (TextView) findViewById(R.id.textView);
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        private String TAG = "HttpAsyncTask";
        @Override
        protected String doInBackground(String... urls) {
            return NetworkSender.POST(urls[0], content.getText().toString());
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Success to send data!", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getBaseContext(), "Fail to send data!", Toast.LENGTH_LONG).show();
        }
    }
}
