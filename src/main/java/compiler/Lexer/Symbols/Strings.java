package compiler.Lexer.Symbols;

import compiler.Code_generation.CodeGenerationVisitor;
import compiler.Parser.TreeStructure.CustomNode;
import compiler.Semantic_analysis.ASTVisitor;

import java.util.ArrayList;
import java.util.Objects;

public class Strings implements Symbols{
    String token=this.getClass().getSimpleName();
    String value;
    int line_nbr = 0;

    CustomNode parent;
    ArrayList<CustomNode> list_children = new ArrayList<>();

    public Strings(){
        this.value = null;
    }

    public Strings(String value, CustomNode parent, ArrayList<CustomNode> list_children, int line_nbr){
        this.value = value;
        this.parent = parent;
        this.list_children = list_children;
        this.line_nbr = line_nbr;
    }

    public Symbols clone(){
        return new Strings(this.value, this.parent, this.list_children, this.line_nbr);
    }

    @Override
    public int getLine() {
        return this.line_nbr;
    }

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public void setLineNbr(int nbr) {
        this.line_nbr = nbr;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    //TODO: check what is defined as printable character, what can be in the string
    @Override
    public boolean matches(String s) {
        if (s.length()<=1 && !(s.equals("\""))){
            return false;
        } else if(Character.toString(s.charAt(0)).equals("\"") && Character.toString(s.charAt(s.length()-1)).equals("\"")){
          return true;
        } else {
            for(int i=0;i<s.length();i++){
                String vstring = Character.toString(s.charAt(i));

                if(i==0 && !(vstring.equals("\""))){
                    return false;
                } else if(i!= 0 && vstring.equals("\"")){
                    if(!Character.toString(s.charAt(i - 1)).equals("\\")){
                        if(i != s.length()-1){
                            return false;
                        }
                    }
                }
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
