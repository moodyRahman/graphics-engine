package src;
/**
 * Holds are the methods to add shapes to an image\
 */
public class EdgeGenerator {

	/**
	 * adds the edges of a circle to the edge matrix parameter
	 * @param edge edge matrix to add the sdgex to
	 * @param cx center x
	 * @param cy center y
	 * @param cz center z
	 * @param radius radius
	 */
	public static void circle(DoubleMatrix edge, double cx, double cy, double cz, double radius) {
		for (double deg = 0; deg < 1; deg += .02) {
			double rad = deg * 6.2831;
			edge.addedge((radius * Math.cos(rad)) + cx, (radius * Math.sin(rad)) + cy, cz,
					(radius * Math.cos(rad + 0.174)) + cx, (radius * Math.sin(rad + 0.174)) + cy,
					cz);
		}
	}


	/**
	 * generates a hermite curve and adds the edges
	 * @param edge edge matrix to add the edges to
	 * @param hx0
	 * @param hy0
	 * @param hx1
	 * @param hy1
	 * @param hrx0
	 * @param hry0
	 * @param hrx1
	 * @param hry1
	 */
	public static void hermite(DoubleMatrix edge, double hx0, double hy0, double hx1, double hy1, double hrx0,
			double hry0, double hrx1, double hry1) {
		double xa = 2 * hx0 - 2 * hx1 + hrx0 + hrx1;
		double xb = -3 * hx0 + 3 * hx1 - 2 * hrx0 - hrx1;
		double xc = hrx0;
		double xd = hx0;

		double ya = 2 * hy0 - 2 * hy1 + hry0 + hry1;
		double yb = -3 * hy0 + 3 * hy1 - 2 * hry0 - hry1;
		double yc = hry0;
		double yd = hy0;

		double plotx, ploty, newplotx, newploty, dt;

		for (double t = 0; t < 1; t += .02) {
			plotx = xa * t * t * t + xb * t * t + xc * t + xd;
			ploty = ya * t * t * t + yb * t * t + yc * t + yd;

			dt = t + .02;

			newplotx = xa * dt * dt * dt + xb * dt * dt + xc * dt + xd;
			newploty = ya * dt * dt * dt + yb * dt * dt + yc * dt + yd;

			edge.addedge(plotx, ploty, 1, newplotx, newploty, 1);
		}
	}

	/**
	 * generates a bezier curves and adds to edge matrix
	 * @param edge edge matrix to add bezier edges to
	 * @param bx0
	 * @param by0
	 * @param bx1
	 * @param by1
	 * @param bx2
	 * @param by2
	 * @param bx3
	 * @param by3
	 */
	public static void bezier(DoubleMatrix edge, double bx0, double by0, double bx1, double by1, double bx2,
			double by2, double bx3, double by3) {
		double bxa = -bx0 + 3 * bx1 - 3 * bx2 + bx3;
		double bxb = 3 * bx0 - 6 * bx1 + 3 * bx2;
		double bxc = -3 * bx0 + 3 * bx1;
		double bxd = bx0;

		double bya = -by0 + 3 * by1 - 3 * by2 + by3;
		double byb = 3 * by0 - 6 * by1 + 3 * by2;
		double byc = -3 * by0 + 3 * by1;
		double byd = by0;

		double plotx, ploty, newplotx, newploty, dt;

		for (double t = 0; t < 1; t += .02) {
			plotx = bxa * t * t * t + bxb * t * t + bxc * t + bxd;
			ploty = bya * t * t * t + byb * t * t + byc * t + byd;

			dt = t + .02;

			newplotx = bxa * dt * dt * dt + bxb * dt * dt + bxc * dt + bxd;
			newploty = bya * dt * dt * dt + byb * dt * dt + byc * dt + byd;

			edge.addedge(plotx, ploty, 1, newplotx, newploty, 1);
		}
	}

	public static void sphere(DoubleMatrix edge, double x, double y, double z, double radius) {
		DoubleMatrix spherepoints = new DoubleMatrix();

		for (int i = 0; i < 180; i+=10) {
			EdgeGenerator.circle(spherepoints, x, y, z, radius);
			DoubleMatrix rotate = TransformGenerator.rotatepoint(x, y, z, 'x', i * 0.0174);
			spherepoints = DoubleMatrix.multiply(rotate, spherepoints);
		}

		edge.addmatrixedge(spherepoints);

		

	}

}