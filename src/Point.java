package src;

import java.lang.Math;
/**
 * Describes a point
 */
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

	public Point(double[] input, int id){
		this.x = input[0];
		this.y = input[1];
		this.z = input[2];
		this.id = id;
	}

	public Point(){

	}

	public String toString() {
	return "POINT " + this.id + "  LOCATED AT: " + x + ", " + y + ", " + z;
	}

	// public boolean equals(Point other){
	// 	if (this.x == other.x && this.y == other.y && this.z == other.z){
	// 		return true;
	// 	}
	// 	return false;
	// }

	public boolean equals(Point other) {
		if (almostEqual(this.x, other.x) && almostEqual(this.y, other.y) && almostEqual(this.z, other.z)) {
			return true;
		}
		return false;
	}

	public static boolean almostEqual(double a, double b) {
		return Math.abs(a - b) < 0.15;
	}


	public static boolean isDegenerate(Point a, Point b, Point c){
		return a.equals(b) || a.equals(c) || b.equals(c);
	}
}


/**
 * Describes a vector
 */
class Vector extends Point{

	static final Vector VIEW_VECTOR = new Vector(0.0, 0.0, 1.0);

	public Vector(Point a, Point b){
		this.x = b.x - a.x;
		this.y = b.y - a.y;
		this.z = b.z - a.z;
	}

	public Vector(double[] a, double[] b){
		this.x = b[0] - a[0];
		this.y = b[1] - a[1];
		this.z = b[2] - a[0];
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

	public String toString() {
		return "VECTOR: " + x + ", " + y + ", " + z;
	}
}