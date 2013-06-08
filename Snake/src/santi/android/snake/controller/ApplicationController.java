package santi.android.snake.controller;

import android.content.Context;

public class ApplicationController {

	public static final ApplicationController INSTANCE = new ApplicationController();
	
	private ApplicationController() {}
	
	public void init(Context context) {
		mApp = context;
	}

	private Context mApp;
	public Context getApp() {
		
		if(mApp == null)
			throw new RuntimeException("Application controller not initialized!");
		return mApp;
	}
	
	private boolean mDebug = true;
	public void setDebug(boolean debug) {
		
		mDebug = debug;
	}
	public boolean getDebug() {
		
		return mDebug;
	}
}
