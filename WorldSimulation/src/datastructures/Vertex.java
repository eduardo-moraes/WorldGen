package datastructures;

public class Vertex {
	
	//---Object Data
	double x, y;
	Edge adjEdge;
	
	//---Constructors
	public Vertex() {
		this(0, 0);
	}
	
	public Vertex(double x, double y) {
		this.x = x;
		this.y = y;
		this.adjEdge = null;
	}
	
	//---Methods
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public void linkEdge(Edge edge) {
		this.adjEdge = edge;
	}
	
	
}
