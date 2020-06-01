package eh.com.musicsdk.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import eh.com.musicsdk.utils.Constants;

public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener {

    private MediaPlayer mediaPlayer = null;
    boolean isStarted, isMusicOver;


    Bundle bundle;


    @Override
    public void onCreate ( ) {
        super.onCreate ();

        busyWork ();

    }


    @Override
    public void onPrepared (MediaPlayer mp) {

        mp.start ();
        isStarted = true;
        isMusicOver = false;
    }


    private void busyWork ( ) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer ();

            try {
                //  mediaPlayer = MediaPlayer.create ( getApplicationContext (), R.raw.jawel );
                mediaPlayer = new MediaPlayer ();
                mediaPlayer.setOnPreparedListener ( this );

            } catch (Exception e) {

            }
        } else {
            mediaPlayer.reset ();
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver () {
        @Override
        public void onReceive (Context context, Intent intent) {

            String action = intent.getExtras ().getString ( Constants.ACTIONNAME );
            switch (action) {
                case Constants.ACTION_PLAY: {
                    if (mediaPlayer.isPlaying ()) {
                        mediaPlayer.pause ();
                    } else {
                        mediaPlayer.start ();
                    }

                }
                break;
                case Constants.ACTION_RESUME: {
                    if (mediaPlayer != null) {
                        mediaPlayer.start ();
                    }
                }
                break;
                case Constants.ACTION_PAUSE: {
                    if (mediaPlayer.isPlaying ()) {
                        mediaPlayer.pause ();
                    }
                }
                break;
                case Constants.ACTION_STOP: {
                    mediaPlayer.pause ();
                    mediaPlayer.seekTo ( 0 );


                }break;
                case Constants.ACTION_RESTART :{

                    if(mediaPlayer!=null)
                    {
                        mediaPlayer.reset ();
                    }
                    if (intent != null) {
                        bundle = intent.getExtras ();
                        String title = bundle.getString ( Constants.TITLE );
                        String artist = bundle.getString ( Constants.ARTIST );
                        String path = bundle.getString ( Constants.PATH );
                        Uri pathUri = Uri.parse ( path );
                        try {
                            mediaPlayer.setDataSource ( getApplicationContext (), pathUri );
                        } catch (IOException e) {
                            e.printStackTrace ();
                        }

                        mediaPlayer.prepareAsync ();//prepare the media without blocking the UI
                        if(mediaPlayer!=null)
                            mediaPlayer.start ();
                        isStarted=true;
                    }
                }break;
            }
        }
    };

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {


        IntentFilter musicIntentFilter = new IntentFilter ( Constants.MY_MUSIC_FILTER );
        registerReceiver ( broadcastReceiver, musicIntentFilter );

        if (intent != null) {
            bundle = intent.getExtras ();
            String title = bundle.getString ( Constants.TITLE );
            String artist = bundle.getString ( Constants.ARTIST );
            String path = bundle.getString ( Constants.PATH );
            Uri pathUri = Uri.parse ( path );


            if (intent.getAction ().equalsIgnoreCase ( Constants.ACTION_FORGROUND )) {
                if (!isStarted) {
                    mediaPlayer.setAudioStreamType ( AudioManager.STREAM_MUSIC );
                    try {
                        mediaPlayer.setDataSource ( getApplicationContext (), pathUri );
                    } catch (IOException e) {
                        e.printStackTrace ();
                    }

                    mediaPlayer.prepareAsync ();//prepare the media without blocking the UI
                    startForgoundNoti ();
                    isStarted = true;
                }
                if (mediaPlayer != null)
                    mediaPlayer.start ();
            } else if (intent.getAction ().equalsIgnoreCase ( Constants.ACTION_PLAY )) {
                if (mediaPlayer != null)
                    mediaPlayer.start ();
            } else if (intent.getAction ().equalsIgnoreCase ( Constants.ACTION_PAUSE )) {
                if (mediaPlayer != null)
                    mediaPlayer.pause ();
            } else if (intent.getAction ().equalsIgnoreCase ( Constants.ACTION_STOP )) {
                if (mediaPlayer != null)
                    mediaPlayer.reset ();

            }
        }
        // return super.onStartCommand ( intent, flags, startId );
        return START_STICKY;
    }

    @Override
    public void onDestroy ( ) {
        super.onDestroy ();
        Log.e ( "Destroy", " I m service destroy" );
        unregisterReceiver ( broadcastReceiver );
        isStarted = false;
        if (mediaPlayer != null)
            mediaPlayer.release ();
        NotificationManager manager = getSystemService ( NotificationManager.class );
        manager.cancelAll ();

    }

    @Nullable
    @Override
    public IBinder onBind (Intent intent) {
        return null;
    }


    @Override
    public void onTaskRemoved (Intent rootIntent) {

        stopSelf ();
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private void startForgoundNoti ( ) {
        String NOTIFICATION_CHANNEL_ID = "music_sdk";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel ( NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE );
        chan.setLightColor ( Color.BLUE );
        chan.setLockscreenVisibility ( Notification.VISIBILITY_PRIVATE );

        NotificationManager manager = (NotificationManager) getSystemService ( Context.NOTIFICATION_SERVICE );
        assert manager != null;
        manager.createNotificationChannel ( chan );

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder ( this, NOTIFICATION_CHANNEL_ID );
        Notification notification = notificationBuilder.setOngoing ( true )
                .setContentTitle ( "App is running in background" )
                .setPriority ( NotificationManager.IMPORTANCE_MIN )
                .setCategory ( Notification.CATEGORY_SERVICE )
                .build ();
        startForeground ( 2, notification );
    }


}
