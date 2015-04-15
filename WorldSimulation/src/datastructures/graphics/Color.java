package datastructures.graphics;

import java.util.HashMap;
import java.util.Map;

public enum Color {
	WHITE,
	RED,
	YELLOW,
	GREEN,
	BLUE,
	BLACK;
	
	private static final Map<Color, float[]> lookup = new HashMap<Color, float[]>();
	static {
		float[] white  = {1.0f, 1.0f, 1.0f};
		float[] red    = {1.0f, 0.0f, 0.0f};
		float[] yellow = {0.9f, 0.5f, 0.2f};
		float[] green  = {0.0f, 1.0f, 0.0f};
		float[] blue   = {0.0f, 0.0f, 1.0f};
		float[] black  = {0.0f, 0.0f, 0.0f};
		
		lookup.put(WHITE, white);
		lookup.put(RED, red);
		lookup.put(YELLOW, yellow);
		lookup.put(GREEN, green);
		lookup.put(BLUE, blue);
		lookup.put(BLACK, black);
	}
	
	public static float[] get(Color color) {
		return lookup.get(color);
	}
}
