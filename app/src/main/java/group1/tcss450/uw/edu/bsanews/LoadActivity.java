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

        AsyncTask<String, Void, String> task =null;
        task = new LoadFromDatabase(this);

        task.execute(PARTIAL_URL, mUsername);
    }

}
