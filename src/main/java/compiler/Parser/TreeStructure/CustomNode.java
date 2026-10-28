package compiler.Parser.TreeStructure;

import compiler.Code_generation.CodeGenerationVisitable;
import compiler.Semantic_analysis.ASTVisitable;


import java.util.ArrayList;

public interface CustomNode extends ASTVisitable, CodeGenerationVisitable {
    CustomNode getCurrent();
    ArrayList<? extends CustomNode> getChildren();
    CustomNode getParent();
    void addChildren(CustomNode children);
    void setParent(CustomNode children);
    String getToken();
    String toString();
}