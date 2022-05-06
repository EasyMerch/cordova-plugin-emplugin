package ru.pronetcom.easymerch2.emplugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TimeChangeReceiver extends BroadcastReceiver {
	private static final String LAST_DIFF_PREF_KEY = "TimeChangeReceiver.lastTimeDifference";
	private static final String CHANGES_PREF_KEY = "TimeChangeReceiver.timeChages";
	private static SharedPreferences pref = null;
	private static Array<TimeChangeListener> listeners = new Array<TimeChangeListener>();

	private static final String SHARED_PREFERENCES_NAME = "EMPref";

	public static interface TimeChangeListener{
		public void onChange(long timeChangeDifference);
	}

	public static void addListener(TimeChangeListener listener){
		listeners.put(listener);
	}

	public static long getTimeDifference(){
		long elapsedTime = SystemClock.elapsedRealtime();
		long currentTime = System.currentTimeMillis();
		return currentTime - elapsedTime;
	}

	public static void startTimeTrack(Context context){
		long lastTimeDifference = getLastTimeDifference(context);
		if(lastTimeDifference == 0L){
			setTimeDifference(context);
		}
	}

	public static long getLastTimeDifference(Context context){
		SharedPreferences pref = getPreferences(context);
		return pref.getLong(LAST_DIFF_PREF_KEY, 0L);
	}

	public static JSONArray getTimeChanges(Context context) throws JSONException{
		SharedPreferences pref = getPreferences(context);
		return new JSONArray(pref.getString(CHANGES_PREF_KEY, ""));
	}

	public static void clearTimeChange(Context context){
		SharedPreferences pref = getPreferences(context);
		pref.edit()
			.putString(CHANGES_PREF_KEY, "[]")
			.apply()
		;
	}

	private static SharedPreferences getPreferences(Context context){
		if(pref != null) return pref;
		pref = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		return pref;
	}

	private static void setTimeDifference(Context context){
		SharedPreferences pref = getPreferences(context);
		pref.edit()
			.putLong(LAST_DIFF_PREF_KEY, getTimeDifference())
			.apply()
		;
	}

	private static void addTimeChange(Context context){
		long lastTimeDifference = getLastTimeDifference(context);
		long currentTimeDifference = getTimeDifference();

		long timeChangeDifference = currentTimeDifference - lastTimeDifference;

		SharedPreferences pref = getPreferences(context);

		String writeValue = null;
		try {
			JSONObject changeObj = new JSONObject();
			changeObj
					.put("timestamp", System.currentTimeMillis())
					.put("timeChangeDifference", timeChangeDifference)
			;

			if(lastTimeDifference == 0L){
				changeObj
					.put("unknownLastDifference", true)
					.put("timeChangeDifference", 0L);
			}

			JSONArray changes = new JSONArray(pref.getString(CHANGES_PREF_KEY, "[]"));
			changes.put(changeObj);

			writeValue = changes.toString();
		} catch (JSONException e) {
			Log.e("TimeChangeReceiver", "TimeChangeReceiver.addTimeChange JSONException "+e.getMessage());
			e.printStackTrace();
		}

		if(writeValue != null){
			pref.edit()
				.putString(CHANGES_PREF_KEY, writeValue)
				.apply()
			;

			for (TimeChangeListener listener : listeners) {
				listener.onChange(timeChangeDifference);
			}
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		switch(intent.getAction()){
			case Intent.ACTION_TIME_CHANGED:
				addTimeChange(context);
				break;
			case Intent.ACTION_BOOT_COMPLETED:
				setTimeDifference(context);
				break;
		}
	}

}