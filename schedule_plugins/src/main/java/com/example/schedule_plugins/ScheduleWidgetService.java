package com.example.schedule_plugins;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class ScheduleWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ScheduleWidgetItemFactory(getApplicationContext(), intent);
    }

   static class ScheduleWidgetItemFactory implements RemoteViewsFactory{

        private Context context;
        private static String dataPath = "/storage/emulated/0/Android/data/gl.fx.ispu/files/ScheduleData.json";//
       private static String color_dataPath = "/storage/emulated/0/Android/data/gl.fx.ispu/files/ColorData.json";//com.example.UnityPlugins
        private static String[] times = {"8:00-9:35","9:50-11:25","11:40-13:15","14:00-15:35","15:50-17:25","17:40-19:15","19:25-21:00"};
        private static ScheduleDay[] data;
        /*private static int mainColor = R.color.coffe;
        private static int semColor = android.R.color.holo_green_light;
       private static int lecColor = android.R.color.holo_orange_light;
       private static int labColor = android.R.color.holo_red_light;
*/
       private static ScheduleColor scheduleColor;
       private static int timeColor = android.R.color.holo_blue_light;
       private static int[] liners = {R.id.liner0, R.id.liner1, R.id.liner2, R.id.liner3, R.id.liner4, R.id.liner5, R.id.liner6};
       private static int mainLayout = R.id.main_layout;
       private static int dateLayout = R.id.layoutDate;
      //private static int[] layouts = {R.layout.linear_layout_item1, R.layout.linear_layout_item2, R.layout.linear_layout_item3, R.layout.linear_layout_item4, R.layout.linear_layout_item5, R.layout.linear_layout_item6, R.layout.linear_layout_item};
       private static int[][] items = {{ R.id.textView0, R.id.textView1, R.id.textView2, R.id.textView3, R.id.textView4, R.id.textView5, R.id.textView6},{ R.id.textView10, R.id.textView11, R.id.textView12, R.id.textView13, R.id.textView14, R.id.textView15, R.id.textView16} };
        ScheduleWidgetItemFactory(Context context, Intent intent){
        this.context = context;
        }
        public static void SetExampleData() throws JSONException {

            String json = null;
            File f = new File(dataPath);
            if(f.exists()) {
            try {
                byte[] buffer = Files.readAllBytes(Paths.get(dataPath));
                json = new String(buffer, "UTF-8");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if(json != null){
            JSONArray result = new JSONArray(json);
            data = new ScheduleDay[result.length()];
            for(int i = 0; i < result.length();i++){
                data[i] = new ScheduleDay();


                    data[i].id = result.optJSONObject(i).getString("id");

                    data[i].date =  result.optJSONObject(i).getString("date");

                data[i].pars = new HashMap<String, SchedulePar>();
                for (String time:times) {
                    if(result.optJSONObject(i).optJSONObject("shedulePars").optJSONObject(data[i].id + " " + time)!=null){
                        SchedulePar par = new SchedulePar();

                        par.id = result.optJSONObject(i).optJSONObject("shedulePars").optJSONObject(data[i].id + " " + time).getString("id");
                        par.date = result.optJSONObject(i).optJSONObject("shedulePars").optJSONObject(data[i].id + " " + time).getString("date");

                        par.group = result.optJSONObject(i).optJSONObject("shedulePars").optJSONObject(data[i].id + " " + time).getString("group");
                        par.partype = result.optJSONObject(i).getJSONObject("shedulePars").optJSONObject(data[i].id + " " + time).getString("partype");
                        par.time = result.optJSONObject(i).optJSONObject("shedulePars").optJSONObject(data[i].id + " " + time).getString("time");
                        par.name = result.optJSONObject(i).getJSONObject("shedulePars").optJSONObject(data[i].id + " " + time).getString("name");
                        par.room = result.optJSONObject(i).optJSONObject("shedulePars").optJSONObject(data[i].id + " " + time).getString("room");
                        data[i].pars.put(data[i].id + " " + time, par);
                        Log.d("My tag", par.room + par.name + par.time);

                    }
                }
            }
            }
            }
            json = null;
            f = new File(color_dataPath);
            if(f.exists()) {
                try {
                    byte[] buffer = Files.readAllBytes(Paths.get(color_dataPath));
                    json = new String(buffer, "UTF-8");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if(json != null){
                    JSONObject result = new JSONObject(json);
                    scheduleColor = new ScheduleColor();
                    scheduleColor.setMainColor((Integer) result.get("mainColor"));
                    scheduleColor.setLecColor((Integer)result.get("lecColor"));
                    scheduleColor.setSemColor((Integer)result.get("semColor"));
                    scheduleColor.setLabColor((Integer)result.get("labColor"));
                    scheduleColor.setTimeColor((Integer)result.get("timeColor"));
                    Log.d("PARSING COLOR", scheduleColor.toString());
                }
            }

        }
        @Override
        public void onCreate() {
            Log.d("ItemFactory", "OnCreate");
            try {
                SetExampleData();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onDataSetChanged() {
            Log.d("ItemFactory", "OnDataSetChanged");
            try {
                SetExampleData();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return data.length;
        }

        @SuppressLint("ResourceType")
        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.linear_layout_item);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && scheduleColor.getMainColor() != 0) {
                //views.setColor(mainLayout, "setBackgroundColor", scheduleColor.getMainColor());
                views.setColor(mainLayout, "setBackgroundColor", scheduleColor.getMainColor());
                views.setColor(dateLayout, "setBackgroundColor", scheduleColor.getMainColor());
                //views.setColor(R.id.parsLayout, "setBackgroundColor", mainColor);
               for ( int liner: liners
                ) {
                    views.setColor(liner, "setBackgroundColor", scheduleColor.getMainColor());
                }
            }
            views.setTextViewText(R.id.layoutDate, data[i].id);
            int check = 0;
            for(int[] item : items){
                for(int itm : item){
                    views.setViewVisibility(itm, View.INVISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        views.setViewLayoutHeight(itm,30, TypedValue.COMPLEX_UNIT_PT);
                        views.setViewLayoutMargin(itm, RemoteViews.MARGIN_LEFT, 3, TypedValue.COMPLEX_UNIT_DIP);
                        views.setViewLayoutMargin(itm, RemoteViews.MARGIN_TOP, 3, TypedValue.COMPLEX_UNIT_DIP);
                        views.setViewLayoutMargin(itm, RemoteViews.MARGIN_RIGHT, 3, TypedValue.COMPLEX_UNIT_DIP);
                        views.setViewLayoutMargin(itm, RemoteViews.MARGIN_BOTTOM, 3, TypedValue.COMPLEX_UNIT_DIP);
                    }
                }
            }

            for(String time : times){
                if(data[i].pars.get(data[i].id + " " + time) != null){

                    views.setTextViewText(items[1][check],data[i].pars.get(data[i].id + " " + time).name + "\n" +data[i].pars.get(data[i].id + " " + time).partype + " " + data[i].pars.get(data[i].id + " " + time).room + " " + data[i].pars.get(data[i].id + " " + time).group); //Сюда инф как значение
                    views.setViewVisibility(items[1][check], View.VISIBLE);
                    if((data[i].pars.get(data[i].id + " " + time).partype).compareTo("Лекция") == 0)
                    {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && scheduleColor.getLecColor() != 0) {
                        views.setColor(items[1][check], "setBackgroundColor", scheduleColor.getLecColor());
                        //Log.d("Yellow", Integer.toString(lecColor));
                    }
                    }
                    if(((data[i].pars.get(data[i].id + " " + time).partype).compareTo("Семинар") == 0 || (data[i].pars.get(data[i].id + " " + time).partype).compareTo("Консультация") == 0) && scheduleColor.getSemColor() != 0)
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            views.setColor(items[1][check], "setBackgroundColor", scheduleColor.getSemColor());

                            //Log.d("Green",  Integer.toString(semColor) );
                        }
                    }
                    if(((data[i].pars.get(data[i].id + " " + time).partype).compareTo("Курсовая работа") == 0 || (data[i].pars.get(data[i].id + " " + time).partype).compareTo("Лабораторная работа") == 0  || (data[i].pars.get(data[i].id + " " + time).partype).compareTo("Экзамен") == 0)&& scheduleColor.getLabColor() != 0)
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            views.setColor(items[1][check], "setBackgroundColor", scheduleColor.getLabColor());
                            //Log.d("COFFe",  Integer.toString(mainColor));
                        }
                    }

                    views.setTextViewText(items[0][check],data[i].pars.get(data[i].id + " " + time).time);// Сюда время
                    views.setViewVisibility(items[0][check], View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && scheduleColor.getTimeColor() != 0) {
                        views.setColor(items[0][check], "setBackgroundColor", scheduleColor.getTimeColor());
                        //Log.d("GetView", "Blue");
                    }
                    Log.d("IN GEt VIEW", data[i].id + " " + data[i].pars.get(data[i].id + " " + time).id + data[i].pars.get(data[i].id + " " + time).name+ " " +data[i].pars.get(data[i].id + " " + time).partype + " " + data[i].pars.get(data[i].id + " " + time).room);
                    check++;
                }

            }
           for(int a = check; a < 7; a++){
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

                    views.setViewLayoutHeight(items[0][a], 0, TypedValue.COMPLEX_UNIT_PT);
                    //views.setViewLayoutWidth(items[0][a], 0, TypedValue.COMPLEX_UNIT_PT);
                    views.setViewLayoutHeight(items[1][a], 0, TypedValue.COMPLEX_UNIT_PT);
                   views.setViewLayoutMargin(items[0][a], RemoteViews.MARGIN_LEFT, 0, TypedValue.COMPLEX_UNIT_DIP);
                   views.setViewLayoutMargin(items[0][a], RemoteViews.MARGIN_TOP, 0, TypedValue.COMPLEX_UNIT_DIP);
                   views.setViewLayoutMargin(items[0][a], RemoteViews.MARGIN_RIGHT, 0, TypedValue.COMPLEX_UNIT_DIP);
                   views.setViewLayoutMargin(items[0][a], RemoteViews.MARGIN_BOTTOM, 0, TypedValue.COMPLEX_UNIT_DIP);
                   views.setViewLayoutMargin(items[1][a], RemoteViews.MARGIN_LEFT, 0, TypedValue.COMPLEX_UNIT_DIP);
                   views.setViewLayoutMargin(items[1][a], RemoteViews.MARGIN_TOP, 0, TypedValue.COMPLEX_UNIT_DIP);
                   views.setViewLayoutMargin(items[1][a], RemoteViews.MARGIN_RIGHT, 0, TypedValue.COMPLEX_UNIT_DIP);
                   views.setViewLayoutMargin(items[1][a], RemoteViews.MARGIN_BOTTOM, 0, TypedValue.COMPLEX_UNIT_DIP);
                    //views.setViewLayoutWidth(items[1][a], 0, TypedValue.COMPLEX_UNIT_PT);

                  // views.setViewLayoutHeight(liners[a], 0, TypedValue.COMPLEX_UNIT_PT);
                //views.setViewPadding(liners[a], 0,0,0,0);
                }
           }
            return views;
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
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
