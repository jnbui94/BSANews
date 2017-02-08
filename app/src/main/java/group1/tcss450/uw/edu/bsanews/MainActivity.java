package group1.tcss450.uw.edu.bsanews;

import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/" +
            "~jnbui94/feedback";
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    }
    public void buttonClick(View view) {
        AsyncTask<String, Void, String> task = null;
        String message = ((EditText) findViewById(R.id.textEdit)).getText().toString();
        switch (view.getId()) {
            case R.id.Head:
                task = new TestWebServiceTask();
                break;
//            case R.id.getbutton:
//                task = new GetWebServiceTask();
//                break;
//            case R.id.postbutton:
//                task = new PostWebServiceTask();
//                break;
            default:
                throw new IllegalStateException("Not implemented");
        }
        task.execute(PARTIAL_URL, message);
    }
    private class TestWebServiceTask extends AsyncTask<String, Void, String> {
        private final String SERVICE = "_static.php";
        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            try {
                URL urlObject = new URL(url + SERVICE);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
            } catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return response;
        }
        @Override
        protected void onPostExecute(String result) {
// Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            mTextView.setText(result);
        }
    }
}
