import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Parser {
    public static void main(String[] args) throws Exception {
        Parser p = new Parser();
        String line;
        Scanner in = new Scanner(System.in);
        while ((line = in.nextLine()) != null) {
            if (!p.parse(line) && !line.isEmpty()) {
                System.out.println(line + " is not recognized as an internal or external command,operable program or batch file.");
            }
        }
    }

    private Terminal terminal = new Terminal();

    // array list to store args
    private ArrayList<String> args;

    // string to store the command
    private String cmd;

    // const array which stores the valid commands
    // I used it to check if the command of the input is valid or not
    private final String[] commands = {"cd", "ls", "cp", "cat", "more", "mkdir", "rmdir", "mv", "rm", "args", "date",
            "help", "pwd", "clear", "exit"};

    public void print(String output) {
        System.out.println(output);
    }

    // fun to check if the input is excutable or not
    // if the input executable, it will change the value of cmd and args
    public boolean parse(String input) throws Exception {

        //replace spaces in files names with ~ to avoid cnflict in splitting
        input = input.replace("\\ ", "~");

        //splitting the input
        String[] List = input.split(" ");

        //recovering spaces in files names
        for (int i = 0; i < List.length; i++) {
            List[i] = List[i].replace("~", " ");
        }


        //checking if the command is valid or not
        boolean cond = Arrays.asList(commands).contains(List[0]);
        if (!cond) return false;
        args = new ArrayList<>();
        cmd = List[0];      //storing the command in cmd
        boolean pipe = false;
        if (Arrays.asList(List).contains("|")) {
            pipe = true;
            for (int i = 0; i < List.length; i++) {
                if (List[i].equals("|") && i < List.length - 1) {
                    if (!Arrays.asList(commands).contains(List[i + 1]))
                        return false;
                }
            }
            args.add(cmd);
        }
        boolean appendOperator = Arrays.asList(List).contains(">");
        boolean overWriteOperator = Arrays.asList(List).contains(">>");

        for (int i = 1; i < List.length; i++) {
            args.add(List[i]);
        }


        if (appendOperator) {
            args.remove(args.indexOf(">"));
            args.add(0, cmd);
            terminal.redirectAppend(args);
            return true;
        } else if (overWriteOperator) {
            args.remove(args.indexOf(">>"));
            args.add(0, cmd);
            terminal.overWrite(args);
            return true;
        } else if (pipe) {
            terminal.pipe(args);
            return true;
        }

        switch (cmd) {
            case "cd":
                terminal.cd(args);
                break;
            case "ls":
                print(terminal.ls(args));
                break;
            case "cp":
                terminal.cp(args);
                break;
            case "cat":
                print(terminal.cat(args));
                break;
            case "more":
                print(terminal.more(args, false));
                break;
            case "mkdir":
                terminal.mkdir(args);
                break;
            case "rmdir":
                terminal.rmdir(args);
                break;

            case "mv":
                terminal.mv(args);
                break;
            case "rm":
                print(terminal.rm(args));
                break;
            case "args":
                print(terminal.args(args));
                break;
            case "date":
                print(terminal.date(args));
                break;
            case "help":
                print(terminal.help());
                break;
            case "pwd":
                print(terminal.pwd());
                break;
            case "clear":
                terminal.clear();
                break;
            default:
                System.exit(0);
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