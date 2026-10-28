import compiler.Lexer.Symbol;
import compiler.Lexer.Symbols.Symbols;
import org.junit.Assert;
import org.junit.Test;
import java.util.*;

import java.io.StringReader;
import compiler.Lexer.Lexer;

import static org.junit.Assert.*;

public class TestLexer {

    // TODO: make more tests
    @Test
    public void test_int() {
        String input = "int x = 5;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Identifier", "Identifier", "AssignementOperator", "NaturalNumber",
                "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_double() {
        String input = "double x = 5.3;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Identifier", "Identifier", "AssignementOperator", "RealNumber",
                "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_string() {
        String input = "String s = \"test\";";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Identifier", "Identifier", "AssignementOperator", "Strings",
                "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }
    @Test
    public void test_operation_plus() {
        String input = "int x = 5 + 3;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Identifier", "Identifier", "AssignementOperator", "NaturalNumber",
        "ArithmeticOperator", "NaturalNumber", "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_operation_minus() {
        String input = "int x = 5.3 - 3.3;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Identifier", "Identifier", "AssignementOperator", "RealNumber",
                "ArithmeticOperator", "RealNumber", "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_operation_mul() {
        String input = "int x = 5.3 * 3.3;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Identifier", "Identifier", "AssignementOperator", "RealNumber",
                "ArithmeticOperator", "RealNumber", "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_operation_div() {
        String input = "int x = 5.3 / 3.3;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Identifier", "Identifier", "AssignementOperator", "RealNumber",
                "ArithmeticOperator", "RealNumber", "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_operation_equals() {
        String input = "3.6 == 5.6;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("RealNumber", "ComparingOperator", "RealNumber", "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_operation_different() {
        String input = "3.6 <> 5.6;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("RealNumber", "ComparingOperator", "RealNumber", "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_operation_greater() {
        String input = "3.6 > 5.6;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("RealNumber", "ComparingOperator", "RealNumber", "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_operation_lower() {
        String input = "3.6 < 5.6;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("RealNumber", "ComparingOperator", "RealNumber", "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_operation_greater_or_equals() {
        String input = "3.6 >= 5.6;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("RealNumber", "ComparingOperator", "RealNumber", "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_operation_lower_or_equals() {
        String input = "3.6 <= 5.6;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("RealNumber", "ComparingOperator", "RealNumber", "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_operation_mod() {
        String input = "int x = 5.3 % 3.3;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Identifier", "Identifier", "AssignementOperator", "RealNumber",
                "ArithmeticOperator", "RealNumber", "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_bool_and() {
        String input = "bool x = a and b;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Identifier", "Identifier", "AssignementOperator", "Identifier",
                "ComparingOperator", "Identifier", "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_bool_or() {
        String input = "bool x = a or b;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Identifier", "Identifier", "AssignementOperator", "Identifier",
                "ComparingOperator", "Identifier", "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_bool_equals() {
        String input = "bool x = a == b;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Identifier", "Identifier", "AssignementOperator", "Identifier",
                "ComparingOperator", "Identifier", "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_bool_not_equals() {
        String input = "bool x = a <> b;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Identifier", "Identifier", "AssignementOperator", "Identifier",
                "ComparingOperator", "Identifier", "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_string_concat() {
        String input = "String x = \"A\" + \"B\";";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Identifier", "Identifier", "AssignementOperator", "Strings",
                "ArithmeticOperator", "Strings", "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_string_equals() {
        String input = "\"A\" == \"B\";";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Strings", "ComparingOperator", "Strings", "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_string_not_equals() {
        String input = "\"A\" <> \"B\";";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Strings", "ComparingOperator", "Strings", "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_method_not() {
        String input = "not(bool) bool";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Identifier", "LParenthesis", "Identifier", "RParenthesis",
                "Identifier");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_method_chr() {
        String input = "chr(int) string";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Identifier", "LParenthesis", "Identifier", "RParenthesis",
                "Identifier");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }
    /**
    @Test
    public void test_method_len() {
        String input = "len(string or array) string";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Identifier", "LParenthesis", "Identifier",
                "Keyword", "Identifier", "RParenthesis", "Identifier");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    } **/

    @Test
    public void test_method_floor() {
        String input = "floor(real) int";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Identifier", "LParenthesis", "Identifier", "RParenthesis",
                "Identifier");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_const_1() {
        String input = "const i int = 3;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Keyword", "Identifier", "Identifier", "AssignementOperator",
                "NaturalNumber", "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_const_2() {
        String input = "const j real = 3.2*5.0;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Keyword", "Identifier", "Identifier", "AssignementOperator",
                "RealNumber", "ArithmeticOperator", "RealNumber", "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_const_3() {
        String input = "const k int = i*3;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Keyword", "Identifier", "Identifier", "AssignementOperator",
                "Identifier", "ArithmeticOperator", "NaturalNumber", "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_const_4() {
        String input = "const message string = \"Hello\";";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Keyword", "Identifier", "Identifier", "AssignementOperator",
                "Strings", "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_const_5() {
        String input = "const isEmpty bool = true;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Keyword", "Identifier", "Identifier", "AssignementOperator",
                "Booleans", "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_record_1() {
        String input = """
                record Point {
                    x int;
                    y int;
                }""";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Keyword", "Identifier", "LBraceBrackets", "Identifier",
                "Identifier", "SpecialCharacter", "Identifier", "Identifier", "SpecialCharacter", "RBraceBrackets");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_record_2() {
        String input = "record Person {\n" +
                "    name string;\n" +
                "    location Point;\n" +
                "    history int[];\n" +
                "}";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Keyword", "Identifier", "LBraceBrackets", "Identifier",
                "Identifier", "SpecialCharacter", "Identifier", "Identifier", "SpecialCharacter", "Identifier",
                "Identifier", "LBoxBrackets", "RBoxBrackets", "SpecialCharacter", "RBraceBrackets");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_global_variables1() {
        String input = "var x=print(\"coucou\");";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Keyword", "Identifier", "AssignementOperator", "Identifier",
                "LParenthesis", "Strings", "RParenthesis", "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_global_variables2() {
        String input = "var d Person = Person(\"me\", Point(3,7), int[](a*2));";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Keyword", "Identifier", "Identifier", "AssignementOperator",
                "Identifier", "LParenthesis", "Strings", "SpecialCharacter", "Identifier", "LParenthesis",
                "NaturalNumber", "SpecialCharacter", "NaturalNumber", "RParenthesis", "SpecialCharacter", "Identifier",
                "LBoxBrackets", "RBoxBrackets", "LParenthesis", "Identifier", "ArithmeticOperator", "NaturalNumber",
                "RParenthesis", "RParenthesis", "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_val_1() {
        String input = "val e int = a*2;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Keyword", "Identifier", "Identifier", "AssignementOperator",
                "Identifier", "ArithmeticOperator", "NaturalNumber", "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_if() {
        String input = "if (e == true){\n" + "        }";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Keyword", "LParenthesis", "Identifier", "ComparingOperator",
                "Booleans", "RParenthesis", "LBraceBrackets", "RBraceBrackets");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_if_else() {
        String input = "if (e == true){\n" +
                       " } else {\n" +
                       " }";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Keyword", "LParenthesis", "Identifier", "ComparingOperator",
                "Booleans", "RParenthesis", "LBraceBrackets", "RBraceBrackets", "Keyword", "LBraceBrackets", "RBraceBrackets");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_while() {
        String input = "while (e) {\n" +
                       " }";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Keyword", "LParenthesis", "Identifier", "RParenthesis",
                "LBraceBrackets", "RBraceBrackets");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_for() {
        String input = "for i=0 to 5 by 1 {" +
                       "}";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Keyword", "Identifier", "AssignementOperator", "NaturalNumber",
                "Keyword", "NaturalNumber", "Keyword", "NaturalNumber", "LBraceBrackets", "RBraceBrackets");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_array() {
        String input = "a[i] = 1234;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Identifier", "LBoxBrackets", "Identifier", "RBoxBrackets",
                "AssignementOperator", "NaturalNumber", "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_field() {
        String input = "a.x = 123;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Identifier", "SpecialCharacter", "Identifier",
                "AssignementOperator", "NaturalNumber", "SpecialCharacter");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_proc1() {
        String input = "proc square(v int) int {\n" +
                       " return v*v;\n" +
                       " }";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Keyword", "Identifier", "LParenthesis", "Identifier",
                "Identifier", "RParenthesis", "Identifier", "LBraceBrackets", "Keyword", "Identifier", "ArithmeticOperator",
                "Identifier", "SpecialCharacter", "RBraceBrackets");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

    @Test
    public void test_proc2() {
        String input = "proc main() void {\n" +
                       " var value int = readInt(); \n" +
                       " writeln(square(value));\n" +
                       " var i int;\n" +
                       " for i=1 to 100 by 2 {\n" +
                       " while value<>3 {\n" +
                       " }\n" +
                       " }\n" +
                       " i = (i+2)*2\n" +
                       "} ";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Keyword", "Identifier", "LParenthesis", "RParenthesis", "Identifier",
                "LBraceBrackets", "Keyword", "Identifier", "Identifier", "AssignementOperator", "Identifier", "LParenthesis", 
                "RParenthesis", "SpecialCharacter", "Identifier", "LParenthesis", "Identifier", "LParenthesis", "Identifier",
                "RParenthesis", "RParenthesis", "SpecialCharacter", "Keyword", "Identifier", "Identifier", "SpecialCharacter",
                "Keyword", "Identifier", "AssignementOperator", "NaturalNumber", "Keyword", "NaturalNumber", "Keyword",
                "NaturalNumber", "LBraceBrackets", "Keyword", "Identifier", "ComparingOperator", "NaturalNumber", "LBraceBrackets",
                "RBraceBrackets", "RBraceBrackets", "Identifier", "AssignementOperator", "LParenthesis", "Identifier",
                "ArithmeticOperator", "NaturalNumber", "RParenthesis", "ArithmeticOperator", "NaturalNumber", "RBraceBrackets");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }
    /**
    @Test
    public void test_comments() {
        String input = "int cava = x - 0// this is a comment\n and not anymore";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Identifier", "Identifier", "AssignementOperator",
                "Identifier", "ArithmeticOperator", "NaturalNumber", "Comment", "Keyword", "Identifier", "Identifier");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    } **/
    @Test
    public void test_string_with_quotes() {
        String input = "int cava = \"x y \\\"z\\\" x\" for";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        List<String> expected_symbols = Arrays.asList("Identifier", "Identifier", "AssignementOperator","Strings", "Keyword");
        int i =0;
        while (s != null){
            System.out.println(s);
            assertEquals(expected_symbols.get(i), s.getToken());
            s = lexer.getNextSymbol();
            i = i+1;
        }
    }

}
