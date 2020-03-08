
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.lang.Math;

public class Parser {

	class Command {
		private String command;
		private String arg;

		public Command(String command, String args) {
			this.command = command;
			this.arg = args;
		}

		public Command(String command) {
			this.command = command;
		}

		public String toString() {
			String out = "";
			out += this.command;
			if (this.arg != null) {
				out += "=>";
				out += this.arg;
			}
			return out;
		}

		public String getCommand() {
			return this.command;
		}

		public String getParameters() {
			return this.arg;
		}

	}

	private DoubleMatrix edge = new DoubleMatrix();
	private DoubleMatrix transform = TransformGenerator.identity();
	private ArrayList<Command> tokens = new ArrayList<Command>();
	ProcessBuilder processBuilder = new ProcessBuilder();

	/**
	 * makes a new Parser
	 * 
	 * @param fname the name of the file to parse
	 * @throws FileNotFoundException
	 */
	public Parser(String fname) throws FileNotFoundException {
		File f = new File(fname);
		Scanner sc = new Scanner(f);
		sc.useDelimiter("\n");
		ArrayList<String> stokens = new ArrayList<String>();
		while (sc.hasNextLine()) {
			try {
				String data = sc.nextLine();
				stokens.add(data);

			} catch (Exception e) {

			}
		}
		sc.close();
		// // System.out.println(stokens);

		for (int x = 0; x < stokens.size(); x++) {
			String command = stokens.get(x);
			if (command.equals("line") || command.equals("rotate") || command.equals("scale")
					|| command.equals("move") || command.equals("save") || command.equals("circle")
					|| command.equals("hermite") || command.equals("bezier")
					|| command.equals("save-convert")) {
				x++;
				String arg = stokens.get(x);
				tokens.add(new Command(command, arg));
			} else {
				tokens.add(new Command(command));
			}
		}

		// // System.out.println(tokens);

	}

	/**
	 * @param args String of int parameters
	 * @return int[] of parameters
	 */
	public double[] argstoarray(String args) {
		String[] arr = args.split(" ");
		double[] out = new double[arr.length];
		for (int x = 0; x < arr.length; x++) {
			if (arr[x].charAt(0) == 'z' || arr[x].charAt(0) == 'x' || arr[x].charAt(0) == 'y') {
				out[x] = (arr[x].charAt(0));
			} else {
				out[x] = Double.parseDouble(arr[x]);
			}
		}

		return out;
	}

