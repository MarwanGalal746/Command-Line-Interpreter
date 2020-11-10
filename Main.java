public class Main {

    public static void main(String[] args) {
        Parser p = new Parser();
        p.parse("cd ./\\ \\ asdasda/\\ allo");
        System.out.println(p.getCmd());
    }
}
