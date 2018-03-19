package com.example.vaish.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    public final IBinder ibinder= new LocalBinder();
    Timer timer;
    TimerTask timerTask;

    private String Date;
    private static String LOG_TAG = "BoundService";

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return ibinder;
    }

    public class LocalBinder extends Binder{
        MyService getService(){
            Log.d(LOG_TAG, "getService: "+"LocalBinder");
            return MyService.this;

        }
    }

}
