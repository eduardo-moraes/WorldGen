package proceduralgeneration;

import java.util.Random;

import utilities.convolution.*;
import datamaps.ElevationMap;

public class ElevationMapGenerator {
	
	//---Class Data
	static Random mRand = new Random();
	static double k = 1.0;
	static double H = 1.0;
	
	//---Class Methods
	
	private static double getGaussian(int level) {
		// Generate a random, Gaussian distributed double
		double value = mRand.nextGaussian();
		// If an iteration level is given and k is not zero, multiply the value to increase the standard deviation
		if (level > 0 && Math.abs(k) < 0.001) value *= (k*Math.pow(2, (-level*H)));
		// 
		return value;
	}
	
	public static ElevationMap getBlank(int size) {
		// If an the size given is less than 1, set it to 1
		if (size < 1) size = 1;
		// Return a new (blank) elevation map of this size
		return new ElevationMap(size, size);
	}
	
	public static ElevationMap TriangleEdge(int iterations) {
		// Calculate the size after the given number of iterations
		int size = (int)(Math.pow(2, iterations)) + 1;
		
		// 
		ElevationMap map = new ElevationMap(size, size);
		map.setElev(0.0, 0, 0);				map.setElev(0.0, (size-1)/2, 0);			map.setElev(0.0, size-1, 0);
		map.setElev(0.0, 0, (size-1)/2);	map.setElev(10.0, (size-1)/2, (size-1)/2);	map.setElev(0.0, size-1, (size-1)/2);
		map.setElev(0.0, 0, size-1);		map.setElev(0.0, (size-1)/2, size-1);		map.setElev(0.0, size-1, size-1);
		
		// 
		int stepSize = (size - 1) / 2;
		
		// Perform the procedure <iteration> times
		for (int i = 1; i < iterations; ++i) {
			// For each x,y position with an existing value
			for (int x = 0; x < size; x += stepSize) {
				for (int y = 0; y < size; y += stepSize) {
					// Find the midpoint where the new values will be generated
					int midX = (x + (x+stepSize)) / 2;
					int midY = (y + (y+stepSize)) / 2;
					
					// If the x position is not at the end, then a right edge can be split
					if (x != size-1) {
						// Calculate avg of left and right heights and displace by a Gaussian distributed random value
						double value = (map.getElev(x, y) + map.getElev(x+stepSize, y)) / 2.0;
						value += getGaussian(i+1);
						// Set the height at the new position
						map.setElev(value, midX, y);
					}
					// If the y position is not at the end, then a bottom edge can be split
					if (y != size-1) {
						// Calculate avg of top and bottom heights and displace by a Gaussian distributed random value
						double value = (map.getElev(x, y) + map.getElev(x, y+stepSize)) / 2.0;
						value += getGaussian(i+1);
						// Set the height at the new position
						map.setElev(value, x, midY);
					}
					// If neither position is at the end, then a diagonal edge can be split
					if (x != size-1 && y != size-1) {
						// Calculate avg of diagonal heights and displace by a Gaussian distributed random value
						double value = (map.getElev(x, y) + map.getElev(x+stepSize, y+stepSize)) / 2.0;
						value += getGaussian(i+1);
						// Set the height at the new position
						map.setElev(value, midX, midY);
					}
				}
			}
			
			// Half the step size before the next iteration
			stepSize /= 2;
		}
		// Return the completed elevation map
		return map;
	}
	
