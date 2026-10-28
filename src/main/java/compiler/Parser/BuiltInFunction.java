package compiler.Parser;

import compiler.Code_generation.CodeGenerationVisitable;
import compiler.Code_generation.CodeGenerationVisitor;
import compiler.Parser.TreeStructure.CustomNode;
import compiler.Lexer.Symbols.Symbols;
import compiler.Semantic_analysis.ASTVisitable;
import compiler.Semantic_analysis.ASTVisitor;

import java.util.ArrayList;

public class BuiltInFunction implements CustomNode, ASTVisitable, CodeGenerationVisitable {
    String nameOfFunction;
    Operation parameters;

    String token=this.getClass().getSimpleName();
    CustomNode parent;
    ArrayList<CustomNode> list_children=new ArrayList<>();


    public BuiltInFunction(String nameOfFunction, Operation parameter) {
        this.nameOfFunction = nameOfFunction;
        this.parameters = parameter;

        this.list_children.add(parameter);

        parameter.setParent(getCurrent());
    }

    public Operation getParameters() {
        return parameters;
    }

    public String getNameOfFunction() {
        return nameOfFunction;
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
        StringBuilder s = new StringBuilder(nameOfFunction);
        s.append("(");
        for(CustomNode param: parameters.list_operation){
            s.append(param.toString());
            s.append(", ");
        }
        s.append(")");
        return s.toString();
    }
}
