package com.example.user.paperflyv0;


import android.app.Application;
import com.example.user.paperflyv0.FontsOverride;

public class CustomFontApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT", "font/helveticaneue.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "font/helveticaneuelight.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "font/helveticaneue.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "font/helveticaneue.ttf");
    }
}