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

	private Pixel ambientc = new Pixel(250, 250, 250);
	private Pixel point_light = new Pixel(200, 200, 200);
	private Vector point_light_location = new Vector(1, 0.5, 1);
	private final double ka = 0.1;
	private final double ks = 0.5;
	private final double kd = 0.5;


	public Random rand = new Random();

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

		if (z > zbuffer[x][y]) {
			this.pixelarray[x][y].set(color);
			zbuffer[x][y] = z;
		}
		// this.pixelarray[x][y].set(color);

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
	 * 
	 * 
	 * @param m Matrix to draw
	 * @param c Color to make the lines
	 */
	public void matrixLineEdge(DoubleMatrix m, Pixel c) {
		double[][] array = m.getArray();
		for (int x = 0; x < array.length - 1; x += 2) {
			double[] p1 = array[x];
			double[] p2 = array[x + 1];
			line(p1, p2, c);
		}

	}

	/**
	 * draw the matrix, iterating through every three to draw a triangle
	 * 
	 * @param m
	 * @param c
	 */
	public void matrixLinePolygon(DoubleMatrix m, Pixel col) {
		double[][] array = m.getArray();
		// Pixel color = new Pixel(8, 146, 208);
		Pixel finalc = new Pixel(8, 146, 208);

		for (int x = 0; x < array.length; x += 3) {
			double[] p0 = array[x];
			double[] p1 = array[x + 1];
			double[] p2 = array[x + 2];

			Vector v1 = new Vector(p0, p1);
			Vector v2 = new Vector(p0, p2);

			Vector normal = Vector.crossproduct(v1, v2);
			double[][] scan = { p0, p1, p2 };

			if (Vector.dotproduct(normal, Vector.VIEW_VECTOR) > 0) {
				Pixel ambient = ambientc.scale(ka);

				Pixel diffuse0 = point_light.scale(kd);
				double difprod = Vector.dotproduct(normal.normalize(), point_light_location.normalize());
				Pixel diffuse = diffuse0.scale(difprod);

				// P * Ks * (2N̂(N̂ • L̂) - L̂) • V̂

				// P * Ks
				Pixel specular0 = point_light.scale(ks);
				
				// (N̂ • L̂)
				double a = Vector.dotproduct(normal, point_light_location);

				// (2N̂(N̂ • L̂)
				Vector b = normal.scale(2*a);

				// (2N̂(N̂ • L̂) - L̂)
				Vector c = new Vector(b.x - point_light_location.x, b.y - point_light_location.y, b.z - point_light_location.z);

				// (2N̂(N̂ • L̂) - L̂) • V̂
				double d = Vector.dotproduct(c, Vector.VIEW_VECTOR);

				// // P * Ks * (2N̂(N̂ • L̂) - L̂) • V̂
				specular0.scale(d);

				// // (P * Ks * (2N̂(N̂ • L̂) - L̂) • V̂) ^ n
				Pixel specular = specular0.pow(1.5);

				// Pixel finalc = Pixel.lightingsum(ambient, diffuse, specular);
				finalc.normalize();


				scanline(scan, finalc);
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
	public void scanline(double[][] points, Pixel randc) {

		// sorts according to height, shorttest to tallest

		if (points[0][1] > points[1][1]) {
			swap(points, 0, 1);
		}
		if (points[0][1] > points[2][1]) {
			swap(points, 0, 2);
		}
		if (points[1][1] > points[2][1]) {
			swap(points, 1, 2);
		}

		boolean flip = false;
		int BOT = 0;
		int TOP = 2;
		int MID = 1;

		// randc = new Pixel(rand.nextInt(), rand.nextInt(), rand.nextInt());
		// Pixel randc = new Pixel(10, 10, 10);

		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				System.out.print(points[x][y]);
				System.out.print(", ");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();

		double x0 = points[BOT][0];
		double z0 = points[BOT][2];
		double x1 = points[BOT][0];
		double z1 = points[BOT][2];
		double y = (int) points[BOT][1];

		double distance0 = (int) (points[TOP][1]) - y * 1.0 + 1;
		double distance1 = (int) (points[MID][1]) - y * 1.0 + 1;
		double distance2 = (int) (points[TOP][1]) - (int) (points[MID][1]) * 1.0 + 1;

		double dx0 = distance0 != 0 ? (points[TOP][0] - points[BOT][0]) / distance0 : 0;
		double dz0 = distance0 != 0 ? (points[TOP][2] - points[BOT][2]) / distance0 : 0;
		double dx1 = distance1 != 0 ? (points[MID][0] - points[BOT][0]) / distance1 : 0;
		double dz1 = distance1 != 0 ? (points[MID][2] - points[BOT][2]) / distance1 : 0;

		while (y <= (int) points[TOP][1]) {
			if (!flip && y >= (int) (points[MID][1])) {
				flip = true;

				dx1 = distance2 != 0 ? (points[TOP][0] - points[MID][0]) / distance2 : 0;
				dz1 = distance2 != 0 ? (points[TOP][2] - points[MID][2]) / distance2 : 0;
				x1 = points[MID][0];
				z1 = points[MID][2];

			}

			line((int) x0, y, z0, (int) x1, y, z1, randc);
			x0 += dx0;
			z0 += dz0;
			x1 += dx1;
			z1 += dz1;
			y += 1;
		}
	}

	private void swap(double[][] p, int from, int to) {
		double[] temp = p[from];
		p[from] = p[to];
		p[to] = temp;
	}

	public void rainbowline() {

		for (int x = 50; x < 200; x++) {
			Pixel randc = new Pixel(rand.nextInt() % 250, rand.nextInt() % 250, rand.nextInt() % 250);
			// Pixel randc = new Pixel(0, 0, 0);

			plot(x, 100, 0, randc);
		}
	}

	public static void main(String[] args) {
		Image i = new Image(500, 500, new Pixel(200, 200, 200));
		i.rainbowline();
		i.display();
	}

}

/**
 * Stores a pixel
 */
class Pixel {

	private double red;
	private double blue;
	private double green;
	private static Random rand = new Random();

	public Pixel(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public Pixel(double r, double g, double b){

	}


	public double getr() {
		return this.red;
	}

	public double getg() {
		return this.green;
	}

	public double getb() {
		return this.blue;
	}

	public Pixel scale(double factor){
		double r = this.red * factor;
		double g = this.green * factor;
		double b = this.blue * factor;
		return new Pixel(r, g, b);
	}

	public void randomize() {
		this.red =  Math.abs(rand.nextInt() % 256);
		this.green =Math.abs(rand.nextInt() % 256);
		this.blue = Math.abs(rand.nextInt() % 256);
	}

	public String toString() {
		String out = "";

		out += this.red + " " + this.green + " " + this.blue + " ";
		return out;
	}

	public void set(Pixel p) {
		this.red = p.getr();
		this.green = p.getb();
		this.blue = p.getg();
	}

	public void set(int r, int g, int b) {
		this.red = r;
		this.green = g;
		this.blue = b;
	}

	public void normalize(){
		red = red < 0 ? 0 : red > 255 ? 255:red; 
		green = green < 0 ? 0 : green > 255 ? 255:green;
		blue = blue < 0 ? 0 : blue > 255 ? 255 : blue;
	}

	public static Pixel lightingsum(Pixel a, Pixel b, Pixel c){
		double rsum = a.red + b.red + c.red;
		double gsum = a.green + b.green + c.green;
		double bsum = a.blue + b.blue + c.blue;
		Pixel out = new Pixel(rsum, gsum, bsum);
		out.normalize();
		return out;
	}

	public Pixel pow(double in){
		return new Pixel(Math.pow(red, in), Math.pow(green, in), Math.pow(blue, in));
	}

}
