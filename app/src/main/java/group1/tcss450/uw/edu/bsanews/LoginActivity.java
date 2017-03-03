package group1.tcss450.uw.edu.bsanews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
 * @author shao-han wang
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    /**
     * url of the database.
     */
    private static final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/" +
            "~shw26/dbconnect";

    /**
     * the key for passing username by intent.
     */
    private static final String KEY_USERNAME = "USERNAME";
    /**
     * SharePref variable
     */
    private SharedPreferences mPrefs;
    /**
     * for inner class to enable the button.
     */
    private Button mSignInBtn;
    /**
     * Variable to keep track of checkBox
     */
    private boolean mCheck;
    /**
     * store the user name for passing to another activity.
     */
    private String mUsername;
    /**
     * Check variable for save log in.
     */
    private boolean mTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPrefs = getSharedPreferences(getString(R.string.SHARED_PREFS), Context.MODE_PRIVATE);
        mUsername = mPrefs.getString(getString(R.string.UserName),"0");
        if (!mUsername.equals("0")) {
            goToMainActivity();
        }
        Button btn = (Button) findViewById(R.id.email_sign_in_button);
        btn.setOnClickListener(this);
        mSignInBtn = btn;
        btn = (Button) findViewById(R.id.login_registerBtn);
        btn.setOnClickListener(this);

    }

    /**
     * when login button clicked, or register button clicked.
     * @param view
     */
    /*public void loginClicked(View view){
        mSignInBtn = (Button) findViewById(R.id.email_sign_in_button);
        switch(view.getId()){
            case R.id.email_sign_in_button:
                attemptLogin(view);
                break;
        }

    }*/
    /**
     * Checking check Box if checked or not
     */
    private boolean isCheck(){
        CheckBox check = (CheckBox) findViewById(R.id.remember_me);
        if(check.isChecked()) { return mCheck = true;}
        return mCheck;
    }
    /**
     * This method will save to sharepref
     */
    public void saveToSharePref(String theUsername){
        if (isCheck()&& mTemp) {
            mPrefs.edit().putString(getString(R.string.UserName),theUsername).apply();
        }
    }
    /**
     * When sign in clicked or register clicked.
     * @param view
     */
    @Override
    public void onClick(View view) {
        //mSignInBtn = (Button) findViewById(R.id.email_sign_in_button);
        switch(view.getId()){
            case R.id.email_sign_in_button:
                attemptLogin(view);
                break;
            case R.id.login_registerBtn:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }

    }

    /**
     * attempt to Login.
     * @param view
     */
    protected void attemptLogin(View view){
        TextView usernameTextView = (TextView) findViewById(R.id.email);
        TextView passwordTextView = (TextView) findViewById(R.id.password);
        boolean cancel = false;
        View focusView = usernameTextView;

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
            usernameTextView.setError("INVALID USERNAME");
            focusView = usernameTextView;
            cancel = true;
        }
        if (username.contains("'")){
            usernameTextView.setError("INVALID USERNAME, contains \" ' \" ");
            focusView = usernameTextView;
            cancel = true;
        }

        if (password.contains("'")){
            passwordTextView.setError("INVALID USERNAME, contains \" ' \" ");
            focusView = passwordTextView;
            cancel = true;
        }
        //if the values are not entered properly, else try to connect the server
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }else {
            //connect server
            mUsername = username;
            AsyncTask<String, Void, String> task = null;
            mSignInBtn.setEnabled(false);
            task = new PostWebServiceTask();
            task.execute(PARTIAL_URL, username, password);

        }
    }

    /**
     * the web service task will call this method if the username/password passed the authentication.
     */
    private void goToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(KEY_USERNAME,mUsername);
        startActivity(intent);
    }


    /**
     * Code provided by instructor.
     */
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
                //my_name=username&my_pw=password
                String data = URLEncoder.encode("my_name", "UTF-8")
                        + "=" + URLEncoder.encode(strings[1], "UTF-8")
                        + "&" + URLEncoder.encode("my_pw", "UTF-8")
                        + "=" + URLEncoder.encode(strings[2], "UTF-8");
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
                mSignInBtn.setEnabled(true);
                return;
            }else if (!result.startsWith("true")){
                //if the username or the password is not correct.
                mSignInBtn.setEnabled(true);
                Toast.makeText(getApplicationContext(),"username or password not correct", Toast.LENGTH_SHORT).show();
            }else if (result.startsWith("true")){
                //if the username and password matches a data in the db.
                Toast.makeText(getApplicationContext(),"login success",Toast.LENGTH_SHORT).show();
                mSignInBtn.setEnabled(true);
                mTemp = true;
                saveToSharePref(mUsername);
                goToMainActivity();
            }

        }

    }

}
