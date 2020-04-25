package src;

import java.io.BufferedWriter;
import java.io.File;
import java.lang.Math;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Represents an image
 */
public class Image {

	private int width;
	private int height;
	private Pixel[][] pixelarray;
	private double[][] zbuffer;
	public final String HEADER;
	public static final Pixel BLACK = new Pixel(0, 0, 0);
	public static final Pixel BEIGE = new Pixel(0, 0, 0);

	/**
	 * Instantiate a new Image
	 * 
	 * @param width  width in the x direction
	 * @param height height in the y direction
	 * @param color  initial color
	 */
	public Image(int width, int height, Pixel color) {
		this.width = width;
		this.height = height;
		this.pixelarray = new Pixel[width][height];
		this.zbuffer = new double[width][height];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				pixelarray[x][y] = new Pixel(255, 255, 255);
				zbuffer[x][y] = -99999;
				pixelarray[x][y].set(color);
			}
		}

		String head = "P3\n" + Integer.toString(width) + " " + Integer.toString(height) + "\n" + "255" + "\n\n";

		this.HEADER = head;
	}

	/**
	 * PPM'd format of the image
	 * 
	 * @return Iterator with the PPM to be written
	 */
	public Iterator<String> getBody() {
		LinkedList<String> out = new LinkedList<String>();

		for (int y = height - 1; y >= 0; y--) {
			for (int x = 0; x < width; x++) {
				out.add(this.pixelarray[x][y].toString());
			}
		}
		return out.iterator();
	}

	/**
	 * writes the image to a file
	 * 
	 * @param fname file to write to
	 */
	public void flushToFile(String fname) {
		File toflush = new File(fname);
		try {
			toflush.createNewFile();

			FileWriter towritefw = new FileWriter(fname);
			BufferedWriter towrite = new BufferedWriter(towritefw);
			towrite.write(this.HEADER);

			Iterator<String> body = this.getBody();
			while (body.hasNext()) {
				towrite.write(body.next());
			}
			towrite.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * plots a point
	 * 
	 * @param x     x coor
	 * @param y     y coor
	 * @param color color to make the point
	 */
	public void plot(int x, int y, double z, Pixel color) {
		if (x < 0 || x >= this.width || y < 0 || y >= this.height) {
			return;
		}

		// if (z > zbuffer[x][y]) {
		// 	// this.pixelarray[x][y].set(color);
		// 	zbuffer[x][y] = z;
		// }
		this.pixelarray[x][y].set(color);

	}

	/**
	 * draws a line where the slopes delta x > delta y
	 * 
	 * @param px1
	 * @param py1
	 * @param px2
	 * @param py2
	 * @param c
	 */
	private void drawLineLow(int px1, int py1, double pz1, int px2, int py2, double pz2, Pixel c) {
		int dx = px2 - px1;
		int dy = py2 - py1;
		double dz = pz2 - pz1;

		int yi = 1;
		if (dy < 0) {
			yi = -1;
			dy = -1 * dy;
		}

		int d = (2 * dy) - dx;

		int y = py1;
		int x = px1;
		double z = pz1;

		while (x < px2) {
			plot(x, y, z, c);
			if (d > 0) {
				y = y + yi;
				d = d - (2 * dx);
			}
			d = d + (2 * dy);
			x++;
			z += dz / dx;
		}
	}

	/**
	 * draws a line where the slopes delta y > delta x
	 * 
	 * @param px1
	 * @param py1
	 * @param px2
	 * @param py2
	 * @param c
	 */
	private void drawLineHigh(int px1, int py1, double pz1, int px2, int py2, double pz2, Pixel c) {
		int dx = px2 - px1;
		int dy = py2 - py1;
		double dz = pz2 - pz1;
		int xi = 1;
		if (dx < 0) {
			xi = -1;
			dx = -1 * dx;
		}
		int d = (2 * dx) - dy;

		int x = px1;
		int y = py1;
		double z = pz1;

		while (y < py2) {
			plot(x, y, z, c);
			if (d > 0) {
				x = x + xi;
				d = d - (2 * dy);
			}
			d = d + (2 * dx);
			y++;
			z += dz / dy;
		}
	}

	/**
	 * Draws a line in the image of the given color
	 * 
	 * @param pxl1
	 * @param pyl1
	 * @param pxl2
	 * @param pyl2
	 * @param c
	 */
	public void line(double pxl1, double pyl1, double pz1, double pxl2, double pyl2, double pz2, Pixel c) {

		// int py2 = (int)pyl2 < 0 ? 0 : (int)pyl2 > this.height ? this.height :
		// (int)pyl2;
		// || || || || || ||
		// define py2 as if py2 less return 0 else py2 greater return else
		// then 0 than this.height? this.height do nothing

		int px1 = (int) pxl1 < 0 ? 0 : (int) pxl1 > this.width ? this.width : (int) pxl1;
		int py1 = (int) pyl1 < 0 ? 0 : (int) pyl1 > this.height ? this.height : (int) pyl1;
		int px2 = (int) pxl2 < 0 ? 0 : (int) pxl2 > this.width ? this.width : (int) pxl2;
		int py2 = (int) pyl2 < 0 ? 0 : (int) pyl2 > this.height ? this.height : (int) pyl2;

		// int px1 = (int)pxl1;
		// int py1 = (int)pyl1;
		// int px2 = (int)pxl2;
		// int py2 = (int)pyl2;

		if (px1 < 0 || py1 < 0 || px2 < 0 || py2 < 0) {
			return;
		}
		if (Math.abs(py2 - py1) <= Math.abs(px2 - px1)) {
			if (px1 > px2) {
				drawLineLow(px2, py2, pz2, px1, py1, pz1, c);
			} else {
				drawLineLow(px1, py1, pz1, px2, py2, pz2, c);
			}
		} else {
			if (py1 > py2) {
				drawLineHigh(px2, py2, pz2, px1, py1, pz1, c);
			} else {
				drawLineHigh(px1, py1, pz1, px2, py2, pz2, c);
			}
		}
	}

	public void line(double[] p1, double[] p2, Pixel c) {
		line(p1[0], p1[1], p1[2], p2[0], p2[1], p2[2], c);
	}

	/**
	 * write the current pixel array to a file and display it
	 */
	public void display() {
		try {
			this.flushToFile("./tmp/d.ppm");
			Runtime.getRuntime().exec("convert ./tmp/d.ppm ./tmp/d.png");
			Thread.sleep(500);
			Picture p = new Picture("./tmp/d.png");
			p.show();
		} catch (Exception e) {

		}
	}

	/**
	 * Write the current pixel array to a file, and display it for 2 seconds, and
	 * then terminate the image
	 */
	public void displayDebug() {
		try {
			this.flushToFile("./tmp/d.ppm");
			Runtime.getRuntime().exec("convert ./tmp/d.ppm ./tmp/d.png");
			Thread.sleep(500);
			Picture p = new Picture("./tmp/d.png");
			p.showdebug(2000);
		} catch (Exception e) {

		}
	}

	/**
	 * draw lines on the image according to a matrix recommended to use
	 * Matrix.flushtofile()
	 * 
	 * @param m Matrix to draw
	 * @param c Color to make the lines
	 */
	public void matrixLineEdge(DoubleMatrix m, Pixel c) {
		double[][] array = m.getArray();
		for (int x = 1; x < array.length; x++) {
			double[] p1 = array[x];
			double[] p2 = array[x - 1];

			line(p1[0], p1[1], 99, p2[0], p2[1], 99, c);
		}

	}

	/**
	 * draw the matrix, iterating through every three to draw a triangle
	 * 
	 * @param m
	 * @param c
	 */
	public void matrixLinePolygon(DoubleMatrix m, Pixel c) {
		double[][] array = m.getArray();
		for (int x = 0; x < array.length - 3; x += 3) {
			double[] p0 = array[x];
			double[] p1 = array[x + 1];
			double[] p2 = array[x + 2];

			Vector v1 = new Vector(p0, p1);
			Vector v2 = new Vector(p0, p2);

			Vector normal = Vector.crossproduct(v1, v2);
			double[][] scan = { p0, p1, p2 };

			if (Vector.dotproduct(normal, Vector.VIEW_VECTOR) > 0) {
				line(p0[0], p0[1], p0[2], p1[0], p1[1], p1[2], c);
				line(p1[0], p1[1], p1[2], p2[0], p2[1], p2[2], c);
				line(p2[0], p2[1], p2[2], p0[0], p0[1], p0[2], c);
				scanline(scan);
				// displayDebug();
			}
		}

	}

	/**
	 * Handles the scanline filling
	 * 
	 * @param p1
	 * @param p2
	 * @param p3
	 */
	public void scanline(double[][] points) {
		// Pixel c = new Pixel(200, 200, 200);
		if (points[0][1] > points[1][1]) {
			swap(points, 0, 1);
		}
		if (points[0][1] > points[2][1]) {
			swap(points, 0, 2);
		}
		if (points[1][1] > points[2][1]) {
			swap(points, 1, 2);
		}

		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				System.out.print(points[x][y]);
				System.out.print(", ");
			}
			System.out.println();
		}

		double[] topvertex = points[0];
		double[] midvertex = points[1];
		double[] bottomvertex = points[2];

		double xt = topvertex[0], yt = topvertex[1], zt = topvertex[2];
		double xm = midvertex[0], ym = midvertex[1], zm = midvertex[2];
		double xb = bottomvertex[0], yb = bottomvertex[1], zb = bottomvertex[2];

		Pixel randc = new Pixel(0, 0, 0);

		double dx0 = (xt - xb) / (yt - yb);
		double dx1 = (xm - xb) / (ym - yb);
		double dx1_1 = (xt - xm) / (yt - ym);

		double yOffset0 = Math.ceil(yb) - yb;
		double yOffset1 = Math.ceil(ym) - ym;

		double x0 = xb + (yOffset0 * dx0);
		double x1 = xb + (yOffset0 * dx1);
		double x2 = xm + (yOffset1 * dx1_1);

		double y = Math.ceil(yb);

		while (y < Math.ceil(ym)) {
			line(x0, y, 0, x1, y, 0, randc);
			x0 += dx0;
			x1 += dx1;
			y += 1;
		}

		while (y < Math.ceil(yt)) {
			line(x0, y, 0, x2, y, 0, randc);
			x0 += dx0;
			x2 += dx1_1;
			y += 1;
		}

	}

	private void swap(double[][] p, int from, int to) {
		double[] temp = p[from];
		p[from] = p[to];
		p[to] = temp;
	}

}

/**
 * Stores a pixel
 */
class Pixel {

	private int red;
	private int blue;
	private int green;
	private static Random rand;

	public Pixel(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		rand = new Random();
	}

	public int[] get() {
		int[] out = new int[3];
		out[0] = this.red;
		out[1] = this.green;
		out[2] = this.blue;
		return out;
	}

	public int getr() {
		return this.red;
	}

	public int getg() {
		return this.green;
	}

	public int getb() {
		return this.blue;
	}

	public static Pixel randomColor() {
		return new Pixel(rand.nextInt(), rand.nextInt(), rand.nextInt());
	}

	public String toString() {
		String out = "";

		out += this.red + " " + this.green + " " + this.blue + " ";
		return out;
	}

	public void set(Pixel p) {
		this.red = p.get()[0];
		this.green = p.get()[1];
		this.blue = p.get()[2];
	}

	public void set(int r, int g, int b) {
		this.red = r;
		this.green = g;
		this.blue = b;
	}

}