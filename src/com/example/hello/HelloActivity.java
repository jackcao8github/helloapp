package com.example.hello;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
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
	private ProgressDialog dialog = null; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// new WebView
		webview = new WebView(this);

		webview.requestFocus();
		webview.getSettings().setJavaScriptEnabled(true);

		webview.getSettings().setGeolocationEnabled(true);
		webview.getSettings().setAppCacheEnabled(true);
		webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webview.requestFocusFromTouch();

		webview.setWebChromeClient(new WebChromeClient());
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView wview, String url) {
				
				wview.loadUrl(url);
				return true;
			}
			@Override
			public void onPageFinished(WebView view,String url) {
				dialog.dismiss();
			}
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				
				dialog.show();
			}
		});
		setContentView(webview);
		
		dialog = ProgressDialog.show(this,null,"loading..");
		webview.loadUrl("http://10.11.17.4:8080/PinBa/");
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
