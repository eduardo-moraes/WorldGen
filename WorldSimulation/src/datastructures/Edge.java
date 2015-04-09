package datastructures;

public class Edge {

	//---Object Data
	Vertex v0, v1;
	
	//---Constructors
	public Edge(Vertex start, Vertex end) {
		this.v0 = start;
		this.v0.linkEdge(this);
		this.v1 = end;
		this.v1.linkEdge(this);
	}
	
	//---Methods
	
	public Vertex split() {
		double newX = (v0.getX() + v1.getX()) / 2.0;
		double newY = (v0.getY() + v1.getY()) / 2.0;
		Vertex newVert = new Vertex(newX, newY);
		Edge newEdge = new Edge(newVert, this.v1);
		this.v1 = newVert;
		return newVert;
	}
}
