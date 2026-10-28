package compiler.Parser;
import compiler.Code_generation.CodeGenerationVisitable;
import compiler.Code_generation.CodeGenerationVisitor;
import compiler.Lexer.Symbol;
import compiler.Lexer.Symbols.Identifier;
import compiler.Lexer.Symbols.Symbols;
import compiler.Parser.TreeStructure.CustomNode;
import compiler.Semantic_analysis.ASTVisitable;
import compiler.Semantic_analysis.ASTVisitor;

import java.util.ArrayList;

// Type -> Identifier + Identifier
// utilisation: a int
public class Type implements CustomNode, ASTVisitable, CodeGenerationVisitable {
    private final Symbols identifier;
    private Symbols type;
    private boolean arrayType;
    String token=this.getClass().getSimpleName();

    CustomNode parent;
    ArrayList<CustomNode> list_children= new ArrayList<>();

    public Type(Symbols identifier, Symbols type, boolean arrayType) {
        this.identifier = identifier;
        this.type = type;
        this.arrayType = arrayType;
        this.list_children.add(identifier);
        this.list_children.add(type);
        identifier.setParent(getCurrent());
        type.setParent(getCurrent());
    }

    public Symbols getIdentifier() {
        return identifier;
    }

    public Symbols getType() {
        return type;
    }

    public void setType(String newType, Symbols s, boolean isarray) {
         this.type = new Identifier(newType, s.getParent(), null, s.getLine());
         this.arrayType = isarray;
    }

    public boolean isArrayType(){return this.arrayType;}

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

    public String getToken(){return this.token;}

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
