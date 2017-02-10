package group1.tcss450.uw.edu.bsanews;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URI;

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
        mTextView = (TextView) findViewById(R.id.main_textView);
        
    }
    public void buttonClicked(View view) {
        Intent intent;
        AsyncTask<String, Void, String> task = null;
        String message = ((TextView) findViewById(R.id.main_textView)).getText().toString();
        switch (view.getId()) {
            case R.id.Head:
                task = new PostWebServiceTask();

                task.execute(mURL, message);
                break;
            case R.id.main_loadBtn:
                intent = new Intent(this, LoadActivity.class);
                startActivity(intent);
                break;
//            case R.id.postbutton:
//                task = new PostWebServiceTask();
//                break;
            case R.id.main_saveBtn:
                intent = new Intent(this, SaveActivity.class);
                startActivity(intent);
                break;

            default:
                throw new IllegalStateException("Not implemented");
        }
    }
    private class PostWebServiceTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 2) {
                throw new IllegalArgumentException("Two String arguments required.");
            }
            //String response = "";
            HttpClient httpclient = HttpClients.createDefault();

            try
            {

                HttpEntity entity;

                URIBuilder builder = new URIBuilder(mURL);


                URI uri = builder.build();
                HttpGet request = new HttpGet(uri);
                request.setHeader("Ocp-Apim-Subscription-Key", mKey);


                HttpResponse response = httpclient.execute(request);
                entity = response.getEntity();
                String result =  new String(EntityUtils.toString(entity));

                if (entity != null)
                {
                    Log.d("entity not null", result);
                }

                return result;
            }
            catch (Exception e)
            {
                Log.d("exception ",e.getMessage());
                return e.getMessage();
            }
//            HttpURLConnection urlConnection = null;
//            String url = strings[0];
//            try {
//                URL urlObject = new URL(mURL);
//                urlConnection = (HttpURLConnection) urlObject.openConnection();
//                urlConnection.setRequestMethod("POST");
//                urlConnection.setDoOutput(true);
//                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
//                String data = URLEncoder.encode("Ocp-Apim-Subscription-Key", "UTF-8")
//                        + "=" + URLEncoder.encode(mKey, "UTF-8");
//                wr.write(data);
//                wr.flush();
//                InputStream content = urlConnection.getInputStream();
//                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
//                String s = "";
//                while ((s = buffer.readLine()) != null) {
//                    response += s;
//                }
//            } catch (Exception e) {
//                response = "Unable to connect, Reason: "
//                        + e.getMessage();
//            } finally {
//                if (urlConnection != null)
//                    urlConnection.disconnect();
//            }
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
