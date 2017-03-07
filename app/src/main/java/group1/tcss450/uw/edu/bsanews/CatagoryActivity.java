package group1.tcss450.uw.edu.bsanews;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Spinner;
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

import java.net.URI;
import java.util.ArrayList;

import group1.tcss450.uw.edu.bsanews.Model.News;
import group1.tcss450.uw.edu.bsanews.Model.NewsListAdapter;

/**
 * activity with a spinner allows user to choose a category.
 * @author Shao-han wang
 */
public class CatagoryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    /**
     * url to connect to our database.
     */
    private static final String mURL
            = "https://api.cognitive.microsoft.com/bing/v5.0/news/";
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
    /**
     * Progress bar.
     */
    private ProgressBar mProgressBar;
    /**
     * Spinner for Categories.
     */
    private Spinner mSpinner;
    public static Activity categoryAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catagory);
        categoryAct = this;
        mSpinner = (Spinner) findViewById(R.id.cat_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);
        mListView = (ListView) findViewById(R.id.cat_news_list_View);
        mUsername = getIntent().getStringExtra(KEY_USERNAME);
        mProgressBar = (ProgressBar) findViewById(R.id.cat_progressBar);
        mThat = this;
    }

    /**
     * Getting selected Item.
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String option = (String) parent.getAdapter().getItem(position);
        Toast.makeText(this,
                "Category: " + option,
                Toast.LENGTH_SHORT).show();
        AsyncTask<String, Void, String> task = new PostWebServiceTask();
            task.execute(mURL, option);
    }

    /**
     * Override method, we have no use for this feature.
     * @param parent
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    /**
     * create option menu.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_button, menu);
        return true;
    }

    /**
     * LogOut when LogOut menu is called.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.Return_home) {
            homeActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method will call the home activity.
     */
    private void homeActivity() {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra(KEY_USERNAME,mUsername);
//        startActivity(intent);
        finish();
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

                //for catagory
                builder.setParameter("Category", strings[1]);
                //set 50 newses
                builder.setParameter("count", "50");

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
            //String[] listItems = new String[0];
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
                    intent.putExtra("Activity", "category");
                    startActivity(intent);

                }
            });
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
