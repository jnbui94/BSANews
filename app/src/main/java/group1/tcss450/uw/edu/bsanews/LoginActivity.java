package group1.tcss450.uw.edu.bsanews;

import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * THE FIRST ACTIVITY.
 * provide login.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/" +
            "~shw26/dbconnect";
    private boolean authentication = false;
    Button sign_in_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    }

    /**
     * when login button clicked.
     * @param view
     */
    public void loginClicked(View view){
        sign_in_btn = (Button) view.findViewById(R.id.email_sign_in_button);
        attemptLogin(view);
    }

    /**
     * attempt to Login.
     * @param view
     */
    protected void attemptLogin(View view){
        TextView usernameTextView = (TextView) view.findViewById(R.id.email);
        TextView passwordTextView = (TextView) view.findViewById(R.id.password);
        boolean cancel = false;
        View focusView = passwordTextView;

        usernameTextView.setError(null);
        passwordTextView.setError(null);
        String username = usernameTextView.getText().toString();
        String password = passwordTextView.getText().toString();

        //if the password is not entered or not legit
        if (TextUtils.isEmpty(password) || password.length() <4) {
            passwordTextView.setError("INVALID PASSWORD, REQUIRED 4");
            focusView = passwordTextView;
            cancel = true;
        }
        //if the username/email is not entered or not legit
        if (TextUtils.isEmpty(username) || username.length() <4){
            usernameTextView.setError("INVALID EMAIL");
            focusView = usernameTextView;
            cancel = true;
        }
        //if authentication failed.
        if (authentication){
            cancel = true;

        }

        //if the values are not entered properly, else try to connect the server
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }else {
            //connect server
            // TODO: 2017/2/6 link to MainActivity
            AsyncTask<String, Void, String> task = null;
            sign_in_btn.setEnabled(false);
            task = new PostWebServiceTask();
            task.execute(PARTIAL_URL, username, password);
        }
    }


    private class PostWebServiceTask extends AsyncTask<String, Void, String> {
        private final String SERVICE = "_login.php";

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 3) {
                throw new IllegalArgumentException("Three String arguments required.");
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
                String data = URLEncoder.encode("my_name", "UTF-8")
                        + "=" + URLEncoder.encode(strings[1], "UTF-8")
                        + "&" + URLEncoder.encode("my_pw", "UTF-8")
                        + URLEncoder.encode(strings[2], "UTF-8");
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
                sign_in_btn.setEnabled(true);
                return;
            }
            // TODO: 2017/2/6  authentication

        }

        protected void authenticate(String result){

        }
    }

}
