import java.util.ArrayList;
import java.util.Arrays;


/**
 * Stores a Matrix
 */
public class Matrix {



private ArrayList<Integer[]> matrix;
private int cols;
private int rows = 4;

/**
 * Initializes a new Matrix with 0 columns
 */
public Matrix(){
        this.matrix = new ArrayList<Integer[]>();
        this.cols = 0;
}

/**
 * Initializes a new Matrix with an input 2D array
 * @param in 2D array to be inputted
 */
public Matrix(int[][] in){
        this.matrix = new ArrayList<Integer[]>();

        for (int x = 0; x < in.length; x++) {
                Integer[] toadd = new Integer[4];
                for (int y = 0; y < in[x].length; y++) {
                        toadd[y] = in[x][y];
                }
                matrix.add(toadd);
        }
        this.cols = in.length;
}


/**
 * returns how many columns (points) are in the matrix
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
public void addedge(int x, int y, int z){
        Integer[] toadd = new Integer[4];
        toadd[0] = x;
        toadd[1] = y;
        toadd[2] = z;
        toadd[3] = 1;
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
                        Integer[] m = matrix.get(x);
                        out = out + m[y];
                        out = out + ", ";
                }
                out = out + "\n";
        }

        return out;
}

/**
 * generates a 4 by 4 identity Matrix
 * @return identity matrix
 */
public static Matrix identity(){
        int[][] out = new int[4][4];
        for (int x = 0; x < 4; x++) {
                for (int y = 0; y < 4; y++) {
                        if (x == y) {
                                out[x][y] = 1;
                        }
                }
        }

        return new Matrix(out);
}

/**
 * converts the internal array for the matrix into an int[][]
 * @return the internal array as an int[][]
 */
public int[][] getarray(){
        int[][] out = new int[this.cols][this.rows];
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
 * @return                      returns a new Matrix object with the result
 * @throws MatrixDimensionError mismatching dimensions for a and b
 */
public static Matrix multiply (Matrix a, Matrix b) throws MatrixDimensionError {
        int[][] out = new int[b.getcols()][a.getrows()];
        int[][] ar = a.getarray();
        int[][] br = b.getarray();
        if (a.getcols() != b.getrows()){
                throw new MatrixDimensionError("mooos");
        }
        for (int x = 0; x < b.getcols(); x++) {
                for (int y = 0; y < a.getrows(); y++) {
                        int cumsum = 0;
                        int[] ber = br[x];
                        ArrayList<Integer> aer= new ArrayList<Integer>();
                        for (int xer = 0; xer < ar.length; xer++) {
                                aer.add(ar[xer][y]);
                        }
                        ArrayList<Integer> toprintber = new ArrayList<Integer>();
                        for (int d = 0; d < ber.length; d++) {
                                toprintber.add(ber[d]);
                        }

                        // System.out.println("looking at: " + x + ", " + y);
                        // System.out.println(aer);
                        // System.out.println(toprintber);
                        // System.out.println();
                        // System.out.println();

                        for (int g = 0; g < ber.length; g++) {
                                cumsum += aer.get(g)*ber[g];
                        }

                        out[x][y] = cumsum;

                }
        }

        // System.out.println(new Matrix(out));
        return new Matrix(out);
}

/**
 * Flushes the current matrix into an image file
 * @return new Image with the drawn matrix
 */
public Image flushToImage(){
        Image out = new Image(20, 20, new Pixel(200, 200, 200));

        Pixel c = new Pixel(0, 0, 0);

        for (int x = 1; x < matrix.size(); x++) {
                Integer[] p1 = matrix.get(x);
                Integer[] p2 = matrix.get(x-1);

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
        System.out.println("expected result:\n5, 7, 1, 4,\n6, 8, 2, 6,\n7, 9, 3, 9,\n1, 1, 1, 1,");


        System.out.println("let's test the drawing functionality");
        
        //
        // Image m = new Image(250, 250, new Pixel(200, 200, 200));
        // Matrix mx = new Matrix();
        // mx.addedge(1, 1, 0);
        // mx.addedge(25, 25, 0);
        // mx.addedge(25, 50, 0);
        // m.matrixLine(mx, new Pixel(0, 0, 0));
        //
        // m.flushToFile("d.ppm");
        // System.out.println(m);

}

}

class MatrixDimensionError extends RuntimeException{
        private static final long serialVersionUID = 1L;
        /**
         * Matrix Dimensions Error
         * @param error error message
         */
        public MatrixDimensionError(String error){
                super(error);
        }
}
