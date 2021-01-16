package com.example.managerapp.noteapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import static android.R.id.text1;
import static android.R.layout.simple_list_item_1;

// Thank you for the tutorial @
public class DataProvider implements RemoteViewsService.RemoteViewsFactory {

    ArrayList<Note> notes = new ArrayList<>();
    Context mContext = null;
    Intent mIntent = null;

    public DataProvider(Context context, Intent intent) {
        // mIntent is at this point useless but i was testing it so it 'll stay
        mContext = context;
        mIntent = intent;
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
        // Awful but it makes updates in notes' ArrayList and updates widget ONCE
        while (this.notes.isEmpty()) {
            initData();
        }
        initData();
    }

    @Override
    public void onDestroy() { }

    @Override
    public int getCount() {
        return this.notes.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        // This view works but somehow (i just don't know) text colour is unchangeable
        RemoteViews view = new RemoteViews(mContext.getPackageName(), simple_list_item_1);
        view.setTextViewText(text1, this.notes.get(position).getNoteTitle());
        Intent intent = new Intent();
        intent.putExtra("noteID", position);
        view.setOnClickFillInIntent(text1, intent);
        return view;
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

    private void initData() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("iNotes", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("notes", null);
        Type type = new TypeToken<ArrayList<Note>>() {
        }.getType();
        this.notes = gson.fromJson(json, type);
        if (this.notes == null) {
            this.notes = new ArrayList<Note>();
        }
    }
}