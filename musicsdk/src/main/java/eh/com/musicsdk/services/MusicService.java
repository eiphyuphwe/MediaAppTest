package eh.com.musicsdk.services;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;

import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import eh.com.musicsdk.R;
import eh.com.musicsdk.receivers.MediaNotificationReceiver;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private MediaPlayer mediaPlayer = null;
    boolean isStarted, isMusicOver;
    private String CHANNELID = "channel1";
    Bundle bundle;
    private String activityName = "";


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

    @Override
    public void onCompletion (MediaPlayer mp) {

    }

    private void busyWork ( ) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer ();

            try {
                //  mediaPlayer = MediaPlayer.create ( getApplicationContext (), R.raw.jawel );
                mediaPlayer = new MediaPlayer ();
                mediaPlayer.setOnPreparedListener ( this );
                mediaPlayer.setOnCompletionListener ( this );

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
                case ServiceProviders.ACTION_PLAY: {
                    if (mediaPlayer.isPlaying ()) {
                        mediaPlayer.pause ();
                    } else {
                        mediaPlayer.start ();
                    }

                }
                break;
                case ServiceProviders.ACTION_RESUME: {
                    if (mediaPlayer != null) {
                        mediaPlayer.start ();
                    }
                }
                break;
                case ServiceProviders.ACTION_PAUSE: {
                    if (mediaPlayer.isPlaying ()) {
                        mediaPlayer.pause ();
                    }
                }
                break;
                case ServiceProviders.ACTION_STOP: {
                    mediaPlayer.reset ();
                    //context.sendBroadcast (new Intent ( MyConstants.MUSIC_NOTI_FILTER )
                    //.putExtra ( MyConstants.NOTIACTIONNAME,intent.getAction () ));
                    stopForeground ( STOP_FOREGROUND_REMOVE );
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
                    System.exit ( 0 );


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
            String title = bundle.getString ( ServiceProviders.TITLE );
            String artist = bundle.getString ( ServiceProviders.ARTIST );
            String path = bundle.getString ( ServiceProviders.PATH );
            Uri pathUri = Uri.parse ( path );
            activityName = bundle.getString ( ServiceProviders.ACTIVITYNAME );

            if (intent.getAction ().equalsIgnoreCase ( ServiceProviders.ACTION_FORGROUND )) {
                if (!isStarted) {
                    mediaPlayer.setAudioStreamType ( AudioManager.STREAM_MUSIC );
                    try {
                        mediaPlayer.setDataSource ( getApplicationContext (), pathUri );
                    } catch (IOException e) {
                        e.printStackTrace ();
                    }

                    mediaPlayer.prepareAsync ();//prepare the media without blocking the UI
                    //prepareNotifcation
                    createNotification ( getApplicationContext (), title, artist, path );
                    isStarted = true;
                }
                if (mediaPlayer != null)
                    mediaPlayer.start ();
            } else if (intent.getAction ().equalsIgnoreCase ( ServiceProviders.ACTION_PLAY )) {
                if (mediaPlayer != null)
                    mediaPlayer.start ();
            } else if (intent.getAction ().equalsIgnoreCase ( ServiceProviders.ACTION_PAUSE )) {
                if (mediaPlayer != null)
                    mediaPlayer.pause ();
            } else if (intent.getAction ().equalsIgnoreCase ( ServiceProviders.ACTION_STOP )) {
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

    private void createNotificationChannel ( ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel (
                    CHANNELID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService ( NotificationManager.class );
            manager.createNotificationChannel ( serviceChannel );
        }
    }

    public void createNotification (Context context, String musicTitle, String artist, String path) {

        createNotificationChannel ();
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from ( context );
            MediaSessionCompat mediaSessionCompat = new MediaSessionCompat ( context, "tag" );
            Bitmap icon = BitmapFactory.decodeResource ( context.getResources (), R.drawable.ic_album_black_24dp );

            int btnPlay = R.drawable.ic_play_arrow_black_24dp;
            Intent intentPlay = new Intent ( context, MediaNotificationReceiver.class )
                    .setAction ( ServiceProviders.ACTION_PLAY );

            PendingIntent pendingPlayIntent = PendingIntent.getBroadcast ( context,
                    0, intentPlay, PendingIntent.FLAG_UPDATE_CURRENT );

            int btnPause = R.drawable.ic_pause_black_24dp;
            Intent intentPause = new Intent ( context, MediaNotificationReceiver.class )
                    .setAction ( ServiceProviders.ACTION_PAUSE );

            PendingIntent pendingPauseIntent = PendingIntent.getBroadcast ( context,
                    0, intentPause, PendingIntent.FLAG_UPDATE_CURRENT );


            int btnStop = R.drawable.ic_stop_black_24dp;
            Intent intentStop = new Intent ( context, MediaNotificationReceiver.class )
                    .setAction ( ServiceProviders.ACTION_STOP );

            PendingIntent pendingStopIntent = PendingIntent.getBroadcast ( context,
                    0, intentStop, PendingIntent.FLAG_UPDATE_CURRENT );


            Class< ? > targetClass = null;
            try {
                targetClass = Class.forName ( activityName );
            } catch (ClassNotFoundException e) {
                e.printStackTrace ();
            }
            Intent notificationIntent = new Intent ( this, targetClass );
            notificationIntent.putExtra ( "path", path );
            notificationIntent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK );
            PendingIntent pendingIntent = PendingIntent.getActivity ( this,
                    0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT );


            notification = new NotificationCompat.Builder ( context, CHANNELID )
                    .setSmallIcon ( R.drawable.ic_music_note )
                    .setContentTitle ( musicTitle )
                    .setContentText ( artist )
                    .setContentIntent ( pendingIntent )
                    .setLargeIcon ( icon )
                    .setOnlyAlertOnce ( true )
                    .setShowWhen ( false )
                    .addAction ( btnPlay, "Play", pendingPlayIntent )
                    .addAction ( btnPause, "Pause", pendingPauseIntent )
                    .addAction ( btnStop, "Stop", pendingStopIntent )
                    .setStyle ( new androidx.media.app.NotificationCompat.MediaStyle ()
                            .setShowActionsInCompactView ( 0, 1, 2 )
                            .setMediaSession ( mediaSessionCompat.getSessionToken () ) )
                    .setPriority ( NotificationCompat.PRIORITY_HIGH )
                    .build ();
            // notificationManagerCompat.notify ( 1,notification );
            startForeground ( 1, notification );

        }
    }


    @Override
    public void onTaskRemoved (Intent rootIntent) {
        //  super.onTaskRemoved ( rootIntent );
        //stopForeground ( Service.STOP_FOREGROUND_REMOVE );
        stopSelf ();
    }


}
