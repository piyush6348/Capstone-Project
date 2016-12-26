package capstoneproject.androidnanodegree.com.capstoneproject2.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import capstoneproject.androidnanodegree.com.capstoneproject2.R;

public class NewsListCursorAdapter extends CursorRecyclerViewAdapter<NewsListCursorAdapter.ViewHolder> {

    static private Context context;

    public NewsListCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView newsHeadline,newsDescription;
        public ImageView newsImage;
        private FirebaseAnalytics mFirebaseAnalytics;

        public ViewHolder(View view) {
            super(view);
            newsHeadline = (TextView) view.findViewById(R.id.heading);
            newsDescription = (TextView) view.findViewById(R.id.description);
            newsImage = (ImageView) view.findViewById(R.id.thumbnail);
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_card, parent, false);

        ViewHolder vh = new ViewHolder(itemView);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        viewHolder.newsHeadline.setText(cursor.getString(cursor.getColumnIndex("newsTitle")));
        viewHolder.newsDescription.setText(cursor.getString(cursor.getColumnIndex("newsDescription")));
        Picasso.with(context)
                .load(cursor.getString(cursor.getColumnIndex("newsImageUrl")))
                .into(viewHolder.newsImage);
    }

}