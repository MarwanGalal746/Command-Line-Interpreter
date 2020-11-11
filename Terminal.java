import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Terminal {

    public File currentDirectory;

    Terminal() {
        currentDirectory = new File(System.getProperty("user.home"));
    }

    public void pwd() {
        System.out.println(currentDirectory.getAbsolutePath());
    }

    public void rm(ArrayList<String> args) throws IOException {
        for (int i = 0; i < args.size(); i++) {
            File f = new File(args.get(i));
            f.delete();
        }
    }

    public File getAbsolute(String path) {
        File file = new File(path);
        if (!file.isAbsolute()) {
            file = new File(currentDirectory.getAbsolutePath(), path);
        }
        return file;
    }

    public void cp(String sourcePath, String destinationPath) {
        File sourceFile;
        File destinationFile;
        InputStream input;
        OutputStream output;
        try {
            sourceFile = getAbsolute(sourcePath);
            destinationFile = getAbsolute(destinationPath);
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
                System.out.println("Could not find file: " + current.getAbsolutePath());
        }
        return content;
    }

    public void cd(String sourcePath) {

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

    public String date() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
        Date dateobj = new Date();
        return df.format(dateobj);
    }

    public void ls(ArrayList<String> args) {
        if (args.size() == 0) {
            File f = currentDirectory;
            String[] files = f.list();
            for (String s : files)
                System.out.println(s);
        } else if (args.size() == 1) {
            File f = getAbsolute(args.get(0));
            String[] files = f.list();
            for (String s : files)
                System.out.println(s);
        } else {
            for (int i = 0; i < args.size(); i++) {
                System.out.println(getAbsolute(args.get(i)) + " : ");
                File f = getAbsolute(args.get(i));;
                String[] files = f.list();
                for (String s : files)
                    System.out.println(s);
                System.out.println();
                System.out.println();
            }
        }
    }
}
    public void clear(){
       for (int i = 0; i < 150; i++) {
           System.out.println();
       }
       System.out.flush();
    }
    public void more(String sourcePath) {
    	File f= getAbsolute(sourcePath);
    	if(!f.exists()) 
    		System.out.println("No such file exists");
    	else {
    		try {
				FileInputStream a = new FileInputStream(f);
				BufferedReader br = new BufferedReader(new InputStreamReader(a));
				String l;
				int c = 0;
				int x;
				Scanner in = new Scanner(System.in);
				while ((l = br.readLine()) != null) {
					System.out.println(l);
					c++;
					if (c % 10 == 0) {
						System.out
								.print("................................. for MORE press 1, otherwise press 2 ");
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
			return;
    	}
    }
    public String args(String command) {
    	String res="";
    	if(command.equalsIgnoreCase("cd"))
    		res="arg1: Path of the desired directory";
    	else if(command.equalsIgnoreCase("ls"))
    		res="arg1: Path of the directory to list the files from (zero args mean current directory)";
    	else if(command.equalsIgnoreCase("cp")||command.equalsIgnoreCase("mv"))
    		res="arg1: SourcePath, arg2: DestinationPath";
    	else if(command.equalsIgnoreCase("cat"))
    		res="arg1: file's name to print it's content, arg2: file's name to print it's content, ans so on...";
    	else if(command.equalsIgnoreCase("more"))
    		res="arg1: file's path to print it's content";
    	else if(command.equalsIgnoreCase(">") || command.equalsIgnoreCase(">>"))
    		res="arg1: file's path to redirect into";
    	else if(command.equalsIgnoreCase("mkdir"))
    		res="arg1: path to create a directory at";
    	else if(command.equalsIgnoreCase("args")||command.equalsIgnoreCase("help"))
    		res="arg1: Command's name";
    	else if(command.equalsIgnoreCase("rmdir"))
    		res="arg1: file's path";
    	else if(command.equalsIgnoreCase("rm"))
    		res="arg1: file's path, arg2: filse's path, and so on...";
    	else if(command.equalsIgnoreCase("date")||command.equalsIgnoreCase("pwd")||command.equalsIgnoreCase("clear"))
    		res="This command has no args!";
    	else
    		res="Invalid command!";
    	return res;
    }
    public void rmdir(String sourcePath)throws DirectoryNotEmptyException,NoSuchFileException,IOException{
        File f = getAbsolute(sourcePath);
        if(!f.exists())
        	System.out.println("No such directory exists");
        else if(f.isFile())
        	System.out.println("Cannot delete file");
        else if(!f.delete())
        	System.out.println("Cannot delete non-empty directory.");
    }
}
