package capstoneproject.androidnanodegree.com.capstoneproject2.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import capstoneproject.androidnanodegree.com.capstoneproject2.R;
import capstoneproject.androidnanodegree.com.capstoneproject2.adapter.NewsListCursorAdapter;
import capstoneproject.androidnanodegree.com.capstoneproject2.database.QuoteProvider;
import capstoneproject.androidnanodegree.com.capstoneproject2.models.NewsItemList;
import capstoneproject.androidnanodegree.com.capstoneproject2.network.NewsApiResponse;
import capstoneproject.androidnanodegree.com.capstoneproject2.utils.Constants;

public class NewsListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static String TAG;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    NewsListCursorAdapter adapter;
    public NewsListActivity con;
    private CoordinatorLayout coordinateLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        con = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinateLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout_news_list);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        TAG = this.getClass().getSimpleName();
        new NewsList().execute();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(this, QuoteProvider.Quotes.CONTENT_URI, null, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (adapter != null)
            adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    public class NewsList extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            NewsApiResponse obj = new NewsApiResponse();

            try {
                String response = obj.run(Constants.NEWS_URL);
                return response;
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: " + " no response ");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e(TAG, "onPostExecute: " + s);
            if (s != null) {
                Gson gson = new GsonBuilder().create();
                NewsItemList newsItemList = gson.fromJson(s, NewsItemList.class);
                // Log.e(TAG, "onPostExecute: "+ newsItemList.getNewsItemList().size() );


                for (int i = 0; i < newsItemList.getNewsItemList().size(); i++) {
                    int presence = 1;

                    Cursor cursor = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI, null, null, null, null);
                    cursor.moveToFirst();

                    String newsTitle = newsItemList.getNewsItemList().get(i).getTitle();
                    String newsDescription = newsItemList.getNewsItemList().get(i).getDescription();
                    String newsUrlToImage = newsItemList.getNewsItemList().get(i).getUrlToImage();

                    Log.e("onPostExecute: ", i + " " + newsTitle);

                    if (cursor.getCount() > 0) {
                        do {
                            if (newsTitle.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex("newsTitle")))) {
                                presence = 0;
                                break;
                            }
                        } while (cursor.moveToNext());
                    }

                    if (presence == 1) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("newsTitle", newsTitle.toString());
                        contentValues.put("newsDescription", newsDescription.toString());
                        contentValues.put("newsImageUrl", newsUrlToImage.toString());

                        getContentResolver().insert(QuoteProvider.Quotes.CONTENT_URI, contentValues);
                    }
                }

                Cursor cur = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI, null, null, null, null);
                adapter = new NewsListCursorAdapter(NewsListActivity.this, cur, NewsListActivity.this);
                adapter.notifyDataSetChanged();

                recyclerView.setAdapter(adapter);
                getSupportLoaderManager().initLoader(0, null, con);

            } else {
                Cursor cur = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI, null, null, null, null);
                if (cur.getCount() > 0) {
                    adapter = new NewsListCursorAdapter(NewsListActivity.this, cur, NewsListActivity.this);
                    adapter.notifyDataSetChanged();

                    recyclerView.setAdapter(adapter);
                    getSupportLoaderManager().initLoader(0, null, con);

                } else {
                    Snackbar snackbar = Snackbar.make(coordinateLayout, R.string.unable_to_connect, Snackbar.LENGTH_SHORT);
                    snackbar.show();

                }
            }
        }

    }
}
