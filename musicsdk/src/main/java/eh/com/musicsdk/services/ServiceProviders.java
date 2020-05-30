package eh.com.musicsdk.services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import eh.com.musicsdk.receivers.MediaNotificationReceiver;

public class ServiceProviders {

    public static final String ACTION_PLAY = "eh.com.musicsdk.action.PLAY";
    public static final String ACTION_PAUSE = "eh.com.musicsdk.action.PAUSE";
    public static final String ACTION_RESUME = "eh.com.musicsdk.action.RESUME";
    public static final String ACTION_STOP = "eh.com.musicsdk.action.STOP";
    public static final String ACTION_FORGROUND = "eh.com.musicsdk.action.FOREGROUND";
    public static final String TITLE = "title";
    public static final String ARTIST = "artist";
    public static final String PATH = "path";
    public static final String ACTIVITYNAME = "activity";



    public static void playMusic(Context context, String title, String artist, String path, String activityName)
    {
        Intent intent = new Intent ( context,MusicService.class );
        Bundle bundle = new Bundle ( );
        bundle.putString ( TITLE,title );
        bundle.putString ( ARTIST,artist );
        bundle.putString ( PATH,path );
        bundle.putString ( ACTIVITYNAME,activityName );
        intent.putExtras ( bundle );
        intent.setAction (ACTION_FORGROUND);

        context.startForegroundService ( intent );
    }

    public static void resumeMusic(Context context)
    {
        context.sendBroadcast (new Intent ( MediaNotificationReceiver.MY_MUSIC_FILTER )
                .putExtra ( MediaNotificationReceiver.ACTIONNAME,ACTION_PAUSE ));
    }

    public static void pauseMusic(Context context)
    {

        context.sendBroadcast (new Intent ( MediaNotificationReceiver.MY_MUSIC_FILTER )
                .putExtra ( MediaNotificationReceiver.ACTIONNAME,ACTION_PAUSE ));

    }
    public static void stopMusic(Context context)
    {
        Intent intent = new Intent ( context,MusicService.class );
        intent.setAction (ACTION_STOP);
        context.stopService (intent);
    }
}
