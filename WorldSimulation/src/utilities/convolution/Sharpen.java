package utilities.convolution;

public class Sharpen extends Filter {

	//---Object Data
	/*	  INHERITED
	 *  final int RADIUS
	 *  final int SIZE
	 *  final double SCALAR
	 *  final double[][] FILTER
	 */
	
	//---Constructors
	public Sharpen(int size) {
		// Set final fields using superclass constructor
		// The scalar is simply 1
		super(size, 1.0);
	}
	
	//---Methods
	
	@Override
	protected void setFilter() {
		// Populate array with 0's
		for (int x = 0; x < SIZE; ++x)
			for (int y = 0; y < SIZE; ++y)
				FILTER[x][y] = 0.0;
		// Set center to 4
		FILTER[RADIUS][RADIUS] = 4.0;
		// Set four nearest neighbors to -1
		FILTER[RADIUS-1][RADIUS] = -1.0;
		FILTER[RADIUS+1][RADIUS] = -1.0;
		FILTER[RADIUS][RADIUS-1] = -1.0;
		FILTER[RADIUS][RADIUS+1] = -1.0;
	}
}
