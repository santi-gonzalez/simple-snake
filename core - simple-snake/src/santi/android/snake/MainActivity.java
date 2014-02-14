package santi.android.snake;

import santi.android.snake.view.PlayboardView;
import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;

public class MainActivity extends Activity {

	private PlayboardView mPlayboardView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		mPlayboardView = new PlayboardView(this);
		mPlayboardView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		setContentView(mPlayboardView);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		mPlayboardView.resume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		mPlayboardView.pause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		mPlayboardView.finalize();
	}
}
