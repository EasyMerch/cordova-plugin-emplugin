package ru.pronetcom.easymerch2.emplugin;

import android.util.Log;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class TimeChangeTrackService extends Service {
	public static int changesCount = 0;
	private final LocalBinder mBinder = new LocalBinder();

	public class LocalBinder extends Binder {
		public TimeChangeTrackService getService() {
			return TimeChangeTrackService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
	}

}