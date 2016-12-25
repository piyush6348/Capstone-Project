package capstoneproject.androidnanodegree.com.capstoneproject2.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dell on 12/25/2016.
 */

public class NewsItemList {
    @SerializedName("articles")
    private List<NewsItem> newsItemList;

    public List<NewsItem> getNewsItemList() {
        return newsItemList;
    }
}