	/**
	 * processes the tokens in the input script executes the commands
	 * 
	 * @throws IOException
	 */
	public void parse() throws IOException {
		for (int x = 0; x < tokens.size(); x++) {
			Command currtoken = tokens.get(x);
			System.out.println(currtoken);
			String c = currtoken.getCommand();
			double[] params;
			double plotx, ploty, newplotx, newploty, dt;
			switch (c) {
				case "line":
					params = argstoarray(currtoken.getParameters());
					double px1 = params[0], py1 = params[1], pz1 = params[2];
					double px2 = params[3], py2 = params[4], pz2 = params[5];
					edge.addedge(px1, py1, pz1, px2, py2, pz2);
					break;
				case "ident":
					this.transform = TransformGenerator.identity();
					break;
				case "scale":
					params = argstoarray(currtoken.getParameters());
					double scalex = params[0], scaley = params[1], scalez = params[2];
					DoubleMatrix scale = TransformGenerator.scale(scalex, scaley, scalez);
					this.transform = DoubleMatrix.multiply(scale, this.transform);
					break;
				case "move":
					params = argstoarray(currtoken.getParameters());
					double movex = params[0], movey = params[1], movez = params[2];
					DoubleMatrix move = TransformGenerator.translate(movex, movey, movez);
					this.transform = DoubleMatrix.multiply(move, this.transform);
					break;
				case "rotate":
					params = argstoarray(currtoken.getParameters());
					double axis = params[0], theta = params[1];
					DoubleMatrix rotate = TransformGenerator.rotate((char) axis, theta);
					this.transform = DoubleMatrix.multiply(rotate, this.transform);
					break;
				case "apply":
					this.edge = DoubleMatrix.multiply(this.transform, this.edge);
					break;
				case "display":
					Image i = this.edge.flushToImage(500, 500, new Pixel(200, 200, 200));
					i.flushToFile("d.ppm");
					Runtime.getRuntime().exec("convert d.ppm d.png");
					try {
						Thread.sleep(500);
					} catch (Exception e) {
					}
					Picture p = new Picture("d.png");
					p.show();
					break;
				case "save":
					String outfile = currtoken.getParameters();
					i = this.edge.flushToImage(500, 500, new Pixel(200, 200, 200));
					i.flushToFile(outfile);
					break;
				case "save-convert":
					String param = currtoken.getParameters();
					String[] arr = param.split(" ");
					i = this.edge.flushToImage(Integer.parseInt(arr[1]), Integer.parseInt(arr[2]),
							new Pixel(200, 200, 200));
					i.flushToFile(arr[0]);
					String outfilepng = arr[0].substring(0, arr[0].length() - 3) + "png";
					String convertcommand = "convert " + arr[0] + " " + outfilepng;
					Runtime.getRuntime().exec(convertcommand);
					// System.out.println(outfilepng);
				case "circle":

					params = argstoarray(currtoken.getParameters());

					double cx = params[0], cy = params[1], cz = params[2], radius = params[3];

					// double oldcx = cx + radius, oldcy = cy;

					for (double deg = 0; deg < 1; deg += .02) {
						double rad = deg * 6.2831;
						edge.addedge((radius * Math.cos(rad)) + cx,
								(radius * Math.sin(rad)) + cy, cz,
								(radius * Math.cos(rad + 0.174)) + cx,
								(radius * Math.sin(rad + 0.174)) + cy, cz);
					}
					// System.out.println(this.edge);

					break;
				case "hermite":
					params = argstoarray(currtoken.getParameters());
					// x0, y0, x1, y1, rx0, ry0, rx1, ry1
					// 0 1 2 3 4 5 6 7
					double hx0 = params[0], hy0 = params[1], hx1 = params[2], hy1 = params[3];
					double hrx0 = params[4], hry0 = params[5], hrx1 = params[6], hry1 = params[6];

					double xa = 2 * hx0 - 2 * hx1 + hrx0 + hrx1;
					double xb = -3 * hx0 + 3 * hx1 - 2 * hrx0 - hrx1;
					double xc = hrx0;
					double xd = hx0;

					double ya = 2 * hy0 - 2 * hy1 + hry0 + hry1;
					double yb = -3 * hy0 + 3 * hy1 - 2 * hry0 - hry1;
					double yc = hry0;
					double yd = hy0;

					for (double t = 0; t < 1; t += .02) {
						plotx = xa * t * t * t + xb * t * t + xc * t + xd;
						ploty = ya * t * t * t + yb * t * t + yc * t + yd;

						dt = t + .02;

						newplotx = xa * dt * dt * dt + xb * dt * dt + xc * dt + xd;
						newploty = ya * dt * dt * dt + yb * dt * dt + yc * dt + yd;

						edge.addedge(plotx, ploty, 1, newplotx, newploty, 1);

					}

					break;
				case "bezier":
					params = argstoarray(currtoken.getParameters());
					// x0, y0, x1, y1, x2, y2, x3, y3
					// 0 1 2 3 4 5 6 7
					double bx0 = params[0], bx1 = params[2], bx2 = params[4], bx3 = params[6];
					double by0 = params[1], by1 = params[3], by2 = params[5], by3 = params[7];

					double bxa = -bx0 + 3 * bx1 - 3 * bx2 + bx3;
					double bxb = 3 * bx0 - 6 * bx1 + 3 * bx2;
					double bxc = -3 * bx0 + 3 * bx1;
					double bxd = bx0;

					double bya = -by0 + 3 * by1 - 3 * by2 + by3;
					double byb = 3 * by0 - 6 * by1 + 3 * by2;
					double byc = -3 * by0 + 3 * by1;
					double byd = by0;

					for (double t = 0; t < 1; t += .02) {
						plotx = bxa * t * t * t + bxb * t * t + bxc * t + bxd;
						ploty = bya * t * t * t + byb * t * t + byc * t + byd;

						dt = t + .02;

						newplotx = bxa * dt * dt * dt + bxb * dt * dt + bxc * dt + bxd;
						newploty = bya * dt * dt * dt + byb * dt * dt + byc * dt + byd;

						edge.addedge(plotx, ploty, 1, newplotx, newploty, 1);
					}

					break;
			}
		}

		// System.out.println(this.transform);
		// System.out.println(this.edge);
		// Image i = this.edge.flushToImage(150, 150, new Pixel(200, 200, 200));
		// i.flushToFile("d.ppm");
		// Runtime.getRuntime().exec("rm d.ppm");
	}

	public static void main(String[] args) throws FileNotFoundException {
		Parser p = new Parser("script.txt");

		try {
			p.parse();
		} catch (Exception e) {
		}

	}

}
