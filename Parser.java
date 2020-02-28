
import java.util.Stack;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class Parser {

class Command {
private String command;
private String arg;
public Command(String command, String args){
        this.command = command;
        this.arg = args;
}

public Command(String command){
        this.command = command;
}

public String toString(){
        String out = "";
        out += this.command;
        if (this.arg != null) {
                out += "=>";
                out += this.arg;
        }
        return out;
}

public String getCommand(){
        return this.command;
}

public String getParameters(){
        return this.arg;
}

}





private Matrix edge= new Matrix();
private Matrix transform = Matrix.identity();
ArrayList<Command> tokens = new ArrayList<Command>();


public Parser(String fname) throws FileNotFoundException {
        File f = new File(fname);
        Scanner sc = new Scanner(f);
        sc.useDelimiter("\n");
        ArrayList<String> stokens = new ArrayList<String>();
        while (sc.hasNextLine()) {
                try {
                        String data = sc.nextLine();
                        stokens.add(data);

                } catch(Exception e) {

                }
        }
        // System.out.println(stokens);

        for (int x = 0; x < stokens.size(); x++) {
                String command = stokens.get(x);
                if (command.equals("line") ||command.equals("rotate") ||command.equals("scale") ||command.equals("move") ||command.equals("save")) {
                        x++;
                        String arg = stokens.get(x);
                        tokens.add(new Command(command, arg));
                }
                else{
                        tokens.add(new Command(command));
                }
        }

        // System.out.println(tokens);

}



public int[] argstoarray(String args){
        String[] arr = args.split(" ");
        int[] out = new int[arr.length];
        for (int x = 0; x < arr.length; x++) {
                out[x] = Integer.parseInt(arr[x]);
        }

        return out;
}



public void parse(){
        for (int x = 0; x < tokens.size(); x++) {
                Command currtoken = tokens.get(x);
                String c = currtoken.getCommand();
                switch (c) {
                case "line":
                        System.out.println("line");
                        int[] params = argstoarray(currtoken.getParameters());
                        edge.addedge(params[0], params[1], params[2]);
                        edge.addedge(params[3], params[4], params[5]);
                        break;
                case "ident":
                        System.out.println("ident");
                        this.transform = Matrix.identity();
                        break;
                case "scale":
                        System.out.println("scale");
                        int[] params = argstoarray(currtoken.getParameters());
                        Matrix scale = new Matrix();
                        
                        break;
                case "move":
                        System.out.println("move");
                        break;
                case "rotate":
                        System.out.println("rotate");
                        break;
                case "apply":
                        System.out.println("apply");
                        break;
                case "display":
                        System.out.println("display");
                        break;
                case "save":
                        System.out.println("save");
                        break;
                }
        }

        System.out.println(this.edge);
}





public static void main(String[] args) throws FileNotFoundException {
        Parser p = new Parser("script.txt");

        p.argstoarray("4 5 6 3");

        p.parse();


}


}
