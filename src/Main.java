import mua.Interpreter;
import mua.LexicalErrorException;
import mua.parser.SyntaxErrorException;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] argv) {
        Scanner scanner = new Scanner(System.in);
        Interpreter interpreter = new Interpreter(scanner, System.out);
        while (scanner.hasNext()) {
            try {
                interpreter.startInterpret();
            } catch (LexicalErrorException | SyntaxErrorException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
