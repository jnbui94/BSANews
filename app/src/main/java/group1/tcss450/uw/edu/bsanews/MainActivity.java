package group1.tcss450.uw.edu.bsanews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;

import group1.tcss450.uw.edu.bsanews.Model.News;
import group1.tcss450.uw.edu.bsanews.Model.NewsListAdapter;

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
     * SharePref variable
     */
    private SharedPreferences mPrefs;
    /**
     * the key for getting the username.
     */
    private static final String KEY_USERNAME = "USERNAME";
    /**
     * key for receiving News object.
     */
    private final static String NEWS_KEY = "news";
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
     * allow async task to access the activity.
     */
    private AppCompatActivity mThat;

    private ProgressBar mProgressBar;

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
        mPrefs = getSharedPreferences(getString(R.string.SHARED_PREFS), Context.MODE_PRIVATE);
        mUsername = getIntent().getStringExtra(KEY_USERNAME);
        mProgressBar = (ProgressBar) findViewById(R.id.main_progressBar);
        mThat = this;
        AsyncTask<String, Void, String> task = null;
        String message = ((TextView) findViewById(R.id.main_textView)).getText().toString();
        task = new PostWebServiceTask();
        task.execute(mURL, message);
    }
    public void logout() {
        mPrefs.edit().putString(getString(R.string.UserName),"0").apply();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
    /**
     * create option menu.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }
    /**
     * LogOut when LogOut menu is called.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.Log_out_item) {
            logout();
        }

        return super.onOptionsItemSelected(item);
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
                // TODO: 3/1/2017 remove this
                intent = new Intent(this, SaveActivity.class);
                // TODO: 2017/2/25 test newsViewActivity
                intent = new Intent(this, NewsViewActivity.class);

                intent = new Intent(this, CatagoryActivity.class);
                intent.putExtra(KEY_USERNAME, mUsername);
                startActivity(intent);
                break;

            case R.id.main_loadLocalBtn:
                intent = new Intent(this, LoadFromLocalActivity.class);
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
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

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

                String result = "Unable to connect, Reason: " + e.getMessage();
                return e.getMessage();
            }
        }
        @Override
        protected void onPostExecute(String result) {

            News news = null;
           
// Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            News[] newses = new News[0];
            try {
                JSONObject resultObj = new JSONObject(result);
                JSONArray value = resultObj.getJSONArray("value");
                newses = new News[value.length()];
                //listItems = new String[value.length()];
                for (int i = 0; i < value.length(); i++){
                    JSONObject oneNews = (JSONObject) value.get(i);
                    Log.d("LoadFromDB one", oneNews.getString("url"));
                    newses[i] = new News(oneNews);
                    //listItems[i] = newses[i].getName();
                }

            } catch (JSONException e){
                e.printStackTrace();
            }

            final News[] tempNewses = newses;
            NewsListAdapter adapter = new NewsListAdapter(mThat, newses);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
                    Intent intent = new Intent(mThat, NewsViewActivity.class);
                    intent.putExtra(KEY_USERNAME, mUsername);
                    intent.putExtra(NEWS_KEY, tempNewses[position]);
                    startActivity(intent);

                }
            });
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
