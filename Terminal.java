import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
}
