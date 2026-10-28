package compiler.Parser;

import compiler.Code_generation.CodeGenerationVisitable;
import compiler.Code_generation.CodeGenerationVisitor;
import compiler.Lexer.Symbols.Symbols;
import compiler.Parser.TreeStructure.CustomNode;
import compiler.Semantic_analysis.ASTVisitable;
import compiler.Semantic_analysis.ASTVisitor;

import java.util.ArrayList;

public class BlockStatement implements CustomNode, ASTVisitable, CodeGenerationVisitable {
    ArrayList<CustomNode> list_statements;

    ArrayList<Expressions> list_return;

    String token=this.getClass().getSimpleName();
    CustomNode parent;
    ArrayList<CustomNode> list_children=new ArrayList<>();

    public BlockStatement(ArrayList<CustomNode> list_statements, ArrayList<Expressions> list_return) {
        for (CustomNode e : list_statements) {
            e.setParent(getCurrent());
            this.list_children.add(e);
        }
        for (CustomNode e : list_return) {
            e.setParent(getCurrent());
            this.list_children.add(e);
        }

        this.list_statements = list_statements;
        this.list_return = list_return;
    }

    public ArrayList<CustomNode> getStatements() {
        return list_statements;
    }

    public ArrayList<Expressions> getReturns() {
        return list_return;
    }

    public int getSizeStatements(){
        return list_statements.size();
    }

    public String getToken(){return this.token;}

    @Override
    public CustomNode getCurrent(){
        return this;
    }

    @Override
    public ArrayList<? extends CustomNode> getChildren(){
        return this.list_children;
    }

    public CustomNode getParent(){
        return this.parent;
    }


    public void addChildren(CustomNode children){
        this.list_children.add(children);
    }

    public void setParent(CustomNode parent){
        this.parent = parent;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void acceptCodeGeneration(CodeGenerationVisitor visitor) {
        visitor.visit(this);
    }
}
