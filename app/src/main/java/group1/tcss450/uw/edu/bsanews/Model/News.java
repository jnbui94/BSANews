package group1.tcss450.uw.edu.bsanews.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jnbui on 2/22/2017.
 */

public class News {
    private String mName;
    private String mUrl;
    private String mDescription;
    private String mImageUrl;

    public News(JSONObject json) throws JSONException{
        create(json);
    }
    private void create(JSONObject json) throws JSONException{
        int error_code = 0;
        if(error_code !=0){
            // Hello
        } else {
            mName = json.getString("name");
            mUrl = json.getString("url");
            mDescription = json.getString("description");
            JSONObject temp = json.getJSONObject("image");
            temp = temp.getJSONObject("thumbnail");
            mImageUrl = temp.getString("contentUrl");
        }

    }

    public String getName(){return mName;}
    public String getUrl() {return mUrl;}
    public String getDescription() {return mDescription;}
    public String getImageUrl() {return mImageUrl;}

}
