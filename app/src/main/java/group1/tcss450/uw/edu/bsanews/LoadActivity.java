package group1.tcss450.uw.edu.bsanews;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import group1.tcss450.uw.edu.bsanews.Model.LoadFromDatabase;

/**
 *This activity loads data from database.
 * @author Aygun Avazova
 */
public class LoadActivity extends AppCompatActivity {
    /**
     * URL to connect database.
     */
    private static final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/" +
            "~shw26/dbconnect";
    /**
     * Allow AsyncTask to set message.
     */
    private TextView mTextView;

    private static final String KEY_USERNAME = "USERNAME";
    private String mUsername;

    /**
     * Initialize components.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        mUsername = getIntent().getStringExtra(KEY_USERNAME);
        mTextView = (TextView) findViewById(R.id.load_TextView);

        AsyncTask<String, Void, String> task =null;
        // TODO: 2017/2/25 for testing LoadFromDatabase.
        //task = new PostWebServiceTask();
        task = new LoadFromDatabase(this, mTextView);

        task.execute(PARTIAL_URL, mUsername);
    }

    // TODO: 2017/2/25 replaced by LoadFromDatabase. 
    /**
     * Code was provieded by Mr. Bryan Charles.
     */
    private class PostWebServiceTask extends AsyncTask<String, Void, String> {
        private final String SERVICE = "_load.php";

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 2) {
                throw new IllegalArgumentException("two String arguments required.");
            }
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            try {
                URL urlObject = new URL(url + SERVICE);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                //my_name=username&my_pw=password
                String data = URLEncoder.encode("my_name", "UTF-8")
                        + "=" + URLEncoder.encode(strings[1], "UTF-8");
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
            }else {
                //Display message from database.
                mTextView.setText(result);

            }

        }

    }


}
