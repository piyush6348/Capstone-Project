package capstoneproject.androidnanodegree.com.capstoneproject2.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import capstoneproject.androidnanodegree.com.capstoneproject2.R;
import capstoneproject.androidnanodegree.com.capstoneproject2.activity.NewsDetailActivity;
import capstoneproject.androidnanodegree.com.capstoneproject2.database.QuoteProvider;

public class NewsListCursorAdapter extends CursorRecyclerViewAdapter<NewsListCursorAdapter.ViewHolder> {

    static private Context context;

    public NewsListCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView newsHeadline,newsDescription;
        public ImageView newsImage;
        private FirebaseAnalytics mFirebaseAnalytics;

        public ViewHolder(View view) {
            super(view);
            newsHeadline = (TextView) view.findViewById(R.id.heading);
            newsDescription = (TextView) view.findViewById(R.id.description);
            newsImage = (ImageView) view.findViewById(R.id.thumbnail);
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "" + getAdapterPosition());
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, newsHeadline.getText().toString());
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

            Cursor c = view.getContext().getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI, null, null, null, null);
            c.moveToFirst();
            c.moveToPosition(getAdapterPosition());

            Log.e( "onClick: ", "Hi"+c.getString(c.getColumnIndex("newsTitle")) );

            Bundle bundle1=new Bundle();
            bundle1.putString("title",c.getString(c.getColumnIndex("newsTitle")));
            bundle1.putString("description",c.getString(c.getColumnIndex("newsDescription")));
            bundle1.putString("url",c.getString(c.getColumnIndex("newsImageUrl")));

            Intent intent=new Intent(context, NewsDetailActivity.class);
            intent.putExtras(bundle1);
            context.startActivity(intent);
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