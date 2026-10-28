package compiler.Semantic_analysis;

public interface ASTVisitable {
    void accept(ASTVisitor visitor);
}
