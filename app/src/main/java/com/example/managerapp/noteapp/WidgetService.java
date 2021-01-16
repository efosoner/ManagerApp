package com.example.managerapp.noteapp;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.managerapp.noteapp.DataProvider;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new DataProvider(this, intent);
    }
}