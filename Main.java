import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Parser p = new Parser();
        p.parse("rm /home/marwan/ggg");
        Terminal t = new Terminal();
        t.rm(p);
    }
}