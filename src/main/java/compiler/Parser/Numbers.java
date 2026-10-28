package compiler.Parser;

import compiler.Code_generation.CodeGenerationVisitable;
import compiler.Code_generation.CodeGenerationVisitor;
import compiler.Lexer.Symbols.NaturalNumber;
import compiler.Lexer.Symbols.RealNumber;
import compiler.Lexer.Symbols.SpecialCharacter;
import compiler.Lexer.Symbols.Symbols;
import compiler.Parser.TreeStructure.CustomNode;
import compiler.Semantic_analysis.ASTVisitable;
import compiler.Semantic_analysis.ASTVisitor;

import java.util.ArrayList;

// Numbers -> NaturalNumber | RealNumber
public class Numbers implements CustomNode, ASTVisitable, CodeGenerationVisitable {
    private Symbols number;
    String token=this.getClass().getSimpleName();

    CustomNode parent;
    ArrayList<CustomNode> list_children = new ArrayList<>();

    public Numbers(Symbols number) {
        if (number instanceof NaturalNumber | number instanceof RealNumber) {
            this.number = number;
            list_children.add(number);
            number.setParent(getCurrent());
        }
    }

    public Symbols getNumber() {
        return this.number;
    }

    public String toString(){
        return this.number.toString();
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
