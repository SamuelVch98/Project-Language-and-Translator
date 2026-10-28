package compiler.Lexer.Symbols;

import compiler.Code_generation.CodeGenerationVisitor;
import compiler.Parser.TreeStructure.CustomNode;
import compiler.Semantic_analysis.ASTVisitor;

import java.util.ArrayList;
import java.util.Objects;

public class AssignementOperator implements Symbols{

    String token=this.getClass().getSimpleName();
    String value;
    int line_nbr = 1;

    CustomNode parent;
    ArrayList<CustomNode> list_children = new ArrayList<>();

    public AssignementOperator(){
        this.value = null;
    }

    public AssignementOperator(String value, CustomNode parent, ArrayList<CustomNode> list_children, int nbr){
        this.value = value;
        this.parent = parent;
        this.list_children = list_children;
        this.line_nbr = nbr;
    }

    public Symbols clone(){
        return new AssignementOperator(this.value, this.parent, this.list_children, this.line_nbr);
    }

    @Override
    public boolean matches(String s) {
        return Objects.equals(s, "=");
    }

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public int getLine() {
        return this.line_nbr;
    }

    @Override
    public void setLineNbr(int nbr) {
        this.line_nbr = nbr;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    public String toString(){
        return this.getValue();
    }

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

    }

    @Override
    public void acceptCodeGeneration(CodeGenerationVisitor visitor) {

    }
}
