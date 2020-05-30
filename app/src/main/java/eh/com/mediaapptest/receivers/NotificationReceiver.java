package eh.com.mediaapptest.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import eh.com.mediaapptest.utils.MediaAppConstants;

public class NotificationReceiver extends BroadcastReceiver {


    @Override
    public void onReceive (Context context, Intent intent) {

        context.sendBroadcast (new Intent ( MediaAppConstants.NOTIFILTER )
        .putExtra ( MediaAppConstants.NOTIACTIONNAME,intent.getAction () ));


    }
}
