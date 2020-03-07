import java.lang.Math;
public class TransformGenerator{


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

public static DoubleMatrix rotate(char axis, double thetadeg){

        double theta = thetadeg *0.01745329252;

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


}