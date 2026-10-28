package compiler.Lexer.Symbols;

import compiler.Code_generation.CodeGenerationVisitor;
import compiler.Parser.TreeStructure.CustomNode;
import compiler.Semantic_analysis.ASTVisitor;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class Identifier implements Symbols{

    String token=this.getClass().getSimpleName();
    String value;
    int line_nbr = 1;

    CustomNode parent;
    ArrayList<CustomNode> list_children = new ArrayList<>();

    public Identifier(){
        this.value = null;
    }

    public Identifier(String value, CustomNode parent, ArrayList<CustomNode> list_children, int nbr){
        this.value = value;
        this.parent = parent;
        this.list_children = list_children;
        this.line_nbr = nbr;
    }

    public Symbols clone(){
        return new Identifier(this.value, this.parent, this.list_children, this.line_nbr);
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
    public boolean matches(String s) {
        for(int i=0;i<s.length();i++){
            String vstring = Character.toString(s.charAt(i));
            if(i==0 && !(vstring.equals("_") || Character.isLetter(s.charAt(i)))){
                return false;
            } else if(!(Character.isLetter(s.charAt(i))|| Character.isDigit(s.charAt(i)) || vstring.equals("_"))){
                return false;
            }
        }
        return true;
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
