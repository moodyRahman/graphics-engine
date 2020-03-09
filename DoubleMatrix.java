import java.util.ArrayList;

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
public void addpoint(double x, double y, double z){
        Double[] toadd = new Double[4];
        toadd[0] = x;
        toadd[1] = y;
        toadd[2] = z;
        toadd[3] = 1.0;
        this.matrix.add(toadd);
        this.cols++;
}



public void addedge(double x0, double y0, double z0, double x1, double y1, double z1){
        this.addpoint(x0, y0, z0);
        this.addpoint(x1, y1, z1);
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
public Image flushToImage(int width, int height, Pixel init, Pixel linecolor){
        Image out = new Image(width, height, init);

        // Pixel c = new Pixel(0, 0, 0);

        // System.out.println("HERE");
        System.out.println(matrix.size());
        for (int x = 0; x < matrix.size(); x+=2) {
                // System.out.println(x);
                Double[] p1 = matrix.get(x);
                Double[] p2 = matrix.get(x+1);
                
                out.line(p1[0], p1[1], p2[0], p2[1], linecolor);
        }

        return out;
}


public static void main(String[] args) {


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
