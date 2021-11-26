package ru.pronetcom.easymerch2.emplugin;

import java.util.Objects;
import java.util.TimeZone;

import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.provider.Settings;

public class EMPlugin extends CordovaPlugin {
	public static final String TAG = "EMPlugin";
	private static String GETPROP_EXECUTABLE_PATH = "/system/bin/getprop";

	/**
	 * Constructor.
	 */
	public EMPlugin() {
	}

	/**
	 * Sets the context of the Command. This can then be used to do things like
	 * get file paths associated with the Activity.
	 *
	 * @param cordova The context of the main Activity.
	 * @param webView The CordovaWebView Cordova is running in.
	 */
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
	}

	/**
	 * Executes the request and returns PluginResult.
	 *
	 * @param action            The action to execute.
	 * @param args              JSONArry of arguments for the plugin.
	 * @param callbackContext   The callback id used when calling back into JavaScript.
	 * @return                  True if the action was valid, false if not.
	 */
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if ("getDeviceInfo".equals(action)) {
			JSONObject r = new JSONObject();
			r.put("isVirtual", this.isVirtual());
			r.put("serial", this.getSerialNumber());
			r.put("info", this.getInfo());
			callbackContext.success(r);
			return true;
		}
		else {
			return false;
		}
	}

	public String getSerialNumber(){
		// TODO
		return "";
	}

	public boolean isVirtual() {
		return android.os.Build.FINGERPRINT.contains("generic")
			|| android.os.Build.PRODUCT.contains("sdk")
			//
			|| android.os.Build.FINGERPRINT.startsWith("generic")
			|| android.os.Build.FINGERPRINT.startsWith("unknown")
			|| android.os.Build.MODEL.contains("google_sdk")
			|| android.os.Build.MODEL.contains("Emulator")
			|| android.os.Build.MODEL.contains("Android SDK built for x86")
			//nox
			|| this.onNox()
			//bluestacks
			|| "QC_Reference_Phone".equals(android.os.Build.BOARD) && !"Xiaomi".equalsIgnoreCase(android.os.Build.MANUFACTURER)
			|| this.onBlueStacks()
			|| android.os.Build.MANUFACTURER.contains("Genymotion")
			|| "Build2".equals(android.os.Build.HOST) //MSI App Player
			|| android.os.Build.BRAND.startsWith("generic") && android.os.Build.DEVICE.startsWith("generic")
			|| "google_sdk".equals(android.os.Build.PRODUCT)
			// another Android SDK emulator check
			|| "1".equals(EMPlugin.getSystemProperty("ro.kernel.qemu"));
	}

	public String getInfo() {
		return String.format(
			"FINGERPRINT:%s\n"+
			"MODEL:%s\n"+
			"MANUFACTURER:%s\n"+
			"BRAND:%s\n"+
			"DEVICE:%s\n"+
			"BOARD:%s\n"+
			"HOST:%s\n"+
			"PRODUCT:%s\n"+
			"HARDWARE:%s\n"+
			"onBlueStacks:%s\n"+
			"onNox:%s\n"+
			"ro.kernel.qemu:%s",
			android.os.Build.FINGERPRINT,
			android.os.Build.MODEL,
			android.os.Build.MANUFACTURER,
			android.os.Build.BRAND,
			android.os.Build.DEVICE,
			android.os.Build.BOARD,
			android.os.Build.HOST,
			android.os.Build.PRODUCT,
			android.os.Build.HARDWARE,
			this.onBlueStacks(),
			this.onNox(),
			EMPlugin.getSystemProperty("ro.kernel.qemu")
		);
	}

	public static String getSystemProperty(String propName) {
		Process process = null;
		BufferedReader bufferedReader = null;

		try {
			process = new ProcessBuilder().command(GETPROP_EXECUTABLE_PATH, propName).redirectErrorStream(true).start();
			bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = bufferedReader.readLine();
			if (line == null){
				line = ""; //prop not set
			}
			Log.i(TAG,"read System Property: " + propName + "=" + line);
			return line;
		} catch (Exception e) {
			Log.e(TAG,"Failed to read System Property " + propName,e);
			return "";
		} finally{
			if (bufferedReader != null){
				try {
					bufferedReader.close();
				} catch (IOException e) {}
			}
			if (process != null){
				process.destroy();
			}
		}
	}

	public Boolean onBlueStacks(){
		File sharedFolder = new File(android.os.Environment
			.getExternalStorageDirectory().toString()
			+ File.separatorChar
			+ "windows"
			+ File.separatorChar
			+ "BstSharedFolder");

		return sharedFolder.exists();
	}

	public Boolean onNox(){
		File noxFolder = new File(File.separatorChar
		+ "storage"
		+ File.separatorChar
		+ "emulated"
		+ File.separatorChar
		+ "0"
		+ File.separatorChar
		+ "BigNoxGameHD"
		);

		return noxFolder.exists();
	}
}