package compiler.Semantic_analysis;

import compiler.Lexer.Lexer;
import compiler.Lexer.Symbols.*;
import compiler.Parser.*;
import compiler.Parser.TreeStructure.CustomNode;

import java.io.StringReader;
import java.util.*;

public class SemanticAnalyser implements ASTVisitor {

    private SymbolTable symbolTable;

    public SemanticAnalyser(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    public void analyze(StartingPoint startingPoint) {
        startingPoint.accept(this);
    }

    @Override
    public void visit(StartingPoint node) {
        for (Constante c : node.getConstante()) {
            c.accept(this);
        }
        for (Records r : node.getRecords()) {
            r.accept(this);
        }
        for (Procedure p : node.getProcedures_list()) {
            p.accept(this);
        }
        for (BlockStatement b : node.getBlockStatement_list()) {
            b.accept(this);
        }
    }

    // si access array, verif que l array existe, si record, verif que variable existe, est de type record et que le record a le field
    // TODO: make it reccursive
    @Override
    public void visit(AccessingData node) {
        node.getChildren().forEach(elements -> elements.accept(this));
        Type type = symbolTable.getVariableType(node.getElements().getValue());
        if(node.getIndexOrRecords() != null && node.getOptionalRecord() == null) {
            if (!Objects.equals(node.getIndexOrRecords().get_detected_type(), "int")) {
                throw new SemanticException("we need an int to access a data in an array.");
            }
            if(!type.isArrayType())
                throw new SemanticException("The variable you tried to access is not of array type.");
            node.setDetected_final_type(type);

        }
        if(node.getOptionalRecord() != null){
            if(node.getIndexOrRecords() == null && type.isArrayType())
                throw new SemanticException("you are trying to access a record into an array identified as "+node.getElements().getValue());
            Type typefield = symbolTable.getRecordTypeField(type.getType().getValue(), node.getOptionalRecord().getValue());
            node.setDetected_final_type(typefield);
        }

    }

    @Override
    public void visit(ArrayInit node) {
        node.getChildren().forEach(elements -> elements.accept(this));
        Operation size = node.getSize();
        if(!Objects.equals(size.get_detected_type(), "int")){
            throw new SemanticException("we need an int to access a data in an array.");
        }

    }

    // verif que le type des deux cotés match
    // si type == array, alors array init | operations avec 1 facteur identifier qui est un array | accessing data of type array
    // si operation, alors type detecte de l operation == type
    // si new record, alors type == identifier record
    @Override
    public void visit(Assignement node) {
        node.getChildren().forEach(elements -> elements.accept(this));
        String type;
        CustomNode name = node.getVariable();
        type = node.getType().getType().getValue();

        if(node.getExpr() != null) {
            Expressions expr = (Expressions) node.getExpr();
            // check if array type matches
            if(node.getType().isArrayType()){
                if(!Objects.equals(expr.getValue().getToken(), "ArrayInit")){
                    if(Objects.equals(expr.getValue().getToken(), "Operation")){
                        if(((Operation)expr.getValue()).get_list_factors().size() != 1)
                            throw new SemanticException("Type mismatch for '" + name + "'");
                        else if(Objects.equals(((Operation)expr.getValue()).get_list_factors().get(0).getToken(), "Identifier")){
                            Identifier var_id = (Identifier)((Operation)expr.getValue()).get_list_factors().get(0);
                            Type typeid = symbolTable.getVariableType(var_id.getValue());
                            if(!typeid.isArrayType())
                                throw new SemanticException(" ");
                        }
                    } else if (Objects.equals(expr.getValue().getToken(), "AccessingData")){
                        if(!((AccessingData) expr.getValue()).getDetectedType().isArrayType())
                            throw new SemanticException("Type mismatch for '" + name + "'");
                    } else
                        throw new SemanticException("Type mismatch for '" + name + "'");
                }
            } else if(Objects.equals(expr.getValue().getToken(), "ArrayInit"))
                throw new SemanticException("Type mismatch for '" + name + "'");
            else if (Objects.equals(expr.getValue().getToken(), "AccessingData")) {
                if (!((AccessingData) expr.getValue()).getDetectedType().isArrayType())
                    throw new SemanticException("Type mismatch for '" + name + "'");
            }


            if(Objects.equals(expr.getValue().getToken(), "Operation")){
                if(!Objects.equals(((Operation)expr.getValue()).get_detected_type(), type))
                    throw new SemanticException("Type mismatch in constant declaration for '" + name + "'");
            } else if(Objects.equals(expr.getValue().getToken(), "NewRecord")){
                if(!Objects.equals(((NewRecord)expr.getValue()).getIdentifier().getValue(), type))
                    throw new SemanticException("Type mismatch in constant declaration for '" + name + "'");
            }
        }
    }

    @Override
    public void visit(BlockStatement node) {
        symbolTable.enterScope();
        node.getStatements().forEach(statement -> statement.accept(this));
        symbolTable.exitScope();
    }

    // check if parameters are of type needed
    //TODO: check type input et output (mettre un champ detected type comme operation pour faciliter operation
    @Override
    public void visit(BuiltInFunction node) {
        node.getChildren().forEach(statement -> statement.accept(this));
        if(Objects.equals(node.getNameOfFunction(), "len")) {
            if (!Objects.equals(node.getParameters().get_detected_type(), "string")){
                if(node.getParameters().get_list_factors().size() != 1){
                    throw new SemanticException(" ");
                } else if(Objects.equals(node.getParameters().get_list_factors().get(0).getToken(), "Identifier")){
                    Identifier var_id = (Identifier)node.getParameters().get_list_factors().get(0);
                    Type type = symbolTable.getVariableType(var_id.getValue());
                    if(!type.isArrayType())
                        throw new SemanticException(" ");
                }
            }
        } else if(Objects.equals(node.getNameOfFunction(), "not")){
            if (!Objects.equals(node.getParameters().get_detected_type(), "bool")){
                throw new SemanticException(" ");
            }
        } else if(Objects.equals(node.getNameOfFunction(), "floor")){
            if (!Objects.equals(node.getParameters().get_detected_type(), "real")){
                throw new SemanticException(" ");
            }
        } else if(Objects.equals(node.getNameOfFunction(), "chr")){
            if (!Objects.equals(node.getParameters().get_detected_type(), "int")){
                throw new SemanticException(" ");
            }
        }
    }

    @Override
    public void visit(Constante node) {
        node.getChildren().forEach(param -> param.accept(this));
        String name = node.getAssignement().getType().getIdentifier().getValue();
        symbolTable.registerVariable(name, node.getAssignement());
        symbolTable.registerVariableType(name, node.getAssignement().getType());
    }

    // check the condition is boolean and for loop ??
    // TODO: pouvoir avoir des return dans control structure
    @Override
    public void visit(ControlStructure node) {
        node.getChildren().forEach(param -> param.accept(this));
        if(node.getCondition() != null){
            if(!Objects.equals(node.getCondition().get_detected_type(), "bool"))
                throw new SemanticException("wrong type of data for condition");
        }
    }

    // TODO: verif si expression est vraiment util
    @Override
    public void visit(Expressions node) {
        node.getChildren().forEach(elements -> elements.accept(this));
    }

    @Override
    public void visit(GlobalVariable node) {
        node.getAssignement().accept(this);
        Type type = node.getAssignement().getType();
        symbolTable.registerVariable(type.getIdentifier().getValue(), node.getAssignement());
        symbolTable.registerVariableType(type.getIdentifier().getValue(), node.getAssignement().getType());
    }


    // verif input est bon
    //TODO: check type input et output (mettre un champ detected type comme operation pour faciliter operation
    @Override
    public void visit(InOutFunction node) {
        node.getChildren().forEach(param -> param.accept(this));
    }

    // verif que paramètre match
    //TODO: verif que le type record existe ou procedure
    //TODO: on peut mettre procedure dans operations car square(2) +4 doit fonctionner
    @Override
    public void visit(NewRecord node) {
        node.getChildren().forEach(record -> record.accept(this));
        if(!symbolTable.isParametersValid(node.getIdentifier().getValue(), node.getListParameters()))
            throw new SemanticException("Procedure return type is void but contains return");

    }

    @Override
    public void visit(Numbers node) {
    }

    // TODO: verif que return match
    // TODO: verif que les parameters ont des types coherents
    //TODO: quid si return dans un control structure?????????
    @Override
    public void visit(Procedure node) {
        //Expression | Array | AccessingData | NewRecord
        node.getParameters().forEach(param -> param.accept(this));
        if (!symbolTable.isFunction(node.getName().getValue())) {
            symbolTable.registerFunction(node.getName(), node);
        }
        String returnType = node.getReturnType().getType().getValue();
        boolean arraytype = node.IsArrayType();
        ArrayList<Expressions> returnList = node.getBlockStatement().getReturns();

        if (returnType.equals("void") && returnList.size() != 0) {
            throw new SemanticException("Procedure return type is void but contains return");
        }

        if (returnType.equals("void") && node.IsArrayType()) {
            throw new SemanticException("Procedure return type is void but contains return");
        }


        if (!returnType.equals("void") && returnList.size() == 0) {
            throw new SemanticException("this procedure requires return statements.");
        }

        for (Expressions type_actual: returnList) {
            if(Objects.equals(type_actual.getValue().getToken(), "Operation")){
                Operation op = (Operation) type_actual.getValue();
                if(!Objects.equals(returnType, op.get_detected_type()))
                    throw new SemanticException("The parameter "+ op +" does not match the expected type.");
                if(arraytype){
                    if(op.get_list_factors().size() != 1)
                        throw new SemanticException("The parameter "+ op +" does not match the expected type.");
                    else if(Objects.equals(op.get_list_factors().get(0).getToken(), "Identifier")){
                        Identifier var_id = (Identifier)op.get_list_factors().get(0);
                        Type typeid = symbolTable.getVariableType(var_id.getValue());
                        if(!typeid.isArrayType())
                            throw new SemanticException("The parameter "+ op +" does not match the expected type.");
                    }
                }
            } else if(Objects.equals(type_actual.getValue().getToken(), "NewRecord")){
                NewRecord newrecord = (NewRecord) type_actual.getValue();
                if ((arraytype && !newrecord.isArrayType()) ||
                        !Objects.equals(returnType, newrecord.getReturnType()))
                    throw new SemanticException("The parameter "+ newrecord +" does not match the expected type.");
            } else if(Objects.equals(type_actual.getValue().getToken(), "ArrayInit")){
                ArrayInit arrayinit = (ArrayInit) type_actual.getValue();
                if (!arraytype ||
                        !Objects.equals(returnType, arrayinit.getType().getValue()))
                    throw new SemanticException("The parameter "+ arrayinit +" does not match the expected type.");
            }
        }
    }

    @Override
    public void visit(Records node) {
        node.getChildren().forEach(param -> param.accept(this));
        symbolTable.registerVariable(node.getIdentifier().getValue(), node);
        symbolTable.newType(node.getIdentifier().getValue());
    }

    // TODO: verifier que type existe
    @Override
    public void visit(Type node) {
        node.getChildren().forEach(param -> param.accept(this));
        if(node.getType() == null)
            node.setType(symbolTable.getVariableType(node.getIdentifier().getValue()).getType().getValue(),
                    node.getIdentifier(),
                    symbolTable.getVariableType(node.getIdentifier().getValue()).isArrayType());
    }

    // ATTENTION: inoutfunction et accessing data
    //TODO: diminuer taille de la fonction
    @Override
    public void visit(Operation operation) {
        HashSet<String> possibility_type = new HashSet<>(Arrays.asList("string", "int", "real", "boolean"));
        boolean string_accepted = true;
        boolean boolean_accepted = true;
        boolean nbr_accepted = true;
        for(CustomNode n: operation.get_list_operators()){
            if(Objects.equals(n.getToken(), "ArithmeticOperator")){
                possibility_type.remove("boolean");
                if(Objects.equals(((ArithmeticOperator) n).getValue(), "%")){
                    if(possibility_type.contains("int"))
                        possibility_type = new HashSet<>(List.of("int"));
                } else if(!Objects.equals(((ArithmeticOperator) n).getValue(), "+")){
                    possibility_type.remove("string");
                }
            }
            if(Objects.equals(n.getToken(), "ComparingOperator")){
                possibility_type = new HashSet<>(List.of("boolean"));
                if(!Objects.equals(((ComparingOperator) n).getValue(), "==") && !Objects.equals(((ComparingOperator) n).getValue(), "<>")){
                    string_accepted = false;
                    if(!Objects.equals(((ComparingOperator) n).getValue(), "and") && !Objects.equals(((ComparingOperator) n).getValue(), "or")){
                        boolean_accepted = false;
                    }
                }
            }
        }


        for(CustomNode n: operation.get_list_factors()){
            n.accept(this);
            if (possibility_type.contains("boolean") && possibility_type.size() == 1) {
                if (Objects.equals(n.getToken(), "Operation")) {
                    if(Objects.equals(((Operation) n).get_detected_type(), "int") || Objects.equals(((Operation) n).get_detected_type(), "real")){
                        if(nbr_accepted) {
                            string_accepted = false;
                            boolean_accepted = false;
                        } else {
                            throw new SemanticException("Sometimes wrong about the operation.");
                        }
                    }
                    else {
                        if(string_accepted || boolean_accepted)
                            nbr_accepted = false;
                        else
                            throw new SemanticException("Sometimes wrong about the operation.");
                    }
                } else if (Objects.equals(n.getToken(), "Numbers")) {
                    if(nbr_accepted) {
                        string_accepted = false;
                        boolean_accepted = false;
                    } else {
                        throw new SemanticException("Sometimes wrong about the operation.");
                    }
                } else if (Objects.equals(n.getToken(), "BuiltInFunction")) {
                    // return int only
                    if(Objects.equals(((BuiltInFunction) n).getNameOfFunction(), "len") || Objects.equals(((BuiltInFunction) n).getNameOfFunction(), "floor")){
                        if(nbr_accepted) {
                            string_accepted = false;
                            boolean_accepted = false;
                        } else {
                            throw new SemanticException("Sometimes wrong about the operation.");
                        }
                    } else if(Objects.equals(((BuiltInFunction) n).getNameOfFunction(), "not")){
                        if(boolean_accepted) {
                            string_accepted = false;
                            nbr_accepted = false;
                        } else {
                            throw new SemanticException("Sometimes wrong about the operation.");
                        }
                    } else if(Objects.equals(((BuiltInFunction) n).getNameOfFunction(), "chr")){
                        if(string_accepted) {
                            nbr_accepted = false;
                            boolean_accepted = false;
                        } else {
                            throw new SemanticException("Sometimes wrong about the operation.");
                        }
                    }
                } else if (Objects.equals(n.getToken(), "InOutFunction")) {

                } else if (Objects.equals(n.getToken(), "Strings")) {
                    if(string_accepted) {
                        nbr_accepted = false;
                        boolean_accepted = false;
                    } else {
                        throw new SemanticException("Sometimes wrong about the operation.");
                    }
                } else if (Objects.equals(n.getToken(), "Booleans")) {
                    if(boolean_accepted) {
                        nbr_accepted = false;
                        string_accepted = false;
                    } else {
                        throw new SemanticException("Sometimes wrong about the operation.");
                    }
                } else if (Objects.equals(n.getToken(), "Identifier")) {
                    Identifier i = (Identifier) n;
                    Type type = symbolTable.getVariableType(i.getValue());
                    if(type.getType().getValue().equals("int") ||
                            type.getType().getValue().equals("real")){
                        if(nbr_accepted) {
                            string_accepted = false;
                            boolean_accepted = false;
                        } else {
                            throw new SemanticException("Sometimes wrong about the operation.");
                        }
                    } else if(type.getType().getValue().equals("string")){
                        if(string_accepted) {
                            nbr_accepted = false;
                            boolean_accepted = false;
                        } else {
                            throw new SemanticException("Sometimes wrong about the operation.");
                        }
                    } else if(type.getType().getValue().equals("bool")){
                        if(boolean_accepted) {
                            nbr_accepted = false;
                            string_accepted = false;
                        } else {
                            throw new SemanticException("Sometimes wrong about the operation.");
                        }
                    }
                } else if(Objects.equals(n.getToken(), "AccessingData")){

                }
            } else {
                if (Objects.equals(n.getToken(), "Operation")) {
                    if(Objects.equals(((Operation) n).get_detected_type(), "int")){
                        if(possibility_type.contains("int"))
                            possibility_type = new HashSet<>(List.of("int"));
                        else if(!possibility_type.contains("real"))
                            throw new SemanticException("Something is wrong about the operation.");
                    } else if (Objects.equals(((Operation) n).get_detected_type(), "real")){
                        if(possibility_type.contains("real") || possibility_type.contains("int"))
                            possibility_type = new HashSet<>(List.of("real"));
                        else
                            throw new SemanticException("Something is wrong about the operation.");
                    } else if(Objects.equals(((Operation) n).get_detected_type(), "string")) {
                        if(possibility_type.contains("string"))
                            possibility_type = new HashSet<>(List.of("string"));
                        else
                            throw new SemanticException("Something is wrong about the operation.");
                    } else {
                        throw new SemanticException("Something is wrong about the operation.");
                    }
                } else if (Objects.equals(n.getToken(), "Numbers")) {
                    if(Objects.equals(((Numbers) n).getNumber().getToken(), "NaturalNumber")){
                        if(possibility_type.contains("int"))
                            possibility_type = new HashSet<>(List.of("int"));
                        else if(!possibility_type.contains("real"))
                            throw new SemanticException("Something is wrong about the operation.");
                    } else if(Objects.equals(((Numbers) n).getNumber().getToken(), "RealNumber")){
                        if(possibility_type.contains("real") || possibility_type.contains("int"))
                            possibility_type = new HashSet<>(List.of("real"));
                        else
                            throw new SemanticException("Something is wrong about the operation.");
                    }
                } else if (Objects.equals(n.getToken(), "BuiltInFunction")) {
                    // return int only
                    if(Objects.equals(((BuiltInFunction) n).getNameOfFunction(), "len") || Objects.equals(((BuiltInFunction) n).getNameOfFunction(), "floor")){
                        if(possibility_type.contains("int"))
                            possibility_type = new HashSet<>(List.of("int"));
                        else if(!possibility_type.contains("real"))
                            throw new SemanticException("Something is wrong about the operation.");
                    } else if(Objects.equals(((BuiltInFunction) n).getNameOfFunction(), "chr")){
                        if(possibility_type.contains("string"))
                            possibility_type = new HashSet<>(List.of("string"));
                        else
                            throw new SemanticException("Something is wrong about the operation.");
                    }else if(Objects.equals(((BuiltInFunction) n).getNameOfFunction(), "not")){
                        throw new SemanticException("Something is wrong about the operation.");
                    }
                } else if (Objects.equals(n.getToken(), "InOutFunction")) {
                    if(Objects.equals(((InOutFunction) n).getNameOfFunction(), "readInt")){
                        if(possibility_type.contains("int"))
                            possibility_type = new HashSet<>(List.of("int"));
                        else if(!possibility_type.contains("real"))
                            throw new SemanticException("Something is wrong about the operation.");
                    } else if(Objects.equals(((InOutFunction) n).getNameOfFunction(), "readReal")){
                        if(possibility_type.contains("real") || possibility_type.contains("int"))
                            possibility_type = new HashSet<>(List.of("real"));
                        else
                            throw new SemanticException("Something is wrong about the operation.");
                    } else if(Objects.equals(((InOutFunction) n).getNameOfFunction(), "ReadString")){
                        if(possibility_type.contains("string"))
                            possibility_type = new HashSet<>(List.of("string"));
                        else
                            throw new SemanticException("Something is wrong about the operation.");
                    } else
                        throw new SemanticException("Something is wrong about the operation.");
                } else if (Objects.equals(n.getToken(), "Strings")) {
                    if(possibility_type.contains("string"))
                        possibility_type = new HashSet<>(List.of("string"));
                    else
                        throw new SemanticException("Something is wrong about the operation.");
                } else if (Objects.equals(n.getToken(), "Booleans")) {
                    throw new SemanticException("Something is wrong about the operation.");
                } else if (Objects.equals(n.getToken(), "Identifier")) {
                    if(symbolTable.getVariableType(((Identifier) n).getValue()).isArrayType() && operation.get_list_operators().size() != 0) {
                        throw new SemanticException("Impossible to perform an operation with an array.");
                    }
                    if(symbolTable.getVariableType(((Identifier) n).getValue()).getType().getValue().equals("int")) {
                        if(possibility_type.contains("int"))
                            possibility_type = new HashSet<>(List.of("int"));
                        else if(!possibility_type.contains("real"))
                            throw new SemanticException("Something is wrong about the operation.");
                    }if(symbolTable.getVariableType(((Identifier) n).getValue()).getType().getValue().equals("real")){
                        if(possibility_type.contains("real") || possibility_type.contains("int"))
                            possibility_type = new HashSet<>(List.of("real"));
                        else
                            throw new SemanticException("Something is wrong about the operation.");
                    } else if(symbolTable.getVariableType(((Identifier) n).getValue()).getType().getValue().equals("string")){
                        if(possibility_type.contains("string"))
                            possibility_type = new HashSet<>(List.of("string"));
                        else
                            throw new SemanticException("Something is wrong about the operation.");
                    } else if(symbolTable.getVariableType(((Identifier) n).getValue()).getType().getValue().equals("bool")){
                        throw new SemanticException("Something is wrong about the operation.");
                    }
                } else if(Objects.equals(n.getToken(), "AccessingData")){

                }
            }
        }
        if(possibility_type.size() != 1){
            throw new SemanticException("Something is wrong about the operation.");
        }
        for( String s: possibility_type)
            operation.setDetected_type(s);
    }

    public static void main(String[] args) throws Exception {
        String input = """
                var a int = 2;
                var b string = "new";
                var test int = (a*3)+2 - len(b);
                var b_bis string = b + "rules";
                var am string = "me";
                var bm string = "you";
                var cm string = am + bm;
                a = 3;

                // int
                var a2 int = 2;
                a = a +1;
                a = 2%10;

                // string
                var b2 string = "new";
                var test2 int = (a*3)+2 - len(b);
                var b_bis2 string = b + "rules";
                var am2 string = "me";
                var bm2 string = "you";
                var cm2 string = am + bm;

                //boolean
                var c7 boolean = true and false;
                var c4 boolean = 2 < 3;
                var c8 boolean = "you" == "me";
                var c5 boolean = true <> false;

                //array
                var array int[] = int[](5);
                delete(array);
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        CustomNode root = parser.parseStartingPoint();

        SymbolTable table = new SymbolTable();
        root.accept(new SemanticAnalyser(table));
    }
}

