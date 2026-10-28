import compiler.Code_generation.Compiler;
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

public class TestCodeGenerator {

    @Test
    public void test_const() throws Exception {
        String input ="""
                var i int = 1;
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        StartingPoint root = parser.parseStartingPoint();
        SymbolTable table = new SymbolTable();
        root.accept(new SemanticAnalyser(table));
        Compiler compiler = new Compiler();
        compiler.generateByteCode(root);
    }
}
