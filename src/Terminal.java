import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.lang.Character.*;

public class Terminal {

    public File currentDirectory;

    Terminal() {
        currentDirectory = new File(System.getProperty("user.home"));
    }
    
    public String pwd() {
        String s = "";
        s += currentDirectory.toString();
        return s;
    }

    public String rm(ArrayList<String> args) throws IOException {
        String s = "";
        for (int i = 0; i < args.size(); i++) {
            File f = new File(args.get(i));
            if (!f.exists()) {
                System.out.println("rm: cannot remove '" + args.get(i) + "': No such file or directory");
                args.remove(args.get(i));
                i--;
            } else if (f.isDirectory()) {
                System.out.println("rm: cannot remove '" + args.get(i) + ": Is a directory");
                args.remove(args.get(i));
                i--;
            }
        }
        for (int i = 0; i < args.size(); i++) {
            File f = new File(args.get(i));
            f.delete();
        }
        return s;
    }

    public File getAbsolute(String path) {
        File file = new File(path);
        if (!file.isAbsolute()) {
            file = new File(currentDirectory.getAbsolutePath(), path);
        }
        return file;
    }

    public void cp(ArrayList<String> args) {
        if (args.size() > 2) {
            System.out.println("Too many argument");
        } else if (args.size() < 2) {
            System.out.println("Few argument");
        } else {
            cp(args.get(0), args.get(1));
        }
    }

