package capstoneproject.androidnanodegree.com.capstoneproject2.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import capstoneproject.androidnanodegree.com.capstoneproject2.R;

public class SimpleWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {

            Intent intent = new Intent(context, ItemWidgetList.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
                remoteViews.setRemoteAdapter(R.id.widget_lv, intent);
            else
                remoteViews.setRemoteAdapter(appWidgetIds[i], R.id.widget_lv, intent);

            remoteViews.setEmptyView(R.id.widget_lv, R.id.empty_view);

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i], R.id.widget_lv);
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
