package eh.com.mediaapptest.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import eh.com.mediaapptest.receivers.NotificationReceiver;
import eh.com.mediaapptest.ui.MainActivity;
import eh.com.musicsdk.data.Music;

public class Utils {

    private static String CHANNELID = "channel1";

    public static void createNotification (Context context, Music music,boolean isPlay) {

        createNotificationChannel (context);
        Notification notification;
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from ( context );
            MediaSessionCompat mediaSessionCompat = new MediaSessionCompat ( context, "tag" );
            Bitmap icon = BitmapFactory.decodeResource ( context.getResources (), eh.com.musicsdk.R.drawable.ic_album_black_24dp );

            int btnPlay=0;
            String playtitle = "";
            PendingIntent pendingPlayIntent=null;
            if(isPlay)
            {
                playtitle = "pause";
                btnPlay = eh.com.musicsdk.R.drawable.ic_pause_black_24dp;
                Intent intentPause = new Intent ( context, NotificationReceiver.class )
                        .setAction ( MediaAppConstants.ACTION_PAUSE );

                pendingPlayIntent = PendingIntent.getBroadcast ( context,
                        0, intentPause, PendingIntent.FLAG_UPDATE_CURRENT );

            }
            else {
                 playtitle = "play";
                 btnPlay = eh.com.musicsdk.R.drawable.ic_play_arrow_black_24dp;
                Intent intentPlay = new Intent ( context, NotificationReceiver.class )
                        .setAction ( MediaAppConstants.ACTION_PLAY );

                pendingPlayIntent = PendingIntent.getBroadcast ( context,
                        0, intentPlay, PendingIntent.FLAG_UPDATE_CURRENT );
            }



            int btnStop = eh.com.musicsdk.R.drawable.ic_stop_black_24dp;
            Intent intentStop = new Intent ( context, NotificationReceiver.class )
                    .setAction ( MediaAppConstants.ACTION_STOP );

            PendingIntent pendingStopIntent = PendingIntent.getBroadcast ( context,
                    0, intentStop, PendingIntent.FLAG_UPDATE_CURRENT );


            Intent notificationIntent = new Intent ( context, MainActivity.class );
            notificationIntent.putExtra ( "path", music.getFilePath () );
            notificationIntent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK );
            PendingIntent pendingIntent = PendingIntent.getActivity ( context,
                    0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT );


            builder = new NotificationCompat.Builder ( context, CHANNELID )
                    .setSmallIcon ( eh.com.musicsdk.R.drawable.ic_music_note )
                    .setContentTitle ( music.getName () )
                    .setContentText ( music.getAlbum () )
                    .setContentIntent ( pendingIntent )
                    .setLargeIcon ( icon )
                    .setOnlyAlertOnce ( true )
                    .setShowWhen ( false )
                    .addAction ( btnPlay, playtitle, pendingPlayIntent )
                    .addAction ( btnStop, "Stop", pendingStopIntent )
                    .setStyle ( new androidx.media.app.NotificationCompat.MediaStyle ()
                            .setShowActionsInCompactView ( 0, 1)
                            .setMediaSession ( mediaSessionCompat.getSessionToken () ) )
                    .setPriority ( NotificationCompat.PRIORITY_HIGH );
             notification = builder.build ();
             notificationManagerCompat.notify ( 1,notification );
            //context.startForeground ( 1, notification );

        }
    }

    private static void createNotificationChannel (Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel (
                    CHANNELID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = context.getSystemService ( NotificationManager.class );
            manager.createNotificationChannel ( serviceChannel );
        }
    }


}
