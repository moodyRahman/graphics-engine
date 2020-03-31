


package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.ArrayDeque;

/**
 * 
 * This is the main executable file for Okuyasu Engine <br>
 * The program flow is as follows <br> <br>
 * 
 * 1) Parser takes some in input file via the first command line argument <br>
 * 	Java Parser sphere.mscript <br> <br>
 * 	
 * 	The specifications for .mscripts and MoodScript are viewable in the README <br> <br>
 * 
 * 2) THe constructor for Parser opens the input file, and splits it by newlines <br>
 * 	It then organizes the inputs into an ArrayList of Command objects <br> <br>
 * 
 * 3) Run Parser.parse(), which processes the Command objects <br>
 * 	The method has one instance of an DoubleMatrix edge, to which all generated edges will be added to <br> <br>
 * 
 * 4) Run through the switch statements and modify the edge and the transformation accordingly <br> <br>
 * 
 * 	4a) Transformation type commands will modify the transformation matrix, that queues up the operations to be <br>
 * 		executed with an apply command <br>
 * 	4b) Edge generating commands will go into their static method with the parameters <br>
 * 		inside the static method, it will generate the edges and then append them to the Parser's edge matrix <br> <br>
 * 
 * 5) when a save type command is encountered, the edge matrix runs .flushToImage, which draws the edges into an Image object <br>
 * 	in accordance to the matrix. <br> <br>
 * 
 */

public class Parser {
	
	/**
	 * Stores a command, paramaters, and line number
	 */
	class Command {
		private String command;
		private String arg;
		private int line;

		public Command(String command, String args, int line) {
			this.command = command;
			this.arg = args;
			this.line = line;
		}

		public Command(String command, int line) {
			this.command = command;
			this.line = line;
		}

		public String toString() {
			String out = "";
			out += this.line + " : ";
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

		public int getLine(){
			return this.line;
		}

	}

	private DoubleMatrix edge = new DoubleMatrix();
	private DoubleMatrix polygon = new DoubleMatrix();
	private DoubleMatrix transform = TransformGenerator.identity();
	private ArrayDeque<DoubleMatrix> coorstack = new ArrayDeque<DoubleMatrix>();
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
					|| command.equals("bg-color") || command.equals("sphere") || command.equals("rotate-point")
					|| command.equals("box") || command.equals("torus") || command.equals("display-custom")) {
				x++;
				String arg = stokens.get(x);
				tokens.add(new Command(command, arg, x));
			} else {
				tokens.add(new Command(command, x));
			}
		}
	}

	/**
	 * @param args String of double parameters
	 * @return double[] of parameters
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
	 * processes the tokens in the input script executes the commands <br>
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

				case "rotate-point":
					params = argstoarray(currtoken.getParameters());
					double xrp = params[0], yrp = params[1], zrp = params[2], axisrp = params[3], thetarp = params[4];
					DoubleMatrix rotatepoint = TransformGenerator.rotatepoint(xrp, yrp, zrp, (char)axisrp, thetarp);
					this.transform = DoubleMatrix.multiply(rotatepoint, this.transform);
					break;

				case "apply":
					this.edge = DoubleMatrix.multiply(this.transform, this.edge);
					this.polygon = DoubleMatrix.multiply(this.transform, this.polygon);
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
					Image i = new Image(500, 500, bgcolor);
					i.matrixLineEdge(edge, linecolor);
					i.matrixLinePolygon(polygon, linecolor);
					i.display();
					break;
				case "display-custom":
					params = argstoarray(currtoken.getParameters());
					Image customdisp = new Image((int)params[0], (int)params[1], bgcolor);
					customdisp.matrixLineEdge(edge, linecolor);
					customdisp.matrixLinePolygon(polygon, linecolor);
					customdisp.display();
					break;

				case "save":
					String outfile = currtoken.getParameters();
					i = new Image(500, 500, bgcolor);
					i.matrixLineEdge(edge, linecolor);
					i.matrixLinePolygon(polygon, linecolor);
					i.flushToFile("./pics/" + outfile);
					break;

				case "save-convert":
					String param = currtoken.getParameters();
					String[] arr = param.split(" ");
					
					Image saveconv = new Image(Integer.parseInt(arr[1]), Integer.parseInt(arr[2]), bgcolor);
					saveconv.matrixLineEdge(edge, linecolor);
					saveconv.matrixLinePolygon(polygon, linecolor);
					
					saveconv.flushToFile("./pics/" + arr[0]);
					String outfilepng = arr[0].substring(0, arr[0].length() - 3) + "png";
					String convertcommand = "convert ./pics/" + arr[0] + " ./pics/" + outfilepng;
					Runtime.getRuntime().exec(convertcommand);
					break;

				case "circle":
					params = argstoarray(currtoken.getParameters());
					double cx = params[0], cy = params[1], cz = params[2], radius = params[3];
					EdgeGenerator.circle(edge, cx, cy, cz, radius, 75);
					break;

				case "hermite":
					params = argstoarray(currtoken.getParameters());
					double hx0 = params[0], hy0 = params[1], hx1 = params[2], hy1 = params[3];
					double hrx0 = params[4], hry0 = params[5], hrx1 = params[6], hry1 = params[6];
					EdgeGenerator.hermite(edge, hx0, hy0, hx1, hy1, hrx0, hry0, hrx1, hry1, 50);
					break;

				case "bezier":
					params = argstoarray(currtoken.getParameters());
					double bx0 = params[0], bx1 = params[2], bx2 = params[4], bx3 = params[6];
					double by0 = params[1], by1 = params[3], by2 = params[5], by3 = params[7];
					EdgeGenerator.bezier(edge, bx0, by0, bx1, by1, bx2, by2, bx3, by3, 50);
					break;

				case "sphere":
					params = argstoarray(currtoken.getParameters());
					double xsp = params[0], ysp = params[1], zsp = params[2], radiussp = params[3];
					EdgeGenerator.sphere(polygon, xsp, ysp, zsp, radiussp);
					break;

				case "pause":
					System.out.println();
					System.out.println("PRESS ENTER TO RESUME SCRIPT");
					scanner.nextLine();
					break;

				case "clear":
					edge.wipe();
					break;

				case "box":
					params = argstoarray(currtoken.getParameters());
					double boxx = params[0], boxy = params[1], boxz = params[2];
					double boxw = params[3], boxh = params[4], boxd = params[5];
					EdgeGenerator.box(polygon, boxx, boxy, boxz, boxw, boxh, boxd);
					break;

				case "torus":
					params = argstoarray(currtoken.getParameters());
					double torx = params[0], tory = params[1], torz = params[2];
					double torin = params[3], torout = params[4];
					EdgeGenerator.torus(polygon, torx, tory, torz, torin, torout);
					break;

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
		} catch (Exception e){
			e.printStackTrace();
		}

	}

}
