package com.example.managerapp.noteapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import androidx.annotation.NonNull;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.managerapp.R;

public class CollectionWidget extends AppWidgetProvider {

    // onUpdate copy-pasted from docs
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {    }

    @Override
    public void onDisabled(Context context) {    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Instantiate the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.list_widget);
        setRemoteAdapter(context, views);
        // Request for NoteEditActivity after onClick on note on the widget
        Intent startActivityIntent = new Intent(context, NoteEditActivity.class);
        PendingIntent startActivityPendingIntent = PendingIntent.getActivity(context, 0, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list_view, startActivityPendingIntent);
        // Request that the AppWidgetManager updates the application widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_list_view, new Intent(context, WidgetService.class));
    }

}
