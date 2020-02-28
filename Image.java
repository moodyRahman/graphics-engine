import java.io.File;
import java.io.FileWriter;
import java.lang.Math;
import java.util.Random;

public class Image{

private int width;
private int height;
private Pixel[][] pixelarray;
private Random rand = new Random();
public final String HEADER;
public static final Pixel BLACK = new Pixel(0, 0, 0);
public static final Pixel BEIGE = new Pixel(0, 0, 0);


/**
 * Instantiate a new Image
 * @param width  width in the x direction
 * @param height height in the y direction
 * @param color  initial color
 */
public Image(int width, int height, Pixel color){
	this.width = width;
	this.height = height;
	this.pixelarray = new Pixel[width][height];

	for (int x = 0; x < width; x++) {
		for (int y = 0; y < height; y++) {
				pixelarray[x][y] = new Pixel(255, 255, 255);
				pixelarray[x][y].set(color);
		}
	}

	String head = "P3\n" + Integer.toString(width) + " " + Integer.toString(height) + "\n" + "255" + "\n\n";

	this.HEADER = head;
}

/**
 * PPM'd format of the image
 * @return String with the PPM to be written
 */
public String toString(){
	String out = "";

	for (int y = height - 1; y >= 0; y--) {
		for (int x = 0; x < width; x++) {
			out = out + this.pixelarray[x][y].toString();
		}
		out += "\n";
	}
	return out;
}


/**
 * writes the image to a file
 * @param fname file to write to
 */
public void flushToFile(String fname){
	File toflush = new File(fname);
	try {
		toflush.createNewFile();

	} catch(Exception e) {
		e.printStackTrace();
	}

	try {
		// FileWriter towrite = new FileWriter(fname);
		System.out.print(this.HEADER);
		System.out.print(this.toString());
		// towrite.write(this.HEADER);
		// towrite.write(this.toString());
		// towrite.close();
	} catch(Exception e) {

	}
}

/**
 * plots a point
 * @param x     x coor
 * @param y     y coor
 * @param color color to make the point
 */
public void plot(int x, int y, Pixel color){
	this.pixelarray[x][y].set(color);
}

/**
 * old draw line algorithm
 * @param px1 [description]
 * @param py1 [description]
 * @param px2 [description]
 * @param py2 [description]
 */
@Deprecated
private void drawLine(int px1, int py1, int px2, int py2){
	int x = px1;
	int y = py1;

	int deltay = py2 - py1;
	int deltax = px2 - px1;

	int a = deltay;
	int b = (-1 * deltax);

	int d = ((2 * a) + b);

	while (x < px2){
		plot(x, y, new Pixel(100, 100, 100));
		if (d > 0){
			y = y + 1;
			d = d + (2 * b);
		}
		x = x + 1;
		d = d + (2 * a);
	}
}


private void drawLineLow(int px1, int py1, int px2, int py2, Pixel c){
	int dx = px2 - px1;
	int dy = py2 - py1;
	int yi = 1;
	if (dy < 0){
		yi = -1;
		dy = -1 * dy;
	}
	int d = (2*dy) - dx;

	int y = py1;
	int x = px1;

	while(x < px2){
		plot(x, y, c);
		if (d > 0){
			y = y + yi;
			d = d - (2*dx);
		}
		d = d + (2*dy);
		x++;
	}
}


private void drawLineHigh(int px1, int py1, int px2, int py2, Pixel c){
	int dx = px2 - px1;
	int dy = py2 - py1;
	int xi = 1;
	if (dx < 0){
		xi = -1;
		dx = -1 * dx;
	}
	int d = (2*dx) - dy;

	int x = px1;
	int y = py1;

	while (y < py2){
		plot(x, y, c);
		if (d > 0){
			x = x + xi;
			d = d - (2 * dy);
		}
		d = d + (2*dx);
		y++;
	}
}

/**
 * draws a line of a given color
 * @param px1 [description]
 * @param py1 [description]
 * @param px2 [description]
 * @param py2 [description]
 * @param c   Color of the line
 */
public void line(int px1, int py1, int px2, int py2, Pixel c){
	if (Math.abs(py2 - py1) <= Math.abs(px2 - px1)){
		if (px1 > px2){
			drawLineLow(px2, py2, px1, py1, c);
		}
		else {
			drawLineLow(px1, py1, px2, py2, c);
		}
	}
	else {
		if (py1 > py2){
			drawLineHigh(px2, py2, px1, py1, c);
		}
		else {
			drawLineHigh(px1, py1, px2, py2, c);
		}
	}
}

public void line(double px1, double py1, double px2, double py2, Pixel c){
	int px1i = (int)px1;
	int py1i = (int)py1;
	int px2i = (int)px2;
	int py2i = (int)py2;
	if (Math.abs(py2i - py1i) <= Math.abs(px2i - px1i)){
		if (px1i > px2i){
			drawLineLow(px2i, py2i, px1i, py1i, c);
		}
		else {
			drawLineLow(px1i, py1i, px2i, py2i, c);
		}
	}
	else {
		if (py1i > py2i){
			drawLineHigh(px2i, py2i, px1i, py1i, c);
		}
		else {
			drawLineHigh(px1i, py1i, px2i, py2i, c);
		}
	}
}

/**
 * draws lines on the image according to a matrix
 * @param m Matrix to draw
 * @param c Color to make the lines
 */
public void matrixLine(Matrix m, Pixel c){
	int[][] array = m.getarray();
	for (int x = 1; x < array.length; x++) {
                int[] p1 = array[x];
                int[] p2 = array[x-1];

                line(p1[0], p1[1], p2[0], p2[1], c);
        }

}

}
