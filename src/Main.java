import mua.Interpreter;
import mua.lexer.LexicalErrorException;

import java.util.Scanner;

public class Main {
    public static void main(String[] argv) {
        Interpreter interpreter = new Interpreter();
        Scanner scanner = new Scanner(System.in);
        System.out.print(interpreter.getPrompt());
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            try {
                interpreter.execute(line);
            } catch (LexicalErrorException e) {
                e.printStackTrace();
            }
            System.out.print(interpreter.getPrompt());
        }
    }
}
