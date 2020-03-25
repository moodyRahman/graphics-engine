package src;

/**
 * Holds all the methods to add shapes to an image
 */
public class EdgeGenerator {

	/**
	 * adds the edges of a circle to the edge matrix parameter
	 * 
	 * @param edge   edge matrix to add the sdgex to
	 * @param cx     center x
	 * @param cy     center y
	 * @param cz     center z
	 * @param radius radius
	 */
	public static void circle(DoubleMatrix edge, double cx, double cy, double cz, double radius, int total_steps) {
		for (int step = 0; step < total_steps; step++) {
			double deg = (double)step/(double)total_steps;
			double rad = deg * 6.2831;
			edge.addedge((radius * Math.cos(rad)) + cx, (radius * Math.sin(rad)) + cy, cz,
					(radius * Math.cos(rad + 0.174)) + cx, (radius * Math.sin(rad + 0.174)) + cy,
					cz);
		}
	}

	/**
	 * generates a hermite curve and adds the edges
	 * 
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
			double hry0, double hrx1, double hry1, int total_step) {
		double xa = 2 * hx0 - 2 * hx1 + hrx0 + hrx1;
		double xb = -3 * hx0 + 3 * hx1 - 2 * hrx0 - hrx1;
		double xc = hrx0;
		double xd = hx0;

		double ya = 2 * hy0 - 2 * hy1 + hry0 + hry1;
		double yb = -3 * hy0 + 3 * hy1 - 2 * hry0 - hry1;
		double yc = hry0;
		double yd = hy0;

		double plotx, ploty, newplotx, newploty, dt;
		
		for (int step = 0; step < total_step; step++) {
			double t = (double)step / (double)total_step;
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
	 * 
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
			double by2, double bx3, double by3, int total_step) {
		double bxa = -bx0 + 3 * bx1 - 3 * bx2 + bx3;
		double bxb = 3 * bx0 - 6 * bx1 + 3 * bx2;
		double bxc = -3 * bx0 + 3 * bx1;
		double bxd = bx0;

		double bya = -by0 + 3 * by1 - 3 * by2 + by3;
		double byb = 3 * by0 - 6 * by1 + 3 * by2;
		double byc = -3 * by0 + 3 * by1;
		double byd = by0;

		double plotx, ploty, newplotx, newploty, dt;

		
		for (int step = 0; step < total_step; step++) {
			double t = (double)step/(double)total_step;
			plotx = bxa * t * t * t + bxb * t * t + bxc * t + bxd;
			ploty = bya * t * t * t + byb * t * t + byc * t + byd;

			dt = t + .02;

			newplotx = bxa * dt * dt * dt + bxb * dt * dt + bxc * dt + bxd;
			newploty = bya * dt * dt * dt + byb * dt * dt + byc * dt + byd;

			edge.addedge(plotx, ploty, 1, newplotx, newploty, 1);
		}
	}

	/**
	 * Generates a DoubleMatrix containing a sphere
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param radius
	 * @return DoubleMatrix containing a sphere
	 */
	public static DoubleMatrix sphereGenerator(double x, double y, double z, double radius, int total_step_circle,
			int total_step_rotation) {
		DoubleMatrix out = new DoubleMatrix();
		ArrayList<Point> spherepoints = new ArrayList<Point>(
				(total_step_circle + 1) * (total_step_rotation + 1));
		System.out.println(spherepoints.size());
		int pointidcounter = 0;

		for (int step_circle = 0; step_circle <= total_step_circle; step_circle++) {
			double theta = ((double) step_circle / (double) total_step_circle) * 3.141 * -1;

			for (int step_rotation = 0; step_rotation < total_step_rotation; step_rotation++) {
				double cir = (double) step_rotation / (double) total_step_rotation;
				double picir = cir * 3.141 * 2;
				double xc = radius * Math.cos(picir) + x;
				double yc = radius * Math.sin(picir) * Math.cos(theta) + y;
				double zc = radius * Math.sin(picir) * Math.sin(theta) + z;
				spherepoints.add(new Point(xc, yc, zc, pointidcounter));
				pointidcounter++;
			}
			}
		System.out.println(spherepoints.size());

		for (int i = 0; i < spherepoints.size() - total_step_rotation - 1; i++) {
			out.addpoint(spherepoints.get(i));
			out.addpoint(spherepoints.get(i + 1));
			out.addpoint(spherepoints.get(i + 1 + total_step_rotation));
		}
		return out;

	}

