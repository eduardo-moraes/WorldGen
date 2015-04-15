package utilities;

import java.util.SortedSet;
import java.util.TreeSet;

public class Utilities {

	//---Class Data
	
	
	//---Class Methods
	public static int indexWrap(int index, int size) {
		if (index < 0) return size + index;
		else if (index >= size) return index - size;
		else return index;
	}
	
	public static double wrap(double value, double end) {
		if (value < 0) return end + value;
		else if (value >= end) return value - end;
		else return value;
	}
	
	public static double clamp(double value, double lower, double upper) {
		if (value < lower) return lower;
		else if (value > upper) return upper;
		else return value;
	}
	
	public static double distance(double x0, double y0, double x1, double y1) {
		double xDistance = (x1 - x0);
		double yDistance = (y1 - y0);
		return Math.sqrt((xDistance*xDistance) + (yDistance*yDistance));
	}
	
	public static boolean isPrime(int number) {
		// If the number is less than 2, it is not prime
		if (number < 2) return false;
		// If the number is 2, it is prime
		if (number == 2) return true;
		// If the number is divisible by 2, it is not prime
		if (number % 2 == 0) return false;
		
		// Set search limit to square root of number plus 1
		int limit = ((int)Math.sqrt(number)) + 1;
		
		// Check if the number is divisible by an odd number between 2 and the limit
		for (int i = 3; i < limit; i += 2) {
			// If the number is evenly divisible by i, it is not prime
			if (number % i == 0) return false;
		}
		// If no value evenly divides the number, it is prime
		return true;
	}
	
	public static Integer[] factor(int positiveNumber) {
		// If the number is less than 1, it has no factors
		if (positiveNumber < 1) return null;
		// If the number is 1, it is its only factor
		if (positiveNumber == 1) return new Integer[] {1};
				
		// 
		SortedSet<Integer> factors = new TreeSet<Integer>();
		factors.add(1);
		factors.add(positiveNumber);
		
		// Set search limit to square root of number plus 1
		int limit = ((int)Math.sqrt(positiveNumber)) + 1;
				
		// For each integer from 3 to the limit
		for (int i = 3; i < limit; i += 2) {
			// If the number is evenly divisible by i
			if (positiveNumber % i == 0) {
				// Then i and number/i are factors
				factors.add(i);
				factors.add(positiveNumber / i);
			}
		}
		// 
		return factors.toArray(new Integer[0]);
	}
}
