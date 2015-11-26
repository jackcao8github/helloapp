package com.njzh.zhaopin;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import com.alipay.sdk.util.PayUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


@SuppressLint("JavascriptInterface")
public class ZhaopinActivity extends Activity {
	final Activity activity = this;
	private WebView webview;
	private ValueCallback<Uri> mUploadMessage;
	private final static int FILECHOOSER_RESULTCODE = 1;
	private String mCameraFilePath = null;
	
	private PayUtil payUtil = null;
	// private ProgressDialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// new WebView
		webview = new WebView(this);

		webview.requestFocus();
		// webview支持js
		webview.getSettings().setJavaScriptEnabled(true);

		webview.getSettings().setAllowContentAccess(true);

		// webview支持缓存
		webview.getSettings().setAppCacheEnabled(true);
		String cachedir = this.getApplicationContext()
				.getDir("cache", Context.MODE_PRIVATE).getPath();
		webview.getSettings().setAppCachePath(cachedir);
		webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		webview.getSettings().setAppCacheMaxSize(8 * 1024 * 1024); // 缓存初始容量8M
		webview.getSettings().setDomStorageEnabled(true);
		webview.getSettings().setAllowFileAccess(true);

		// webview扶持定位֧
		webview.getSettings().setDatabaseEnabled(true);
		String dir = this.getApplicationContext()
				.getDir("database", Context.MODE_PRIVATE).getPath();
		webview.getSettings().setGeolocationEnabled(true);
		webview.getSettings().setGeolocationDatabasePath(dir);

		webview.requestFocusFromTouch();
		webview.getSettings().setSupportZoom(false);

		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onGeolocationPermissionsShowPrompt(String origin,
					GeolocationPermissions.Callback callback) {
				callback.invoke(origin, true, true);
				super.onGeolocationPermissionsShowPrompt(origin, callback);
			}

			// 接收js中的alert
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					final JsResult result) {
				Builder builder = new Builder(ZhaopinActivity.this);
				builder.setTitle("Alert");
				builder.setMessage(message);
				builder.setPositiveButton(android.R.string.ok,
						new AlertDialog.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								result.confirm();
							}
						});
				builder.setCancelable(false);
				builder.create();
				builder.show();
				return true;
			};

			// 接收js中的confirm
			@Override
			public boolean onJsConfirm(WebView view, String url,
					String message, final JsResult result) {
				Builder builder = new Builder(ZhaopinActivity.this);
				builder.setTitle("confirm");
				builder.setMessage(message);
				builder.setPositiveButton(android.R.string.ok,
						new AlertDialog.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								result.confirm();
							}
						});
				builder.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								result.cancel();
							}
						});
				builder.setCancelable(false);
				builder.create();
				builder.show();
				return true;
			};

			// 缓存达到上限时的处理
			@Override
			public void onReachedMaxAppCacheSize(long spaceNeeded,
					long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
				quotaUpdater.updateQuota(spaceNeeded * 2);
			}

			// webView文件选择
			// For Android 3.0+
			public void openFileChooser(ValueCallback<Uri> uploadMsg,
					String acceptType) {
				if (mUploadMessage != null)
					return;
				mUploadMessage = uploadMsg;

				startActivityForResult(createDefaultOpenableIntent(),
						FILECHOOSER_RESULTCODE);
				/*
				 * Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				 * i.addCategory(Intent.CATEGORY_OPENABLE);
				 * i.setType("image/*");
				 * startActivityForResult(Intent.createChooser(i,
				 * "File Chooser"), FILECHOOSER_RESULTCODE);
				 */
			}

			// For Android < 3.0
			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
				openFileChooser(uploadMsg, "");
			}

			// For Android > 4.1.1
			public void openFileChooser(ValueCallback<Uri> uploadMsg,
					String acceptType, String capture) {
				openFileChooser(uploadMsg, acceptType);
			}

			private Intent createDefaultOpenableIntent() {
				// Create and return a chooser with the default OPENABLE
				// actions including the camera, camcorder and sound
				// recorder where available.
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("image/*");

				Intent chooser = createChooserIntent(createCameraIntent());
				chooser.putExtra(Intent.EXTRA_INTENT, i);
				return chooser;
			}

			private Intent createChooserIntent(Intent... intents) {
				Intent chooser = new Intent(Intent.ACTION_CHOOSER);
				chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);
				chooser.putExtra(Intent.EXTRA_TITLE, "File Chooser");
				return chooser;
			}

			private Intent createCameraIntent() {
				Intent cameraIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				File externalDataDir = Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
				File cameraDataDir = new File(externalDataDir.getAbsolutePath()
						+ File.separator + "browser-photos");
				cameraDataDir.mkdirs();
				mCameraFilePath = cameraDataDir.getAbsolutePath()
						+ File.separator + System.currentTimeMillis() + ".jpg";
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(mCameraFilePath)));
				return cameraIntent;
			}

			private Intent createCamcorderIntent() {
				return new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			}

			private Intent createSoundRecorderIntent() {
				return new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
			}
		});
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView wview, String url) {

				wview.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				// dialog.dismiss();
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);

				// dialog.show();
			}

		});
		
		//设置js调用的支付宝接口
		payUtil = new PayUtil(ZhaopinActivity.this); 
		webview.addJavascriptInterface(payUtil, "payUtil");
		webview.addJavascriptInterface(new SysFuncAgent(), "sysFunc");
		
		setContentView(webview);

		// dialog = ProgressDialog.show(this,null,"loading..");
		webview.loadUrl("http://112.124.114.120:8080/PinBa/");
		//webview.loadUrl("http://192.168.1.107:8080/PinBa/");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == FILECHOOSER_RESULTCODE) {
			if (null == mUploadMessage)
				return;
			Uri result = intent == null || resultCode != RESULT_OK ? null
					: intent.getData();

			if (result == null && intent == null
					&& resultCode == Activity.RESULT_OK) {
				File cameraFile = new File(mCameraFilePath);
				if (cameraFile.exists()) {
					result = Uri.fromFile(cameraFile);
					// Broadcast to the media scanner that we have a new photo
					// so it will be added into the gallery for the user.
					sendBroadcast(new Intent(
							Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, result));
				}
			}
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
		}
	}

	private void testGeolocationOK() {
		LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		boolean gpsProviderOK = manager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean networkProviderOK = manager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		boolean geolocationOK = gpsProviderOK && networkProviderOK;
		Toast.makeText(
				this,
				"gpsProviderOK = " + gpsProviderOK + "; networkProviderOK = "
						+ networkProviderOK + "; geoLocationOK="
						+ geolocationOK, Toast.LENGTH_SHORT).show();
	}

	@Override
	// onKeyDown(int keyCoder,KeyEvent event)
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (webview.canGoBack()) {
				webview.goBack(); // goBack()
				return true;
			} else {
				exitBy2Click();
			}
		}

		return false;
	}

	/**
	 * 
	 */
	private static Boolean isExit = false;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true;
			Toast.makeText(this, "再次点击退出!", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false;
				}
			}, 2000);
		} else {
			finish();
			System.exit(0);
		}
	}
	
	private class SysFuncAgent {
		
		/**拔打电话
		 * @param cellphone 手机号码
		 */
		@JavascriptInterface
		public void call(String cellphone){
			Uri uri=Uri.parse("tel:"+cellphone);  
			Intent intent=new Intent(Intent.ACTION_DIAL,uri);  
			startActivity(intent);  
		}
	}
}
