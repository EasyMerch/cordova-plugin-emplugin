package ru.pronetcom.easymerch2.emplugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EMTimeChangeChecker extends CordovaPlugin {
	public static final String TAG = "EMTimeChangeChecker";
	
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		switch(action) {
			case "getTimeChanges":
				callbackContext.success(TimeChangeReceiver.getTimeChanges(cordova.getContext()));
				return true;
			case "clearTimeChanges":
				TimeChangeReceiver.clearTimeChanges(cordova.getContext());
				callbackContext.success();
				return true;
			case "watchTimeChanges":
				TimeChangeReceiver.addListener(new TimeChangeReceiver.TimeChangeListener() {
					@Override
					public void onChange(JSONObject changeObj) {
						PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, changeObj);
						pluginResult.setKeepCallback(true);
						callbackContext.sendPluginResult(pluginResult);
					}
				});
				return true;
		}
		return false;
	}

	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
		TimeChangeReceiver.startTimeTrack(cordova.getContext());
	}
}
