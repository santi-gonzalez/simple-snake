package santi.android.snake.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import santi.android.snake.adt.AbsFruitADT;
import santi.android.snake.adt.CherryADT;
import santi.android.snake.adt.SnakeNodeADT;
import santi.android.snake.controller.ApplicationController;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public class PlayboardView extends View {

	public static final String TAG = "PlayboardView";
	
	public static final int BOARD_SIZE = 15;
	public static final int MAX_FRUITS = 2;
	public static final int START_LEVEL = 1;
	public static final int NUTRIENTS_LEVEL = 5;
	
	public static final int DIRECTION_LEFT = 0;
	public static final int DIRECTION_TOP = 1;
	public static final int DIRECTION_RIGHT = 2;
	public static final int DIRECTION_BOTTOM = 3;
	
	public static final int STATE_ERROR = -1;
	public static final int STATE_INIT = 0;
	public static final int STATE_READY = 1;
	public static final int STATE_RUNNING = 2;
	public static final int STATE_PAUSED = 3;
	public static final int STATE_WIN = 4;
	public static final int STATE_LOST = 5;
	
	public static final int SNAKE_START_X = BOARD_SIZE - 2;
	public static final int SNAKE_START_Y =  1;
	public static final int SNAKE_HEAD_COLOR =  0xff0000ff;
	public static final int SNAKE_BODY_COLOR =  0xff5555ff;
	public static final int CHERRY_COLOR =  0xffff0000;
	
	private int mWidth;
	private int mHeigth;
	
	private float mTileSize;

	private int mDirection;
	private float mTouchX;
	private float mTouchY;

	private int mState;
	
	private int mCount = 0;
	private boolean mSolidBoundaries = true;
	
	private SnakeNodeADT mSnake = new SnakeNodeADT(SNAKE_START_X, SNAKE_START_Y, true);
	private List<AbsFruitADT> mFruits = new ArrayList<AbsFruitADT>();
	
	private Paint mBackgroundPaint;
	private Paint mBoardPaint;
	private Paint mTilePaint;
	
	private Rect mBackgroundRect;
	private Rect mBoardRect;
	private RectF mTileRectF;
	
	private SnakeGameTask mTask;
	
	public PlayboardView(Context context) {
		super(context);
		
		setWillNotDraw(false);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		
		mWidth = right;
		mHeigth = bottom;

		init();
	}

	@Override
	protected void onDraw(Canvas canvas) {

		drawBackground(canvas);
		drawBoard(canvas);
		drawCherries(canvas);
		drawSnake(canvas);

		if (ApplicationController.INSTANCE.getDebug()) {
			__deb_printStats(canvas);
			__deb_printGrid(canvas);
		}
		
		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		mTouchX = event.getX();
		mTouchY = event.getY();

		if (mTouchX < 0) mTouchX = 0;
		if (mTouchY < 0) mTouchY = 0;

		invalidate();

		return true;
	}

	class SnakeGameTask extends AsyncTask<Object, Object, Object> {

		@Override
		protected Object doInBackground(Object... params) {
			
			while(!isCancelled()) {
			
				try {
					Thread.sleep(1000 / ((int)(mSnake.getBodyCount() / NUTRIENTS_LEVEL) + START_LEVEL));
				} catch (InterruptedException e) {}
				
				publishProgress();
			}
			
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Object... values) {
			super.onProgressUpdate(values);
			
			if(mState != STATE_RUNNING)
				return;
			
			mCount++;
			
			mSnake.eatFruit(pickFruit(mSnake.pos.x, mSnake.pos.y));
			
			if(!mSnake.moveTo(mDirection, mSolidBoundaries))
				setState(STATE_LOST);
			
			invalidate();
		}
		
		@Override
		protected void onCancelled() {
			super.onCancelled();
			
			mTask = null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			
			mTask = null;
		}
	}
	
	
	class PlayboardGestureListener extends SimpleOnGestureListener {

		private static final int SWIPE_MIN_DISTANCE 		= 0;
	    private static final int SWIPE_MAX_OFF_PATH 		= 9999;
	    private static final int SWIPE_THRESHOLD_VELOCITY 	= 0;
	    
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			
			try {
				
				float deltaX = Math.abs(e1.getX() - e2.getX());
				float deltaY = Math.abs(e1.getY() - e2.getY());
				
				if(deltaX > deltaY) {
					
					if (deltaY > SWIPE_MAX_OFF_PATH)
						return false;
					
	                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	                    mDirection = DIRECTION_LEFT;
	                } else if(e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	                	mDirection = DIRECTION_RIGHT;
	                }	
				} else {
					
					if (deltaX > SWIPE_MAX_OFF_PATH)
						return false;
					
	                if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	                	mDirection = DIRECTION_TOP;
	                } else if(e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	                	mDirection = DIRECTION_BOTTOM;
	                }
				}
            } catch (Exception e) {

            	Log.e(TAG, "Problem on geture detection!");
            }
            return false;
		}
	}
	
	public void resume() {

		if(mTask == null) {
			
			mTask = new SnakeGameTask();
			mTask.execute();
		}
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {

				setState(STATE_RUNNING);
			}
		}, 2000);
	}
	
	public void pause() {
		
		setState(STATE_PAUSED);
	}
	
	public void finalize() {
		
		if(mTask != null) {
			mTask.cancel(true);
		}
		
		setState(STATE_LOST);
	}

	//- ####################################################################################################
	//- PRIVATE METHODS
	//- ####################################################################################################

	private void init() {
		
		setState(STATE_INIT);
		
		mTileSize = (float)mHeigth / (float)BOARD_SIZE;

		mBackgroundPaint = new Paint();
		mBackgroundPaint.setColor(Color.BLACK);
		
		mBoardPaint = new Paint();
		mBoardPaint.setColor(Color.LTGRAY);
		
		mTilePaint = new Paint();
		mTilePaint.setColor(Color.BLACK);
		
		__debug_textPaint = new Paint();
		__debug_textPaint.setTextSize(__DEBUG_TEXT_SIZE);
		__debug_textPaint.setColor(Color.WHITE);
		
		mBackgroundRect = new Rect(0, 0, mWidth, mHeigth);
		mBoardRect = new Rect((mWidth - mHeigth) / 2, 0, ((mWidth - mHeigth) / 2) + mHeigth, mHeigth);
		mTileRectF = new RectF();
		
		final GestureDetector gestureDetector = new GestureDetector(getContext(), new PlayboardGestureListener());
		setOnTouchListener(new OnTouchListener(	) {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		});
		
		generateFruits();
		
		setState(STATE_READY);
	}
	
	private void generateFruits() {
		
		if(mSnake.getBodyCount() >= (BOARD_SIZE * BOARD_SIZE) * 9 / 10) {
			setState(STATE_WIN);
			return;
		}
		
		Random rand = new Random(System.currentTimeMillis());
		
		if(mFruits == null)
			mFruits = new ArrayList<AbsFruitADT>();
		
		while(mFruits.size() < MAX_FRUITS) {
			
			int x = rand.nextInt(BOARD_SIZE);
			int y = rand.nextInt(BOARD_SIZE);
			
			if(isEmptyTile(x, y))
				mFruits.add(new CherryADT(x, y));
		}
	}
	
	private int pickFruit(int x, int y) {
		
		for(int index = 0 ; index < mFruits.size() ; index++) {
		
			AbsFruitADT fruit = mFruits.get(index);
			if(fruit.pos.x == x && fruit.pos.y == y) {
				
				mFruits.remove(fruit);
				
				generateFruits();
				
				return fruit.getNutrients();
			}
		}
		return 0;
	}
	
	private boolean isEmptyTile(int x, int y) {
		
		for(Iterator<AbsFruitADT> iterator = mFruits.iterator(); iterator.hasNext() ; ) {
			
			AbsFruitADT fruit = iterator.next();
			if(fruit.pos.x == x && fruit.pos.y == y)
				return false;
		}
		
		if(mSnake.isBody(x, y))
			return false;
		return true;
	}
	
	private void setState(int state) {
		
		mState = state;
		invalidate();
	}
	
	//- ####################################################################################################
	//- DRAW
	//- ####################################################################################################
	
	private void drawBackground(Canvas canvas) {
	
		canvas.drawRect(mBackgroundRect, mBackgroundPaint);
	}
	
	private void drawBoard(Canvas canvas) {

		canvas.drawRect(mBoardRect, mBoardPaint);
	}

	private void drawSnake(Canvas canvas) {
		
		drawTile(canvas, mSnake.pos.x, mSnake.pos.y, SNAKE_HEAD_COLOR);
		
		List<SnakeNodeADT> bodyChunks = mSnake.getBody();
		for(int index = 0 ; index < bodyChunks.size(); index++) {
			
			SnakeNodeADT bodyChunk = bodyChunks.get(index);
			int color = (bodyChunk.isHead) ? SNAKE_HEAD_COLOR : SNAKE_BODY_COLOR;
			drawTile(canvas, bodyChunk.pos.x, bodyChunk.pos.y, color);
		}
	}
	
	private void drawCherries(Canvas canvas) {
		
		for(int index = 0 ; index < mFruits.size() ; index++) {

			AbsFruitADT fruit = mFruits.get(index);
			drawTile(canvas, fruit.pos.x, fruit.pos.y, fruit.getColor());
		}
	}
	
	private void drawTile(Canvas canvas, int x, int y, int color) {
		
		mTilePaint.setColor(color);
		
		mTileRectF.left 		= (float)(mBoardRect.left + (x * mTileSize));
		mTileRectF.top 		= (float)(mBoardRect.top  + (y * mTileSize));
		mTileRectF.right 	= (float)(mBoardRect.left + (x * mTileSize) + mTileSize);
		mTileRectF.bottom 	= (float)(mBoardRect.top  + (y * mTileSize) + mTileSize);
		canvas.drawRect(mTileRectF, mTilePaint);
	}

	//- ####################################################################################################
	//- DEBUG
	//- ####################################################################################################
	
	private static final float __DEBUG_TEXT_SIZE = 24;
	private static final int __DEBUG_TEXT_OFFSET_X = 12;
	private static final int __DEBUG_TEXT_OFFSET_Y = 32;
	private static final int __DEBUG_GRID_COLOR_A = 0x88cccccc;
	private static final int __DEBUG_GRID_COLOR_B = 0x88aaaaaa;
	
	private Paint __debug_textPaint;

	private void __deb_printStats(Canvas canvas) {

		String coordX = "coord x: %.0f";
		String coordY = "coord y: %.0f";
		String direction = "direction: %s";
		String ticks = "ticks: %d";
		String state = "state: %s";
		String nutrients = "nutrients: %d";
		String level = "level: %d";

		canvas.drawText(String.format(coordX, mTouchX), __DEBUG_TEXT_OFFSET_X, __DEBUG_TEXT_OFFSET_Y * 1, __debug_textPaint);
		canvas.drawText(String.format(coordY, mTouchY), __DEBUG_TEXT_OFFSET_X, __DEBUG_TEXT_OFFSET_Y * 2, __debug_textPaint);
		switch(mDirection) {
		case DIRECTION_LEFT:
			canvas.drawText(String.format(direction, "left"), __DEBUG_TEXT_OFFSET_X, __DEBUG_TEXT_OFFSET_Y * 3, __debug_textPaint);
			break;
		case DIRECTION_TOP:
			canvas.drawText(String.format(direction, "top"), __DEBUG_TEXT_OFFSET_X, __DEBUG_TEXT_OFFSET_Y * 3, __debug_textPaint);
			break;
		case DIRECTION_RIGHT:
			canvas.drawText(String.format(direction, "right"), __DEBUG_TEXT_OFFSET_X, __DEBUG_TEXT_OFFSET_Y * 3, __debug_textPaint);
			break;
		case DIRECTION_BOTTOM:
			canvas.drawText(String.format(direction, "bottom"), __DEBUG_TEXT_OFFSET_X, __DEBUG_TEXT_OFFSET_Y * 3, __debug_textPaint);
			break;
		}
		canvas.drawText(String.format(ticks, mCount), __DEBUG_TEXT_OFFSET_X, __DEBUG_TEXT_OFFSET_Y * 4, __debug_textPaint);
		switch(mState) {
		case STATE_ERROR:
			canvas.drawText(String.format(state, "error"), __DEBUG_TEXT_OFFSET_X, __DEBUG_TEXT_OFFSET_Y * 5, __debug_textPaint);
			break;
		case STATE_INIT:
			canvas.drawText(String.format(state, "init"), __DEBUG_TEXT_OFFSET_X, __DEBUG_TEXT_OFFSET_Y * 5, __debug_textPaint);
			break;
		case STATE_READY:
			canvas.drawText(String.format(state, "ready"), __DEBUG_TEXT_OFFSET_X, __DEBUG_TEXT_OFFSET_Y * 5, __debug_textPaint);
			break;
		case STATE_RUNNING:
			canvas.drawText(String.format(state, "running"), __DEBUG_TEXT_OFFSET_X, __DEBUG_TEXT_OFFSET_Y * 5, __debug_textPaint);
			break;
		case STATE_PAUSED:
			canvas.drawText(String.format(state, "paused"), __DEBUG_TEXT_OFFSET_X, __DEBUG_TEXT_OFFSET_Y * 5, __debug_textPaint);
			break;
		case STATE_WIN:
			canvas.drawText(String.format(state, "win"), __DEBUG_TEXT_OFFSET_X, __DEBUG_TEXT_OFFSET_Y * 5, __debug_textPaint);
			break;
		case STATE_LOST:
			canvas.drawText(String.format(state, "lost"), __DEBUG_TEXT_OFFSET_X, __DEBUG_TEXT_OFFSET_Y * 5, __debug_textPaint);
			break;
		}
		canvas.drawText(String.format(nutrients, mSnake.getBodyCount()), __DEBUG_TEXT_OFFSET_X, __DEBUG_TEXT_OFFSET_Y * 6, __debug_textPaint);
		canvas.drawText(String.format(level, ((int)(mSnake.getBodyCount() / NUTRIENTS_LEVEL) + START_LEVEL)), __DEBUG_TEXT_OFFSET_X, __DEBUG_TEXT_OFFSET_Y * 7, __debug_textPaint);
	}
	
	private void __deb_printGrid(Canvas canvas) {
		
		for(int y = 0 ; y < BOARD_SIZE ; y++) {
			for(int x = 0 ; x < BOARD_SIZE ; x++) {
				
				int color;
				if((y + x) % 2 == 0)
					color = __DEBUG_GRID_COLOR_A;
				else
					color = __DEBUG_GRID_COLOR_B;
				
				drawTile(canvas, x, y, color);
			}
		}
	}
}