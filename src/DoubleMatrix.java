package src;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Iterator;

/**
 * Stores a DoubleMatrix
 */
public class DoubleMatrix{

	private ArrayDeque<Double[]> matrix;
	private int cols;
	private int rows = 4;

	/**
	 * Initializes a new DoubleMatrix with 0 columns
	 */
	public DoubleMatrix() {
		this.matrix = new ArrayDeque<Double[]>();
		this.cols = 0;
	}

	/**
	 * Initializes a new DoubleMatrix with an input 2D array
	 * 
	 * @param in 2D array to be inputted
	 */
	public DoubleMatrix(double[][] in) {
		this.matrix = new ArrayDeque<Double[]>();

		for (int x = 0; x < in.length; x++) {
			Double[] toadd = new Double[4];
			for (int y = 0; y < in[x].length; y++) {
				toadd[y] = in[x][y];
			}
			matrix.add(toadd);
		}
		this.cols = in.length;
	}

	public Iterator<Double[]> getIterator() {
		return this.matrix.iterator();
	}

	/**
	 * Wipes the current matrix and deletes all columns
	 */
	public void wipe() {
		this.matrix.clear();
	}

	/**
	 * returns how many columns (podoubles) are in the matrix
	 * 
	 * @return how many columsn there are
	 */
	public int getCols() {
		return this.cols;
	}

	/**
	 * returns how many rows there are in the matrix
	 * 
	 * @return how many rows there are
	 */
	public int getRows() {
		return this.rows;
	}

	/**
	 * Adds a new column (edge) to the matrix
	 * 
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param z z coordinate
	 */
	public void addPoint(double x, double y, double z) {
		Double[] toadd = new Double[4];
		toadd[0] = x;
		toadd[1] = y;
		toadd[2] = z;
		toadd[3] = 1.0;
		this.matrix.add(toadd);
		this.cols++;
	}

	/**
	 * Adds a new column (edge) to the matrix
	 * @param in
	 */
	public void addPoint(Point in){
		Double[] toadd = new Double[4];
		toadd[0] = in.x;
		toadd[1] = in.y;
		toadd[2] = in.z;
		toadd[3] = 1.0;
		this.matrix.add(toadd);
		this.cols++;
	}

	/**
	 * Adjoins a matrix to this instance of matrix
	 * 
	 * @param append
	 */
	public void addMatrixEdge(DoubleMatrix append) {
		this.matrix.addAll(append.getInnerMatrix());
		cols += append.getCols();
	}

	/**
	 * Returns the inner data of the matrix
	 * @return
	 */
	private ArrayDeque<Double[]> getInnerMatrix() {
		return this.matrix;
	}

	/**
	 * add an edge (two columns) from two points to the matrix
	 * 
	 * @param x0
	 * @param y0
	 * @param z0
	 * @param x1
	 * @param y1
	 * @param z1
	 */
	public void addEdge(double x0, double y0, double z0, double x1, double y1, double z1) {
		this.addPoint(x0, y0, z0);
		this.addPoint(x1, y1, z1);
	}

	/**
	 * Add a polygon (three columns) given three points
	 * @param x0
	 * @param y0
	 * @param z0
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 */
	public void addPolygon(double x0, double y0, double z0, double x1, double y1, double z1, double x2, double y2,
			double z2) {
		this.addPoint(x0, y0, z0);
		this.addPoint(x1, y1, z1);
		this.addPoint(x2, y2, z2);
	}
	
	/**
	 * Add a polygon (three columns) given three points
	 * @param a
	 * @param b
	 * @param c
	 */
	public void addPolygon(Point a, Point b, Point c){
		this.addPoint(a);
		this.addPoint(b);
		this.addPoint(c);
	}

	/**
	 * returns a deep copy of this matrix
	 */
	public DoubleMatrix clone(){
		DoubleMatrix out = new DoubleMatrix();
		for (Double[] e:matrix){
			Double[] temp = e.clone();
			out.addPoint(temp[0], temp[1], temp[2]);
		}
		return out;
	}

	/**
	 * Graphical representation of the matrix
	 * 
	 * @return returns a String of the matrix, formatted nicely
	 */
	public String toString() {
		String out = "";
		out += this.getCols();
		return out;
	}

	/**
	 * converts the internal array for the matrix into an double[][]
	 * 
	 * @return the doubleernal array as an double[][]
	 */
	public double[][] getArray() {
		double[][] out = new double[this.cols][this.rows];
		Iterator<Double[]> outiter = this.matrix.iterator();
		for (int x = 0; outiter.hasNext(); x++) {
			Double[] row = outiter.next();
			for (int y = 0; y < this.rows; y++) {
				out[x][y] = row[y];
			}
		}
		return out;
	}

	/**
	 * multiplies two matrices
	 * 
	 * @param a matrix a
	 * @param b matrix b
	 * @return returns a new DoubleMatrix object with the result
	 * @throws DoubleMatrixDimensionError mismatching dimensions for a and b
	 */
	public static DoubleMatrix multiply(DoubleMatrix a, DoubleMatrix b) throws DoubleMatrixDimensionError {
		double[][] out = new double[b.getCols()][a.getRows()];
		double[][] ar = a.getArray();
		double[][] br = b.getArray();
		if (a.getCols() != b.getRows()) {
			throw new DoubleMatrixDimensionError("mooos");
		}
		for (int x = 0; x < b.getCols(); x++) {
			for (int y = 0; y < a.getRows(); y++) {
				double cumsum = 0;
				double[] ber = br[x];
				ArrayList<Double> aer = new ArrayList<Double>();
				for (int xer = 0; xer < ar.length; xer++) {
					aer.add(ar[xer][y]);
				}
				ArrayList<Double> toprdoubleber = new ArrayList<Double>();
				for (int d = 0; d < ber.length; d++) {
					toprdoubleber.add(ber[d]);
				}

				for (int g = 0; g < ber.length; g++) {
					cumsum += aer.get(g) * ber[g];
				}

				out[x][y] = cumsum;

			}
		}
		return new DoubleMatrix(out);
	}

	/**
	 * Flushes the current matrix into an image file
	 * 
	 * @return new Image with the drawn matrix
	 */
	public Image flushToImage(int width, int height, Pixel init, Pixel linecolor) {
		Image out = new Image(width, height, init);
		Iterator<Double[]> outiter = this.matrix.iterator();
		while (outiter.hasNext()) {
			Double[] p1 = outiter.next();
			Double[] p2 = outiter.next();
			out.line(p1[0], p1[1], p2[0], p2[1], linecolor);
		}
		return out;
	}

	public static void main(String[] args) {

	}

}
