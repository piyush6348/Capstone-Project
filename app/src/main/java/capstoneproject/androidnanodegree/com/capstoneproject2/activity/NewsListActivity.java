package capstoneproject.androidnanodegree.com.capstoneproject2.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import capstoneproject.androidnanodegree.com.capstoneproject2.R;
import capstoneproject.androidnanodegree.com.capstoneproject2.models.NewsItemList;
import capstoneproject.androidnanodegree.com.capstoneproject2.network.NewsApiResponse;
import capstoneproject.androidnanodegree.com.capstoneproject2.utils.Constants;

public class NewsListActivity extends AppCompatActivity {

    private static  String TAG ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

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

            Log.e(TAG, "onPostExecute: "+s );
            Gson gson= new GsonBuilder().create();
            NewsItemList newsItemList=gson.fromJson(s,NewsItemList.class);
            Log.e(TAG, "onPostExecute: "+ newsItemList.getNewsItemList().size() );
        }

    }
}
