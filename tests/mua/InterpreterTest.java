package mua;

public class InterpreterTest {
}

/*
public class InterpreterTest {
    private String test(String[] cmds, String input) throws UnsupportedEncodingException, SyntaxErrorException, LexicalErrorException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream, true, "utf-8");
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Interpreter interpreter = new Interpreter(new Scanner(inputStream), System.out);
        for (String cmd : cmds)
            interpreter.execute(cmd);
        return outputStream.toString("UTF8");
    }

    @Test
    public void testMakeAndPrint() throws UnsupportedEncodingException, LexicalErrorException, SyntaxErrorException {
        final String var = "a";
        final Number value = 10;
        final String[] testCmd = new MuaCmdBuilder()
                .command(Command.kMake).wordOf(var).number(value).newLine()
                .command(Command.kPrint).valueOf(var)
                .build();
        final String expectedResult = value.toString() + "\n";
        String result = test(testCmd, "");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testThing() throws UnsupportedEncodingException, LexicalErrorException, SyntaxErrorException {
        final String var = "a";
        final Number value = 10;
        final String[] testCmd = new MuaCmdBuilder()
                .command(Command.kMake).wordOf(var).number(value).newLine()
                .command(Command.kPrint).command(Command.kThing).wordOf(var) // 10
                .build();
        final String expectedResult = value.toString() + "\n";
        String result = test(testCmd, "");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testEraseAndIsname() throws UnsupportedEncodingException, LexicalErrorException, SyntaxErrorException {
        final String var = "a";
        final Number value = 10;
        final String[] testCmd = new MuaCmdBuilder()
                .command(Command.kMake).wordOf(var).number(value).newLine()
                .command(Command.kPrint).command(Command.kIsname).wordOf(var).newLine() // true
                .command(Command.kErase).wordOf(var).newLine()
                .command(Command.kPrint).command(Command.kIsname).wordOf(var).newLine() // false
                .build();
        final String expectedResult = "true\nfalse\n";
        String result = test(testCmd, "");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testRead() throws UnsupportedEncodingException, LexicalErrorException, SyntaxErrorException {
        final String var = "a";
        final Number value = 10;
        final String[] testCmd = new MuaCmdBuilder()
                .command(Command.kMake).wordOf(var).command(Command.kRead).newLine()
                .command(Command.kPrint).valueOf(var) // 10
                .build();
        final String expectedResult = value.toString() + "\n";
        String result = test(testCmd, value.toString());
        assertEquals(expectedResult, result);
    }

    @Test
    public void testReadlinst() throws UnsupportedEncodingException, LexicalErrorException, SyntaxErrorException {
        final String var = "a";
        final Object[] value = new Object[]{
                "a",
                "b",
                1,
                2,
                3
        };
        final String[] testCmd = new MuaCmdBuilder()
                .command(Command.kMake).wordOf(var).command(Command.kReadlinst).newLine()
                .command(Command.kPrint).valueOf(var)
                .build();
        final String expectedResult = MuaCmdBuilder.makeListWithBrackets(value) + "\n";
        String result = test(testCmd, MuaCmdBuilder.makeList(value));
        assertEquals(expectedResult, result);
    }

    @Test
    public void testAdd() throws UnsupportedEncodingException, LexicalErrorException, SyntaxErrorException {
        final Number a = 1, b = 2, c = a.intValue() + b.intValue();
        final String[] testCmd = new MuaCmdBuilder()
                .command(Command.kPrint).command(Command.kAdd).number(a).number(b) // 3
                .build();
        final String expectedResult = c.toString() + "\n";
        String result = test(testCmd, "");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSub() throws UnsupportedEncodingException, LexicalErrorException, SyntaxErrorException {
        final Number a = 7, b = 2.5, c = a.intValue() - b.doubleValue();
        final String[] testCmd = new MuaCmdBuilder()
                .command(Command.kPrint).command(Command.kSub).number(a).number(b) // 4.5
                .build();
        final String expectedResult = c.toString() + "\n";
        String result = test(testCmd, "");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testMul() throws UnsupportedEncodingException, LexicalErrorException, SyntaxErrorException {
        final Number a = -2.5, b = 2.5, c = a.doubleValue() * b.doubleValue();
        final String[] testCmd = new MuaCmdBuilder()
                .command(Command.kPrint).command(Command.kMul).number(a).number(b) // -6.25
                .build();
        final String expectedResult = c.toString() + "\n";
        String result = test(testCmd, "");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDiv() throws UnsupportedEncodingException, LexicalErrorException, SyntaxErrorException {
        final Number a = -100.0, b = 4, c = a.doubleValue() / b.intValue();
        final String[] testCmd = new MuaCmdBuilder()
                .command(Command.kPrint).command(Command.kDiv).number(a).number(b) // -25
                .build();
        final String expectedResult = c.toString() + "\n";
        String result = test(testCmd, "");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testMod() throws UnsupportedEncodingException, LexicalErrorException, SyntaxErrorException {
        final Number a = 25, b = 4, c = a.intValue() % b.intValue();
        final String[] testCmd = new MuaCmdBuilder()
                .command(Command.kPrint).command(Command.kMod).number(a).number(b) // 1
                .build();
        final String expectedResult = c.toString() + "\n";
        String result = test(testCmd, "");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testEq() throws UnsupportedEncodingException, LexicalErrorException, SyntaxErrorException {
        final String var = "a";
        final Number a = 24.2;
        final String[] testCmd = new MuaCmdBuilder()
                .command(Command.kMake).wordOf(var).number(a).newLine()
                .command(Command.kPrint).command(Command.kEq).valueOf(var).number(a) // true
                .command(Command.kPrint).command(Command.kEq).valueOf(var).number(a.doubleValue() * 2) // false
                .build();
        final String expectedResult = "true\nfalse\n";
        String result = test(testCmd, "");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGt() throws UnsupportedEncodingException, LexicalErrorException, SyntaxErrorException {
        final String var = "a";
        final Number a = 24;
        final String[] testCmd = new MuaCmdBuilder()
                .command(Command.kMake).wordOf(var).number(a).newLine()
                .command(Command.kPrint).command(Command.kGt).valueOf(var).number(a.intValue() - 1) // true
                .command(Command.kPrint).command(Command.kGt).valueOf(var).number(a.intValue() + 1) // false
                .build();
        final String expectedResult = "true\nfalse\n";
        String result = test(testCmd, "");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testLt() throws UnsupportedEncodingException, LexicalErrorException, SyntaxErrorException {
        final String var = "a";
        final Number a = 24;
        final String[] testCmd = new MuaCmdBuilder()
                .command(Command.kMake).wordOf(var).number(a).newLine()
                .command(Command.kPrint).command(Command.kLt).valueOf(var).number(a.intValue() + 1) // true
                .command(Command.kPrint).command(Command.kLt).valueOf(var).number(a.intValue() - 1) // false
                .build();
        final String expectedResult = "true\nfalse\n";
        String result = test(testCmd, "");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testAnd() throws UnsupportedEncodingException, LexicalErrorException, SyntaxErrorException {
        final String var = "a";
        final Number a = 24;
        final String[] testCmd = new MuaCmdBuilder()
                .command(Command.kMake).wordOf(var).number(a).newLine()
                .command(Command.kPrint).command(Command.kAnd)
                .command(Command.kLt).valueOf(var).number(a.intValue() + 1) // true
                .command(Command.kGt).valueOf(var).number(a.intValue() - 1) // true
                .build();
        final String expectedResult = "true\n";
        String result = test(testCmd, "");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testOr() throws UnsupportedEncodingException, LexicalErrorException, SyntaxErrorException {
        final String var = "a";
        final Number a = 24;
        final String[] testCmd = new MuaCmdBuilder()
                .command(Command.kMake).wordOf(var).number(a).newLine()
                .command(Command.kPrint).command(Command.kOr)
                .command(Command.kGt).valueOf(var).number(a.intValue() + 1) // false
                .command(Command.kLt).valueOf(var).number(a.intValue() - 1) // false
                .build();
        final String expectedResult = "false\n";
        String result = test(testCmd, "");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testNot() throws UnsupportedEncodingException, LexicalErrorException, SyntaxErrorException {
        final String var = "a";
        final Number a = 24;
        final String[] testCmd = new MuaCmdBuilder()
                .command(Command.kMake).wordOf(var).number(a).newLine()
                .command(Command.kPrint).command(Command.kNot)
                .command(Command.kGt).valueOf(var).number(a.intValue() + 1) // true
                .build();
        final String expectedResult = "true\n";
        String result = test(testCmd, "");
        assertEquals(expectedResult, result);
    }

    private enum Command {
        kMake("make"),
        kThing("thing"),
        kErase("erase"),
        kIsname("isname"),
        kPrint("print"),
        kRead("read"),
        kReadlinst("readlinst"),
        kAdd("add"),
        kSub("sub"),
        kMul("mul"),
        kDiv("div"),
        kMod("mod"),
        kEq("eq"),
        kGt("gt"),
        kLt("lt"),
        kAnd("and"),
        kOr("or"),
        kNot("not");

        private String mStr;

        Command(String str) {
            mStr = str;
        }

        @Override
        public String toString() {
            return mStr;
        }
    }

    private static class MuaCmdBuilder {
        private StringBuilder mBuilder = new StringBuilder();
        private ArrayList<String> mCommands = new ArrayList<>();

        static private String makeList(Object[] objects, boolean withBrackets) {
            StringBuilder builder = new StringBuilder();
            if (withBrackets)
                builder.append("[");
            for (int i = 0; i < objects.length; ++i) {
                if (i < objects.length - 1)
                    builder.append(objects[i]).append(" ");
                else
                    builder.append(objects[i]);
            }
            if (withBrackets)
                builder.append("]");
            return builder.toString();
        }

        static String makeList(Object[] objects) {
            return makeList(objects, false);
        }

        static String makeListWithBrackets(Object[] objects) {
            return makeList(objects, true);
        }

        private MuaCmdBuilder appendLiteral(String literal) {
            mBuilder.append(literal);
            return this;
        }

        private MuaCmdBuilder append(Object value) {
            mBuilder.append(value).append(" ");
            return this;
        }

        MuaCmdBuilder newLine() {
            if (mBuilder.length() > 0) {
                mCommands.add(mBuilder.toString());
                mBuilder = new StringBuilder();
            }
            return this;
        }

        String[] build() {
            newLine();
            return mCommands.toArray(new String[]{});
        }

        private MuaCmdBuilder command(Command cmd) {
            return append(cmd.toString());
        }

        MuaCmdBuilder number(Number number) {
            return append(number);
        }

        MuaCmdBuilder wordOf(String var) {
            return appendLiteral("\"").append(var);
        }

        MuaCmdBuilder valueOf(String var) {
            return appendLiteral(":").append(var);
        }

        private MuaCmdBuilder numberOrWord(Object value) {
            if (value instanceof String)
                valueOf((String) value);
            else if (value instanceof Number)
                append(value);
            return this;
        }

        public MuaCmdBuilder list(Object[] objects) {
            appendLiteral("[");
            for (int i = 0; i < objects.length; ++i) {
                if (i < objects.length - 1)
                    append(objects[i]);
                else
                    appendLiteral(objects[i].toString());
            }
            return appendLiteral("]");
        }
    }
}
*/