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

public class Terminal {

    public File currentDirectory;

    Terminal() {
        currentDirectory = new File(System.getProperty("user.home"));
    }

    public String pwd() {
        String s = "";
        s += currentDirectory.getAbsolutePath();
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

    public void checkCpAgruments(ArrayList<String> args) {
        if (args.size() > 2) {
            System.out.println("Too many argument");
        } else if (args.size() < 2) {
            System.out.println("Few argument");
        } else {
            cp(args.get(0), args.get(1));
        }
    }

    public void cp(String source, String destination) {
        try {
            File sourceFile = getAbsolute(source);
            File destinationFile = getAbsolute(destination);
            Path sourcePath = sourceFile.toPath();
            Path destinationPath = destinationFile.toPath();
            Files.copy(sourcePath, destinationPath.resolve(sourcePath.getFileName()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            System.out.println(e.getMessage() + " is not exist");
        }
    }

    public void mkdir(String path) {
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
            File f = new File(args.get(i));
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
        for (int i = 0; i < 150; i++) {
            System.out.println();
        }
        System.out.flush();
    }

    // changes the current directory to the given one
    public void cd(ArrayList<String> args) {
        if (args.size() > 1) {
            System.out.println("too many arguments!");
            return;
        } else if (args.size() == 0) {
            currentDirectory = new File(System.getProperty("user.home"));
            return;
        }
        String sourcePath = args.get(0);
        if (sourcePath.equals("..")) {
            String parent = currentDirectory.getParent();
            File f = new File(parent);
            currentDirectory = f.getAbsoluteFile();
        } else {
            File f = getAbsolute(sourcePath);
            if (!f.exists())
                System.out.println("No such file exists");
            else
                currentDirectory = f.getAbsoluteFile();
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

    public void redirectAppend(ArrayList<String> args) throws IOException {
        String command = args.get(0);
        String file = args.get(args.size() - 1);
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
        File output = getAbsolute(file);
        if (!output.exists() || !output.isFile()) {
            System.out.println("Could not find file: " + output);
            return;
        }
        FileWriter fr = new FileWriter(output, true);
        BufferedWriter br = new BufferedWriter(fr);
        br.write(content);
        br.close();
        fr.close();
    }

    public String pipe(ArrayList<String> args) throws IOException {
        String res = "";
        String temp = "";
        String cmd = args.get(0);
        ArrayList<String> paths = new ArrayList<>();
        args.remove(0);
        int r = 0;
        while (args.size() > 1) {
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
                        checkCpAgruments(paths);
                    else if (cmd.equals("rm"))
                        rm(paths);
                    else if (cmd.equals("rmdir"))
                        rmdir(paths);
                    else if (cmd.equals("mv"))
                        mv(paths);
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
                        checkCpAgruments(paths);
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
                        checkCpAgruments(paths);
                    else if (cmd.equals("rm"))
                        rm(paths);
                    else if (cmd.equals("rmdir"))
                        rmdir(paths);
                    else if (cmd.equals("mv"))
                        mv(paths);
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
        String file = args.get(args.size() - 1);
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
        File output = getAbsolute(file);
        if (!output.exists() || !output.isFile()) {
            System.out.println("Could not find file: " + output);
            return;
        }
        FileWriter fr = new FileWriter(output, false);
        BufferedWriter br = new BufferedWriter(fr);
        br.write(content);
        br.close();
        fr.close();
    }
    

}