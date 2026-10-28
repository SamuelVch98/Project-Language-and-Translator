package compiler.Parser;

import compiler.Code_generation.CodeGenerationVisitable;
import compiler.Code_generation.CodeGenerationVisitor;
import compiler.Lexer.Symbols.Symbols;
import compiler.Parser.TreeStructure.CustomNode;
import compiler.Semantic_analysis.ASTVisitable;
import compiler.Semantic_analysis.ASTVisitor;

import java.util.ArrayList;

// GettingByIndex -> strings+lboxbracket+naturalnumber+rboxbracket | identifier+lboxbracket+naturalnumber+rboxbracket
public class AccessingData implements CustomNode, ASTVisitable, CodeGenerationVisitable {
    Symbols elements;
    Operation indexOrRecords;
    Symbols optionalRecord;
    Type detected_final_type;

    String token=this.getClass().getSimpleName();
    CustomNode parent;
    ArrayList<CustomNode> list_children=new ArrayList<>();

    public AccessingData(Symbols elements, Operation indexOrRecords, Symbols optionalRecord) {
        this.list_children.add(elements);
        if(indexOrRecords != null)
            this.list_children.add(indexOrRecords);
        if(optionalRecord != null)
            this.list_children.add(optionalRecord);

        for(CustomNode e: this.list_children){
            e.setParent(getCurrent());
        }

        this.elements = elements;
        this.indexOrRecords = indexOrRecords;
        this.optionalRecord = optionalRecord;
    }

    public Symbols getElements() {
        return elements;
    }

    public Operation getIndexOrRecords() {
        return indexOrRecords;
    }

    public Symbols getOptionalRecord() {
        return optionalRecord;
    }

    public String getToken(){return this.token;}

    public Type getDetectedType(){return detected_final_type;}

    public void setDetected_final_type(Type type){this.detected_final_type = type;}

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

    public String toString(){
        StringBuilder s = new StringBuilder(elements.getValue());
        if(indexOrRecords != null) {
            s.append("[");
            for (CustomNode c : indexOrRecords.list_operation) {
                s.append(c.toString());
            }
            s.append("]");
        }
        if(optionalRecord != null){
            s.append(".");
            s.append(optionalRecord);
        }
        return s.toString();
    }
}
