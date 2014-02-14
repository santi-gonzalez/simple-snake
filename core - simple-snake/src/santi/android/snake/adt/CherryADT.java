package santi.android.snake.adt;

public class CherryADT extends AbsFruitADT {

	public CherryADT(int x, int y) {
		super(x, y);
	}

	@Override
	public int getNutrients() {
		return 1;
	}

	@Override
	public int getColor() {
		return 0xffff0000;
	}
}
