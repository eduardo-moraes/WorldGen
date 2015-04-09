package datastructures;

import java.util.HashSet;
import java.util.Set;

public class Continent {
	
	//---Object Data
	Set<Coordinate> mSquares;
	
	//---Constructors
	public Continent() {
		mSquares = new HashSet<Coordinate>();
	}
	
	//---Methods
	
	public int getSize() {
		return mSquares.size();
	}
	
	public boolean contains(int x, int y) {
		Coordinate location = new Coordinate(x, y);
		return mSquares.contains(location);
	}
	
	public boolean addSquare(int x, int y) {
		Coordinate location = new Coordinate(x, y);
		if (mSquares.contains(location)) return false;
		else {
			mSquares.add(location);
			return true;
		}
	}
}
