
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

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
	private DoubleMatrix transform = DoubleMatrix.identity();
	ArrayList<Command> tokens = new ArrayList<Command>();
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
					|| command.equals("hermite") || command.equals("bezier")) {
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
					edge.addpoint(params[0], params[1], params[2]);
					edge.addpoint(params[3], params[4], params[5]);
					break;
				case "ident":
					this.transform = DoubleMatrix.identity();
					break;
				case "scale":
					params = argstoarray(currtoken.getParameters());
					DoubleMatrix scale = DoubleMatrix.scale(params[0], params[1], params[2]);
					this.transform = DoubleMatrix.multiply(scale, this.transform);
					break;
				case "move":
					params = argstoarray(currtoken.getParameters());
					DoubleMatrix move = DoubleMatrix.translate(params[0], params[1], params[2]);
					this.transform = DoubleMatrix.multiply(move, this.transform);
					break;
				case "rotate":
					params = argstoarray(currtoken.getParameters());
					DoubleMatrix rotate = DoubleMatrix.rotate((char) params[0], params[1]);
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
					System.out.println("DONE");
					break;
				case "save":
					String outfile = currtoken.getParameters();
					System.out.println("SAVE");
					i = this.edge.flushToImage(150, 150, new Pixel(200, 200, 200));
					i.flushToFile(outfile);
					break;
				case "circle":
					System.out.println("circle");
					params = argstoarray(currtoken.getParameters());
					for(double d = 0; d <= 1; d+=.02){
						
					}
					break;
				case "hermite":
					params = argstoarray(currtoken.getParameters());
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
