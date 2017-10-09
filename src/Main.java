import mua.Interpreter;
import mua.lexer.LexicalErrorException;
import mua.parser.SyntaxErrorException;

import java.util.Scanner;

public class Main {
    public static void main(String[] argv) {
        Scanner scanner = new Scanner(System.in);
        Interpreter interpreter = new Interpreter(scanner, System.out);
        interpreter.printPrompt();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            try {
                interpreter.execute(line);
            } catch (LexicalErrorException e) {
                e.printStackTrace();
            } catch (SyntaxErrorException e) {
                e.printStackTrace();
            }
            interpreter.printPrompt();
        }
    }
}
