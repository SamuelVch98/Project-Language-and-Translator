package compiler.Parser;

import compiler.Code_generation.CodeGenerationVisitable;
import compiler.Code_generation.CodeGenerationVisitor;
import compiler.Lexer.Symbols.Symbols;
import compiler.Parser.TreeStructure.CustomNode;
import compiler.Semantic_analysis.ASTVisitable;
import compiler.Semantic_analysis.ASTVisitor;

import java.util.ArrayList;

// Records -> "record" + identifier + lbracebracket + BlockRecords + rbracebrackets

public class Records implements CustomNode, ASTVisitable, CodeGenerationVisitable {
    Symbols identifier;
    ArrayList<Type> blockRecord;

    String token=this.getClass().getSimpleName();
    CustomNode parent=null;
    ArrayList<CustomNode> list_children=new ArrayList<>();

    public Records(Symbols identifier, ArrayList<Type> blockRecords) {
        this.identifier = identifier;
        this.blockRecord = blockRecords;

        this.list_children.add(identifier);
        identifier.setParent(getCurrent());

        for(Type type: blockRecords) {
            this.list_children.add(type);
            type.setParent(getCurrent());
        }
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

    public Symbols getIdentifier() {
        return identifier;
    }

    public ArrayList<Type> getBlockRecord() {
        return blockRecord;
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