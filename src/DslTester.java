
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * a detailed tester for A4 computer systems
 * 
 * @author syntex
 */
public class DslTester {

    @Test
    public void declarationsTest() {
        String expected = "ok";

        List<String> program = new ArrayList<String>();

        program.add("int a;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void declarationsDuplicateTest() {
        String expected = "error";

        List<String> program = new ArrayList<String>();

        program.add("int num;");
        program.add("int num;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void multipleIdentifiersInDeclaration() {
        String expected = "ok";

        List<String> program = new ArrayList<String>();

        program.add("int num, num1, num2;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void multipleIdentifiersInDeclarationWithDuplicates() {
        String expected = "error";

        List<String> program = new ArrayList<String>();

        program.add("int num, num1, num2, num1;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void multipleRandomDeclarations() {
        String expected = "ok";

        List<String> program = new ArrayList<String>();

        program.add("int a, b, c, d;");
        program.add("int e, f, g, h;");
        program.add("int i;");
        program.add("int j;");
        program.add("int k, l;");
        program.add("int m;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void multipleRandomDeclarationInvalid() {
        String expected = "error";

        List<String> program = new ArrayList<String>();

        program.add("int a, b, c, d;");
        program.add("int e, f, g, h;");
        program.add("int 2i;");
        program.add("int j;");
        program.add("int k, l;");
        program.add("int m;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void multipleRandomDeclarationsWithDuplicates() {
        String expected = "error";

        List<String> program = new ArrayList<String>();

        program.add("int a, b, c, d;");
        program.add("int e, f, g, h;");
        program.add("int i;");
        program.add("int j, l;");
        program.add("int k, l;");
        program.add("int m;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testValidProgramWithMultipleIntegerDeclarations() {
        List<String> program = new ArrayList<String>();
        program.add("int a;");
        program.add("int b;");
        program.add("int c;");
        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();
        assertEquals("ok", output);
    }

    @Test
    public void testValidProgramWithMultipleIntegerDeclarationsAndDuplicates() {
        List<String> program = new ArrayList<String>();
        program.add("int a;");
        program.add("int b;");
        program.add("int a;");
        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();
        assertEquals("error", output);
    }

    @Test
    public void testEmptyProgramReturnsError() {
        List<String> program = new ArrayList<String>();
        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();
        assertEquals("error", output);
    }

    @Test
    public void testProgramWithOnlySemicolonReturnsError() {
        List<String> program = new ArrayList<String>();
        program.add(";");
        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();
        assertEquals("error", output);
    }

    @Test
    public void testSimpleAssignment() {
        String expected = "ok";

        List<String> program = new ArrayList<String>();

        program.add("int a;");
        program.add("a := 2;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testSimpleAssignmentWithInvalidVariable() {
        String expected = "error";

        List<String> program = new ArrayList<String>();

        program.add("int a;");
        program.add("a1 := 2;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testMultipleSimpleAssignment() {
        String expected = "ok";

        List<String> program = new ArrayList<String>();

        program.add("int a;");
        program.add("a := 2;");
        program.add("a := 2;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testSimpleAssignmentWithOperator() {
        String expected = "ok";

        List<String> program = new ArrayList<String>();

        program.add("int a;");
        program.add("a := 2 + 3;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testSimpleAssignmentWithOperatorAndNoSecondTerm() {
        String expected = "error";

        List<String> program = new ArrayList<String>();

        program.add("int a;");
        program.add("a := 2 + ;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testAssignments() {
        String expected = "ok";

        List<String> program = new ArrayList<String>();

        program.add("int a;");
        program.add("a := 2 + (3 + 4);");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testAssignmentsEasy() {
        String expected = "ok";

        List<String> program = new ArrayList<String>();

        program.add("int a;");
        program.add("a := 5 + (3 + 4);");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testAssignmentsMeduim() {
        String expected = "ok";

        List<String> program = new ArrayList<String>();

        program.add("int a;");
        program.add("a := ((3 + 4) + (3 + 4)) + ((3 + 4) + (3 + 4));");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testAssignmentsHard() {
        String expected = "ok";

        List<String> program = new ArrayList<String>();

        program.add("int a;");
        program.add("a := (---(3 + -4) + ----(3 + -4)) - (---(3 + -4) + ----(3 + -4));");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testAssignmentsEasyInvalid() {
        String expected = "error";

        List<String> program = new ArrayList<String>();

        program.add("int a;");
        program.add("a := 5 + (3 + );");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testAssignmentsMeduimInvalid() {
        String expected = "error";

        List<String> program = new ArrayList<String>();

        program.add("int a;");
        program.add("a := ((3 + 4) + (3 + 4) + ((3 + 4) + (3 + 4));");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testAssignmentsHardInvalid() {
        String expected = "error";

        List<String> program = new ArrayList<String>();

        program.add("int a;");
        program.add("a := (---(3 + -4) + ----( + -4)) - (---(3 + -4) + ----(3 + -4));");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testAssignmentsExtreme() {
        String expected = "ok";

        List<String> program = new ArrayList<String>();

        program.add("int a;");
        program.add(
                "a := ----((-----((-----((-a + a) + (1 + -3)) + ----((-a + a) + (1 + -3))) + (----((-a + a) + (1 + -3)) + -3)) + a) + (1 + -----((-a + a) + (1 + -3))));");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);

    }

    @Test
    public void testAssignmentsExtremeInvalid() {
        String expected = "error";

        List<String> program = new ArrayList<String>();

        program.add("int a;");
        program.add(
                "a := ----((-----((-----((-a + a) + (1 + -3)) + ----((-a + ) + (1 + -3))) + (----((-a + a) + (1 + -3)) + -3)) + a) + (1 + -----((-a + a) + (1 + -3))));");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testSimpleConditional() {
        String expected = "ok";

        List<String> program = new ArrayList<String>();

        program.add("int x;");
        program.add("x := 10;");
        program.add("if x > 5 then");
        program.add("print x;");
        program.add("fi;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testConditionalWithElse() {
        String expected = "ok";

        List<String> program = new ArrayList<String>();

        program.add("int x;");
        program.add("x := 10;");
        program.add("if x > 5 then");
        program.add("print x;");
        program.add("else");
        program.add("print -x;");
        program.add("fi;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testNestedConditionals() {
        String expected = "ok";

        List<String> program = new ArrayList<String>();

        program.add("int x, y;");
        program.add("x := 10;");
        program.add("y := 5;");
        program.add("if x > y then");
        program.add("if (x + y) > 15 then");
        program.add("print x + y;");
        program.add("fi;");
        program.add("fi;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testComplexNestedConditionalsWithErrors() {
        String expected = "error";

        List<String> program = new ArrayList<String>();

        program.add("int x, y, z;");
        program.add("x := 10;");
        program.add("y := 5;");
        program.add("z := 8;");
        program.add("if x > y then");
        program.add("if z > 10 then");
        program.add("print z;");
        program.add("fi;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testConditionalInvalidExpression() {
        String expected = "ok";

        List<String> program = new ArrayList<String>();

        program.add("int x, y;");
        program.add("x := 10;");
        program.add("y := 5;");
        program.add("if x + y then");
        program.add("print x;");
        program.add("fi;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testConditionalsWithAbsurdExpression() {
        String expected = "ok";

        List<String> program = new ArrayList<String>();

        program.add("int num, sum;");
        program.add("int ch;");
        program.add("");
        program.add("sum := 0;");
        program.add("num := 1;");
        program.add("ch := 65;");
        program.add("");
        program.add("if (num != 0) + (ch != 127)");
        program.add("    then");
        program.add("        sum := sum + (num - 2);");
        program.add("        ch := ch - 1;");
        program.add("        if (num != 0) + (ch != 127)");
        program.add("            then");
        program.add("                sum := sum + (num - 2);");
        program.add("                ch := ch - 1;");
        program.add("             fi;");
        program.add("    else");
        program.add("        sum := sum - num;");
        program.add("        ch := ch + 1;");
        program.add("        if (num != 0) + (ch != 127)");
        program.add("            then");
        program.add("                sum := sum + (num - 2);");
        program.add("                ch := ch - 1;");
        program.add("                if (num != 0) + (ch != 127)");
        program.add("                    then");
        program.add("                        sum := sum + (num - 2);");
        program.add("                        ch := ch - 1;");
        program.add("                    fi;");
        program.add("            else");
        program.add("                if (num != 0) + (ch != 127)");
        program.add("    then");
        program.add("        sum := sum + (num - 2);");
        program.add("        ch := ch - 1;");
        program.add("        if (num != 0) + (ch != 127)");
        program.add("            then");
        program.add("                sum := sum + (num - 2);");
        program.add("                ch := ch - 1;");
        program.add("             fi;");
        program.add("    else");
        program.add("        sum := sum - num;");
        program.add("        ch := ch + 1;");
        program.add("        if (num != 0) + (ch != 127)");
        program.add("            then");
        program.add("                sum := sum + (num - 2);");
        program.add("                ch := ch - 1;");
        program.add("                if (num != 0) + (ch != 127)");
        program.add("                    then");
        program.add("                        sum := sum + (num - 2);");
        program.add("                        ch := ch - 1;");
        program.add("                    fi;");
        program.add("            else");
        program.add("                sum := sum - num;");
        program.add("                if (num != 0) + (ch != 127)");
        program.add("                    then");
        program.add("                        sum := sum + (num - 2);");
        program.add("                        ch := ch - 1;");
        program.add("                        if (num != 0) + (ch != 127)");
        program.add("                            then");
        program.add("                                sum := sum + (num - 2);");
        program.add("                                ch := ch - 1;");
        program.add("                            fi;");
        program.add("                    else");
        program.add("                        sum := sum - num;");
        program.add("                        ch := ch + 1;");
        program.add("                    fi;");
        program.add("                if (num != 0) + (ch != 127)");
        program.add("                    then");
        program.add("                        sum := sum + (num - 2);");
        program.add("                        ch := ch - 1;");
        program.add("                        if (num != 0) + (ch != 127)");
        program.add("                            then");
        program.add("                                sum := sum + (num - 2);");
        program.add("                                ch := ch - 1;");
        program.add("                            fi;");
        program.add("                    else");
        program.add("                        sum := sum - num;");
        program.add("                        ch := ch + 1;");
        program.add("                    fi;");
        program.add("            fi;");
        program.add("     fi;");
        program.add("                sum := sum - num;");
        program.add("                if (num != 0) + (ch != 127)");
        program.add("                    then");
        program.add("                        sum := sum + (num - 2);");
        program.add("                        ch := ch - 1;");
        program.add("                        if (num != 0) + (ch != 127)");
        program.add("                            then");
        program.add("                                sum := sum + (num - 2);");
        program.add("                                ch := ch - 1;");
        program.add("                            fi;");
        program.add("                    else");
        program.add("                        sum := sum - num;");
        program.add("                        ch := ch + 1;");
        program.add("                    fi;");
        program.add("                if (num != 0) + (ch != 127)");
        program.add("                    then");
        program.add("                        sum := sum + (num - 2);");
        program.add("                        ch := ch - 1;");
        program.add("                        if (num != 0) + (ch != 127)");
        program.add("                            then");
        program.add("                                sum := sum + (num - 2);");
        program.add("                                ch := ch - 1;");
        program.add("                            fi;");
        program.add("                    else");
        program.add("                        sum := sum - num;");
        program.add("                        ch := ch + 1;");
        program.add("                    fi;");
        program.add("            fi;");
        program.add("     fi;");
        program.add("");
        program.add("");
        program.add("");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testConditionalsWithAbsurdExpressionInvalid() {
        String expected = "error";

        List<String> program = new ArrayList<String>();

        program.add("int num, sum;");
        program.add("int ch;");
        program.add("");
        program.add("sum := 0;");
        program.add("num := 1;");
        program.add("ch := 65;");
        program.add("");
        program.add("if (num != 0) + (ch != 127)");
        program.add("    then");
        program.add("        sum := sum + (num - 2);");
        program.add("        ch := ch - 1;");
        program.add("        if (num != 0) + (ch != 127)");
        program.add("            then");
        program.add("                sum := sum + (num - 2);");
        program.add("                ch := ch - 1;");
        program.add("             fi;");
        program.add("    else");
        program.add("        sum := sum - num;");
        program.add("        ch := ch + 1;");
        program.add("        if (num != 0) + (ch != 127)");
        program.add("            then");
        program.add("                sum := sum + (num - 2);");
        program.add("                ch := ch - 1;");
        program.add("                if (num != 0) + (ch != 127)");
        program.add("                    then");
        program.add("                        sum := sum + (num - 2);");
        program.add("                        ch := ch - 1;");
        program.add("                    fi;");
        program.add("            else");
        program.add("                if (num != 0) + (ch != 127)");
        program.add("    then");
        program.add("        sum := sum + (num - 2);");
        program.add("        ch := ch - 1;");
        program.add("        if (num != 0) + (ch != 127)");
        program.add("            then");
        program.add("                sum := sum + (num - 2);");
        program.add("                ch := ch - 1;");
        program.add("             fi;");
        program.add("    else");
        program.add("        sum := sum - num;");
        program.add("        ch := ch + 1;");
        program.add("        if (num != 0) + (ch != 127)");
        program.add("            then");
        program.add("                sum := sum + (num - 2);");
        program.add("                ch := ch - 1;");
        program.add("                if (num != 0) + (ch != 127)");
        program.add("                    then");
        program.add("                        sum := sum + (num - 2);");
        program.add("                        ch := ch - 1;");
        program.add("                    fi;");
        program.add("            else");
        program.add("                sum := sum - num;");
        program.add("                if (num != 0) + (ch != 127)");
        program.add("                    then");
        program.add("                        sum := sum + (num - 2);");
        program.add("                        ch := ch - 1;");
        program.add("                        if (num != 0) + (ch != 127)");
        program.add("                            then");
        program.add("                                sum := sum + (num - 2);");
        program.add("                                ch := ch - 1;");
        program.add("                            fi;");
        program.add("                    else");
        program.add("                        sum := sum - num;");
        program.add("                        ch := ch + 1;");
        program.add("                    fi");
        program.add("                if (num != 0) + (ch != 127)");
        program.add("                    then");
        program.add("                        sum := sum + (num - 2);");
        program.add("                        ch := ch - 1;");
        program.add("                        if (num != 0) + (ch != 127)");
        program.add("                            then");
        program.add("                                sum := sum + (num - 2);");
        program.add("                                ch := ch - 1;");
        program.add("                            fi;");
        program.add("                    else");
        program.add("                        sum := sum - num;");
        program.add("                        ch := ch + 1;");
        program.add("                    fi;");
        program.add("            fi;");
        program.add("     fi;");
        program.add("                sum := sum - num;");
        program.add("                if (num != 0) + (ch != 127)");
        program.add("                    then");
        program.add("                        sum := sum + (num - 2);");
        program.add("                        ch := ch - 1;");
        program.add("                        if (num != 0) + (ch != 127)");
        program.add("                            then");
        program.add("                                sum := sum + (num - 2);");
        program.add("                                ch := ch - 1;");
        program.add("                            fi;");
        program.add("                    else");
        program.add("                        sum := sum - num;");
        program.add("                        ch := ch + 1;");
        program.add("                    fi;");
        program.add("                if (num != 0) + (ch != 127)");
        program.add("                    then");
        program.add("                        sum := sum + (num - 2);");
        program.add("                        ch := ch - 1;");
        program.add("                        if (num != 0) + (ch != 127)");
        program.add("                            then");
        program.add("                                sum := sum + (num - 2);");
        program.add("                                ch := ch - 1;");
        program.add("                            fi;");
        program.add("                    else");
        program.add("                        sum := sum - num;");
        program.add("                        ch := ch + 1;");
        program.add("                    fi;");
        program.add("            fi;");
        program.add("     fi;");
        program.add("");
        program.add("");
        program.add("");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testLoopValid() {
        String expected = "ok";

        List<String> program = new ArrayList<String>();

        program.add("int num, sum;");
        program.add("sum := 0;");
        program.add("num := 1;");
        program.add("while num < 10 do");
        program.add("    sum := sum + num;");
        program.add("    num := num + 1;");
        program.add("od;");
        program.add("print sum;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testLoopInvalid() {
        String expected = "error";

        List<String> program = new ArrayList<String>();

        program.add("int num, sum;");
        program.add("sum := 0;");
        program.add("num := 1;");
        program.add("while num < 10 do");
        program.add("    sum := sum + num;");
        program.add("    num := num + a;"); // Using an undeclared identifier 'a'
        program.add("od;");
        program.add("print sum;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testLoopWithNestedConditionalValid() {
        String expected = "ok";

        List<String> program = new ArrayList<String>();

        program.add("int num, sum, flag;");
        program.add("sum := 0;");
        program.add("num := 1;");
        program.add("flag := 1;");
        program.add("while num < 6 do");
        program.add("    if num > 3 then");
        program.add("        sum := sum + 2;");
        program.add("    else");
        program.add("        sum := sum + 1;");
        program.add("    fi;");
        program.add("    num := num + 1;");
        program.add("od;");
        program.add("print sum;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testLoopWithInvalidAssignmentInsideConditional() {
        String expected = "error";

        List<String> program = new ArrayList<String>();

        program.add("int num, sum, flag;");
        program.add("sum := 0;");
        program.add("num := 1;");
        program.add("flag := 1;");
        program.add("while num < 6 do");
        program.add("    if num > 3 then");
        program.add("        sum := sum + 2;");
        program.add("    else");
        program.add("        sum := sum + a;"); // Using an undeclared identifier 'a' inside conditional statement
        program.add("    fi;");
        program.add("    num := num + 1;");
        program.add("od;");
        program.add("print sum;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testSimplePrint() {
        String expected = "ok";

        List<String> program = new ArrayList<String>();

        program.add("int num, sum, flag;");
        program.add("print num;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testEntireProgram1() {
        String expected = "ok";

        List<String> program = new ArrayList<String>();

        program.add("int num, sum;");
        program.add("int ch;");
        program.add("sum := 0;");
        program.add("num := 1;");
        program.add("ch := 65;");
        program.add("while ch < 128");
        program.add("do");
        program.add("    if (num != 0) + (ch != 127)");
        program.add("    then");
        program.add("        sum := sum + (num - 2);");
        program.add("        ch := ch - 1;");
        program.add("    else");
        program.add("        sum := sum - num;");
        program.add("        ch := ch + 1;");
        program.add("    fi;");
        program.add("    num := num + 1; ");
        program.add("od;");
        program.add("print sum;");
        program.add("print ch;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testEntireProgram2() {
        String expected = "ok";

        List<String> program = new ArrayList<String>();

        program.add("int num, sum;");
        program.add("int ch;");
        program.add("sum := 0;");
        program.add("num := 1;");
        program.add("ch := 65;");
        program.add("while ch < 128");
        program.add("do");
        program.add("    if (num != 0) + (ch != 127)");
        program.add("    then");
        program.add("        sum := sum + (num - 2);");
        program.add("        ch := ch - 1;");
        program.add("        while ch < 128");
        program.add("do");
        program.add("    if (num != 0) + (ch != 127)");
        program.add("    then");
        program.add("        sum := sum + (num - 2);");
        program.add("        ch := ch - 1;");
        program.add("    else");
        program.add("        sum := sum - num;");
        program.add("        ch := ch + 1;");
        program.add("    fi;");
        program.add("    num := num + 1; ");
        program.add("od;");
        program.add("    else");
        program.add("        sum := sum - num;");
        program.add("        ch := ch + 1;");
        program.add("    fi;");
        program.add("    num := num + 1; ");
        program.add("od;");
        program.add("print sum;");
        program.add("print ch;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testEntireProgram3() {
        String expected = "ok";

        List<String> program = new ArrayList<String>();

        program.add("int num, sum;");
        program.add("int ch;");
        program.add("sum := 0;");
        program.add("num := 1;");
        program.add("ch := 65;");
        program.add("while ch < 128");
        program.add("do");
        program.add(
                "    if ((((1 + 3) + ((1 + 3) + ((1 + 3) + 3))) + (((1 + (1 + 3)) + 3) + (1 + 3))) + ((((1 + 3) + (1 + 2)))))");
        program.add("    then");
        program.add("while ch < 128");
        program.add("    do");
        program.add(
                "        if ((((1 + 3) + ((1 + 3) + ((1 + 3) + 3))) + (((1 + (1 + 3)) + 3) + (1 + 3))) + ((((1 + 3) + (1 + 2)))))");
        program.add("        then");
        program.add("while ch < 128");
        program.add("    do");
        program.add(
                "        if ((((1 + 3) + ((1 + 3) + ((1 + 3) + 3))) + (((1 + (1 + 3)) + 3) + (1 + 3))) + ((((1 + 3) + (1 + 2)))))");
        program.add("        then");
        program.add("            sum := sum + (num - 2);");
        program.add("            ch := ch - 1;");
        program.add("        else");
        program.add("            sum := sum - num;");
        program.add("            ch := ch + 1;");
        program.add("        fi;");
        program.add("        num := num + 1; ");
        program.add("    od;");
        program.add("        else");
        program.add("while ch < 128");
        program.add("    do");
        program.add(
                "        if ((((1 + 3) + ((1 + 3) + ((1 + 3) + 3))) + (((1 + (1 + 3)) + 3) + (1 + 3))) + ((((1 + 3) + (1 + 2)))))");
        program.add("        then");
        program.add("            sum := sum + (num - 2);");
        program.add("            ch := ch - 1;");
        program.add("        else");
        program.add("            sum := sum - num;");
        program.add("            ch := ch + 1;");
        program.add("        fi;");
        program.add("        num := num + 1; ");
        program.add("    od;");
        program.add("        fi;");
        program.add("        num := num + 1; ");
        program.add("    od;");
        program.add("    else");
        program.add("while ch < 128");
        program.add("    do");
        program.add(
                "        if ((((1 + 3) + ((1 + 3) + ((1 + 3) + 3))) + (((1 + (1 + 3)) + 3) + (1 + 3))) + ((((1 + 3) + (1 + 2)))))");
        program.add("        then");
        program.add("            sum := sum + (num - 2);");
        program.add("            ch := ch - 1;");
        program.add("        else");
        program.add("            sum := sum - num;");
        program.add("            ch := ch + 1;");
        program.add("        fi;");
        program.add("        num := num + 1; ");
        program.add("    od;");
        program.add("    fi;");
        program.add("    num := num + 1; ");
        program.add("od;");
        program.add("");
        program.add("print sum;");
        program.add("print ch;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testPrintInvalid() {
        String expected = "error";

        List<String> program = new ArrayList<String>();

        program.add("int num, sum;");
        program.add("int ch;");
        program.add("sum := 0;");
        program.add("num := 1;");
        program.add("print sum;");
        program.add("print ch1;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testPrintInvalid2() {
        String expected = "error";

        List<String> program = new ArrayList<String>();

        program.add("int num, sum;");
        program.add("int ch;");
        program.add("sum := 0;");
        program.add("num := 1;");
        program.add("print (sum + ch) + num1;");
        program.add("print ch;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testPrintInvalid3() {
        List<String> program = new ArrayList<String>();

        program.add("int num, sum;");
        program.add("int ch;");
        program.add("sum := 0;");
        program.add("num := 1;");
        program.add("print sum;");
        program.add("print ch1;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals("error", output);
    }

    @Test
    public void testSimpleProgramWithCustomFormatting() {
        String expected = "ok";

        List<String> program = new ArrayList<String>();

        program.add("int num,sum;");
        program.add("int ch;");
        program.add("sum:=0;");
        program.add("num:=1;");
        program.add("print sum;");
        program.add("print ch;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void testBiggerProgramWithCustomFormatting() {
        String expected = "error";

        List<String> program = new ArrayList<String>();

        program.add("int num,sum,flag;");
        program.add("sum:=0;");
        program.add("num:=1;");
        program.add("flag:=1;");
        program.add("while num<6 do");
        program.add("if num>3 then");
        program.add("sum:=sum+2;");
        program.add("else");
        program.add("sum:=sum+a;"); // Using an undeclared identifier 'a' inside conditional statement
        program.add("fi;");
        program.add("num:=num+1;");
        program.add("od;");
        program.add("print sum;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void finalHugeProgramWithComplexSyntaxValid() {
        String expected = "ok";

        List<String> program = new ArrayList<String>();

        program.add("int x, ch;");
        program.add("x := 10;");
        program.add("ch := 1;");
        program.add("");
        program.add("while ch < 128 do");
        program.add("    while ch < 128 do");
        program.add("        while ch < 128 do");
        program.add(
                "    if ((((1 + 3) + ((1 + 3) + ((1 + 3) + 3))) + (((1 + (1 + 3)) + 3) + (1 + 3))) + ((((1 + 3) + (1 + 2))))) then");
        program.add(
                "        if ((((1 + 3) + ((1 + 3) + ((1 + 3) + 3))) + (((1 + (1 + 3)) + 3) + (1 + 3))) + ((((1 + 3) + (1 + 2))))) then");
        program.add(
                "            if ((((1 + 3) + ((1 + 3) + ((1 + 3) + 3))) + (((1 + (1 + 3)) + 3) + (1 + 3))) + ((((1 + 3) + (1 + 2))))) then");
        program.add(
                "                if ((((1 + 3) + ((1 + 3) + ((1 + 3) + 3))) + (((1 + (1 + 3)) + 3) + (1 + 3))) + ((((1 + 3) + (1 + 2))))) then");
        program.add(
                "                    if ((((1 + 3) + ((1 + 3) + ((1 + 3) + 3))) + (((1 + (1 + 3)) + 3) + (1 + 3))) + ((((1 + 3) + (1 + 2))))) then");
        program.add(
                "                            if ((((1 + 3) + ((1 + 3) + ((1 + 3) + 3))) + (((1 + (1 + 3)) + 3) + (1 + 3))) + ((((1 + 3) + (1 + 2))))) then");
        program.add(
                "            if ((((1 + 3) + ((1 + 3) + ((1 + 3) + 3))) + (((1 + (1 + 3)) + 3) + (1 + 3))) + ((((1 + 3) + (1 + 2))))) then");
        program.add(
                "                if ((((1 + 3) + ((1 + 3) + ((1 + 3) + 3))) + (((1 + (1 + 3)) + 3) + (1 + 3))) + ((((1 + 3) + (1 + 2))))) then");
        program.add(
                "                    if ((((1 + 3) + ((1 + 3) + ((1 + 3) + 3))) + (((1 + (1 + 3)) + 3) + (1 + 3))) + ((((1 + 3) + (1 + 2))))) then");
        program.add("                        print x;");
        program.add("                        while ch < 128 do");
        program.add("                            while ch < 128 do");
        program.add("                                while ch < 128 do");
        program.add("                                print ch;");
        program.add("                    od;od;od;fi;fi;fi;fi;fi;fi;fi;fi;else");
        program.add(
                "        print ----((((1 + 3) + ((1 + 3) + ((1 + 3) + 3))) + (((1 + (1 + 3)) + 3) + (1 + 3))) + ((((1 + 3) + (1 + 2)))));");
        program.add("    fi;");
        program.add("od;od;od;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    @Test
    public void finalHugeProgramWithComplexSyntaxInvalid() {
        String expected = "error";

        List<String> program = new ArrayList<String>();

        program.add("int x, ch;");
        program.add("x := 10;");
        program.add("ch := 1;");
        program.add("");
        program.add("while ch < 128 do");
        program.add("    while ch < 128 do");
        program.add("        while ch < 128 do");
        program.add(
                "    if ((((1 + 3) + ((1 + 3) + ((1 + 3) + 3))) + (((1 + (1 + 3)) + 3) + (1 + 3))) + ((((1 + 3) + (1 + 2))))) then");
        program.add(
                "        if ((((1 + 3) + ((1 + 3) + ((1 + 3) + 3))) + (((1 + (1 + 3)) + 3) + (1 + 3))) + ((((1 + 3) + (1 + 2))))) then");
        program.add(
                "            if ((((1 + 3) + ((1 + 3) + ((1 + 3) + 3))) + (((1 + (1 + 3)) + 3) + (1 + 3))) + ((((1 + 3) + (1 + 2))))) then");
        program.add(
                "                if ((((1 + 3) + ((1 + 3) + ((1 + 3) + 3))) + (((1 + (1 + 3)) + 3) + (1 + 3))) + ((((1 + 3) + (1 + 2))))) then");
        program.add(
                "                    if (((1 + 3) + ((1 + 3) + ((1 + 3) + 3))) + (((1 + (1 + 3)) + 3) + (1 + 3))) + ((((1 + 3) + (1 + 2))))) then");
        program.add(
                "                            if ((((1 + 3) + ((1 + 3) + ((1 + 3) + 3))) + (((1 + (1 + 3)) + 3) + (1 + 3))) + ((((1 + 3) + (1 + 2))))) then");
        program.add(
                "            if ((((1 + 3) + ((1 + 3) + ((1 + 3) + 3))) + (((1 + (1 + 3)) + 3) + (1 + 3))) + ((((1 + 3) + (1 + 2))))) then");
        program.add(
                "                if ((((1 + 3) + ((1 + 3) + ((1 + 3) + 3))) + (((1 + (1 + 3)) + 3) + (1 + 3))) + ((((1 + 3) + (1 + 2))))) then");
        program.add(
                "                    if ((((1 + 3) + ((1 + 3) + ((1 + 3) + 3))) + (((1 + (1 + 3)) + 3) + (1 + 3))) + ((((1 + 3) + (1 + 2))))) then");
        program.add("                        print x;");
        program.add("                        while ch < 128 do");
        program.add("                            while ch < 128 do");
        program.add("                                while ch < 128 do");
        program.add("                                print ch;");
        program.add("                    od;od;od;fi;fi;fi;fi;fi;fi;fi;fi;else");
        program.add(
                "        print ----((((1 + 3) + ((1 + 3) + ((1 + 3) + 3))) + (((1 + (1 + 3)) + 3) + (1 + 3))) + ((((1 + 3) + (1 + 2)))));");
        program.add("    fi;");
        program.add("od;od;od;");

        DSLTestFile test = new DSLTestFile(program);
        String output = test.testProgramOutput();

        assertEquals(expected, output);
    }

    private class DSLTestFile {

        private List<String> content;
        private Path tempFile;

        public DSLTestFile(List<String> content) {
            this.content = content;
            try {
                this.tempFile = Files.createTempFile("temporaryTestProgram", ".dsl");
                this.writeToFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void writeToFile() {
            try (FileWriter writer = new FileWriter(tempFile.toFile())) {
                for (String line : content) {
                    writer.write(line);
                    writer.write(System.lineSeparator());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void deleteFile() {
            try {
                Files.deleteIfExists(tempFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public File getFile() {
            return this.tempFile.toFile();
        }

        public String testProgramOutput() {
            String[] arguments = { this.getFile().getAbsolutePath() };

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(outputStream);

            PrintStream originalOut = System.out;
            System.setOut(printStream);

            Main.main(arguments);

            System.setOut(originalOut);

            String programOutput = outputStream.toString().replace(System.lineSeparator(), "");

            this.deleteFile();

            return programOutput;
        }
    }
}