    public void cp(String source, String destination) {
        File sourceFile;
        File destinationFile;
        InputStream input;
        OutputStream output;
        try {
            sourceFile = getAbsolute(source);
            if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) { // if my operating system is windows
                destination += "\\";
            } else { // otherwise (Linux)
                destination += '/';
            }
            destination += sourceFile.getName();
            destinationFile = new File(destination);
            input = new FileInputStream(sourceFile);
            output = new FileOutputStream(destinationFile);
            byte[] buffer = new byte[1024];
            int numberOfBytes;
            while ((numberOfBytes = input.read(buffer)) > 0) {
                output.write(buffer, 0, numberOfBytes);
            }
            input.close();
            output.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void mkdir(ArrayList<String> args) {
        if(args.size()>1){
            System.out.println("Too many argumnet");
            return;
        }else if(args.size()<1){
            System.out.println("Few argument");
            return;
        }
        String path = args.get(0);
        try {
            if (Files.exists(Paths.get(path))) {
                System.out.println("Directory alreay exists");
            } else {
                Files.createDirectories(Paths.get(path));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String cat(ArrayList<String> args) throws IOException {
        if (args.size() == 0) {
            System.out.println("There is no arguments");
            return "";
        }
        String content = "";
        for (int idx = 0; idx < args.size(); idx++) {
            File current = getAbsolute(args.get(idx));
            if (current.exists()) {
                BufferedReader read = new BufferedReader(new FileReader(current));
                String line;
                while ((line = read.readLine()) != null)
                    content += line + '\n';
                read.close();
            } else
                System.out.println("Could not find file: " + current);
        }
        return content;
    }

    public String date(ArrayList<String> args) {
        if (args.size() != 0) {
            System.out.println("Date command don't take any parameters");
            return "";
        }
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
        Date dateobj = new Date();
        return df.format(dateobj);
    }

    public String ls(ArrayList<String> args) {
        String res = "";
        boolean cond = true;
        for (int i = 0; i < args.size(); i++) {
            File f = getAbsolute(args.get(i));
            if (!f.isDirectory()) {
                cond = false;
                System.out.println("ls: cannot access '" + args.get(i) + "': No such file or directory");
                args.remove(args.get(i));
                i--;
            }
        }
        if (args.size() == 0 && cond) {
            File f = currentDirectory;
            String[] files = f.list();
            for (String s : files)
                res += s + '\n';
        } else if (args.size() == 1 && cond) {
            File f = getAbsolute(args.get(0));
            String[] files = f.list();
            for (String s : files)
                res += s + '\n';
        } else {
            for (int i = 0; i < args.size(); i++) {
                res += '\n' + getAbsolute(args.get(i)).getAbsolutePath() + " : \n";
                File f = getAbsolute(args.get(i));
                String[] files = f.list();
                for (String s : files)
                    res += s + '\n';
                res += '\n' + '\n';
            }
        }
        return res;
    }

    public void clear() {
        System.out.print("\033[H\033[2J");
    }

    // changes the current directory to the given one
    public void cd(ArrayList<String> args) {
        if (args.size() > 1) {
            System.out.println("too many arguments!");
            return;
        } else if (args.size() == 0) {
            System.out.println("Few arguments");
            return;
        }
        String sourcePath = args.get(0);
        if (sourcePath.equals("..")) {
            String parent = currentDirectory.getParent();
            File f = getAbsolute(parent);
            currentDirectory = f;
        } else {
            File f = getAbsolute(sourcePath);
            if (!f.exists())
                System.out.println("No such file exists");
            else
                currentDirectory = f;
        }
    }

//changes into default directory

    public String more(ArrayList<String> args, boolean isPipeOrRedirect) {
        String res = "";
        if (args.size() == 0) {
            System.out.println("too few arguments!");
            return "";
        } else if (args.size() > 1) {
            System.out.println("too many arguments!");
            return "";
        }
        String sourcePath = args.get(0);
        File f = getAbsolute(sourcePath);
        if (!f.exists())
            System.out.println("No such Path exists");
        else {
            try {
                FileInputStream a = new FileInputStream(f);
                BufferedReader br = new BufferedReader(new InputStreamReader(a));
                String l;
                int c = 0;
                int x;
                Scanner in = new Scanner(System.in);
                while ((l = br.readLine()) != null) {
                    if (isPipeOrRedirect)
                        res += l + "\n";
                    else
                        System.out.println(l);
                    c++;
                    if (c % 10 == 0 && isPipeOrRedirect == false) {
                        System.out.print("................................. for MORE press 1, otherwise press 2 ");
                        x = in.nextInt();
                        if (x == 2)
                            break;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return res;
        }
        return res;
    }

    public String args(ArrayList<String> argsList) {
        String res = "";
        if (argsList.size() == 0) {
            System.out.println("too few arguments!");
            return "";
        } else if (argsList.size() > 1) {
            System.out.println("too many arguments!");
            return "";
        }
        String command = argsList.get(0);
        if (command.equalsIgnoreCase("cd"))
            res = "arg1: Path of the desired directory";
        else if (command.equalsIgnoreCase("ls"))
            res = "arg1: Path of the directory to list the files from (zero args mean current directory)";
        else if (command.equalsIgnoreCase("cp") || command.equalsIgnoreCase("mv"))
            res = "arg1: SourcePath, arg2: DestinationPath";
        else if (command.equalsIgnoreCase("cat"))
            res = "arg1: file's name to print it's content, arg2: file's name to print it's content, ans so on...";
        else if (command.equalsIgnoreCase("more"))
            res = "arg1: file's path to print it's content";
        else if (command.equalsIgnoreCase(">") || command.equalsIgnoreCase(">>"))
            res = "arg1: file's path to redirect into";
        else if (command.equalsIgnoreCase("mkdir"))
            res = "arg1: path to create a directory at";
        else if (command.equalsIgnoreCase("args") || command.equalsIgnoreCase("help"))
            res = "arg1: Command's name";
        else if (command.equalsIgnoreCase("rmdir"))
            res = "arg1: file's path";
        else if (command.equalsIgnoreCase("rm"))
            res = "arg1: file's path, arg2: filse's path, and so on...";
        else if (command.equalsIgnoreCase("date") || command.equalsIgnoreCase("pwd")
                || command.equalsIgnoreCase("clear"))
            res = "This command has no args!";
        else
            res = "Invalid command!";
        return res;
    }

    public void rmdir(ArrayList<String> args) {
        if (args.size() > 1) {
            System.out.println("too many arguments!");
            return;
        } else if (args.size() == 0) {
            System.out.println("too few arguments!");
            return;
        }
        String sourcePath = args.get(0);
        File f = getAbsolute(sourcePath);
        if (!f.exists())
            System.out.println("No such directory exists");
        else if (f.isFile())
            System.out.println("Cannot delete file");
        else if (!f.delete())
            System.out.println("Cannot delete non-empty directory.");
    }

    public void redirectAppend(ArrayList<String> args) throws Exception {
        String command = args.get(0);
        String path = args.get(args.size() - 1);
        args.remove(args.size() - 1);
        args.remove(0);
        String content = "";
        switch (command) {
            case "ls":
                content = ls(args);
                break;
            case "pwd":
                content = pwd();
                break;
            case "cat":
                content = cat(args);
                break;
            case "date":
                content = date(args);
                break;
            case "more":
                content = more(args, true);
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + command);
        }
        File file = getAbsolute(path);
        if (!file.exists() || !file.isFile()) {
            System.out.println("Could not find file: " + file);
            return;
        }
        FileWriter writer = new FileWriter(file, true);
        writer.append(content);
        writer.append('\n');
        writer.close();
    }

    public String pipe(ArrayList<String> args) throws IOException {
        String res = "";
        String temp = "";
        String cmd = args.get(0);
        ArrayList<String> paths = new ArrayList<>();
        args.remove(0);
        int r = 0;
        while (args.size() > 0) {
            for (int i = 0; i < args.size(); i++) {
                if (args.get(i).equals("|")) {
                    args.remove(i);
                    i--;
                    if (cmd.equals("pwd"))
                        temp = pwd();
                    else if (cmd.equals("cd"))
                        cd(paths);
                    else if (cmd.equals("ls")) {
                        if (r != 0)
                            paths.add(temp);
                        temp = ls(paths);
                    } else if (cmd.equals("cat")) {
                        if (r != 0)
                            paths.add(temp);
                        temp = cat(paths);
                    } else if (cmd.equals("args")) {
                        temp = args(paths);
                    } else if (cmd.equals("date"))
                        temp = date(paths);
                    else if (cmd.equals("cp"))
                        cp(paths);
                    else if (cmd.equals("rm"))
                        rm(paths);
                    else if (cmd.equals("rmdir"))
                        rmdir(paths);
                    else if (cmd.equals("mv"))
                        mv(paths);
                    else if (cmd.equals("help"))
                        temp=help();
                    cmd = args.get(i + 1);
                    paths.clear();
                } else if (i + 1 == args.size()) {
                    if (cmd.equals("pwd"))
                        temp = pwd();
                    else if (cmd.equals("cd"))
                        cd(paths);
                    else if (cmd.equals("ls")) {
                        if (r != 0)
                            paths.add(temp);
                        temp = ls(paths);
                    } else if (cmd.equals("cat")) {
                        if (r != 0)
                            paths.add(temp);
                        temp = cat(paths);
                    } else if (cmd.equals("args")) {
                        temp = args(paths);
                    } else if (cmd.equals("date"))
                        temp = date(paths);
                    else if (cmd.equals("cp"))
                        cp(paths);
                    else if (cmd.equals("rm"))
                        rm(paths);
                    else if (cmd.equals("rmdir"))
                        rmdir(paths);
                    else if (cmd.equals("mv"))
                        mv(paths);
                    else if (cmd.equals("more")) {
                        moreStr(temp);
                        return "";
                    }
                    else if (cmd.equals("help"))
                        temp=help();
                } else if (args.get(i + 1).equals("|")) {
                    paths.add(args.get(0));
                    args.remove(i + 1);
                    args.remove(i);
                    i--;
                    if (cmd.equals("pwd"))
                        temp = pwd();
                    else if (cmd.equals("cd"))
                        cd(paths);
                    else if (cmd.equals("ls")) {
                        if (r != 0)
                            paths.add(temp);
                        temp = ls(paths);
                    } else if (cmd.equals("cat")) {
                        if (r != 0)
                            paths.add(temp);
                        temp = cat(paths);
                    } else if (cmd.equals("args")) {
                        temp = args(paths);
                    } else if (cmd.equals("date"))
                        temp = date(paths);
                    else if (cmd.equals("cp"))
                        cp(paths);
                    else if (cmd.equals("rm"))
                        rm(paths);
                    else if (cmd.equals("rmdir"))
                        rmdir(paths);
                    else if (cmd.equals("mv"))
                        mv(paths);
                    else if (cmd.equals("help"))
                        temp=help();
                    cmd = args.get(i + 1);
                    paths.clear();
                } else {
                    paths.add(args.get(i));
                    args.remove(i);
                    i--;
                }
            }
            r++;
        }
        return res;
    }

    private void moreStr(String s) {
        int n = 1;
        for (int i = 0; i < s.length(); i++) {
            System.out.print(s.charAt(i));
            if (s.charAt(i) == '\n')
                n++;
            if (n % 10 == 0) {
                System.out.print("................................. for MORE press 1, otherwise press 2 ");
                n = 1;
                int x;
                Scanner in = new Scanner(System.in);
                x = in.nextInt();
                if (x == 2)
                    break;
            }
        }
    }

    public void mv(ArrayList<String> args) throws IOException {
        if (args.size() > 2) {
            System.out.println("Too many argument");
            return;
        }
        if (args.size() < 2) {
            System.out.println("Few argument");
            return;
        }
        cp(args.get(0), args.get(1));
        args.remove(1);
        File f = getAbsolute(args.get(0));
        if (f.isFile())
            rm(args);
        else if (f.isDirectory())
            rmdir(args);
        else
            System.out.println("There is an error");
    }

    public void overWrite(ArrayList<String> args) throws IOException {
        String command = args.get(0);
        String path = args.get(args.size() - 1);
        args.remove(args.size() - 1);
        args.remove(0);
        String content = "";
        switch (command) {
            case "ls":
                content = ls(args);
                break;
            case "pwd":
                content = pwd();
                break;
            case "cat":
                content = cat(args);
                break;
            case "date":
                content = date(args);
                break;
            case "more":
                content = more(args, true);
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + command);
        }
        File file = getAbsolute(path);
        if (!file.exists() || !file.isFile()) {
            System.out.println("Could not find file: " + file);
            return;
        }
        FileWriter writer = new FileWriter(file, false);
        writer.write(content);
        writer.close();
    }

    public String help() {
        String helpText = new String(), line;

        try {
            File file = new File("commandsDescription.txt");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while ((line = bufferedReader.readLine()) != null) {
                helpText += line;
                helpText += '\n';
            }
            bufferedReader.close();
        } catch (Exception e) {

            helpText = e.getMessage();
        }

        return helpText;
    }

}