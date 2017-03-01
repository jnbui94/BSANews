package group1.tcss450.uw.edu.bsanews;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

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
    ProgressBar mProgressBar;

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
        mProgressBar = (ProgressBar) findViewById(R.id.news_progressBar);
        mWebView = (WebView) findViewById(R.id.news_webView);

        //load webpage without opening external browser, and show/hide progress bar
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressBar.setVisibility(View.VISIBLE);
                //mWebView.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
            }
        });

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
        if (mUsername == null){
            MenuItem item = menu.findItem(R.id.save_menu_button);
            item.setVisible(false);
        }
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
