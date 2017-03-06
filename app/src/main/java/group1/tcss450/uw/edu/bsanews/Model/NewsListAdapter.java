package group1.tcss450.uw.edu.bsanews.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import group1.tcss450.uw.edu.bsanews.R;

/**
 * Adaptor for list to show the image and details.
 * @author Aygun Avazova
 */
public class NewsListAdapter extends BaseAdapter{
    /**
     * Context variable
     */
    private Context mContext;
    /**
     * Layout inflator to inflate layout.
     */
    private LayoutInflater mInflater;
    /**
     * List of Newses.
     *
     */
    private News[] mNewses;

    /**
     * Constructor.
     * @param context
     * @param newses
     */
    public NewsListAdapter(Context context, News[] newses){
        mContext=context;
        mNewses=newses;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /**
     * @return length of news.
     */
    @Override
    public int getCount() {
        return mNewses.length;
    }

    /**
     *
     * @param position position of news.
     * @return news of obect of a particular position.
     */
    @Override
    public Object getItem(int position) {
        return mNewses[position];
    }

    /**
     * @param position given position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * get View.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        News news = mNewses[position];
        View row = mInflater.inflate(R.layout.news_item, parent, false);
        TextView name = (TextView) row.findViewById(R.id.news_item_name);
        TextView desc = (TextView) row.findViewById(R.id.news_item_desc);
        ImageView image = (ImageView) row.findViewById(R.id.news_item_image);

        name.setText(news.getName());
        desc.setText(news.getDescription());
        //use Picaso to load image.
        Picasso.with(mContext).load(news.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(image);

        return row;
    }
}
