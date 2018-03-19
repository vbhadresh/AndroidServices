package com.example.vaish.services;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button StartServices;
    private Button StopServices;
    private Button Start;
    private Button Stop;
    private Button Reset;
    private TextView displayTime;
    MyService myService;
    boolean isbound=false;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    final Handler handler = new Handler();
    private static String LOG_TAG = "BoundService";
    private boolean isServicestarted=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StartServices=(Button)findViewById(R.id.startServices);
        StopServices=(Button)findViewById(R.id.stopServices);
        Start=(Button)findViewById(R.id.Start);
        Stop=(Button)findViewById(R.id.Stop);
        Reset=(Button)findViewById(R.id.Reset);
        displayTime=findViewById(R.id.time);
        StartServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tent= new Intent(getApplicationContext(),MyService.class);
                bindService(tent,serviceConnection, Context.BIND_AUTO_CREATE);
                Log.d(LOG_TAG, "onClick: "+"Click Service");
                isServicestarted=true;
                Toast.makeText(getApplicationContext(), "Service Started", Toast.LENGTH_SHORT).show();
            }
        });
        StopServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isServicestarted) {
                    unbindService(serviceConnection);
                    resetService();
                    Toast.makeText(getApplicationContext(), "Service Disconnected", Toast.LENGTH_SHORT).show();
                    isServicestarted=false;
                }
                else{
                    Toast.makeText(getApplicationContext(), "Unable to Stop Service before Starting it", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isServicestarted) {
                    initializeTimerTask();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Start the Service before Starting Stop Watch", Toast.LENGTH_SHORT).show();
                }
                // displayTime.setText();
            }
        });
        Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isServicestarted) {
                    StopService();
                }else{
                    Toast.makeText(getApplicationContext(), "Start the Service before Stoping Watch", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isServicestarted) {
                    resetService();
                } else {
                    Toast.makeText(getApplicationContext(), "Start the Service before Resetting Stop Watch", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void StopService(){
        TimeBuff += MillisecondTime;
        handler.removeCallbacks(runnable);
        Reset.setEnabled(true);
    }

    public void resetService(){

        MillisecondTime = 0L ;
        StartTime = 0L ;
        TimeBuff = 0L ;
        UpdateTime = 0L ;
        Seconds = 0 ;
        Minutes = 0 ;
        MilliSeconds = 0 ;
        displayTime.setText("00:00:00");
    }
    public void initializeTimerTask() {
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);

        //reset.setEnabled(false);
    }

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            displayTime.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%03d", MilliSeconds));

            handler.postDelayed(this, 0);
        }

    };
    int Seconds, Minutes, MilliSeconds ;
    private ServiceConnection serviceConnection= new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyService.LocalBinder binder=  (MyService.LocalBinder)iBinder;
            myService=binder.getService();
            isbound=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(LOG_TAG, "onServiceDisconnected: "+"Service Stopped");
            isbound=false;

        }
    };
}
