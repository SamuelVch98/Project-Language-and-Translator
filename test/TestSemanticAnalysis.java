import compiler.Lexer.Lexer;
import compiler.Parser.Parser;
import compiler.Parser.StartingPoint;
import compiler.Parser.TreeStructure.CustomNode;
import compiler.Semantic_analysis.SemanticAnalyser;
import compiler.Semantic_analysis.SemanticException;
import compiler.Semantic_analysis.SymbolTable;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.fail;

public class TestSemanticAnalysis {
    // Procedure
    @Test
    public void test_proc_return_type_and_call() throws Exception {
        /*
        type possible: string, int, real, boolean, record, array
         */
        String input = """
                record Point {
                    x int;
                    y int;
                }
                                
                proc square(v int) int {
                    return (v*v)+v;
                }
                                
                proc copyPoints(p Point[]) Point {
                     return Point(p[0].x+p[1].x, p[0].y+p[1].y);
                }
                proc main() void {
                    var value int = readInt();
                    writeln(square(value));
                    var i int;
                    for i=1 to 100 by 2 {
                        value = i +1;
                    }
                    i = (i+2)*2;
                }
                                
                proc concatString(s string[]) string{
                    var new_s string;
                    var i int;
                    for i=0 to len(s)-1 by 1 {
                        if i==0 {
                            new_s = s[i];
                        } else {
                            new_s = new_s + s[i];
                        }
                    }
                    return new_s;
                }
                                
                main();
                square(2);
                var p Point[] = Point[](2);
                p[0] = Point(3,7);
                p[1] = Point(0,-2);
                var new_p Point = copyPoints(p);
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        CustomNode root = parser.parseStartingPoint();

        SymbolTable table = new SymbolTable();
        root.accept(new SemanticAnalyser(table));
    }


    @Test
    public void test_proc_unmatched_type() throws Exception {
        String input = """
                proc square(v int) int {
                    return v==v;
                }
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        StartingPoint root = parser.parseStartingPoint();
        SymbolTable table = new SymbolTable();
        try {
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (SemanticException ignored) {
        }

        input = """             
                proc copyPoints(p int[]) int {
                     return p;
                }
                """;
        reader = new StringReader(input);
        lexer = new Lexer(reader);
        parser = new Parser(lexer);
        root = parser.parseStartingPoint();
        table = new SymbolTable();
        try {
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (SemanticException ignored) {
        }

        input = """
                proc main() void {
                    var value int = readInt();
                    writeln(square(value));
                    var i int;
                    for i=1 to 100 by 2 {
                        value = i +1;
                    }
                    i = (i+2)*2;
                    return i;
                }
                """;
        reader = new StringReader(input);
        lexer = new Lexer(reader);
        parser = new Parser(lexer);
        root = parser.parseStartingPoint();
        table = new SymbolTable();
        try {
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (SemanticException ignored) {
        }
    }
    
    @Test
    public void test_proc_variable_of_proc_not_available_outside() throws Exception {
        String input = """
                proc main() void {
                    var value int = readInt();
                    writeln(square(value));
                    var i int;
                    for i=1 to 100 by 2 {
                        value = i +1;
                    }
                    i = (i+2)*2;
                }
                                
                var wrong int  = value *2;
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        StartingPoint root = parser.parseStartingPoint();
        SymbolTable table = new SymbolTable();
        try {
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (SemanticException ignored) {
        }
    }

    @Test
    public void test_proc_assigning_return_value_to_wrong_variable() throws Exception {
        /*
        We should assign the return of a function to a variable which type will match
         */
        String input = """
                proc copyPoints(p Point[]) Point {
                     return Point(p[0].x+p[1].x, p[0].y+p[1].y);
                }
                                
                var p Point[] = Point[](2);
                p[0] = Point(3,7);
                p[1] = Point(0,-2);
                p = copyPoints(p);
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        StartingPoint root = parser.parseStartingPoint();
        SymbolTable table = new SymbolTable();
        try {
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (SemanticException ignored) {
        }
    }

    // RECORDS
    
    @Test
    public void test_new_record_wrong_type() throws Exception {
        String input = """
                record Point {
                    x string;
                    y int;
                    history int[];
                }
                var test2 Point = Point(4, 3, int[](2));
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        CustomNode root = parser.parseStartingPoint();

        SymbolTable table = new SymbolTable();
        try {
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (SemanticException ignored) {
        }
    }

    @Test
    public void test_new__unknown_record() throws Exception {
        String input = """
                var test2 Point = Point(4, 3, int[](2));
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        CustomNode root = parser.parseStartingPoint();

        SymbolTable table = new SymbolTable();
        try {
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (SemanticException ignored) {
        }
    }

    @Test
    public void test_new_record_good() throws Exception {
        String input = """
                record Point {
                    x string;
                    y int;
                    history int[];
                }
                var d string = "me";
                var test2 Point = Point(d, 3, int[](2));
                test2.history[0] = 0;
                test2.history[1] = 1;
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        CustomNode root = parser.parseStartingPoint();

        SymbolTable table = new SymbolTable();
        root.accept(new SemanticAnalyser(table));
    }

    // IF/ELSE structure

    @Test
    public void test_if_else_wrong_condition() throws Exception {
        /*
        error expected because: string operations, comparing wrong types together, wrong boolean operation for type
         */
        String input ="""
                if "me" + "you" {
                    var i int  = 5;
                } else {
                    var i int  = 6;
                    }
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        CustomNode root = parser.parseStartingPoint();
        try {
            SymbolTable table = new SymbolTable();
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (Exception ignored) {
        }

        input = """
                if "me" > "you" {
                    var i int  = 5;
                } else {
                    var i int  = 6;
                    }
                """;
        reader = new StringReader(input);
        lexer = new Lexer(reader);
        parser = new Parser(lexer);
        root = parser.parseStartingPoint();
        try {

            SymbolTable table = new SymbolTable();
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (Exception ignored) {
        }

        input ="""
                if 1 == "you" {
                    var i int  = 5;
                } else {
                    var i int  = 6;
                    }
                """;
        reader = new StringReader(input);
        lexer = new Lexer(reader);
        parser = new Parser(lexer);
        root = parser.parseStartingPoint();
        try {
            SymbolTable table = new SymbolTable();
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (Exception ignored) {
        }

        input ="""
                if true == "you" {
                    var i int  = 5;
                } else {
                    var i int  = 6;
                    }
                """;
        reader = new StringReader(input);
        lexer = new Lexer(reader);
        parser = new Parser(lexer);
        root = parser.parseStartingPoint();
        try {

            SymbolTable table = new SymbolTable();
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (Exception ignored) {
        }

        input ="""
                if true >= false {
                    var i int  = 5;
                } else {
                    var i int  = 6;
                    }
                """;
        reader = new StringReader(input);
        lexer = new Lexer(reader);
        parser = new Parser(lexer);
        root = parser.parseStartingPoint();
        try {

            SymbolTable table = new SymbolTable();
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (Exception ignored) {
        }

        input ="""
                if true > false {
                    var i int  = 5;
                } else {
                    var i int  = 6;
                    }
                """;
        reader = new StringReader(input);
        lexer = new Lexer(reader);
        parser = new Parser(lexer);
        root = parser.parseStartingPoint();

        try {
            SymbolTable table = new SymbolTable();
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (Exception ignored) {
        }
    }

    @Test
    public void test_if_else_right_conditions() throws Exception {
        String input = """
                var i int;
                if "me" == "you" {
                    i = 5;
                } else {
                    i = 6;
                    }
                if 3 > 6 {
                    i = 5;
                }
                if true == false {
                    i = 5;
                }
                if true and false{
                    i = 5;
                }
                if true or false {
                    i = 5;
                }
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        CustomNode root = parser.parseStartingPoint();

        SymbolTable table = new SymbolTable();
        root.accept(new SemanticAnalyser(table));
    }

    // WHILE LOOP

    @Test
    public void test_while_wrong_condition() throws Exception {
        /*
        error expected because: string operations, comparing wrong types together, wrong boolean operation for type
         */
        String input ="""
                while "me" + "you" {
                    value = value -1;
                }
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        CustomNode root = parser.parseStartingPoint();
        try {
            SymbolTable table = new SymbolTable();
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (Exception ignored) {
        }

        input = """
                while "me" > "you" {
                    var i int  = 5;
                }
                """;
        reader = new StringReader(input);
        lexer = new Lexer(reader);
        parser = new Parser(lexer);
        root = parser.parseStartingPoint();
        try {

            SymbolTable table = new SymbolTable();
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (Exception ignored) {
        }

        input ="""
                while 1 == "you" {
                    var i int  = 5;
                }
                """;
        reader = new StringReader(input);
        lexer = new Lexer(reader);
        parser = new Parser(lexer);
        root = parser.parseStartingPoint();
        try {
            SymbolTable table = new SymbolTable();
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (Exception ignored) {
        }

        input ="""
                while true == "you"{
                    var i int  = 5;
                }
                """;
        reader = new StringReader(input);
        lexer = new Lexer(reader);
        parser = new Parser(lexer);
        root = parser.parseStartingPoint();
        try {

            SymbolTable table = new SymbolTable();
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (Exception ignored) {
        }

        input ="""
                while true >= false {
                    var i int  = 5;
                }
                """;
        reader = new StringReader(input);
        lexer = new Lexer(reader);
        parser = new Parser(lexer);
        root = parser.parseStartingPoint();
        try {

            SymbolTable table = new SymbolTable();
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (Exception ignored) {
        }

        input ="""
                while true > false {
                    var i int  = 5;
                }
                """;
        reader = new StringReader(input);
        lexer = new Lexer(reader);
        parser = new Parser(lexer);
        root = parser.parseStartingPoint();

        try {
            SymbolTable table = new SymbolTable();
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (Exception ignored) {
        }
    }

    @Test
    public void test_while_right_conditions() throws Exception {
        String input = """
                var value int = readInt();
                var i int;
                while value <> 0{
                    value = value -1;
                }
                while 3 > 6{
                    i = 5;
                }
                while true == false {
                    i = 5;
                }
                while true and false{
                    i = 5;
                }
                while true or false {
                    i = 5;
                }
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        CustomNode root = parser.parseStartingPoint();

        SymbolTable table = new SymbolTable();
        root.accept(new SemanticAnalyser(table));
    }

    // FOR LOOP
    //TODO: à étoffer

    @Test
    public void test_for_wrong_condition() throws Exception {
        /*
        error expected because: string operations, comparing wrong types together, wrong boolean operation for type
         */
        String input ="""
                if "me" + "you" {
                    value = value -1;
                }
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        CustomNode root = parser.parseStartingPoint();
        try {
            SymbolTable table = new SymbolTable();
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (Exception ignored) {
        }

        input = """
                var i int;
                for i = 2.5 to 100.00 by \"me\" {
                    var i int  = 5;
                }
                """;
        reader = new StringReader(input);
        lexer = new Lexer(reader);
        parser = new Parser(lexer);
        root = parser.parseStartingPoint();
        try {

            SymbolTable table = new SymbolTable();
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (Exception ignored) {
        }

    }

    @Test
    public void test_for_right_conditions() throws Exception {
        String input = """
                var i int;
                for i = 5 to 0 by -1{
                //do something
                }
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        CustomNode root = parser.parseStartingPoint();

        SymbolTable table = new SymbolTable();
        root.accept(new SemanticAnalyser(table));
    }

    // BUILT-IN FUNCTIONS AND I/O
    // SCOPES
    // CONSTANTS
    @Test
    public void test_const_wrong_init() throws Exception {
        /*
        error expected because: string operations, comparing wrong types together, wrong boolean operation for type
         */
        String input ="""
                const i int = 1.3;
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        CustomNode root = parser.parseStartingPoint();
        try {
            SymbolTable table = new SymbolTable();
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (Exception ignored) {
        }
    }

    @Test
    public void test_const_good_init() throws Exception {
        String input = """
                const a int = 5;
                const i int = 3;
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        CustomNode root = parser.parseStartingPoint();

        SymbolTable table = new SymbolTable();
        root.accept(new SemanticAnalyser(table));
    }

    // VARIABLES

    @Test
    public void test_variables_wrong_type() throws Exception {
        String input = """
                var a string = "2";
                var test int = a*3;
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        CustomNode root = parser.parseStartingPoint();

        SymbolTable table = new SymbolTable();
        try {
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (SemanticException ignored) {
        }

        input = """
                var a int = 2;
                var b string = a+a;
                """;
        reader = new StringReader(input);
        lexer = new Lexer(reader);
        parser = new Parser(lexer);
        root = parser.parseStartingPoint();

        table = new SymbolTable();
        try {
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (SemanticException ignored) {
        }

        input = """
                var a int = 2+a;
                """;
        reader = new StringReader(input);
        lexer = new Lexer(reader);
        parser = new Parser(lexer);
        root = parser.parseStartingPoint();

        table = new SymbolTable();
        try {
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (SemanticException ignored) {
        }

        input = """
                var a int = "2";""";
        reader = new StringReader(input);
        lexer = new Lexer(reader);
        parser = new Parser(lexer);
        root = parser.parseStartingPoint();

        table = new SymbolTable();
        try {
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (SemanticException ignored) {
        }

        input = """
                var a string = "me";
                var b string = "you";
                var c string = a - b;
                """;
        reader = new StringReader(input);
        lexer = new Lexer(reader);
        parser = new Parser(lexer);
        root = parser.parseStartingPoint();

        table = new SymbolTable();
        try {
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (SemanticException ignored) {
        }

        //wrong operation with real
        input = """
                var c int = 1.52 % 2;
                """;
        reader = new StringReader(input);
        lexer = new Lexer(reader);
        parser = new Parser(lexer);
        root = parser.parseStartingPoint();

        table = new SymbolTable();
        try {
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (SemanticException ignored) {
        }

        input = """
                var a int = 5;
                delete(5);
                """;
        reader = new StringReader(input);
        lexer = new Lexer(reader);
        parser = new Parser(lexer);
        root = parser.parseStartingPoint();

        table = new SymbolTable();
        try {
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (SemanticException ignored) {
        }

        input = """
                var i bool = ((3+2)>(1-3)) < true;
                """;
        reader = new StringReader(input);
        lexer = new Lexer(reader);
        parser = new Parser(lexer);
        root = parser.parseStartingPoint();
        table = new SymbolTable();
        try {
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (Exception ignored) {
        }
    }

    @Test
    public void test_variables_error_array_creation() throws Exception {
        String input = """
                var b real = 0.05;
                var a int[] = int[](b);
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        CustomNode root = parser.parseStartingPoint();

        SymbolTable table = new SymbolTable();
        try {
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (SemanticException ignored) {
        }
    }

    @Test
    public void test_variables_error_array_accessing() throws Exception {

        /*
        Error accessing array further than its size
         */

        String input = """
                var a int[] = int[](1);
                var b int = a[3];
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        StartingPoint root = parser.parseStartingPoint();

        SymbolTable table = new SymbolTable();
        try {
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (SemanticException ignored) {
        }

        /*
        Error accessing array when empty
        TODO: Should we throw error in this case?
         */

        input = """
                var a int[] = int[](1);
                var b int = a[0];
                """;
        reader = new StringReader(input);
        lexer = new Lexer(reader);
        parser = new Parser(lexer);
        root = parser.parseStartingPoint();

        table = new SymbolTable();
        try {
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (SemanticException ignored) {
        }
    }

    @Test
    public void test_variables_reassigning_val_error() throws Exception {
        String input = """
                val a int = 2;
                a = true;
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        StartingPoint root = parser.parseStartingPoint();

        SymbolTable table = new SymbolTable();
        try {
            root.accept(new SemanticAnalyser(table));
            fail( "My method didn't throw when I expected it to" );
        } catch (SemanticException ignored) {
        }
    }

    @Test
    public void test_variables_good_syntax_creation() throws Exception {
        String input = """
                var a int = 2;
                var b string = "new";
                var test int = (a*3)+2 - len(b);
                var b_bis string = b + "rules";
                var am string = "me";
                var bm string = "you";
                var cm string = am + bm;
                a = 3;
                
                // int
                var a int = 2;
                a = a +1;
                a = 2%10;
                
                // string
                var b string = "new";
                var test int = (a*3)+2 - len(b);
                var b_bis string = b + "rules";
                var am string = "me";
                var bm string = "you";
                var cm string = am + bm;
                
                //boolean
                var c boolean = true and false;
                var c boolean = 2 < 3;
                var c boolean = "you" == "me";
                var c boolean = true <> false;
                
                //array
                var array int[] = int[](5);
                delete(array);
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        StartingPoint root = parser.parseStartingPoint();

        SymbolTable table = new SymbolTable();
        root.accept(new SemanticAnalyser(table));
    }

    @Test
    public void test_variables_good_syntax_reassignement() throws Exception {
        String input = """
                // int
                var a int = 2;
                a = a +1;
                
                // string
                var b string = "new";
                a = (a*3)+2 - len(b);
                
                var am string = "me";
                var bm string = "you";
                am = am + bm;
                
                //boolean
                var c boolean = true and false;
                c = 2 < 3;
                c = "you" == "me";
                c = true <> false;
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        StartingPoint root = parser.parseStartingPoint();

        SymbolTable table = new SymbolTable();
        root.accept(new SemanticAnalyser(table));
    }
}
