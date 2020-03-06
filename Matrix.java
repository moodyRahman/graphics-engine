import java.util.ArrayList;

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
        if (a.getcols() != b.getrows()) {
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

        Matrix s = Generator.translate(5, 6, 7);
        System.out.println(s);

}

}

class MatrixDimensionError extends RuntimeException {
private static final long serialVersionUID = 1L;
/**
 * Matrix Dimensions Error
 * @param error error message
 */
public MatrixDimensionError(String error){
        super(error);
}
}
