import compiler.Lexer.Lexer;
import compiler.Lexer.Symbols.Symbols;
import compiler.Parser.Parser;
import compiler.Parser.TreeStructure.CustomNode;
import compiler.Semantic_analysis.SemanticAnalyser;
import compiler.Semantic_analysis.SymbolTable;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestParser {

    @Test
    public void test_const() throws Exception {
        String input = """
                const i int = 3;
                const x int = 5 + 3;
                const x int = (5 - 3) + ( 6 + 2 );
                const k int = 6%3;
                const k int = i * 3;
                const k int = (6 / 3) +1;
                const j real = 3.2+5.0;
                const j real = 3.2-5.0;
                const j real = 3.2*5.0;
                const message string = \"Hello\";
                const message string = \"Hello\" + \"World\" + \"World\";
                const isEmpty bool = true;
                const isEmpty bool = true and false or false;
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        String expectedRoot = "StartingPoint";
        String result = parser.getAST();
        assertEquals(expectedRoot, result);

        input ="""
                const i int = 3;
                var l int = 5;
                const l real = 0.05;
                """;
        reader = new StringReader(input);
        lexer = new Lexer(reader);
        parser = new Parser(lexer);
        try {
            CustomNode root = parser.parseStartingPoint();
            fail( "My method didn't throw when I expected it to" );
        } catch (Exception ignored) {
        }

        input ="""
                const i int = 3;
                const l real[] = real[](5);
                """;
        reader = new StringReader(input);
        lexer = new Lexer(reader);
        parser = new Parser(lexer);
        try {
            CustomNode root = parser.parseStartingPoint();
            fail( "My method didn't throw when I expected it to" );
        } catch (Exception ignored) {
        }
    }

    @Test
    public void test_if_else_error_else_without_if() throws Exception {
        /*
        error expected because: string operations, comparing wrong types together, wrong boolean operation for type
         */
        String input = """
                else {
                    var i int  = 6;
                    }
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        try {
            CustomNode root = parser.parseStartingPoint();
            fail("My method didn't throw when I expected it to");
        } catch (Exception ignored) {
        }
    }

    @Test
    public void test_builtin_func() throws Exception {
        //length string or array => array can be in records
        String input = """
                var value int = not(true);
                var value int = chr(5);
                var value int = len(a);
                var value int = len(Point.a);
                var value int = floor(5.3);
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        String expectedRoot = "BlockStatement";
        String result = parser.getAST();
        assertEquals(expectedRoot, result);
    }

    @Test
    public void test_record() throws Exception {
        String input = """
                record Point {
                    x int;
                    y int;
                    history int[];
                }""";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        String expectedRoot = "Records";
        String result = parser.getAST();
        assertEquals(expectedRoot, result);
    }

    @Test
    public void test_global_var() throws Exception {
        String input = """
                var a int = 3;
                var c int[] = int[](5);
                var d Person = Person(\"me\", Point(3,7), int[](a*2));
                var x Point = Point(3,5);
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        String expectedRoot = "BlockStatement";
        String result = parser.getAST();
        assertEquals(expectedRoot, result);
    }

    @Test
    public void test_global_var5() throws Exception {
        String input = "delete x;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        String expectedRoot = "Identifier";
        String result = parser.getAST();
        assertEquals(expectedRoot, result);
    }

    @Test
    public void test_builtin_funcIO() throws Exception {
        String input = """
                var value int = readInt();
                var value real = readReal();
                var value string = readString();
                var value string = writeInt(5);
                var value string = writeReal(5.0);
                var value string = write(\"Hello World\");
                var value string = writeln(\"Hello World\");
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        String expectedRoot = "BlockStatement";
        String result = parser.getAST();
        assertEquals(expectedRoot, result);

    }

    //DON'T FORGET
    //Procedures and while/if/else/for blocks can declare local variables and values
    //mixed with statements.

    @Test
    public void test_reassigning_data() throws Exception {
        String input = """
                    a[3] = 1234;
                    a.x = 123;
                    a[3].x = 12;
                    """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        String expectedRoot = "BlockStatement";
        String result = parser.getAST();
        assertEquals(expectedRoot, result);
    }

    @Test
    public void test_booleanOperations() throws Exception {
        String input = """
                var value bool = 3.6 > 5.6;
                var value bool = 3.6 < 5.6;
                var value bool = 3.6 >= 5.6;
                var value bool = \"hello world\" == true;
                var value bool = 3 <> 5;
                var value bool = 3 >= \"this will be solved by semantic\";
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        String expectedRoot = "BlockStatement";
        String result = parser.getAST();
        assertEquals(expectedRoot, result);
    }

    @Test
    public void test_Control_Structure() throws Exception {
        String input = """
                var i int = 0;
                if e == true {
                    i = i+1;
                }else{
                    i = i - 1;
                }
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        String expectedRoot = "BlockStatement";
        String result = parser.getAST();
        assertEquals(expectedRoot, result);
    }
    @Test
    public void test_while() throws Exception {
        String input = """
                       while e {
                            if (e == true) {
                            } else { 
                              }
                       }
                       """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        String expectedRoot = "ControlStructure";
        String result = parser.getAST();
        assertEquals(expectedRoot, result);
    }
    @Test
    public void test_for() throws Exception {
        String input = "for i=0 to 5 by 1 {" +
                       "}";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        String expectedRoot = "ControlStructure";
        String result = parser.getAST();
        assertEquals(expectedRoot, result);
    }

    @Test
    public void test_proc_and_calls() throws Exception {
        String input = """
                        proc square(v int) int {
                            return v*v;
                        }
                        proc main() void {
                            var value int = readInt();
                            writeln(value);
                            var i int;
                            for i=1 to 100 by 2 {
                                while value<>3 {
                                    if e == true {}else{}
                                }
                            }
                            i = (i+2)*2;
                        }
                        main();
                        square(5);
                        """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        String expectedRoot = "StartingPoint";
        String result = parser.getAST();
        assertEquals(expectedRoot, result);
    }
    @Test
    public void test_comments() throws Exception {
        String input = "var value int = x - 0;// this is a comment\n";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        String expectedRoot = "GlobalVariable";
        String result = parser.getAST();
        assertEquals(expectedRoot, result);
    }
    @Test
    public void test_string_with_quotes() throws Exception {
        String input = "var value int = \"x y \\\"z\\\" x\";";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        String expectedRoot = "GlobalVariable";
        String result = parser.getAST();
        assertEquals(expectedRoot, result);
    }

    /* Return can be multiple things: ==> param?
            1) nouveau record
            2) identifier
            3) operations (string, boolean, numbers)
            4) accessing data
            5)
     */
    @Test
    public void test_operations() throws Exception {
        String input = """
                        var i boolean = true and false;
                        var j int = (1+2)*3;
                        var k int = ((1+2)*3);
                        var l boolean = ((5*\"me\")+5*(3+2));
                        var d Person = Person("me", Point(3,7), int[](a*2));
                        
                        proc copyPoints(p Point[]) Point {
                             return Point(p[0].x+p[1].x, p[0].y+p[1].y);
                        }
                        
                        proc sum(x int, y int) int {
                            return (x * y) + 21; 
                        } 
                        
                        proc getJ() int {
                            return j;
                        }
                        
                        proc getName(p Person) string {
                            return p.name;
                        }
                        """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        String expectedRoot = "StartingPoint";
        String result = parser.getAST();
        assertEquals(expectedRoot, result);
    }
}