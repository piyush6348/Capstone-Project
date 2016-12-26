package capstoneproject.androidnanodegree.com.capstoneproject2.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.squareup.picasso.Picasso;

import capstoneproject.androidnanodegree.com.capstoneproject2.R;

public class NewsDetailActivity extends AppCompatActivity {

    private static final int REQUEST_INVITE = 100;
    private ImageView newsImage;
    private TextView newsTitle,newsDescription;
    private Button shareNews;
    public static String TAG;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_INVITE)
        {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.e(TAG, "onActivityResult: sent invitation " + id);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        TAG=this.getClass().getSimpleName();

        newsImage=(ImageView) findViewById(R.id.news_image);
        newsTitle=(TextView) findViewById(R.id.news_heading);
        newsDescription=(TextView)findViewById(R.id.news_description);
        shareNews=(Button)findViewById(R.id.share_news);

        Picasso.with(this).load(getIntent().getExtras().getString("url")).into(newsImage);
        newsTitle.setText(getIntent().getExtras().getString("title"));
        newsDescription.setText(getIntent().getExtras().getString("description"));

        shareNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                        .setMessage(getString(R.string.invitation_message))
                        .setCallToActionText(getString(R.string.invitation_cta))
                        .build();
                startActivityForResult(intent, REQUEST_INVITE);
            }
        });
    }
}
