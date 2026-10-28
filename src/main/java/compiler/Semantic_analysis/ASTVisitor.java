package compiler.Semantic_analysis;

import compiler.Parser.*;

public interface ASTVisitor {
    void visit(StartingPoint node);
    void visit(AccessingData node);
    void visit(ArrayInit node);
    void visit(Assignement node);
    void visit(BlockStatement node);
    void visit(BuiltInFunction node);
    void visit(Constante node);
    void visit (ControlStructure node);
    void visit(Expressions node);
    void visit(GlobalVariable node);
    void visit(InOutFunction node);
    void visit(NewRecord node);
    void visit(Numbers node);
    void visit(Procedure node);
    void visit(Records node);
    void visit(Type node);

    void visit(Operation operation);
}
