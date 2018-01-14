package mua;

import mua.exceptions.MuaExceptions;
import mua.values.Function;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class InterpreterTest {
    @Test
    public void testFactorial() throws IOException, MuaExceptions, Function.FunctionStop {
        final String PREFIX = "tests/res/testFactorial";
        Scanner codeScanner = new Scanner(Paths.get(PREFIX + ".mua"));
        OutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        Interpreter interpreter = new Interpreter(codeScanner, printStream);
        interpreter.run();
        String result = outputStream.toString();
        String expected = String.join("\n", Files.readAllLines(Paths.get(PREFIX + ".out"))) + "\n";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testLeetCode746() throws IOException, MuaExceptions, Function.FunctionStop {
        final String PREFIX = "tests/res/testLeetCode746";
        Scanner codeScanner = new Scanner(Paths.get(PREFIX + ".mua"));
        OutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        Interpreter interpreter = new Interpreter(codeScanner, printStream);
        interpreter.run();
        String result = outputStream.toString();
        String expected = String.join("\n", Files.readAllLines(Paths.get(PREFIX + ".out"))) + "\n";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testSaveLoad() throws IOException, MuaExceptions, Function.FunctionStop {
        final String PREFIX = "tests/res/testSaveLoad";
        Scanner codeScanner = new Scanner(Paths.get(PREFIX + ".mua"));
        OutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        Interpreter interpreter = new Interpreter(codeScanner, printStream);
        interpreter.run();
        String result = outputStream.toString();
        String expected = String.join("\n", Files.readAllLines(Paths.get(PREFIX + ".out"))) + "\n";
        Assert.assertEquals(expected, result);
    }
}

