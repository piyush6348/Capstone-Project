package capstoneproject.androidnanodegree.com.capstoneproject2.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import capstoneproject.androidnanodegree.com.capstoneproject2.R;
import capstoneproject.androidnanodegree.com.capstoneproject2.database.DatabseColumns;
import capstoneproject.androidnanodegree.com.capstoneproject2.database.QuoteProvider;


public class ItemWidgetList extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new OtherWidgetList(this.getApplicationContext(), intent);
    }
}

class OtherWidgetList implements RemoteViewsService.RemoteViewsFactory {
    Cursor c;
    private Context context = null;
    private int appWidgetId;
    RemoteViews row;

    public OtherWidgetList(Context context, Intent intent) {

        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        Log.e("Item_Widget_Oncreate", "is running");
    }

    @Override
    public void onDataSetChanged() {
        Log.e("Item_Widget_onDataSEt", "is running");
        initData();
    }

    private void initData() {
        if (c != null)
            c.close();

        c = context.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onDestroy() {
        if (c != null)
            c.close();
    }

    @Override
    public int getCount() {
        return c.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        Log.e("Item_Widget_getview", "is running");
        String url = "";
        String title = "";

        if (c.moveToPosition(position)) {
            url = c.getString(c.getColumnIndex(DatabseColumns.URL));

            title = c.getString(c.getColumnIndex(DatabseColumns.TITLE));

        }
        Log.e("getViewAt: ", "hi " + title);
        row = new RemoteViews(context.getPackageName(), R.layout.list_item_widget);
        row.setTextViewText(R.id.heading, title);

        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

