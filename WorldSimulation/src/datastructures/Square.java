package datastructures;

public class Square {

	//---Object Data
	Vertex topLeft, topRight, bottomLeft, bottomRight;
	//Edge left, right, top, bottom;
	Square[][] subgrid;
	double height;
	
	
	//---Constructors
	public Square(Vertex topLeft, Vertex topRight, Vertex bottomLeft, Vertex bottomRight) {
		this.topLeft = topLeft;
		this.topRight = topRight;
		this.bottomLeft = bottomLeft;
		this.bottomRight = bottomRight;
		
		this.subgrid = null;
		this.height = 2*Math.random() - 1.0;
		
//		this.left = new Edge(topLeft, bottomLeft);
//		this.right = new Edge(topRight, bottomRight);
//		this.top = new Edge(topRight, topRight);
//		this.bottom = new Edge(bottomLeft, bottomRight);
	}
	
	//---Methods
	
	public double getHeight() {
		return this.height;
	}
	
	public Square[][] getGrid() {
		// 
		if (this.subgrid == null) return null;
		// 
		Square[][] upperLeft  = this.subgrid[0][0].getGrid();
		Square[][] upperRight = this.subgrid[0][1].getGrid();
		Square[][] lowerLeft  = this.subgrid[1][0].getGrid();
		Square[][] lowerRight = this.subgrid[1][1].getGrid();
		// 
		if (upperLeft == null && upperRight == null && lowerLeft == null && lowerRight == null) return this.subgrid;
		// 
		int newRows = upperLeft.length + upperRight.length;
		int newCols = upperLeft[0].length + lowerLeft[0].length;
		Square[][] superGrid = new Square[newRows][newCols];
		// 
		for (int x = 0; x < upperLeft.length; ++x)
			for (int y = 0; y < upperLeft[0].length; ++y)
				superGrid[x][y] = upperLeft[x][y];
		// 
		for (int x = 0; x < upperRight.length; ++x)
			for (int y = 0; y < upperRight[0].length; ++y)
				superGrid[x+upperLeft.length][y] = upperRight[x][y];
		// 
		for (int x = 0; x < lowerLeft.length; ++x)
			for (int y = 0; y < lowerLeft[0].length; ++y)
				superGrid[x][y+upperLeft[0].length] = lowerLeft[x][y];
		// 
		for (int x = 0; x < lowerRight.length; ++x)
			for (int y = 0; y < lowerRight[0].length; ++y)
				superGrid[x+upperLeft.length][y+upperLeft[0].length] = lowerRight[x][y];
		// 
		return superGrid;
	}
	
	public void split() {
		// If this square was already split, split each sub-Square and return
		if (subgrid != null) {
			subgrid[0][0].split();
			subgrid[0][1].split();
			subgrid[1][0].split();
			subgrid[1][1].split();
			return;
		}
		
		// Find the midpoint of the square
		double midX = (topLeft.getX() + topRight.getX()) / 2.0;
		double midY = (topLeft.getY() + bottomLeft.getY()) / 2.0;
		// Create the five new Vertices
		Vertex left = new Vertex(topLeft.getX(), midY);
		Vertex right = new Vertex(topRight.getX(), midY);
		Vertex top = new Vertex(midX, topLeft.getY());
		Vertex bottom = new Vertex(midX, bottomLeft.getY());
		Vertex middle = new Vertex(midX, midY);
		// Create the subgrid and each Square
		subgrid = new Square[2][2];
		subgrid[0][0] = new Square(topLeft, top, left, middle);
		subgrid[0][1] = new Square(top, topRight, middle, right);
		subgrid[1][0] = new Square(left, middle, bottomLeft, bottom);
		subgrid[1][1] = new Square(middle, right, bottom, bottomRight);
		// 
	}
}
