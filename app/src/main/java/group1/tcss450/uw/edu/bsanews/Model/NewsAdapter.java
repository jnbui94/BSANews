package group1.tcss450.uw.edu.bsanews.Model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import group1.tcss450.uw.edu.bsanews.R;

// TODO: 2/28/2017  
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder>{
    private News[] mNewses;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView desc;
        public ImageView image;

        public MyViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.news_item_name);
            desc = (TextView) view.findViewById(R.id.news_item_desc);
            image = (ImageView) view.findViewById(R.id.news_item_image);
        }
    }

    public NewsAdapter(News[] newses){
        mNewses = newses;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mNewses.length;
    }


}
