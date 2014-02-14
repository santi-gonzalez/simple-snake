package santi.android.snake.adt;

public class BananaADT extends AbsFruitADT {

	public BananaADT(int x, int y) {
		super(x, y);
	}

	@Override
	public int getNutrients() {
		return 2;
	}

	@Override
	public int getColor() {
		return 0xffffff00;
	}
}
