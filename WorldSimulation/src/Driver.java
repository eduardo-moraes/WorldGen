
import world.*;
import ui.*;

public class Driver {

	public static void main(String[] args) {
		World theWorld = new World(10.0, 10.0);
		WorldView mWorldView = new WorldView(theWorld);
	}
}
