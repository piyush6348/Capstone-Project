package capstoneproject.androidnanodegree.com.capstoneproject2.network;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by dell on 12/25/2016.
 */

public class NewsApiResponse {
    OkHttpClient client = new OkHttpClient();

    public String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        Log.e("i", response.code() + "");
        return response.body().string();
    }
}