	/**
	 * Wrapper method to seperate adding a sphere to the current edge matrix and
	 * generating the sphere Generates a torus and adds it to the edge matrix
	 * 
	 * @param edge
	 * @param x
	 * @param y
	 * @param z
	 * @param radius
	 */
	public static void sphere(DoubleMatrix edge, double x, double y, double z, double radius) {
		edge.addmatrixedge(EdgeGenerator.sphereGenerator(x, y, z, radius, 50, 50));
		// slices points per slice
	}

	/**
	 * Adds a box to the edge matrix
	 * 
	 * @param edge
	 * @param x
	 * @param y
	 * @param z
	 * @param width
	 * @param height
	 * @param depth
	 */
	public static void box(DoubleMatrix edge, double x, double y, double z, double width, double height,
			double depth) {
		// Note: Yes, I could possible make this into a few for-loops but allow me to
		// ask you, why?
		// It's dead just 12 edges, no point in debugging when this is garunteed to work
		// So, me in the future reading this
		// Don't you dare try to change anything

		edge.addpoint(x, y, z);
		edge.addpoint(x + width, y, z);

		edge.addpoint(x + width, y, z);
		edge.addpoint(x + width, y, z - depth);
		// draw the top face
		edge.addpoint(x + width, y, z - depth);
		edge.addpoint(x, y, z - depth);

		edge.addpoint(x, y, z - depth);
		edge.addpoint(x, y, z);
		// ----------------------------------------------------------------------- |
		edge.addpoint(x, y - height, z);
		edge.addpoint(x + width, y - height, z);

		edge.addpoint(x + width, y - height, z);
		edge.addpoint(x + width, y - height, z - depth);
		// draw the bottom face
		edge.addpoint(x + width, y - height, z - depth);
		edge.addpoint(x, y - height, z - depth);

		edge.addpoint(x, y - height, z - depth);
		edge.addpoint(x, y - height, z);
		// ----------------------------------------------------------------------- |
		edge.addpoint(x, y, z);
		edge.addpoint(x, y - height, z);

		edge.addpoint(x + width, y, z);
		edge.addpoint(x + width, y - height, z);
		// draw the height
		edge.addpoint(x + width, y, z - depth);
		edge.addpoint(x + width, y - height, z - depth);

		edge.addpoint(x, y, z - depth);
		edge.addpoint(x, y - height, z - depth);
		// ----------------------------------------------------------------------- |

	}

	/**
	 * generates a DoubleMatrix containing a torus
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param inner_rad
	 * @param outer_rad
	 * @return
	 */
	public static DoubleMatrix torusGenerator(double x, double y, double z, double inner_rad, double outer_rad, int total_step_translate, int total_step_circle) {
		DoubleMatrix out = new DoubleMatrix();
		for (int step_circle = 0; step_circle < total_step_circle; step_circle++) {
			// double deg = (double)step_circle/(double)total_step_circle
			double theta = ((double)step_circle/(double)total_step_circle) * 6.2831;
			// double theta = deg * 1;

			for (int step_translate = 0; step_translate < total_step_translate; step_translate++) {
				double picir = ((double)step_translate/(double)total_step_translate) * 6.2831;
				double tx = (outer_rad + inner_rad * Math.cos(picir)) * Math.cos(theta) + x;
				double ty = inner_rad * Math.sin(picir) + y;
				double tz = (outer_rad + inner_rad * Math.cos(picir)) * Math.sin(theta) + z;

				out.addpoint(tx, ty, tz);
				out.addpoint(tx + 1, ty, tz);
			}
		}
		return out;
	}

	/**
	 * Wrapper method to seperate adding a torus to the current edge matrix and
	 * generating the torus Generates a torus and adds it to the edge matrix
	 * 
	 * @param edge      edge matrix to add to
	 * @param x
	 * @param y
	 * @param z
	 * @param inner_rad
	 * @param outer_rad
	 */
	public static void torus(DoubleMatrix edge, double x, double y, double z, double inner_rad, double outer_rad) {
		edge.addmatrixedge(EdgeGenerator.torusGenerator(x, y, z, inner_rad, outer_rad, 50, 50));
	}

}