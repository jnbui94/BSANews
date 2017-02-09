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
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    private static final String mURL
            = "https://api.cognitive.microsoft.com/bing/v5.0/news/";
    private TextView mTextView;
    private static  final String mKey = "3abb779da1e740bfab3f95c7fed2475c";
    private static final String mKey1 = "cbfd463af4de4614af5482ce40870522";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    }
    public void buttonClicked(View view) {
        AsyncTask<String, Void, String> task = null;
        String message = ((EditText) findViewById(R.id.textEdit)).getText().toString();
        switch (view.getId()) {
            case R.id.Head:
                task = new PostWebServiceTask();
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
        task.execute(mURL, message);
    }
    private class PostWebServiceTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 2) {
                throw new IllegalArgumentException("Two String arguments required.");
            }
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            try {
                URL urlObject = new URL(mURL);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                String data = URLEncoder.encode("Ocp-Apim-Subscription-Key", "UTF-8")
                        + "=" + URLEncoder.encode(mKey, "UTF-8");
                wr.write(data);
                wr.flush();
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
