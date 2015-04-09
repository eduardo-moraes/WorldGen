package utilities;

public class Utilities {

	//---Class Data
	
	
	//---Class Methods
	public static int indexWrap(int index, int size) {
		if (index < 0) return size + index;
		else if (index >= size) return index - size;
		else return index;
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
}
