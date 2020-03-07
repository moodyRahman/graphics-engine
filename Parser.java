
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
			String c = currtoken.getCommand();
			double[] params;
			switch (c) {
				case "line":
					params = argstoarray(currtoken.getParameters());
					double px1 = params[0], py1 = params[1], pz1 = params[2];
					double px2 = params[3], py2 = params[4], pz2 = params[5];
					edge.addpoint(px1, py1, pz1);
					edge.addpoint(px2, py2, pz2);
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
					Image i = this.edge.flushToImage(150, 150, new Pixel(200, 200, 200));
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
					i = this.edge.flushToImage(150, 150, new Pixel(200, 200, 200));
					i.flushToFile(outfile);
					break;
				case "save-convert":
					outfile = currtoken.getParameters();
					i = this.edge.flushToImage(150, 150, new Pixel(200, 200, 200));
					i.flushToFile(outfile);
					String outfilepng = outfile.substring(0, outfile.length() - 3) + "png";
					String convertcommand = "convert " + outfile + " " + outfilepng;
					System.out.println(convertcommand);
					Runtime.getRuntime().exec(convertcommand);
					// System.out.println(outfilepng);
				case "circle":
					params = argstoarray(currtoken.getParameters());

					double cx = params[0], cy = params[1], cz = params[2], radius = params[3];

					// double oldcx = cx + radius, oldcy = cy;

					for (double deg = 0; deg < 1; deg += .02) {
						double rad = deg * 6.2831;
						edge.addpoint((radius * Math.cos(rad)) + cx,
								(radius * Math.sin(rad)) + cy, cz);
						edge.addpoint((radius * Math.cos(rad + 0.174)) + cx,
								(radius * Math.sin(rad + 0.174)) + cy, cz);
					}
					// System.out.println(this.edge);

					break;
				case "hermite":
					params = argstoarray(currtoken.getParameters());
					// x0, y0, x1, y1, rx0, ry0, rx1, ry1
					// 0    1   2   3   4    5    6    7

					break;
				case "bezier":
					params = argstoarray(currtoken.getParameters());
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
