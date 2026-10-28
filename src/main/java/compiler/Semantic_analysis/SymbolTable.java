package compiler.Semantic_analysis;

import compiler.Lexer.Symbols.Identifier;
import compiler.Lexer.Symbols.Symbols;
import compiler.Parser.*;
import compiler.Parser.TreeStructure.CustomNode;

import java.util.*;

public class SymbolTable {
    private Stack<Map<String, CustomNode>> scopes;
    private Stack<Map<String, Type>> types;
    private Set<String> available_types;

    public SymbolTable() {
        this.scopes = new Stack<>();
        this.types = new Stack<>();
        this.available_types = new HashSet<>();
        this.available_types.add("bool");
        this.available_types.add("int");
        this.available_types.add("string");
        this.available_types.add("real");
        enterScope();
    }

    public void enterScope() {
        this.scopes.push(new HashMap<>());
        this.types.push(new HashMap<>());
    }

    public void exitScope() {
        this.scopes.pop();
        this.types.pop();
    }

    public void registerVariable(String name, CustomNode type) {
        if (scopes.peek().containsKey(name)) {
            throw new SemanticException("Variable '" + name + "' already declared in this scope");
        }
        scopes.peek().put(name, type);
    }

    public void registerFunction(Symbols name, Procedure p) {
        String functionName = name.getValue();
        if (types.peek().containsKey(functionName)) {
            throw new SemanticException("Function '" + name + "' already declared as a type");
        }
        if (scopes.peek().containsKey(functionName)) {
            throw new SemanticException("Function '" + name + "' already declared in this scope");
        }
        types.peek().put(functionName, p.getReturnType());
        scopes.peek().put(functionName, p);
    }

    public void registerVariableType(String identifier, Type type) {
        if(!this.isType(type.getType().getValue()))
            throw new SemanticException("the type '" + type + "' doesn't exist");
        types.peek().put(identifier, type);
    }

    //Function is already present ?
    public boolean isFunction(String name) {
        for (Map<String, CustomNode> scope : scopes) {
            if (scope.containsKey(name) && scope.get(name).getToken().equals("Procedure")) {
                return true;
            }
        }
        return false;
    }

    public Type getVariableType(String name) {
        for (Map<String, Type> type : types) {
            if (type.containsKey(name)) {
                return type.get(name);
            }
        }
        throw new SemanticException("Variable '" + name + "' not defined");
    }

    public boolean isType(String type) {
        return this.available_types.contains(type);
    }

    public void newType(String type) {
        if(isType(type))
            throw new SemanticException("the type '" + type + "' already exist");
        this.available_types.add(type);
    }

    public boolean lookupFunctionSignature(String name, Type[] argTypes) {
        boolean match = true;
        for (Map<String, CustomNode> scope : scopes) {
            if (scope.containsKey(name) && scope.get(name).getToken().equals("Procedure")) {
                Procedure p = (Procedure) scope.get(name);
                if (p.getParameters().size() != argTypes.length) {
                    continue;
                }
                for (int i = 0; i < argTypes.length; i++) {
                   if(!p.getParameters().get(i).equals(argTypes[i])) {
                        match = false;
                        break;
                    }
                }
            }
        }
        return match;
    }

    public Type getRecordTypeField(String nameRecord, String field){
        for (Map<String, CustomNode> scope : scopes) {
            if (scope.containsKey(nameRecord) && scope.get(nameRecord).getToken().equals("Records")) {
                for(Type t: ((Records) scope.get(nameRecord)).getBlockRecord()){
                    if (t.getIdentifier().getValue().equals(field)) return t;
                }
                throw new SemanticException("the field "+field+" have not been found in the record "+nameRecord);
            }
        }
        throw new SemanticException("the record "+nameRecord+" have not been found.");
    }

    //For new records or function calls
    // check that it is procedure or function
    // check that each type matches
    //expression is either operation, either initarray, either new record
    //todo: finish this
    public boolean isParametersValid(String nameRcFc, List<Expressions> list_param){
        CustomNode block_parameters;
        for (Map<String, CustomNode> scope : scopes) {
            if (scope.containsKey(nameRcFc)) {
                block_parameters = scope.get(nameRcFc);
                ArrayList<Type> list_param_expected;
                if(Objects.equals(block_parameters.getToken(), "Records")){
                    list_param_expected = ((Records) block_parameters).getBlockRecord();
                } else if (Objects.equals(block_parameters.getToken(), "Procedure")) {
                    list_param_expected = ((Procedure) block_parameters).getParameters();
                } else {
                    throw new SemanticException("The name "+ nameRcFc+" does not define a record or procedure.");
                }
                if(list_param_expected.size() != list_param.size())
                    throw new SemanticException("There is parameters missing in this record creation.");
                for(int i=0; i<list_param_expected.size(); i++){
                    Type type_expected = list_param_expected.get(i);
                    Expressions type_actual = list_param.get(i);
                    if(Objects.equals(type_actual.getValue().getToken(), "Operation")){
                        Operation op = (Operation) type_actual.getValue();
                        if(!Objects.equals(type_expected.getType().getValue(), op.get_detected_type()))
                            throw new SemanticException("The parameter "+ op +" does not match the expected type.");
                        if(type_expected.isArrayType()){
                            if(op.get_list_factors().size() != 1)
                                throw new SemanticException("The parameter "+ op +" does not match the expected type.");
                            else if(Objects.equals(op.get_list_factors().get(0).getToken(), "Identifier")){
                                Identifier var_id = (Identifier)op.get_list_factors().get(0);
                                Type typeid = this.getVariableType(var_id.getValue());
                                if(!typeid.isArrayType())
                                    throw new SemanticException("The parameter "+ op +" does not match the expected type.");
                            }
                        }
                    } else if(Objects.equals(type_actual.getValue().getToken(), "NewRecord")){
                        NewRecord newrecord = (NewRecord) type_actual.getValue();
                        if ((type_expected.isArrayType() && !newrecord.isArrayType()) ||
                                !Objects.equals(type_expected.getType().getValue(), newrecord.getReturnType()))
                            throw new SemanticException("The parameter "+ newrecord +" does not match the expected type.");
                    } else if(Objects.equals(type_actual.getValue().getToken(), "ArrayInit")){
                        ArrayInit arrayinit = (ArrayInit) type_actual.getValue();
                        if (!type_expected.isArrayType() ||
                                !Objects.equals(type_expected.getType().getValue(), arrayinit.getType().getValue()))
                            throw new SemanticException("The parameter "+ arrayinit +" does not match the expected type.");
                    }
                }
            }
        }
        throw new SemanticException("The record "+ nameRcFc+" is undefined.");
    }
}