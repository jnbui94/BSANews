package group1.tcss450.uw.edu.bsanews.Model;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jnbui on 2/22/2017.
 */

public class News implements Serializable {
    private String mName;
    private String mUrl;
    private String mDescription;
    private String mImageUrl;

    public News(JSONObject json)  throws JSONException{
        create(json);
//        getNews(json);
    }
    private void create(JSONObject json) throws JSONException{
            mName = json.getString("name");
            mUrl = json.getString("url");
            mDescription = json.getString("description");

            if(json.has("image")) {
                JSONObject temp = json.getJSONObject("image");
                temp = temp.getJSONObject("thumbnail");
                mImageUrl = temp.getString("contentUrl");
            }

    }


//    public static ArrayList<News> getNews(JSONObject json) {
//        final ArrayList<News> recipeList = new ArrayList<>();
//        try {
//            News news = new News(json);
//            news.mName = json.getString("name");
//            news.mDescription = json.getString("description");
//            if (json.has("image")) {
//                JSONObject temp = json.getJSONObject("image");
//                temp = temp.getJSONObject("thumbnail");
//                news.mImageUrl = temp.getString("contentUrl");
//            }
//            news.mUrl = json.getString("url");
//
//            recipeList.add(news);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return recipeList;
//    }
    public String getName(){return mName;}
    public String getUrl() {return mUrl;}
    public String getDescription() {return mDescription;}
    public String getImageUrl() {return mImageUrl;}
}
