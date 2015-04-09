package utilities.convolution;

public class EdgeDetection extends Filter {

	//---Object Data
	/*	  INHERITED
	 *  final int RADIUS
	 *  final int SIZE
	 *  final double SCALAR
	 *  final double[][] FILTER
	 */

	//---Constructors
	public EdgeDetection(int size) {
		// Set final fields using superclass constructor
		// The scalar is simply 1
		super(size, 1.0);
	}

	//---Methods
	
	@Override
	protected void setFilter() {
		// Populate array with -1's
		for (int x = 0; x < SIZE; ++x)
			for (int y = 0; y < SIZE; ++y)
				FILTER[x][y] = -1.0;
		// Set center to 8
		FILTER[RADIUS][RADIUS] = 8.0;
	}
}
