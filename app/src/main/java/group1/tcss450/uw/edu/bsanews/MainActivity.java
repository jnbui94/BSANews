package group1.tcss450.uw.edu.bsanews;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;

import group1.tcss450.uw.edu.bsanews.Model.News;

/**
 * this activity provide the main menu.
 * @author John Bui
 */
public class MainActivity extends AppCompatActivity {
    /**
     * url to connect to our database.
     */
    private static final String mURL
            = "https://api.cognitive.microsoft.com/bing/v5.0/news/";
    /**
     * allow the asynctask to change the content.
     */
    private TextView mTextView;
    /**
     * keys for connect to external database.
     */
    private static  final String mKey = "3abb779da1e740bfab3f95c7fed2475c";
    private static final String mKey1 = "cbfd463af4de4614af5482ce40870522";

    /**
     * the key for getting the username.
     */
    private static final String KEY_USERNAME = "USERNAME";
    /**
     * ListView field
     */
    private ListView mListView;
    /**
     * ArrayList for listview.
     */
    private ArrayList<News> newsList = null;
    /**
     * hold the username.
     */
    private String mUsername;

    /**
     * initialize the contents.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.main_textView);
        mListView = (ListView) findViewById(R.id.News_list_View);
        mUsername = getIntent().getStringExtra(KEY_USERNAME);
        
    }

    /**
     * when a button is clicked.
     * @param view
     */
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
                intent.putExtra(KEY_USERNAME, mUsername);
                startActivity(intent);
                break;
//            case R.id.postbutton:
//                task = new PostWebServiceTask();
//                break;
            case R.id.main_saveBtn:
                intent = new Intent(this, SaveActivity.class);
                // TODO: 2017/2/25 test newsViewActivity
                intent = new Intent(this, NewsViewActivity.class);
                intent.putExtra(KEY_USERNAME, mUsername);
                startActivity(intent);
                break;

            default:
                throw new IllegalStateException("Not implemented");
        }
    }

    /**
     * sample code provided by instructor.
     */
    private class PostWebServiceTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 2) {
                throw new IllegalArgumentException("Two String arguments required.");
            }
            //String response = "";
            HttpClient httpclient = HttpClients.createDefault();

            //modified example code from microsoft
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

                //debug purposes.
                if (entity != null)
                {
                    Log.d("entity not null", result);
                }

                return result;
            }
            catch (Exception e)
            {
                //Log.d("exception ",e.getMessage());
                String result = "Unable to connect, Reason: " + e.getMessage();
                return e.getMessage();
            }
        }
        @Override
        protected void onPostExecute(String result) {
            String[] listItems = null;
            News news = null;
           
// Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(result);
//                News news = new News(jsonObject);
                newsList = news.getNews(jsonObject);

                listItems = new String[newsList.size()];
                for(int i = 0; i < newsList.size(); i++){
                    News temp = newsList.get(i);
                    listItems[i] = temp.getName();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_activated_1, listItems);
            mListView.setAdapter(adapter);
           // mTextView.setText(result);
        }
    }
}
