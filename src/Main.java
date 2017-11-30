import mua.Interpreter;
import mua.exceptions.MuaExceptions;
import mua.values.Function;

import java.util.Scanner;

public class Main {
    public static void main(String[] argv) {
        Scanner scanner = new Scanner(System.in);
        Interpreter interpreter = new Interpreter(scanner, System.out);
        while (interpreter.hasNextInstruction()) {
            try {
                interpreter.run();
            } catch (MuaExceptions e) {
                e.printStackTrace();
            } catch (Function.FunctionStop functionStop) {
                // Ignore
            }
        }
    }
}