	public static ElevationMap DiamondSquare(int iterations) {
		// 
		int size = (int)(Math.pow(2, iterations)) + 1;
		
		//
		ElevationMap map = getBlank(size);
		for (int y = 0; y < (size-1)/2; ++y)
			map.setElev(20.0, (size-1)/2, y);
		
		// 
		int stepSize = (size - 1) / 2;
		
		// Perform the procedure <iterations> times
		for (int i = 1; i < iterations; ++i) {
			// For each x,y position with an existing value, create a center point
			for (int x = 0; x < size; x += stepSize) {
				for (int y = 0; y < size; y += stepSize) {
					// Find the midpoint where the new values will be generated
					int midX = (x + (x+stepSize)) / 2;
					int midY = (y + (y+stepSize)) / 2;
					
					// If neither position is at the end, then a middle point can be generated
					if (x != size-1 && y != size-1) {
						// Calculate avg of corner heights and displace by a Gaussian distributed random value
						double value = (  map.getElev(x			, y			) // UPPER-LEFT
										+ map.getElev(x+stepSize, y			) // UPPER-RIGHT
										+ map.getElev(x			, y+stepSize) // LOWER-LEFT
										+ map.getElev(x+stepSize, y+stepSize) // LOWER-RIGHT
										) / 4.0;
						value += getGaussian(i+1);
						// Set the height at the new position
						map.setElev(value, midX, midY);
					}
				}
			}
			// For each x,y position with an existing value, create edge points
			for (int x = 0; x < size; x += stepSize) {
				for (int y = 0; y < size; y += stepSize) {
					// Find the midpoint where the new values will be generated
					int midX = (x + (x + stepSize)) / 2;
					int midY = (y + (y + stepSize)) / 2;
					
					// If the x position is not at the end, then a right edge can be split
					if (x != size-1) {
						// Calculate avg of diamond heights and displace by a Gaussian distributed random value
						double value = (  map.getElev(x				, y)	// LEFT
										+ map.getElev(x+stepSize	, y)	// RIGHT
										+ map.getElev(x+(stepSize/2), (y == 0 ? (size-1)-(stepSize/2) : y-(stepSize/2)))	// UP
										+ map.getElev(x+(stepSize/2), (y == (size-1) ? stepSize/2 : y+(stepSize/2)))		// DOWN
										) / 4.0;
						value += getGaussian(i+1);
						// Set the height at the new position
						map.setElev(value, midX, y);
					}
					// If the y position is not at the end, then a bottom edge can be split
					if (y != size-1) {
						// Calculate avg of diamond heights and displace by a Gaussian distributed random value
						double value = (  map.getElev((x == 0 ? (size-1)-(stepSize/2) : x-(stepSize/2))	, y+(stepSize/2))	// LEFT
										+ map.getElev((x == (size-1) ? (stepSize/2) : x+(stepSize/2))	, y+(stepSize/2))	// RIGHT
										+ map.getElev(x				, y)			// UP
										+ map.getElev(x				, y+stepSize)	// DOWN
										) / 4.0;
						value += getGaussian(i+1);
						// Set the height at the new position
						map.setElev(value, x, midY);
					}
				}
			}
			
			// Half the step size before the next iteration
			stepSize /= 2;
		}
		
		// Return the completed elevation map
		return map;
	}
	
	public static ElevationMap SquareSquare(ElevationMap seed, int iterations) {
		// 
		if (seed == null || iterations < 0) return seed;
		// 
		if (seed.w != seed.h) return seed;
		
		// 
		ElevationMap map = null;
		
		//
		for (int i = 0; i < iterations; ++i) {
			// 
			int newSize = (seed.w-1) * 2;
			map = new ElevationMap(newSize, newSize);
			
			// For each point in the seed
			for (int x = 0; x < seed.w-1; ++x) {
				for (int y = 0; y < seed.h-1; ++y) {
					// Get heights at corners of square that this point occupies on the top-left
					double topLeft     = seed.getElev(x  , y  );
					double topRight    = seed.getElev(x+1, y  );
					double bottomLeft  = seed.getElev(x  , y+1);
					double bottomRight = seed.getElev(x+1, y+1);
					// Use these heights to set the heights of the new points
					// UPPER-LEFT
					double value = (9*topLeft + 3*topRight + 3*bottomLeft + 1*bottomRight) / 16.0;
					value += getGaussian(i+1);
					map.setElev(value, 2*x,   2*y  );
					// UPPER-RIGHT
					value = (3*topLeft + 9*topRight + 1*bottomLeft + 3*bottomRight) / 16.0;
					value += getGaussian(i+1);
					map.setElev(value, 2*x+1, 2*y  );
					// LOWER-LEFT
					value = (3*topLeft + 1*topRight + 9*bottomLeft + 3*bottomRight) / 16.0;
					value += getGaussian(i+1);
					map.setElev(value, 2*x,   2*y+1);
					// LOWER-RIGHT
					value = (1*topLeft + 3*topRight + 3*bottomLeft + 9*bottomRight) / 16.0;
					value += getGaussian(i+1);
					map.setElev(value, 2*x+1, 2*y+1);
				}
			}
			
			// Set the new map as the next seed
			seed = map;
		}
		
		// 
		return map;
	}
	
	public static void filter(ElevationMap map, int filterSize, int filterType) {
		// If the map is null or the filter size is inappropriate, return
		if (map == null || filterSize < 1 || filterSize % 2 != 1) return;
		
		// Create array for convolution
		double[][] temp = new double[map.w][map.h];
		// Copy elevation values to the array
		for (int x = 0; x < map.w; ++x)
			for (int y = 0; y < map.h; ++y)
				temp[x][y] = map.getElev(x, y);
		
		// 
		if (filterType == 0) {
			// Create BoxBlur filter and convolve
			BoxBlur filter = new BoxBlur(filterSize);
			temp = Filter.convolve(temp, filter);
		}
		else if (filterType == 1) {
			// Create Sharpen filter and convolve
			Sharpen filter = new Sharpen(filterSize);
			temp = Filter.convolve(temp, filter);
		}
		else if (filterType == 2) {
			// Create EdgeDetection filter and convolve
			EdgeDetection filter = new EdgeDetection(filterSize);
			temp = Filter.convolve(temp, filter);
		}
		else return;
		
		// Copy elevation values back to the map
		for (int x = 0; x < map.w; ++x)
			for (int y = 0; y < map.h; ++y)
				map.setElev(temp[x][y], x, y);
	}
}
