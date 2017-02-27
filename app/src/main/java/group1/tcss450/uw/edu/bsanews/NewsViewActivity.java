package group1.tcss450.uw.edu.bsanews;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import group1.tcss450.uw.edu.bsanews.Model.News;
import group1.tcss450.uw.edu.bsanews.Model.SaveToDatabase;

/**
 * this activity will display a web page and provide the ability to save the page in database.
 * required a News object.
 * @author Shao-han wang
 */
public class NewsViewActivity extends AppCompatActivity {
    /**
     * url for connect the database.
     */
    private static final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/" +
            "~shw26/dbconnect";
    /**
     * key for receiving News object.
     */
    private final static String NEWS_KEY = "news";
    /**
     * key for username.
     */
    private static final String KEY_USERNAME = "USERNAME";

    /**
     * hold News object
     */
    private News mNews;
    /**
     * hold user name;
     */
    private String mUsername;
    /**
     * hold the webview.
     */
    private WebView mWebView;

    /**
     * onCreate, initialize the fields.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUsername = getIntent().getStringExtra(KEY_USERNAME);
        mNews = (News) getIntent().getSerializableExtra(NEWS_KEY);

        mWebView = (WebView) findViewById(R.id.news_webView);
        mWebView.setWebViewClient(new WebViewClient());

        mWebView.loadUrl(mNews.getUrl());
        // TODO: 2017/2/25 test data 
        //mWebView.loadUrl("https://www.google.com/");

    }

    /**
     * create option menu.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.option_menu, menu);
        //mMenu = menu;

        return true;
    }

    /**
     * the action when an option is pressed.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.save_menu_button:
                AsyncTask<String, Void, String> task;

                // TODO: 2017/2/25 test savetodatabase class.
                //task = new SaveActivity.PostWebServiceTask();

                task = new SaveToDatabase(this);

                // TODO: 2017/2/25 test data.
//                task.execute(PARTIAL_URL,
//                        mUsername,
//                        mWebView.getUrl(),
//                        "yahoo",
//                        "search engine");
                //save the web page to the database.
                task.execute(PARTIAL_URL,
                        mUsername,
                        mWebView.getUrl(),
                        mNews.getName(),
                        mNews.getDescription());
                break;
        }


        return super.onOptionsItemSelected(item);
    }

}
