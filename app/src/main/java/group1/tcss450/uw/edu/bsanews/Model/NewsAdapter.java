package group1.tcss450.uw.edu.bsanews.Model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder>{
    private News[] mNewses;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, desc;

        public MyViewHolder(View view){
            super(view);
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
