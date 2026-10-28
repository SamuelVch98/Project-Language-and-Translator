package compiler.Parser;

import compiler.Code_generation.CodeGenerationVisitable;
import compiler.Code_generation.CodeGenerationVisitor;
import compiler.Parser.TreeStructure.CustomNode;
import compiler.Semantic_analysis.ASTVisitable;
import compiler.Semantic_analysis.ASTVisitor;

import java.util.ArrayList;
import java.util.List;

// NegateBoolean -> "not" + booleans
public class InOutFunction implements CustomNode, ASTVisitable, CodeGenerationVisitable {
    private String nameOfFunction;
    List<Expressions> list_parameters;
    String token=this.getClass().getSimpleName();

    CustomNode parent;
    ArrayList<CustomNode> list_children = new ArrayList<>();

    public InOutFunction(String nameOfFunction, List<Expressions> list_parameters) {
        for(CustomNode e: list_parameters){
            e.setParent(getCurrent());
            this.list_children.add(e);
        }
        this.nameOfFunction = nameOfFunction;
        this.list_parameters = list_parameters;
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

    public List<Expressions> getList_parameters() {
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

    public String toString(){
        StringBuilder s = new StringBuilder(nameOfFunction);
        s.append("(");
        for(CustomNode param: list_parameters){
            s.append(param.toString());
            s.append(", ");
        }
        s.append(")");
        return s.toString();
    }
}
