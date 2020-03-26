package src;

import java.io.BufferedWriter;
import java.io.File;
import java.lang.Math;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Represents an image
 */
public class Image{

private int width;
private int height;
private Pixel[][] pixelarray;
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
 * @return Iterator with the PPM to be written
 */
public Iterator<String> getBody(){
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
 * @param fname file to write to
 */
public void flushToFile(String fname){
	File toflush = new File(fname);
	try {
		toflush.createNewFile();

		FileWriter towritefw = new FileWriter(fname);
		BufferedWriter towrite = new BufferedWriter(towritefw);
		towrite.write(this.HEADER);

		Iterator<String> body = this.getBody();
		while (body.hasNext()){
			towrite.write(body.next());
		}
		towrite.close();
	} catch(Exception e) {
		e.printStackTrace();
	}
}

/**
 * plots a point
 * @param x     x coor
 * @param y     y coor
 * @param color color to make the point
 */
public void plot(int x, int y, Pixel color){
	if (x < 0 || x >= this.width || y < 0 || y >= this.height){
		return;
	}
	this.pixelarray[x][y].set(color);
}

/**
 * @deprecated old draw line algorithm
 * @param px1 [description]
 * @param py1 [description]
 * @param px2 [description]
 * @param py2 [description]
 */
@Deprecated
protected void drawLine(int px1, int py1, int px2, int py2){
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
 * @deprecated draws a line of a given color
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

/**
 * Draws a line in the image of the given color
 * @param pxl1
 * @param pyl1
 * @param pxl2
 * @param pyl2
 * @param c
 */
public void line(double pxl1, double pyl1, double pxl2, double pyl2, Pixel c){

	//  int py2   =       (int)pyl2 < 0       ?     0 :       (int)pyl2 > this.height      ?     this.height : (int)pyl2;
	//    ||                  ||                    ||               ||                                 ||        ||           
	//  define py2 as      if py2 less          return 0       else py2 greater                      return       else       
	//                      then 0                             than this.height?                 this.height    do nothing
	
	int px1 = (int)pxl1 < 0 ? 0 : (int)pxl1 >  this.width ? this.width : (int)pxl1;
	int py1 = (int)pyl1 < 0 ? 0 : (int)pyl1 > this.height ? this.height : (int)pyl1;
	int px2 = (int)pxl2 < 0 ? 0 : (int)pxl2 >  this.width ? this.width : (int)pxl2;
	int py2 = (int)pyl2 < 0 ? 0 : (int)pyl2 > this.height ? this.height : (int)pyl2;

	// int px1 = (int)pxl1;
	// int py1 = (int)pyl1;
	// int px2 = (int)pxl2;
	// int py2 = (int)pyl2;


	if (px1 < 0 || py1 < 0 || px2 < 0 || py2 < 0){
		return;
	}
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


public void display(){
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
 * draw lines on the image according to a matrix
 * recommended to use Matrix.flushtofile()
 * @param m Matrix to draw
 * @param c Color to make the lines
 */
public void matrixLineEdge(DoubleMatrix m, Pixel c){
	double[][] array = m.getarray();
	for (int x = 1; x < array.length; x++) {
		double[] p1 = array[x];
		double[] p2 = array[x-1];

		line(p1[0], p1[1], p2[0], p2[1], c);
	}

}

public void matrixLinePolygon(DoubleMatrix m, Pixel c){
	double[][] array = m.getarray();
	for (int x = 2; x < array.length; x+=3) {
		double[] p0 = array[x];
		double[] p1 = array[x-1];
		double[] p2 = array[x-2];

		line(p0[0], p0[1], p1[0], p1[1], c);
		line(p1[0], p1[1], p2[0], p2[1], c);
		line(p2[0], p2[1], p0[0], p0[1], c);
	}

}



public static void main(String[] args) {

	Image i = new Image(200, 200, new Pixel(100, 100, 0));
	DoubleMatrix edges = new DoubleMatrix();
	DoubleMatrix polygons = new DoubleMatrix();
	edges.addedge(10, 10, 0, 70, 150, 0);
	System.out.println(edges);
	polygons.addpolygon(20, 20, 0, 50, 50, 0, 80, 20, 0);
	i.matrixLineEdge(edges, new Pixel(255, 255, 255));
	i.matrixLinePolygon(polygons, new Pixel(8, 146, 208));
	i.display();
}


}
