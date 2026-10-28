package compiler.Parser;

import compiler.Code_generation.CodeGenerationVisitable;
import compiler.Code_generation.CodeGenerationVisitor;
import compiler.Lexer.Symbols.SpecialCharacter;
import compiler.Lexer.Symbols.Symbols;
import compiler.Parser.TreeStructure.CustomNode;
import compiler.Semantic_analysis.ASTVisitable;
import compiler.Semantic_analysis.ASTVisitor;

import java.util.ArrayList;

public class GlobalVariable implements CustomNode, ASTVisitable, CodeGenerationVisitable {
    private final Symbols keyword;
    private final Assignement assignement;

    String token=this.getClass().getSimpleName();
    CustomNode parent;
    ArrayList<CustomNode> list_children=new ArrayList<>();

    public GlobalVariable(Symbols keyword, Assignement assignement) {
        this.keyword = keyword;
        this.assignement = assignement;

        this.list_children.add(keyword);
        keyword.setParent(getCurrent());

        this.list_children.add(assignement);
        assignement.setParent(getCurrent());
    }

    public Symbols getKeyword() {
        return this.keyword;
    }

    public Assignement getAssignement() {
        return this.assignement;
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
