package compiler.Parser;

import compiler.Code_generation.CodeGenerationVisitable;
import compiler.Code_generation.CodeGenerationVisitor;
import compiler.Lexer.Symbols.Symbols;
import compiler.Parser.TreeStructure.CustomNode;
import compiler.Semantic_analysis.ASTVisitable;
import compiler.Semantic_analysis.ASTVisitor;

import java.util.ArrayList;

public class NewRecord implements CustomNode, ASTVisitable, CodeGenerationVisitable {
    private Symbols identifier;
    ArrayList<Expressions> list_parameters;
    String token=this.getClass().getSimpleName();
    String returnType;
    boolean arrayType;

    CustomNode parent;
    ArrayList<CustomNode> list_children = new ArrayList<>();

    public NewRecord(Symbols identifier, ArrayList<Expressions> list_new_values) {
        this.list_children.add(identifier);
        identifier.setParent(getCurrent());
        for(CustomNode l: list_new_values){
            this.list_children.add(l);
            l.setParent(getCurrent());
        }

        this.identifier = identifier;
        list_parameters = list_new_values;
    }

    public Symbols getIdentifier() {
        return identifier;
    }

    public String getToken(){return this.token;}

    public void setReturnType(String type, boolean arraytype){
        this.returnType = type;
        this.arrayType = arraytype;
    }

    public boolean isArrayType(){return this.arrayType;}

    public String getReturnType(){return this.returnType;}

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

    public ArrayList<Expressions> getListParameters() {
        return list_parameters;
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
