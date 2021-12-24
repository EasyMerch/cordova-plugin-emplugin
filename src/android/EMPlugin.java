package ru.pronetcom.easymerch2.emplugin;

import java.util.Objects;
import java.util.TimeZone;
import java.util.List;

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

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class EMPlugin extends CordovaPlugin {
	public static final String TAG = "EMPlugin";
	private static final String GETPROP_EXECUTABLE_PATH = "/system/bin/getprop";

	CallbackContext callbackContext;
	JSONArray args;

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
		switch(action){
			case "getDeviceInfo": getDeviceInfo(callbackContext); return true;
			case "getMockPermissionApps": getMockPermissionApps(callbackContext); return true;
		}

		return false;
	}

	public void getDeviceInfo(CallbackContext callbackContext) throws JSONException {
		JSONObject r = new JSONObject();
		r.put("isVirtual", this.isVirtual());
		r.put("serial", this.getSerialNumber());
		r.put("info", this.getInfo());
		callbackContext.success(r);
	}

	public String getSerialNumber(){
		return android.os.Build.SERIAL;
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
			// || "QC_Reference_Phone".equals(android.os.Build.BOARD) && !"Xiaomi".equalsIgnoreCase(android.os.Build.MANUFACTURER)
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

	public void getMockPermissionApps(CallbackContext callbackContext){
		cordova.getThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				try{
					callbackContext.success(getMockPermissionAppsList(cordova.getActivity()));
				} catch(JSONException e){
					callbackContext.error(e.getMessage());
				}
			}
		});
	}

	public static JSONArray getMockPermissionAppsList(Context context) throws JSONException {
		JSONArray applist = new JSONArray();

		PackageManager pm = context.getPackageManager();
		List<ApplicationInfo> packages =
				pm.getInstalledApplications(PackageManager.GET_META_DATA);

		for (ApplicationInfo applicationInfo : packages) {
			try {
				PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName, PackageManager.GET_PERMISSIONS);
				JSONObject appinfo = new JSONObject();

				// Get Permissions
				String[] requestedPermissions = packageInfo.requestedPermissions;

				if (requestedPermissions != null) {
					for (int i = 0; i < requestedPermissions.length; i++) {
						// Check for System App //
						if(!((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1)) {
							if (!(
								requestedPermissions[i].equals("android.permission.ACCESS_MOCK_LOCATION") &&
								!applicationInfo.packageName.equals(context.getPackageName())
							)) continue;
							
							String packageName = applicationInfo.packageName;

							try{
								appinfo.put("name", packageName);
							} catch(JSONException e) {
								String errorString = "ERROR ".concat(packageName).concat(": ").concat(e.getMessage());
								appinfo.put("error", errorString);
								Log.e(TAG, errorString);
							}
							applist.put(appinfo);
							
							break;
						}
					}
				}
			} catch (PackageManager.NameNotFoundException e) {
				Log.e("Got exception " , e.getMessage());
			}
		}
		
		return applist;
	}
}
