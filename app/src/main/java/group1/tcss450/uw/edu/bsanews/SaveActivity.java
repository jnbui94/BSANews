package group1.tcss450.uw.edu.bsanews;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Save Activity allow user to save a URL.
 * @author Aygun Avazova
 */
public class SaveActivity extends AppCompatActivity implements View.OnClickListener{

    /**
     * URL to connect to database.
     */
    private static final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/" +
            "~shw26/dbconnect";
    /**
     * Allow AsyncTask to set message.
     */
    private EditText mEditText;

    private static final String KEY_USERNAME = "USERNAME";
    private String mUsername;

    /**
     * Initialize components.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        mUsername = getIntent().getStringExtra(KEY_USERNAME);
        mEditText=(EditText) findViewById(R.id.save_edit_text);
        Button btn =(Button) findViewById(R.id.save_submit_button);
        btn.setOnClickListener(this);

    }

    /**
     * When submit button is pressed.
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_submit_button:
                String url = mEditText.getText().toString();
                AsyncTask<String, Void, String> task =null;
                task = new SaveActivity.PostWebServiceTask();
                //qwerty is a place holder.
                task.execute(PARTIAL_URL, mUsername, url);
        }
    }

    /**
     * Code was provieded by Mr. Bryan Charles.
     */
    private class PostWebServiceTask extends AsyncTask<String, Void, String> {
        private final String SERVICE = "_save.php";

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 3) {
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
                        + "=" + URLEncoder.encode(strings[1], "UTF-8")
                        + "&" + URLEncoder.encode("my_url", "UTF-8")
                        + "=" + URLEncoder.encode(strings[2],"UTF-8");
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
            }else if(result.startsWith("true")){

                //if saved successfully show toast
               Toast.makeText(getApplicationContext(),"Saved Successfully",
                       Toast.LENGTH_SHORT).show();
            }else if (result.startsWith("false")){
                //if failed show this message.
                Toast.makeText(getApplicationContext(),"the URL already exists in Database",
                        Toast.LENGTH_SHORT).show();
            }

        }

    }
}
