import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Terminal {

    public void pwd(Parser p) {
        System.out.println(System.getProperty("user.dir"));
    }

    public void rm(Parser p) throws IOException {
        File f = new File(p.getArguments().get(0));
        f.delete();
    }
}
