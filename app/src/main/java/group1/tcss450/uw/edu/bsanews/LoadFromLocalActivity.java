package group1.tcss450.uw.edu.bsanews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import group1.tcss450.uw.edu.bsanews.Model.News;
import group1.tcss450.uw.edu.bsanews.Model.NewsDB;
import group1.tcss450.uw.edu.bsanews.Model.NewsListAdapter;

/**
 * load a list of news from local database.
 * @author SHoa-han wang.
 */
public class LoadFromLocalActivity extends AppCompatActivity {


    private final static String NEWS_KEY = "news";
    /**
     * the key for getting the username.
     */
    private static final String KEY_USERNAME = "USERNAME";
    AppCompatActivity mActivity;
    private String mUsername;
    private NewsDB mCourseDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_from_local);
        loadFromLocalDB();
        mUsername = getIntent().getStringExtra(KEY_USERNAME);
    }

    private void loadFromLocalDB(){
        if(mCourseDB == null) {
            mCourseDB = new NewsDB(this);
        }
        News[] newses = mCourseDB.getBookmarks();
        final News[] tempNewses = newses;
        NewsListAdapter adapter = new NewsListAdapter(this, newses);
        ListView mListView = (ListView) findViewById(R.id.load_from_local_ListView);
        mListView.setAdapter(adapter);
        mActivity = this;
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent(mActivity, NewsViewActivity.class);
                intent.putExtra(KEY_USERNAME, mUsername);
                intent.putExtra(NEWS_KEY, tempNewses[position]);
                startActivity(intent);

            }
        });
    }
}
