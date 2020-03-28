package src;

public class Point{
	public double x;
	public double y;
	public double z;
	public int id;
	public Point(double x, double y, double z, int id){
		this.x = x;
		this.y = y;
		this.z = z;
		this.id = id;
	}

	public Point(){

	}

	public String toString() {
	return "I AM POINT " + this.id + "LOCATED AT: " + x + ", " + y + ", " + z;
	}

	public boolean equals(Point other){
		if (this.x == other.x && this.y == other.y && this.z == other.z){
			return true;
		}
		return false;
	}


	public static boolean isdegenerate(Point a, Point b, Point c){
		return a.equals(b) || a.equals(c) || b.equals(c);
	}
}


class Vector extends Point{

	static final Vector VIEW_VECTOR = new Vector(0.0, 0.0, 1.0);

	public Vector(Point a, Point b){
		this.x = a.x - b.x;
		this.y = a.y - b.y;
		this.z = a.z - b.z;
	}

	public Vector(double dx, double dy, double dz){
		this.x = dx;
		this.y = dy;
		this.z = dz;
	}

	public static Vector crossproduct(Vector a, Vector b){
		return new Vector(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x*b.y - a.y * b.x);
	}

	public static double dotproduct(Vector a, Vector b){
		
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}
}