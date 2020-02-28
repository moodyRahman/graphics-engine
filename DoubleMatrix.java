import java.util.ArrayList;
import java.util.Arrays;
import java.lang.Math;

/**
 * Stores a DoubleMatrix
 */
public class DoubleMatrix {



private ArrayList<Double[]> matrix;
private int cols;
private int rows = 4;

/**
 * Initializes a new DoubleMatrix with 0 columns
 */
public DoubleMatrix(){
        this.matrix = new ArrayList<Double[]>();
        this.cols = 0;
}

/**
 * Initializes a new DoubleMatrix with an input 2D array
 * @param in 2D array to be inputted
 */
public DoubleMatrix(double[][] in){
        this.matrix = new ArrayList<Double[]>();

        for (int x = 0; x < in.length; x++) {
                Double[] toadd = new Double[4];
                for (int y = 0; y < in[x].length; y++) {
                        toadd[y] = in[x][y];
                }
                matrix.add(toadd);
        }
        this.cols = in.length;
}


/**
 * returns how many columns (podoubles) are in the matrix
 * @return how many columsn there are
 */
public int getcols(){
        return this.cols;
}

/**
 * returns how many rows there are in the matrix
 * @return how many rows there are
 */
public int getrows(){
        return this.rows;
}


/**
 * Adds a new column (edge) to the matrix
 * @param x x coordinate
 * @param y y coordinate
 * @param z z coordinate
 */
public void addedge(double x, double y, double z){
        Double[] toadd = new Double[4];
        toadd[0] = x;
        toadd[1] = y;
        toadd[2] = z;
        toadd[3] = 1.0;
        this.matrix.add(toadd);
        this.cols++;
}

/**
 * Graphical representation of the matrix
 * @return returns a String of the matrix, formatted nicely
 */
public String toString(){
        String out = "";
        for (int y = 0; y < 4; y++) {
                for (int x = 0; x < matrix.size(); x++) {
                        Double[] m = matrix.get(x);
                        out = out + m[y];
                        out = out + ", ";
                }
                out = out + "\n";
        }

        return out;
}

/**
 * generates a 4 by 4 identity DoubleMatrix
 * @return identity matrix
 */
public static DoubleMatrix identity(){
        double[][] out = new double[4][4];
        for (int x = 0; x < 4; x++) {
                for (int y = 0; y < 4; y++) {
                        if (x == y) {
                                out[x][y] = 1;
                        }
                }
        }

        return new DoubleMatrix(out);
}


public static DoubleMatrix scale(double sx, double sy, double sz){
        double[][] out = { {sx, 0, 0, 0},
                           {0, sy, 0, 0},
                           {0, 0, sz, 0},
                           {0, 0, 0,  1}};

        return new DoubleMatrix(out);
}

public static DoubleMatrix translate(double dx, double dy, double dz){
        double[][] out = { {1, 0, 0, 0},
                           {0, 1, 0, 0},
                           {0, 0, 1, 0},
                           {dx, dy, dz,  1}};
        return new DoubleMatrix(out);
}

public static DoubleMatrix rotate(char axis, double theta){

        switch (axis) {
        case 'z':
                double[][] outz = {
                        { Math.cos(theta),      Math.sin(theta), 0, 0},
                        { -1 * Math.sin(theta), Math.cos(theta), 0, 0},
                        { 0,                    0,               1, 0},
                        { 0,                    0,               0, 1}
                };
                return new DoubleMatrix(outz);
        case 'y':
                double[][] outy = {
                        {Math.cos(theta),   0, -1 * Math.sin(theta), 0},
                        {0,                 1,                    0, 0},
                        {Math.sin(theta),   0,      Math.cos(theta), 0},
                        {0,                 0,                    0, 1}
                };
                return new DoubleMatrix(outy);
        case 'x':
                double[][] outx = {
                        {1,                    0,               0, 0},
                        {0,      Math.cos(theta), Math.sin(theta), 0},
                        {0, -1 * Math.sin(theta), Math.cos(theta), 0},
                        {0,                    0,               0,  1}
                };
                return new DoubleMatrix(outx);
        }

        return new DoubleMatrix();
}


/**
 * converts the doubleernal array for the matrix doubleo an double[][]
 * @return the doubleernal array as an double[][]
 */
public double[][] getarray(){
        double[][] out = new double[this.cols][this.rows];
        for (int x = 0; x < this.cols; x++) {
                for (int y = 0; y < this.rows; y++) {
                        out[x][y] = this.matrix.get(x)[y];
                }
        }
        return out;
}


/**
 * multiplies two matrices
 * @param  a                    matrix a
 * @param  b                    matrix b
 * @return                      returns a new DoubleMatrix object with the result
 * @throws DoubleMatrixDimensionError mismatching dimensions for a and b
 */
public static DoubleMatrix multiply (DoubleMatrix a, DoubleMatrix b) throws DoubleMatrixDimensionError {
        double[][] out = new double[b.getcols()][a.getrows()];
        double[][] ar = a.getarray();
        double[][] br = b.getarray();
        if (a.getcols() != b.getrows()) {
                throw new DoubleMatrixDimensionError("mooos");
        }
        for (int x = 0; x < b.getcols(); x++) {
                for (int y = 0; y < a.getrows(); y++) {
                        double cumsum = 0;
                        double[] ber = br[x];
                        ArrayList<Double> aer= new ArrayList<Double>();
                        for (int xer = 0; xer < ar.length; xer++) {
                                aer.add(ar[xer][y]);
                        }
                        ArrayList<Double> toprdoubleber = new ArrayList<Double>();
                        for (int d = 0; d < ber.length; d++) {
                                toprdoubleber.add(ber[d]);
                        }

                        // System.out.prdoubleln("looking at: " + x + ", " + y);
                        // System.out.prdoubleln(aer);
                        // System.out.prdoubleln(toprdoubleber);
                        // System.out.prdoubleln();
                        // System.out.prdoubleln();

                        for (int g = 0; g < ber.length; g++) {
                                cumsum += aer.get(g)*ber[g];
                        }

                        out[x][y] = cumsum;

                }
        }

        // System.out.prdoubleln(new DoubleMatrix(out));
        return new DoubleMatrix(out);
}

/**
 * Flushes the current matrix into an image file
 * @return new Image with the drawn matrix
 */
public Image flushToImage(int width, int height, Pixel init){
        Image out = new Image(width, height, init);

        Pixel c = new Pixel(0, 0, 0);

        for (int x = 1; x < matrix.size(); x++) {
                Double[] p1 = matrix.get(x);
                Double[] p2 = matrix.get(x-1);

                out.line(p1[0], p1[1], p2[0], p2[1], c);
        }

        return out;
}


public static void main(String[] args) {
        Matrix i = Matrix.identity();
        Matrix a = new Matrix();
        a.addedge(5, 6, 7);
        a.addedge(7, 8, 9);
        a.addedge(1, 2, 3);
        a.addedge(4, 6, 9);

        Matrix b = new Matrix();
        b.addedge(5, 6, 7);
        b.addedge(8, 2, 9);
        b.addedge(0, 4, 6);
        b.addedge(3, 3, 3);


        System.out.println("\n----------------\nenter, the moodtrix\n----------------\n\n");
        System.out.println("here be matrix a\n" + a);
        System.out.println("here be matrix b\n" + b);
        System.out.println("here be thy identity matrix\n" + i);
        System.out.println();
        System.out.println("multiply a and b gives us:\n" + Matrix.multiply(a, b));
        System.out.println("expected result:\n78 67 38 43\n98 88 50 54\n119 110 63 66\n19 20 11 10\n");
        System.out.println();
        System.out.println("multiply a and identity gives us:\n" + Matrix.multiply(a, i));
        System.out.println("expected result:\n5, 7, 1, 4,\n6, 8, 2, 6,\n7, 9, 3, 9,\n1, 1, 1, 1,");


        System.out.println("let's test the drawing functionality");

}

}

class DoubleMatrixDimensionError extends RuntimeException {
private static final long serialVersionUID = 1L;
/**
 * DoubleMatrix Dimensions Error
 * @param error error message
 */
public DoubleMatrixDimensionError(String error){
        super(error);
}
}
