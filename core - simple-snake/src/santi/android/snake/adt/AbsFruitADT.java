package santi.android.snake.adt;

import android.graphics.Point;

public abstract class AbsFruitADT {

	public Point pos;
	
	public AbsFruitADT(int x, int y) {
		
		pos = new Point();
		pos.x = x;
		pos.y = y;
	}
	
	public abstract int getNutrients();

	public abstract int getColor();
}
