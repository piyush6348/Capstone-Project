package capstoneproject.androidnanodegree.com.capstoneproject2.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import capstoneproject.androidnanodegree.com.capstoneproject2.R;

public class NewsDetailActivity extends AppCompatActivity {

    private ImageView newsImage;
    private TextView newsTitle,newsDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        newsImage=(ImageView) findViewById(R.id.news_image);
        newsTitle=(TextView) findViewById(R.id.news_heading);
        newsDescription=(TextView)findViewById(R.id.news_description);

        Picasso.with(this).load(getIntent().getExtras().getString("url")).into(newsImage);
        newsTitle.setText(getIntent().getExtras().getString("title"));
        newsDescription.setText(getIntent().getExtras().getString("description"));
    }
}
