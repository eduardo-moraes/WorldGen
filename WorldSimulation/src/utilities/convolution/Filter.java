package utilities.convolution;

import utilities.Utilities;

public abstract class Filter {
	
	//---Object Data
	protected final int RADIUS;
	protected final int SIZE;
	protected final double SCALAR;
	protected final double[][] FILTER;
	
	//---Constructors
	protected Filter(int size, double scalar) {
		// Set radius and size
		this.RADIUS = (size-1) / 2;
		this.SIZE = size;
		this.SCALAR = scalar;
		
		// Create filter array
		this.FILTER = new double[SIZE][SIZE];
		
		// Call filter setting method implemented in subclass
		setFilter();
	}
	
	//---ABSTRACT METHODS (NEED IMPLEMENTATION)
	
	/*
	 *  Sets filter values
	 */
	protected abstract void setFilter();
	
	//---Methods
	
	public int getRadius() { return this.RADIUS; }
	
	public int getSize() { return this.SIZE; }
	
	public double getScalar() { return this.SCALAR; }
	
	public double get(int dx, int dy) {
		// Find the filter x,y position
		int x = RADIUS + dx;
		int y = RADIUS + dy;
		// If the position is out of bounds, return NaN
		if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) return Double.NaN;
		// Otherwise, return the appropriate filter value
		else return FILTER[x][y];
	}
	
	//---Static Methods
	
	public static double[][] convolve(double[][] array, Filter filter) {
		// If either argument is null, return input array
		if (array == null || filter == null) return array;
		// Get filter size and radius
		int filterRadius = filter.getRadius();
		int filterSize = filter.getSize();
		// If the filter is larger than the array in either dimension, return input array
		if (array.length < filterSize || array[0].length < filterSize) return array;
		
		// Create a new array to hold the changed values
		double[][] temp = new double[array.length][array[0].length];
		
		// For each x,y index in the array
		for (int x = 0; x < array.length; ++x) {
			for (int y = 0; y < array[0].length; ++y) {
				// Create a variable to accumulate the sum
				double sum = 0.0;
				// For each dx,dy index from the center of the filter
				for (int dx = -filterRadius; dx <= filterRadius; ++dx) {
					for (int dy = -filterRadius; dy <= filterRadius; ++dy) {
						// Get the multiplication factor at this dx,dy index
						double factor = filter.get(dx, dy);
						// If the value found is NaN, return the input array
						if (Double.isNaN(factor)) return array;
						// Find position in the array using wrapping
						int realX = Utilities.indexWrap(x+dx, array.length);
						int realY = Utilities.indexWrap(y+dy, array[0].length);
						// Accumulate the product
						sum += factor*array[realX][realY];
					}
				}
				// Multiply sum by filter scalar and store in array
				sum *= filter.getScalar();
				temp[x][y] = sum;
			}
		}
		// Return the temporary array
		return temp;
	}
}
