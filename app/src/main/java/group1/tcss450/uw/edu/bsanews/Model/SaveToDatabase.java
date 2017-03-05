package group1.tcss450.uw.edu.bsanews.Model;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * provide service for saving a news.
 * template provided by instructor.
 * @author Shao-Han Wang
 */
public class SaveToDatabase extends AsyncTask<String, Void, String> {
    private final String SERVICE = "_save.php";

    /**
     * get the activity using this class for showing Toast Msg.
     */
    private AppCompatActivity mActivity;

    /**
     * constructor, takes a activity as argument for showing toast.
     * @param activity
     */
    public SaveToDatabase(AppCompatActivity activity){
        mActivity = activity;
    }

    @Override
    protected String doInBackground(String... strings) {

        if (strings.length != 5) {
            throw new IllegalArgumentException("five String arguments required.");
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
                    + "&" + URLEncoder.encode("my_url", "UTF-8")
                    + "=" + URLEncoder.encode(strings[2],"UTF-8")
                    + "&" + URLEncoder.encode("my_title", "UTF-8")
                    + "=" + URLEncoder.encode(strings[3],"UTF-8")
                    + "&" + URLEncoder.encode("my_description", "UTF-8")
                    + "=" + URLEncoder.encode(strings[4],"UTF-8");
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


        Log.d("SaveToDatabase", "response: " + response);

        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        // Something wrong with the network or the URL.
        if (result.startsWith("Unable to")) {
            Toast.makeText(mActivity.getApplicationContext(), result, Toast.LENGTH_LONG)
                    .show();

            return;
        }else if(result.startsWith("true")){

            //if saved successfully show toast
            Toast.makeText(mActivity.getApplicationContext(),"Saved Successfully",
                    Toast.LENGTH_SHORT).show();
        }else if (result.startsWith("false")){
            //if failed show this message.
            Toast.makeText(mActivity.getApplicationContext(),"the URL already exists in Database",
                    Toast.LENGTH_SHORT).show();
        }else {
            // something wrong with sql.
            Toast.makeText(mActivity.getApplicationContext(),result, Toast.LENGTH_SHORT).show();
            Log.e("SavetoDB",result);
        }

    }

}
