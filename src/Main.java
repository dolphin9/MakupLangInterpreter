import mua.Interpreter;

import java.util.Scanner;

public class Main {
    public static void main(String[] argv) {
        Interpreter interpreter = new Interpreter();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            System.out.print(interpreter.getPrompt());
            String[] line = scanner.nextLine().split(" ");
            interpreter.execute(line);
        }
    }
}
