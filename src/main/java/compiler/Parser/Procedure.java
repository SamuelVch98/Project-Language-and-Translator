package compiler.Parser;

import compiler.Code_generation.CodeGenerationVisitable;
import compiler.Code_generation.CodeGenerationVisitor;
import compiler.Lexer.Symbols.Symbols;
import compiler.Parser.TreeStructure.CustomNode;
import compiler.Semantic_analysis.ASTVisitable;
import compiler.Semantic_analysis.ASTVisitor;

import java.util.ArrayList;

public class Procedure implements CustomNode, ASTVisitable, CodeGenerationVisitable {
    Symbols name;
    ArrayList<Type> parameters;
    Type returnType;
    BlockStatement blockStatement;
    boolean ArrayType;

    String token=this.getClass().getSimpleName();
    CustomNode parent;
    ArrayList<CustomNode> list_children=new ArrayList<>();

    public Procedure(Symbols name, ArrayList<Type> parameters, Type returnType, BlockStatement blockStatement, boolean ArrayType) {
        for(CustomNode e: parameters){
            this.list_children.add(e);
            e.setParent(getCurrent());
        }
        this.list_children.add(name);
        name.setParent(getCurrent());
        this.list_children.add(returnType);
        returnType.setParent(getCurrent());
        this.list_children.add(blockStatement);
        blockStatement.setParent(getCurrent());


        this.name = name;
        this.ArrayType = ArrayType;
        this.parameters = parameters;
        this.returnType = returnType;
        this.blockStatement = blockStatement;
    }

    public Symbols getName() {
        return name;
    }
    public boolean IsArrayType(){return ArrayType;}

    public Type getReturnType() {
        return returnType;
    }

    public BlockStatement getBlockStatement() {
        return blockStatement;
    }

    public ArrayList<Type> getParameters() {
        return parameters;
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
