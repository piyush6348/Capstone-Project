package capstoneproject.androidnanodegree.com.capstoneproject2.activity;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import capstoneproject.androidnanodegree.com.capstoneproject2.R;
import capstoneproject.androidnanodegree.com.capstoneproject2.adapter.NewsListCursorAdapter;
import capstoneproject.androidnanodegree.com.capstoneproject2.database.DatabseColumns;
import capstoneproject.androidnanodegree.com.capstoneproject2.database.QuoteProvider;
import capstoneproject.androidnanodegree.com.capstoneproject2.models.NewsItemList;
import capstoneproject.androidnanodegree.com.capstoneproject2.network.NewsApiResponse;
import capstoneproject.androidnanodegree.com.capstoneproject2.utils.Constants;

public class NewsListActivity extends AppCompatActivity {

    private static  String TAG ;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView=(RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        TAG=this.getClass().getSimpleName();
        new NewsList().execute();
    }

    public class NewsList extends AsyncTask<Void,Void,String>
    {

        @Override
        protected String doInBackground(Void... voids) {

            NewsApiResponse obj=new NewsApiResponse();

            try{
                String response= obj.run(Constants.NEWS_URL);
                return response;
            }catch (Exception e){
                Log.e(TAG, "doInBackground: "+" no response " );
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //Log.e(TAG, "onPostExecute: "+s );
            Gson gson= new GsonBuilder().create();
            NewsItemList newsItemList=gson.fromJson(s,NewsItemList.class);
           // Log.e(TAG, "onPostExecute: "+ newsItemList.getNewsItemList().size() );

            for(int i=0;i<newsItemList.getNewsItemList().size();i++)
            {
                int presence=1;

                Cursor cursor=getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,null,null,null,null);
                cursor.moveToFirst();

                String newsTitle=newsItemList.getNewsItemList().get(i).getTitle();
                String newsDescription=newsItemList.getNewsItemList().get(i).getDescription();
                String newsUrlToImage=newsItemList.getNewsItemList().get(i).getUrlToImage();

                Log.e("onPostExecute: ", i+ " "+ newsTitle);

                if(cursor.getCount()>0)
                {
                    do {
                        if(newsTitle.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex("newsTitle"))))
                        {
                            presence=0;
                            break;
                        }
                    }while (cursor.moveToNext());
                }

                if(presence==1)
                {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("newsTitle", newsTitle.toString());
                    contentValues.put("newsDescription", newsDescription.toString());
                    contentValues.put("newsImageUrl", newsUrlToImage.toString());

                    getContentResolver().insert(QuoteProvider.Quotes.CONTENT_URI, contentValues);
                }
            }

            Cursor cur=getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,null,null,null,null);
            NewsListCursorAdapter adapter=new NewsListCursorAdapter(NewsListActivity.this,cur);
            adapter.notifyDataSetChanged();

            recyclerView.setAdapter(adapter);
        }

    }
}
