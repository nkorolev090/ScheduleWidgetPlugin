package com.example.schedule_plugins;

import android.app.Activity;
import java.io.*;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
 */ public class ScheduleWidgetScroll extends AppWidgetProvider {



    static String IdDath = "/storage/emulated/0/Android/data/gl.fx.ispu/cache/ScrollWidgetId.bin";//com.example.UnityPlugins//ru.ispu.mess


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

            Log.d("UpdateFromUnityScroll", String.valueOf(appWidgetId));
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(UnityPlayer.currentActivity);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_scroll_view);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

    }



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        //DynamicColors.applyToActivitiesIfAvailable(UnityPlayer.currentActivity.getApplication());
        /*Log.d("coffe", Integer.toString(R.color.coffe));
        Log.d("light_blue_200", Integer.toString(R.color.light_blue_200));
        Log.d("oldLeft", Integer.toString(R.color.oldLeft));
        Log.d("newRight", Integer.toString(R.color.newRight));
        Log.d("light_blue_900", Integer.toString(R.color.light_blue_900));*/
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            Log.d("OnUpdateScroll", String.valueOf(appWidgetId));
            Intent serviceIntent = new Intent(context, ScheduleWidgetService.class);
            RemoteViews views  = new RemoteViews(context.getPackageName(), R.layout.schedule_widget_scroll);

            byte[] data = ByteBuffer.allocate(4).putInt(appWidgetId).array();
            try {
                Files.write(Paths.get(IdDath), data);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            /*try(FileWriter writer = new FileWriter("/storage/emulated/0/Android/data/com.example.UnityPlugins/files/ColorData.json",false )) {
                writer.write("{\"mainColor\": 0, \"lecColor\": 0, \"semColor\": 0, \"labColor\": 0, \"timeColor\": 0}");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }*/
            views.setRemoteAdapter(R.id.widget_scroll_view, serviceIntent);
            views.setEmptyView(R.id.widget_scroll_view, R.id.widget_scroll_empty_view);
            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_scroll_view);
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