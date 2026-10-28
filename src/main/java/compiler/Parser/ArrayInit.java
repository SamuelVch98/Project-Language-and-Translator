package compiler.Parser;

import compiler.Code_generation.CodeGenerationVisitable;
import compiler.Code_generation.CodeGenerationVisitor;
import compiler.Lexer.Symbols.Symbols;
import compiler.Parser.TreeStructure.CustomNode;
import compiler.Semantic_analysis.ASTVisitable;
import compiler.Semantic_analysis.ASTVisitor;

import java.util.ArrayList;

public class ArrayInit implements CustomNode, ASTVisitable, CodeGenerationVisitable {
    private final Symbols type;
    private final Operation size;

    String token=this.getClass().getSimpleName();
    CustomNode parent;
    ArrayList<CustomNode> list_children=new ArrayList<>();

    public ArrayInit(Symbols type, Operation size) {
        this.type = type;
        this.size = size;

        this.list_children.add(type);
        type.setParent(getCurrent());

        this.list_children.add(size);
        size.setParent(getCurrent());
    }

    public Symbols getType() {
        return this.type;
    }

    public Operation getSize() {
        return this.size;
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
    public String toString(){
        StringBuilder s = new StringBuilder(type.getValue());
        s.append("[");
        s.append("]");
        s.append("(");
        for (CustomNode c : size.list_operation) {
            s.append(c.toString());
        }
        s.append(")");
        return s.toString();
    }
}
