package datamaps;

import world.Region;

public class ContinentMap {
	
	//---Object Data
	Region mRegion;
	final int w, h;
	int[][] mContinents;
	int mTotalContinents;

	//---Constructors
	public ContinentMap(int width, int height, Region owner) {
		// Set owner, width and height
		this.mRegion = owner;
		this.w = width;
		this.h = height;
		// Map the continents
		mapContinents();
	}

	//---Methods
	
	public int getWidth() { return this.w; }

	public int getHeight() { return this.h; }
	
	private void mapContinents() {
		// Get elevation map and ensure it is the same size
		ElevationMap elevs = mRegion.getElevationMap();
		if (elevs.w != w || elevs.h != h)
			throw new RuntimeException("Elevation map and continent map are not the same size.");
		
		// Create continent map
		this.mContinents = new int[w][h];
		// Set points below sea level to 0, all else to 2
		for (int x = 0; x < w; ++x) {
			for (int y = 0; y < h; ++y) {
				// MUST CHANGE THIS; SEA LEVEL HARD CODED TO 5
				if (elevs.getElev(x, y) < 5) mContinents[x][y] = 0;
				else mContinents[x][y] = 2;
			}
		}
		// Previous step separated land and water; now we distinguish unique land masses
		for (int x = 0; x < w; ++x) {
			for (int y = 0; y < h; ++y) {
				// 
				if (mContinents[x][y] == 0) continue;
				else if (mContinents[x][y] == 2) {
					
				}
			}
		}
	}
}
