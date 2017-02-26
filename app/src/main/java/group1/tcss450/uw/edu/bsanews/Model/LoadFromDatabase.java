package group1.tcss450.uw.edu.bsanews.Model;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * provide service for loading news from database.
 * template provided by instructor.
 * @author Shao-Han Wang
 */
public class LoadFromDatabase extends AsyncTask<String, Void, String> {
    private final String SERVICE = "_load.php";

    /**
     * get the activity using this class for showing Toast Msg.
     */
    private AppCompatActivity mActivity;

    private TextView mTextView;

    // TODO: 2017/2/25 take out textview later, this class should return a array of News. 
    /**
     * constructor, takes a activity as argument for showing toast,
     * and textView for showing result.
     * @param activity
     * @param textView
     */
    public LoadFromDatabase(AppCompatActivity activity, TextView textView){
        mActivity = activity;
        mTextView = textView;
    }

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
            Toast.makeText(mActivity.getApplicationContext(), result, Toast.LENGTH_LONG)
                    .show();

            return;
        }else {
            News[] newses = new News[0];
            try {
                JSONObject resultObj = new JSONObject(result);
                JSONArray value = resultObj.getJSONArray("value");
                newses = new News[value.length()];
                for (int i = 0; i < value.length(); i++){
                    JSONObject oneNews = (JSONObject) value.get(i);
                    Log.d("LoadFromDB one", oneNews.getString("url"));
                    newses[i] = new News(oneNews);
                }

            } catch (JSONException e){
                e.printStackTrace();
            }

            // TODO: 2017/2/25 will send back a array of News <newses>.
            //Display message from database.
            for (int i = 0; i < newses.length; i++) {
                mTextView.append(newses[i].getName()+"\n");
                mTextView.append(newses[i].getUrl()+"\n");
                mTextView.append(newses[i].getDescription()+"\n");
                mTextView.append("==============================\n");
            }
        }

    }

}
