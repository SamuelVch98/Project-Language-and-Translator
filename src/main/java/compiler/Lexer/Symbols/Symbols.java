package compiler.Lexer.Symbols;

import compiler.Parser.TreeStructure.CustomNode;
import compiler.Semantic_analysis.ASTVisitable;

public interface Symbols extends CustomNode, ASTVisitable {

    boolean matches(String s);

    String getToken();
    String getValue();
    void setValue(String value);

    String toString();
    Symbols clone();

    int getLine();
    void setLineNbr(int nbr);
}
