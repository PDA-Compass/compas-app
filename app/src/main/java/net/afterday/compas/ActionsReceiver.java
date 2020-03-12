package net.afterday.compas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by spaka on 7/21/2018.
 */

public class ActionsReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        context.stopService(new Intent(context, LocalMainService.class));
        android.os.Process.killProcess(android.os.Process.myPid());
//        Intent i = new Intent(context, MainActivity.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        i.addFlags (Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        i.putExtra("close_activity",true);
//        context.startActivity(i);
    }
}
