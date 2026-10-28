package compiler.Parser;

import compiler.Code_generation.CodeGenerationVisitable;
import compiler.Code_generation.CodeGenerationVisitor;
import compiler.Parser.TreeStructure.CustomNode;
import compiler.Semantic_analysis.ASTVisitable;
import compiler.Semantic_analysis.ASTVisitor;

import java.util.ArrayList;

public class Operation implements CustomNode, ASTVisitable, CodeGenerationVisitable {
    ArrayList<CustomNode> list_operation;
    ArrayList<CustomNode> list_factor;
    ArrayList<CustomNode> list_operators;
    String detected_type = null;

    public Operation(ArrayList<CustomNode> list_operations, ArrayList<CustomNode> list_factor, ArrayList<CustomNode> list_operators) {
        this.list_operation = list_operations;
        this.list_factor = list_factor;
        this.list_operators = list_operators;

        for(CustomNode e: list_operations){
            this.list_children.add(e);
            e.setParent(getCurrent());
        }
    }

    String token=this.getClass().getSimpleName();
    CustomNode parent=null;
    ArrayList<CustomNode> list_children=new ArrayList<>();

    public String getToken(){return this.token;}
    public void setDetected_type(String s){detected_type = s;}

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

    public ArrayList<CustomNode> get_list_operation() {
        return list_operation;
    }
    public String get_detected_type() {
        return detected_type;
    }

    public ArrayList<CustomNode> get_list_operators() {
        return list_operators;
    }

    public ArrayList<CustomNode> get_list_factors() {
        return list_factor;
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
