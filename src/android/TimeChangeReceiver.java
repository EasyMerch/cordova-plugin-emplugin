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

import java.util.ArrayList;

public class TimeChangeReceiver extends BroadcastReceiver {
	private static final String LAST_DIFF_PREF_KEY = "TimeChangeReceiver.lastTimeDifference";
	private static final String CHANGES_PREF_KEY = "TimeChangeReceiver.timeChanges";
	private static SharedPreferences pref = null;
	private static final ArrayList<TimeChangeListener> listeners = new ArrayList<>();

	private static final String SHARED_PREFERENCES_NAME = "EMPref";

	public interface TimeChangeListener{
		void onChange(JSONObject changeObj);
	}

	public static void addListener(TimeChangeListener listener){
		listeners.add(listener);
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
		} else {
			long currentTimeDifference = getTimeDifference();
			if(currentTimeDifference != lastTimeDifference){
				addTimeChange(context, true);
			}
		}
	}

	public static long getLastTimeDifference(Context context){
		SharedPreferences pref = getPreferences(context);
		return pref.getLong(LAST_DIFF_PREF_KEY, 0L);
	}

	public static JSONArray getTimeChanges(Context context) throws JSONException{
		SharedPreferences pref = getPreferences(context);
		return new JSONArray(pref.getString(CHANGES_PREF_KEY, "[]"));
	}

	public static void clearTimeChanges(Context context){
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
		addTimeChange(context, false);
	}

	private static void addTimeChange(Context context, boolean supposed){
		long lastTimeDifference = getLastTimeDifference(context);
		long currentTimeDifference = getTimeDifference();

		long timeChangeDifference = currentTimeDifference - lastTimeDifference;

		SharedPreferences pref = getPreferences(context);

		try {
			String status = "OK";

			JSONObject changeObj = new JSONObject();
			changeObj
					.put("timestamp", System.currentTimeMillis())
					.put("timeChangeDifference", timeChangeDifference)
			;

			if(lastTimeDifference == 0L){
				changeObj.put("timeChangeDifference", 0L);
				status = "UNKNOWN";
			}

			if(supposed){
				status = "SUPPOSED";
			}

			changeObj.put("status", status);

			JSONArray changes = new JSONArray(pref.getString(CHANGES_PREF_KEY, "[]"));
			changes.put(changeObj);

			pref.edit()
				.putString(CHANGES_PREF_KEY, changes.toString())
				.apply()
			;

			setTimeDifference(context);

			for (TimeChangeListener listener : listeners) {
				listener.onChange(changeObj);
			}
		} catch (JSONException e) {
			Log.e("TimeChangeReceiver", "TimeChangeReceiver.addTimeChange JSONException "+e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void onReceive(Context context, Intent intent){
		Log.e("DEBUG", "TimeChangeReceiver.onReceive");
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