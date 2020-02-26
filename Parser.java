
import java.util.Stack;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class Parser{

class Command{
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
                if (this.arg != null){
                        out += "=>";
                        out += this.arg;
                }
                return out;
        }

}

private static Matrix edge= new Matrix();
private static Stack<Matrix> transforms = new Stack<Matrix>();


public Parser(String fname) throws FileNotFoundException{
        ArrayList<Command> tokens = new ArrayList<Command>();
        File f = new File(fname);
        Scanner sc = new Scanner(f);
        sc.useDelimiter("\n");
        ArrayList<String> stokens = new ArrayList<String>();
        while (sc.hasNextLine()){
                try {
                        String data = sc.nextLine();
                        stokens.add(data);

                } catch(Exception e) {

                }
        }
        // System.out.println(stokens);

        for (int x = 0; x < stokens.size(); x++) {
                String command = stokens.get(x);
                if (command.equals("line")){
                        x++;
                        String arg = stokens.get(x);
                        tokens.add(new Command(command, arg));
                }
                else{
                        tokens.add(new Command(command));
                }
        }

        System.out.println(tokens);
}





public static void main(String[] args) throws FileNotFoundException{
        Parser p = new Parser("script.txt");


}


}
