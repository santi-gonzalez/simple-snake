package santi.android.snake.adt;

import java.util.ArrayList;
import java.util.List;

import santi.android.snake.view.PlayboardView;
import android.graphics.Point;

public class SnakeNodeADT {

	
	public Point pos = new Point();
	public int direction;
	public SnakeNodeADT next = null;
	
	public boolean isHead;
	public boolean isGrowing;
	
	public SnakeNodeADT(int x, int y, boolean head) {
		
		pos.x = x;
		pos.y = y;
		
		if(head) {
			isHead = true;
			isGrowing = false;
		} else {
			isHead = false;
			isGrowing = true;
		}
	}
	
	public boolean moveTo(int direction, boolean solidBoundaries) {

		int oldDirection = this.direction;
		this.direction = direction;
		
		switch(direction) {
		case PlayboardView.DIRECTION_LEFT: 		pos.x--; 	break;
		case PlayboardView.DIRECTION_TOP:		pos.y--; 	break;
		case PlayboardView.DIRECTION_RIGHT:		pos.x++; 	break;
		case PlayboardView.DIRECTION_BOTTOM:	pos.y++; 	break;
		}
		
		if(solidBoundaries) {
			
			if(pos.x < 0 || pos.x >= PlayboardView.BOARD_SIZE || pos.y < 0 || pos.y >= PlayboardView.BOARD_SIZE) {
				
				switch(direction) {
				case PlayboardView.DIRECTION_LEFT: 		pos.x++; 	break;
				case PlayboardView.DIRECTION_TOP:		pos.y++; 	break;
				case PlayboardView.DIRECTION_RIGHT:		pos.x--; 	break;
				case PlayboardView.DIRECTION_BOTTOM:	pos.y--; 	break;
				}
				
				return false;
			}
		}
		
		if(pos.x < 0) pos.x = PlayboardView.BOARD_SIZE - 1;
		if(pos.x >= PlayboardView.BOARD_SIZE) pos.x = 0;
		if(pos.y < 0) pos.y = PlayboardView.BOARD_SIZE - 1;
		if(pos.y >= PlayboardView.BOARD_SIZE) pos.y = 0;
		
		if(next != null) {
			
			if (next.isGrowing) {
				
				next.isGrowing = false;
//				return true;
			} else {
				
				next.moveTo(oldDirection, solidBoundaries);
//				return next.moveTo(oldDirection, solidBoundaries);
			}
		}
		
		if(isHead && next != null && next.isBody(pos.x, pos.y))
			return false;
		
		return true;
	}
	
	public void eatFruit(int nutrients) {
		
		if(nutrients == 0)
			return;
		
		if(this.next == null) {
			next = new SnakeNodeADT(pos.x, pos.y, false);
			next.eatFruit(nutrients - 1);
		} else {
			next.eatFruit(nutrients);
		}
	}
	
	public List<SnakeNodeADT> getBody() {
		
		if(next == null) {
			
			List<SnakeNodeADT> list = new ArrayList<SnakeNodeADT>();
			list.add(this);
			return list;
		} else {
			
			List<SnakeNodeADT> list = next.getBody();
			list.add(this);
			return list;
		}
	}

	public boolean isBody(int x, int y) {
		
		if(pos.x == x && pos.y == y)
			return true;
		else if(next != null)
			return next.isBody(x, y);
		return false;
	}

	public int getBodyCount() {
		
		return getBody().size();
	}
}
