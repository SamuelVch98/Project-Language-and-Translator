package compiler.Parser;

import compiler.Code_generation.CodeGenerationVisitable;
import compiler.Code_generation.CodeGenerationVisitor;
import compiler.Parser.TreeStructure.CustomNode;
import compiler.Semantic_analysis.ASTVisitable;
import compiler.Semantic_analysis.ASTVisitor;

import java.util.ArrayList;

public class ControlStructure implements CustomNode, ASTVisitable, CodeGenerationVisitable {
    String nameOfFunction;
    Operation condition;
    BlockStatement body;
    ArrayList<CustomNode> list_for_loop;


    String token=this.getClass().getSimpleName();
    CustomNode parent;
    ArrayList<CustomNode> list_children=new ArrayList<>();

    public ControlStructure(String nameOfFunction, Operation condition, BlockStatement body, ArrayList<CustomNode> list_for_loop) {
        if(condition != null)
        this.list_children.add(condition);
        this.list_children.add(body);
        if(list_for_loop != null)
            for (CustomNode e: list_for_loop){
                this.list_children.add(e);
                e.setParent(getCurrent());
            }

        if(condition != null)
        condition.setParent(getCurrent());
        body.setParent(getCurrent());

        this.nameOfFunction = nameOfFunction;
        this.condition = condition;
        this.body = body;
    }

    public String getNameOfFunction() {
        return nameOfFunction;
    }

    public Operation getCondition() {
        return condition;
    }

    public BlockStatement getBody() {
        return body;
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
