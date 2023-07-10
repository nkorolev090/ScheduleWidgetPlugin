package com.example.schedule_plugins;

import android.app.Activity;
import java.io.*;

import android.app.Application;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.ViewDebug;
import android.widget.RemoteViews;

import com.google.android.material.color.DynamicColors;
import com.unity3d.player.UnityPlayer;

import org.json.JSONException;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Dictionary;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */ public class ScheduleWidget extends AppWidgetProvider {



    static String IdDath = "/storage/emulated/0/Android/data/ru.ispu.mess/cache/StackWidgetId.bin";//com.example.UnityPlugins


    static void UpdateFromUnity(){

        int appWidgetId = 0;
        File f = new File(IdDath);
        if(f.exists()) {
            try {
                byte[] buffer = Files.readAllBytes(Paths.get(IdDath));
                appWidgetId = ByteBuffer.wrap(buffer).getInt();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Log.d("UpdateFromUnity", String.valueOf(appWidgetId));
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(UnityPlayer.currentActivity);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_stack_view);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

    }



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        //DynamicColors.applyToActivitiesIfAvailable(UnityPlayer.currentActivity.getApplication());
        Log.d("light_purple", Integer.toString(R.color.light_purple));
        Log.d("dark_purple", Integer.toString(R.color.dark_purple));
        Log.d("light_green", Integer.toString(R.color.light_green));
        Log.d("dark_green", Integer.toString(R.color.dark_green));
        Log.d("light_red", Integer.toString(R.color.light_red));
        Log.d("dark_red", Integer.toString(R.color.dark_red));
        Log.d("light_blue", Integer.toString(R.color.light_blue));
        Log.d("dark_blue", Integer.toString(R.color.dark_blue));
        Log.d("light_yellow", Integer.toString(R.color.light_yellow));
        Log.d("dark_yellow", Integer.toString(R.color.dark_yellow));
        Log.d("light_rose", Integer.toString(R.color.light_rose));
        Log.d("dark_rose", Integer.toString(R.color.dark_rose));
        Log.d("light_cyan", Integer.toString(R.color.light_cyan));
        Log.d("dark_cyan", Integer.toString(R.color.dark_cyan));
        Log.d("light_orange", Integer.toString(R.color.light_orange));
        Log.d("dark_orange", Integer.toString(R.color.dark_orange));
        Log.d("light_azure", Integer.toString(R.color.light_azure));
        Log.d("dark_azure", Integer.toString(R.color.dark_azure));
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            Log.d("OnUpdate", String.valueOf(appWidgetId));
            Intent serviceIntent = new Intent(context, ScheduleWidgetService.class);
            RemoteViews views  = new RemoteViews(context.getPackageName(), R.layout.schedule_widget);

            byte[] data = ByteBuffer.allocate(4).putInt(appWidgetId).array();
            try {
                Files.write(Paths.get(IdDath), data);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            views.setRemoteAdapter(R.id.widget_stack_view, serviceIntent);
            views.setEmptyView(R.id.widget_stack_view, R.id.widget_empty_view);
            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_stack_view);
        }


    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}