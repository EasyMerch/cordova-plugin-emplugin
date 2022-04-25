package ru.pronetcom.easymerch2.emplugin;

import android.content.BroadcastReceiver;
import android.util.Log;

public class TimeChangeReceiver extends BroadcastReceiver{
	public static int changesCount = 0;

	@Override
    public void onReceive(Context context, Intent intent) {
		TimeChangeReceiver.changesCount++;
		Log.i("TimeChangeReceiver", "changed");
    }
}
