package com.example.hello;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class HelloActivity extends Activity {
	/*
	 * protected void onCreate(Bundle savedInstanceState) {
	 * super.onCreate(savedInstanceState);
	 * super.loadUrl("http://10.11.17.223:8080/PinBa/"); }
	 */
	final Activity activity = this;
	private WebView webview;

	// private ProgressDialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// new WebView
		webview = new WebView(this);

		webview.requestFocus();
		//webview中支持js
		webview.getSettings().setJavaScriptEnabled(true);
		
		webview.getSettings().setAllowContentAccess(true);

		//webview支持页面缓存
		webview.getSettings().setAppCacheEnabled(true);
		String cachedir = this.getApplicationContext()
				.getDir("cache", Context.MODE_PRIVATE).getPath();
		webview.getSettings().setAppCachePath(cachedir);
		webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		webview.getSettings().setAppCacheMaxSize(8*1024*1024);   //缓存最多可以有8M
		webview.getSettings().setDomStorageEnabled(true);
		webview.getSettings().setAllowFileAccess(true);

		//webview支持定位
		webview.getSettings().setDatabaseEnabled(true);
		String dir = this.getApplicationContext()
				.getDir("database", Context.MODE_PRIVATE).getPath();
		webview.getSettings().setGeolocationEnabled(true);
		webview.getSettings().setGeolocationDatabasePath(dir);

		webview.requestFocusFromTouch();

		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onGeolocationPermissionsShowPrompt(String origin,
			GeolocationPermissions.Callback callback) {
				callback.invoke(origin, true, false);
				super.onGeolocationPermissionsShowPrompt(origin, callback);
			}

			// 处理javascript中的alert
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					final JsResult result) {
				// 构建一个Builder来显示网页中的对话框
				Builder builder = new Builder(HelloActivity.this);
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

			// 处理javascript中的confirm
			@Override
			public boolean onJsConfirm(WebView view, String url,
					String message, final JsResult result) {
				Builder builder = new Builder(HelloActivity.this);
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

			// 扩充缓存的容量
			@Override
			public void onReachedMaxAppCacheSize(long spaceNeeded,
					long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
				quotaUpdater.updateQuota(spaceNeeded * 2);
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
		setContentView(webview);

		// dialog = ProgressDialog.show(this,null,"loading..");
		webview.loadUrl("http://10.11.17.65:8080/PinBa/");
		testGeolocationOK();
	}
	private void testGeolocationOK() {
	      LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	      boolean gpsProviderOK = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	      boolean networkProviderOK = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	      boolean geolocationOK = gpsProviderOK && networkProviderOK;
	      Toast.makeText(this, "gpsProviderOK = " + gpsProviderOK + "; networkProviderOK = " + networkProviderOK + "; geoLocationOK=" + geolocationOK, Toast.LENGTH_SHORT).show();
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
			Toast.makeText(this, "再次点击返回退出!", Toast.LENGTH_SHORT).show();
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
}
