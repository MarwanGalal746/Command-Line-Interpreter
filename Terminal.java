import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Terminal {
    
    public File currentDirectory;   

    Terminal(){
        currentDirectory = new File(System.getProperty("user.home"));
    }

    public void pwd(Parser p) {
        System.out.println(System.getProperty("user.dir"));
    }

    public void rm(Parser p) throws IOException {
        File f = new File(p.getArguments().get(0));
        f.delete();
    }
    public File getAbsolute(String path){
        File file = new File(path);
        if(!file.isAbsolute()){
            file = new File(currentDirectory.getAbsolutePath(),path);
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
		if(args.size()==0) {
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
    public void cd(String sourcePath){
    	
        if(sourcePath.equals("..")){
        	
            String parent = currentDirectory.getParent();
            File f = new File(parent);
            currentDirectory = f.getAbsoluteFile();
        }
        else{
        	File f= getAbsolute(sourcePath);
        	if(!f.exists()) 
        		System.out.println("No such file exists");
        	else
        		currentDirectory = f.getAbsoluteFile();
        }
    }
    public String date(){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
        Date dateobj = new Date();
        return df.format(dateobj);
    }
    public void clear(){
       for (int i = 0; i < 150; i++) {
           System.out.println();
       }
       System.out.flush();
    }
}
