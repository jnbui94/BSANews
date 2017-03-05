package group1.tcss450.uw.edu.bsanews.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import group1.tcss450.uw.edu.bsanews.R;

/**
 * local database.
 * @author Shao-han wang
 */
public class NewsDB implements Serializable{
    public static final int DB_VERSION = 1;
    private final String DB_NAME;
    private final String News_TABLE;
    private final String[] COLUMN_NAMES;
    private Context mContext;
    private NewsDBHelper mNewsDBHelper;
    private SQLiteDatabase mSQLiteDatabase;

    public NewsDB(Context context){
        COLUMN_NAMES = context.getResources().getStringArray(R.array.DB_COLUMN_NAMES);
        DB_NAME = context.getString(R.string.DB_NAME);
        News_TABLE = context.getString(R.string.TABLE_NAME);
        mContext = context;

        mNewsDBHelper = new NewsDBHelper(
                context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mNewsDBHelper.getWritableDatabase();
    }

    /**
     * Insert new dataset into database.
     * will check if the value already exist or not.
     * @param name
     * @param url
     * @param desc
     * @return true if success, false otherwise.
     */
    public boolean insertNews(String name, String url, String desc){

        if (!checkExist(url)) {
            ContentValues contextValues = new ContentValues();

            contextValues.put(COLUMN_NAMES[0], name);
            contextValues.put(COLUMN_NAMES[1], url);
            contextValues.put(COLUMN_NAMES[2], desc);


            long rowId = mSQLiteDatabase.insert(News_TABLE, null, contextValues);
            return rowId != -1;
        } else {
            return false;
        }
    }

    /**
     * check if url exist in the db.
     * @param url
     * @return true if exist, false otherwise.
     */
    public boolean checkExist(String url){
        String query = "SELECT url FROM Bookmarks WHERE url=?";
        Cursor c = mSQLiteDatabase.rawQuery(query, new String[]{url});
        if (c.moveToFirst()){
            return true;
        }else {
            return false;
        }
    }

    public void closeDB(){
        mSQLiteDatabase.close();
    }

    public News[] getBookmarks(){

        Cursor c = mSQLiteDatabase.query(
                News_TABLE,
                COLUMN_NAMES,
                null, null, null, null, null
        );
        c.moveToFirst();
        List<News> list = new ArrayList<News>();
        for (int i = 0 ; i < c.getCount(); i++){
            String name = c.getString(0);
            Log.d("newsdb", name);
            String url = c.getString(1);
            String desc = c.getString(2);
            JSONObject newsJson = new JSONObject();
            try {
                newsJson.put("name", name);
                newsJson.put("url", url);
                newsJson.put("description", desc);
                list.add(new News(newsJson));
                c.moveToNext();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return (News[]) list.toArray(new News[list.size()]);
    }

    class NewsDBHelper extends SQLiteOpenHelper{

        private final String CREATE_NEWS_SQL;

        private final String DROP_NEWS_SQL;

        public NewsDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
            super(context, name, factory, version);
            CREATE_NEWS_SQL = context.getString(R.string.CREATE_NEWS_SQL);
            DROP_NEWS_SQL = context.getString(R.string.DROP_NEWS_SQL);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_NEWS_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_NEWS_SQL);
            onCreate(db);
        }
    }
}
