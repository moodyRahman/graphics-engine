package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Parses an input file, in accordance to either GraphicsScript or it's superset: MoodScript
 */
public class Parser {
	
	/**
	 * Stores a command and paramaters
	 */
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
	Pixel linecolor = new Pixel(0, 0, 0);
	Pixel bgcolor = new Pixel(200, 200, 200);

	/**
	 * makes a new Parser and processes all of it's tokens
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

		for (int x = 0; x < stokens.size(); x++) {
			String command = stokens.get(x);
			if (command.equals("line") || command.equals("rotate") || command.equals("scale")
					|| command.equals("move") || command.equals("save") || command.equals("circle")
					|| command.equals("hermite") || command.equals("bezier")
					|| command.equals("save-convert") || command.equals("pen-color")
					|| command.equals("bg-color") || command.equals("sphere") || command.equals("rotatepoint")) {
				x++;
				String arg = stokens.get(x);
				tokens.add(new Command(command, arg));
			} else {
				tokens.add(new Command(command));
			}
		}
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
		Scanner scanner = new Scanner(System.in);
		for (int x = 0; x < tokens.size(); x++) {
			Command currtoken = tokens.get(x);
			System.out.println(currtoken);
			String c = currtoken.getCommand();
			double[] params;
			Runtime.getRuntime().exec("mkdir tmp");
			Runtime.getRuntime().exec("mkdir pics");
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
				case "rotatepoint":
					params = argstoarray(currtoken.getParameters());
					double xrp = params[0], yrp = params[1], zrp = params[2], axisrp = params[3], thetarp = params[4];
					DoubleMatrix rotatepoint = TransformGenerator.rotatepoint(xrp, yrp, zrp, (char)axisrp, thetarp);
					this.transform = DoubleMatrix.multiply(rotatepoint, this.transform);

				case "apply":
					this.edge = DoubleMatrix.multiply(this.transform, this.edge);
					break;

				case "pen-color":
					params = argstoarray(currtoken.getParameters());
					linecolor.set(new Pixel((int) params[0], (int) params[1], (int) params[2]));
					break;

				case "bg-color":
					params = argstoarray(currtoken.getParameters());
					bgcolor.set(new Pixel((int) params[0], (int) params[1], (int) params[2]));
					break;

				case "display":
					Image i = this.edge.flushToImage(500, 500, bgcolor, linecolor);
					i.flushToFile("./tmp/d.ppm");
					Runtime.getRuntime().exec("convert ./tmp/d.ppm ./tmp/d.png");
					try {
						Thread.sleep(500);
					} catch (Exception e) {
					}
					Picture p = new Picture("./tmp/d.png");
					p.show();
					break;

				case "save":
					String outfile = currtoken.getParameters();
					i = this.edge.flushToImage(500, 500, bgcolor, linecolor);
					i.flushToFile("./pics/" + outfile);
					break;

				case "save-convert":
					String param = currtoken.getParameters();
					String[] arr = param.split(" ");
					i = this.edge.flushToImage(Integer.parseInt(arr[1]), Integer.parseInt(arr[2]),
							bgcolor, linecolor);
					i.flushToFile("./pics/" + arr[0]);
					String outfilepng = arr[0].substring(0, arr[0].length() - 3) + "png";
					String convertcommand = "convert ./pics/" + arr[0] + " ./pics/" + outfilepng;
					Runtime.getRuntime().exec(convertcommand);
					break;

				case "circle":
					params = argstoarray(currtoken.getParameters());
					double cx = params[0], cy = params[1], cz = params[2], radius = params[3];
					EdgeGenerator.circle(edge, cx, cy, cz, radius);
					break;

				case "hermite":
					params = argstoarray(currtoken.getParameters());
					double hx0 = params[0], hy0 = params[1], hx1 = params[2], hy1 = params[3];
					double hrx0 = params[4], hry0 = params[5], hrx1 = params[6], hry1 = params[6];
					EdgeGenerator.hermite(edge, hx0, hy0, hx1, hy1, hrx0, hry0, hrx1, hry1);
					break;

				case "bezier":
					params = argstoarray(currtoken.getParameters());
					double bx0 = params[0], bx1 = params[2], bx2 = params[4], bx3 = params[6];
					double by0 = params[1], by1 = params[3], by2 = params[5], by3 = params[7];
					EdgeGenerator.bezier(edge, bx0, by0, bx1, by1, bx2, by2, bx3, by3);
					break;
				case "sphere":
					params = argstoarray(currtoken.getParameters());
					double xsp = params[0], ysp = params[1], zsp = params[2], radiussp = params[3];
					EdgeGenerator.sphere(edge, xsp, ysp, zsp, radiussp);
				case "pause":
					scanner.nextLine();
			}

		}
		scanner.close();

		// System.out.println(this.transform);
		// System.out.println(this.edge);
		// Image i = this.edge.flushToImage(150, 150, new Pixel(200, 200, 200));
		// i.flushToFile("d.ppm");
		Runtime.getRuntime().exec("rm -rf ./tmp");
	}

	public static void main(String[] args) throws FileNotFoundException {
		Parser p = new Parser(args[0]);

		try {
			p.parse();
		} catch (Exception e) {
		}

	}

}
