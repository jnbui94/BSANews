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

import group1.tcss450.uw.edu.bsanews.Model.News;
import group1.tcss450.uw.edu.bsanews.Model.SaveToDatabase;

public class NewsViewActivity extends AppCompatActivity {
    private static final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/" +
            "~shw26/dbconnect";
    Menu mMenu;
    WebView mWebView;
    News mNews;
    String mUsername;
    final static String NEWS_KEY = "news";
    private static final String KEY_USERNAME = "USERNAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUsername = getIntent().getStringExtra(KEY_USERNAME);
        mNews = (News) getIntent().getSerializableExtra(NEWS_KEY);

        mWebView = (WebView) findViewById(R.id.news_webView);
        //mWebView.loadUrl(mNews.getUrl());
        // TODO: 2017/2/25 test data 
        mWebView.loadUrl("https://www.google.com/");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.option_menu, menu);
        mMenu = menu;


        return true;
    }

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
                task.execute(PARTIAL_URL,
                        mUsername,
                        "https://www.google.com/",
                        "google",
                        "search engine");
//                //save the web page to the database.
//                task.execute(PARTIAL_URL,
//                        mUsername,
//                        mNews.getUrl(),
//                        mNews.getName(),
//                        mNews.getDescription());
                break;
        }


        return super.onOptionsItemSelected(item);
    }

}
