package utilities.convolution;

public class BoxBlur extends Filter {

	//---Object Data
	/*	  INHERITED
	 *  final int RADIUS
	 *  final int SIZE
	 *  final double SCALAR
	 *  final double[][] FILTER
	 */

	//---Constructors
	public BoxBlur(int size) {
		// Set final fields using superclass constructor
		// The scalar is the inverse of the total number of values
		super(size, (1.0 / (double)(size*size)));
	}

	//---Methods
	
	@Override
	protected void setFilter() {
		// Populate array with 1's
		for (int x = 0; x < SIZE; ++x)
			for (int y = 0; y < SIZE; ++y)
				FILTER[x][y] = 1.0;
	}
}
