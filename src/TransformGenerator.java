package src;

import java.lang.Math;

/**
 * Stores all the static methods used to generate the transformational matrices
 */
public class TransformGenerator{


/**
 * generates and returns a 4 by 4 identity DoubleMatrix
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


/**
 * generates and returns a scaling matrix
 * @param sx scale x
 * @param sy scale y
 * @param sz scale z
 * @return DoubleMatrix
 */
public static DoubleMatrix scale(double sx, double sy, double sz){
	double[][] out = { {sx, 0, 0, 0},
			   {0, sy, 0, 0},
			   {0, 0, sz, 0},
			   {0, 0, 0,  1}};

	return new DoubleMatrix(out);
}

/**
 * generates and returns a translating matrix
 * @param dx move by x pixels
 * @param dy move by y pixels
 * @param dz move by z pixels
 * @return DoubleMatrix
 */
public static DoubleMatrix translate(double dx, double dy, double dz){
	double[][] out = { {1, 0, 0, 0},
			   {0, 1, 0, 0},
			   {0, 0, 1, 0},
			   {dx, dy, dz,  1}};
	return new DoubleMatrix(out);
}

/**
 * generate and return a rotational matrix
 * @param axis char of the axis to rotate about, 'x', 'y', 'z'
 * @param thetadeg degrees to rotate by
 * @return DoubleMatrix
 */
public static DoubleMatrix rotate(char axis, double thetadeg){

	double theta = thetadeg *0.0174;

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