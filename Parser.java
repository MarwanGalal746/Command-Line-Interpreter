import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class Parser {

    //array list to store args
    private ArrayList<String> args;

    //string to store the command
    private String cmd;

    //const array which stores the valid commands
    //I used it to check if the command of the input is valid or not
    private final String[] commands = {"cd", "ls", "cp", "cat", "more", "|", "<", "<<",
            "mkdir", "rmdir", "mv", "rm", "args", "date", "help", "pwd", "clear"};


    //fun to check if the input is excutable or not
    //if the input executable, it will change the value of cmd and args
    public boolean parse(String input) {

        //replace spaces in files names with ~ to avoid cnflict in splitting
        input = input.replace("\\ ", "~");

        //splitting the input
        String[] List = input.split(" ");

        //recovering spaces in files names
        for (String s : List)
            s = s.replace("~", " ");

        //checking if the comman is valid or not
        boolean cond = Arrays.asList(commands).contains(List[0]);
        if (!cond) return false;


        cmd = List[0];      //storing the command in cmd

        //checking if parameters of (< and <<) valid or not
        //and storing the parameters of the command in args
        args = new ArrayList<>();
        for (int i = 1; i < List.length; i++) {
            if (List[i].equals("<") || List[i].equals("<<")) {
                if (List[i].equals(List[List.length - 1]))
                    return false;
            }
            args.add(List[i]);
        }
        return true;
    }

    public String getCmd() {
        return cmd;
    }

    public ArrayList<String> getArguments() {
        return args;
    }
}
