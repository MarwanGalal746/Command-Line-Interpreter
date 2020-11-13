import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) throws Exception {
        Parser p = new Parser();
        String line;
        Scanner in = new Scanner(System.in);
        while ((line = in.nextLine()) != null) {
           boolean ok =  p.parse(line);
           if(!ok){
                System.out.println(line + "is not recognized as an internal or external command,operable program or batch file.");
           }
        }
    }
}