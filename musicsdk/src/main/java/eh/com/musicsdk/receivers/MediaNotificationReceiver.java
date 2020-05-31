package eh.com.musicsdk.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import eh.com.musicsdk.utils.Constants;

public class MediaNotificationReceiver extends BroadcastReceiver {


    @Override
    public void onReceive (Context context, Intent intent) {

        context.sendBroadcast ( new Intent ( Constants.MY_MUSIC_FILTER )
                .putExtra ( Constants.ACTIONNAME, intent.getAction () ) );


    }
}
