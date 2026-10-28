package compiler.Parser;

import compiler.Code_generation.CodeGenerationVisitable;
import compiler.Code_generation.CodeGenerationVisitor;
import compiler.Parser.TreeStructure.CustomNode;
import compiler.Semantic_analysis.ASTVisitable;
import compiler.Semantic_analysis.ASTVisitor;

import java.util.ArrayList;

public class StartingPoint implements CustomNode, ASTVisitable, CodeGenerationVisitable {
    String token=this.getClass().getSimpleName();
    ArrayList<Constante> constante;
    ArrayList<Records> records;
    ArrayList<Procedure> procedures_list;
    ArrayList<BlockStatement> blockStatement_list;

    CustomNode parent=null;
    ArrayList<CustomNode> list_children=new ArrayList<>();
    public StartingPoint(ArrayList<Constante> constantes, ArrayList<Records> records,
                         ArrayList<Procedure> procedures_list,
                         ArrayList<BlockStatement> blockStatement_list){
        for(CustomNode e: constantes) {
            this.list_children.add(e);
            e.setParent(getCurrent());
        }
        this.constante = constantes;

        for(CustomNode e: records) {
            this.list_children.add(e);
            e.setParent(getCurrent());
        }
        this.records = records;

        for(CustomNode e: procedures_list) {
            this.list_children.add(e);
            e.setParent(getCurrent());
        }
        this.procedures_list = procedures_list;

        for(CustomNode e: blockStatement_list) {
            this.list_children.add(e);
            e.setParent(getCurrent());
        }
        this.blockStatement_list = blockStatement_list;
    }

    public String toString(){
        return constante.toString();
    }

    public String getToken(){
        return this.token;
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

    public ArrayList<Constante> getConstante() {
        return constante;
    }

    public ArrayList<Records> getRecords() {
        return records;
    }

    public ArrayList<Procedure> getProcedures_list() {
        return procedures_list;
    }

    public ArrayList<BlockStatement> getBlockStatement_list() {
        return blockStatement_list;
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
