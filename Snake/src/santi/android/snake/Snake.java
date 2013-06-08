package santi.android.snake;

import santi.android.snake.controller.ApplicationController;
import android.app.Application;

public class Snake extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		ApplicationController.INSTANCE.init(getApplicationContext());
	}
}
