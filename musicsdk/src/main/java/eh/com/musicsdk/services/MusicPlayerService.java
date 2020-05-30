package eh.com.musicsdk.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;

import java.io.IOException;

import androidx.annotation.Nullable;
import eh.com.musicsdk.receivers.MediaNotificationReceiver;
import eh.com.musicsdk.utils.MyConstants;

public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener{

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

            String action = intent.getExtras ().getString ( MediaNotificationReceiver.ACTIONNAME );
            switch (action) {
                case MyConstants.ACTION_PLAY: {
                    if (mediaPlayer.isPlaying ()) {
                        mediaPlayer.pause ();
                    } else {
                        mediaPlayer.start ();
                    }

                }
                break;
                case MyConstants.ACTION_RESUME: {
                    if (mediaPlayer != null) {
                        mediaPlayer.start ();
                    }
                }
                break;
                case MyConstants.ACTION_PAUSE: {
                    if (mediaPlayer.isPlaying ()) {
                        mediaPlayer.pause ();
                    }
                }
                break;
                case MyConstants.ACTION_STOP: {
                    mediaPlayer.pause ();
                    mediaPlayer.seekTo ( 0 );
                    //context.sendBroadcast (new Intent ( MyConstants.MUSIC_NOTI_FILTER )
                    //.putExtra ( MyConstants.NOTIACTIONNAME,intent.getAction () ));
                    /*stopForeground ( STOP_FOREGROUND_REMOVE );
                    Class< ? > targetClass = null;
                    try {
                        targetClass = Class.forName ( activityName );
                        try {
                            {
                            }
                            Activity activity = (Activity) targetClass.newInstance ();
                            activity.finishAffinity ();
                        } catch (Exception e) {
                            e.printStackTrace ();
                        }

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace ();
                    }
                    System.exit ( 0 );*/


                }
                break;
            }
        }
    };

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {


        IntentFilter musicIntentFilter = new IntentFilter ( MediaNotificationReceiver.MY_MUSIC_FILTER );
        registerReceiver ( broadcastReceiver, musicIntentFilter );

        if (intent != null) {
            bundle = intent.getExtras ();
            String title = bundle.getString ( MyConstants.TITLE );
            String artist = bundle.getString ( MyConstants.ARTIST );
            String path = bundle.getString ( MyConstants.PATH );
            Uri pathUri = Uri.parse ( path );


            if (intent.getAction ().equalsIgnoreCase ( MyConstants.ACTION_FORGROUND )) {
                if (!isStarted) {
                    mediaPlayer.setAudioStreamType ( AudioManager.STREAM_MUSIC );
                    try {
                        mediaPlayer.setDataSource ( getApplicationContext (), pathUri );
                    } catch (IOException e) {
                        e.printStackTrace ();
                    }

                    mediaPlayer.prepareAsync ();//prepare the media without blocking the UI
                    isStarted = true;
                }
                if (mediaPlayer != null)
                    mediaPlayer.start ();
            } else if (intent.getAction ().equalsIgnoreCase ( MyConstants.ACTION_PLAY )) {
                if (mediaPlayer != null)
                    mediaPlayer.start ();
            } else if (intent.getAction ().equalsIgnoreCase ( MyConstants.ACTION_PAUSE )) {
                if (mediaPlayer != null)
                    mediaPlayer.pause ();
            } else if (intent.getAction ().equalsIgnoreCase ( MyConstants.ACTION_STOP )) {
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
        unregisterReceiver ( broadcastReceiver );
        isStarted = false;
        if (mediaPlayer != null)
            mediaPlayer.release ();

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


    public  boolean isMusicPlaying()
    {

        if(mediaPlayer.isPlaying ())
        {
            return true;
        }
        else
            return false;
    }

}
