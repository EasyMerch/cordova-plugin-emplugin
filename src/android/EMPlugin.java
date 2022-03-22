package ru.pronetcom.easymerch2.emplugin;

import java.io.FileNotFoundException;
import java.io.OutputStream;
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

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Criteria;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import android.location.Location;
import android.location.LocationManager;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class EMPlugin extends CordovaPlugin {
	public static final String TAG = "EMPlugin";
	private static final String GETPROP_EXECUTABLE_PATH = "/system/bin/getprop";

	public static LocationManager locationManager = null;
	public static Criteria locationCriteria = null;

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
	 * @return True if the action was valid, false if not.
	 */
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		switch (action) {
			case "getDeviceInfo":
				getDeviceInfo(callbackContext);
				return true;
			case "getMockPermissionApps":
				getMockPermissionApps(callbackContext);
				return true;
			case "locationIsMock":
				getLocationIsMock(callbackContext);
				return true;
			case "saveImageToGallery":
				saveImageToGallery(callbackContext, args);
				return true;
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

	public String getSerialNumber() {
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
				"FINGERPRINT:%s\n" +
						"MODEL:%s\n" +
						"MANUFACTURER:%s\n" +
						"BRAND:%s\n" +
						"DEVICE:%s\n" +
						"BOARD:%s\n" +
						"HOST:%s\n" +
						"PRODUCT:%s\n" +
						"HARDWARE:%s\n" +
						"onBlueStacks:%s\n" +
						"onNox:%s\n" +
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
			if (line == null) {
				line = ""; //prop not set
			}
			Log.i(TAG, "read System Property: " + propName + "=" + line);
			return line;
		} catch (Exception e) {
			Log.e(TAG, "Failed to read System Property " + propName, e);
			return "";
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
				}
			}
			if (process != null) {
				process.destroy();
			}
		}
	}

	public Boolean onBlueStacks() {
		File sharedFolder = new File(android.os.Environment
				.getExternalStorageDirectory().toString()
				+ File.separatorChar
				+ "windows"
				+ File.separatorChar
				+ "BstSharedFolder");

		return sharedFolder.exists();
	}

	public Boolean onNox() {
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

	public void getMockPermissionApps(CallbackContext callbackContext) {
		cordova.getThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				try {
					callbackContext.success(getMockPermissionAppsList(cordova.getActivity()));
				} catch (JSONException e) {
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
						if (!((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1)) {
							if (!(
									requestedPermissions[i].equals("android.permission.ACCESS_MOCK_LOCATION") &&
											!applicationInfo.packageName.equals(context.getPackageName())
							)) continue;

							String packageName = applicationInfo.packageName;

							try {
								appinfo.put("name", packageName);
							} catch (JSONException e) {
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
				Log.e("Got exception ", e.getMessage());
			}
		}

		return applist;
	}

	public void getLocationIsMock(CallbackContext callbackContext) {
		cordova.getThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				try {
					locationIsMock(callbackContext);
				} catch (Exception e) {
					callbackContext.error(e.getMessage());
				}
			}
		});
	}

	public void locationIsMock(CallbackContext callbackContext) throws Exception {
		JSONObject result = new JSONObject();

		Location location = null;
		initLocationData();

		JSONArray errors = new JSONArray();
		String errorString = null;

		try {
			location = getLastLocation();
		} catch (Exception e) {
			errors.put("getLastLocation error: " + e.getMessage());
		}

		if (location != null) {
			boolean isMock = locationIsMock(location);

			try {
				result.put("isMock", isMock);
				result.put("provider", location.getProvider());
				result.put("lat", location.getLatitude());
				result.put("lng", location.getLongitude());
			} catch (JSONException e) {
				errorString = e.getMessage();
			}
		} else {
			errorString = "No location";
		}

		if (errorString != null) {
			try {
				result.put("error", errorString);
				result.put("errors", errors);
				callbackContext.error(result);
			} catch (JSONException e) {
				callbackContext.error(e.getMessage());
			}
			return;
		}

		callbackContext.success(result);
	}

	public boolean locationIsMock(Location location) {
		boolean isMock = false;

		if (Build.VERSION.SDK_INT < 31) {
			isMock = location.isFromMockProvider();
		} else {
			isMock = location.isMock();
		}

		return isMock;
	}

	public Location getLastLocation() throws Exception {
		Context context = cordova.getContext();
		if (
				ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
						ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
		) {
			Log.e(TAG, "getLastLocation permission not granted");
			return null;
		}
		Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(locationCriteria, true));

		return location;
	}

	public void initLocationData() {
		locationManager = (LocationManager) this.cordova.getActivity().getSystemService(Context.LOCATION_SERVICE);

		if(locationCriteria == null){
			locationCriteria = new Criteria();
			locationCriteria.setPowerRequirement(Criteria.POWER_LOW);
			locationCriteria.setAccuracy(Criteria.ACCURACY_FINE);
			locationCriteria.setSpeedRequired(true);
			locationCriteria.setAltitudeRequired(false);
			locationCriteria.setBearingRequired(false);
			locationCriteria.setCostAllowed(false);
		}
	}

	public void saveImageToGallery(CallbackContext callbackContext, JSONArray args){
		cordova.getThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				String
						path,
						filename = null,
						description = null;
				JSONObject options;
				try{
					path = args.getString(0);
					options = args.getJSONObject(1);
					if(options.has("filename")) filename = options.getString("filename");
					if(options.has("description")) description = options.getString("description");
				} catch(JSONException e){
					e.printStackTrace();
					callbackContext.error(e.getMessage());
					return;
				}

				path = path.replaceFirst("^file://", "");

				Bitmap bitmap = BitmapFactory.decodeFile(path);

				String url = EMPlugin.insertImage(cordova.getActivity().getApplicationContext().getContentResolver(), bitmap, filename, description);

				if(url == null){
					callbackContext.error("File not created");
				}

				callbackContext.success();
			}
		});
	}

	/**
	 * A copy of the Android internals  insertImage method, this method populates the
	 * meta data with DATE_ADDED and DATE_TAKEN. This fixes a common problem where media
	 * that is inserted manually gets saved at the end of the gallery (because date is not populated).
	 * @see android.provider.MediaStore.Images.Media#insertImage(ContentResolver, Bitmap, String, String)
	 */
	public static final String insertImage(ContentResolver cr,
										   Bitmap source,
										   String title,
										   String description) {

		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, title);
		values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
		values.put(MediaStore.Images.Media.DESCRIPTION, description);
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		// Add the date meta data to ensure the image is added at the front of the gallery
		values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
		values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

		Uri url = null;
		String stringUrl = null;    /* value to be returned */

		try {
			url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

			if (source != null) {
				OutputStream imageOut = cr.openOutputStream(url);
				try {
					source.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);
				} finally {
					imageOut.close();
				}

				long id = ContentUris.parseId(url);
				// Wait until MINI_KIND thumbnail is generated.
				Bitmap miniThumb = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
				// This is for backward compatibility.
				storeThumbnail(cr, miniThumb, id, 50F, 50F, MediaStore.Images.Thumbnails.MICRO_KIND);
			} else {
				cr.delete(url, null, null);
				url = null;
			}
		} catch (Exception e) {
			if (url != null) {
				cr.delete(url, null, null);
				url = null;
			}
		}

		if (url != null) {
			stringUrl = url.toString();
		}

		return stringUrl;
	}

	/**
	 * A copy of the Android internals StoreThumbnail method, it used with the insertImage to
	 * populate the android.provider.MediaStore.Images.Media#insertImage with all the correct
	 * meta data. The StoreThumbnail method is private so it must be duplicated here.
	 * @see android.provider.MediaStore.Images.Media (StoreThumbnail private method)
	 */
	private static final Bitmap storeThumbnail(
			ContentResolver cr,
			Bitmap source,
			long id,
			float width,
			float height,
			int kind) {

		// create the matrix to scale it
		Matrix matrix = new Matrix();

		float scaleX = width / source.getWidth();
		float scaleY = height / source.getHeight();

		matrix.setScale(scaleX, scaleY);

		Bitmap thumb = Bitmap.createBitmap(source, 0, 0,
				source.getWidth(),
				source.getHeight(), matrix,
				true
		);

		ContentValues values = new ContentValues(4);
		values.put(MediaStore.Images.Thumbnails.KIND,kind);
		values.put(MediaStore.Images.Thumbnails.IMAGE_ID,(int)id);
		values.put(MediaStore.Images.Thumbnails.HEIGHT,thumb.getHeight());
		values.put(MediaStore.Images.Thumbnails.WIDTH,thumb.getWidth());

		Uri url = cr.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

		try {
			OutputStream thumbOut = cr.openOutputStream(url);
			thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
			thumbOut.close();
			return thumb;
		} catch (FileNotFoundException ex) {
			return null;
		} catch (IOException ex) {
			return null;
		}
	}
}
