package com.paperflywings.user.paperflyv0;


import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class CustomFontApp extends Application {
    public static final String CHANNEL_1_ID = "channel1";
    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT", "font/helveticaneuelight.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "font/helveticaneuelight.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "font/helveticaneuelight.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "font/helveticaneuelight.ttf");

        createNotificationChannels();
    }

    private void createNotificationChannels(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("this is channel 1");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
}