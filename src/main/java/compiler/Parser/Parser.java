package compiler.Parser;

import compiler.Lexer.Lexer;
import compiler.Lexer.Symbols.Symbols;
import compiler.Parser.TreeStructure.CustomNode;

import javax.naming.ldap.Control;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Parser {
    Lexer lexer;
    Symbols currentSymbol;
    CustomNode root;

    public Parser(Lexer lexer){
        this.lexer = lexer;
        currentSymbol = lexer.getNextSymbol();
    }

    public Symbols match(String s) throws Exception {
        //if(currentSymbol == null) throw new Exception("something is missing");
        if(currentSymbol != null) {
            if (!currentSymbol.getToken().equals(s) && !currentSymbol.getValue().equals(s)) {
                //System.out.println("Expected " + s + " but got " + currentSymbol.getToken());
            } else if (currentSymbol.getValue().equals(s) | currentSymbol.getToken().equals(s)) {
                Symbols matchingSymbol = currentSymbol;
                currentSymbol = lexer.getNextSymbol();
                return matchingSymbol;
            }
        }
        return null;
    }

    // Type -> identifier + identifier | identifier + identifier + [ + ]
    // reads from structure: "name_of_variable type_of_variable"
    public Type parseType() throws Exception {
        Lexer lexer_copy = lexer.clone();
        Symbols currentSymbols_copy = currentSymbol.clone();
        Symbols identifier = match("Identifier");
        Symbols type = match("Identifier");
        if (identifier != null && type != null){
            if(match("[") != null){
                if(match("]") != null){
                    return new Type(identifier, type, true);
                }
                throw new Exception("Missing a closing bracket for the type at line: "+currentSymbol.getLine());
            }
            return new Type(identifier, type, false);
        }
        this.currentSymbol = currentSymbols_copy;
        this.lexer = lexer_copy;
        return null;
    }

    // Array -> identifier + size
    // reads from structure: "type_of_variable[](size_of_array)"
    public ArrayInit parseArrayInit() throws Exception {
        Lexer lexer_copy = lexer.clone();
        Symbols currentSymbols_copy = currentSymbol.clone();

        Symbols identifier = match("Identifier");
        if(identifier != null){

            Symbols lBox = match("[");
            if(lBox != null) {
                Symbols rBox = match("]");
                if(rBox != null) {
                    Symbols lpar = match("(");
                    if(lpar != null) {
                        Operation size = parseOperations(false);
                        if(size != null) {
                            Symbols rpar = match(")");
                            if(rpar != null)
                                return new ArrayInit(identifier, size);
                        } else {
                            System.out.println("erreur? obligé d'avoir une taille?");
                        }
                    }
                }
            }
        }
        this.currentSymbol = currentSymbols_copy;
        this.lexer = lexer_copy;
        return null;
    }

    // Expression -> NumberOperations | StringOperations | BooleanOperations
    // reads from structure: ""string" + "string"| 1*2 | true and false
    public Expressions parseExpression(boolean onlyBaseTypes) throws Exception {
        Lexer lexer_copy = lexer.clone();
        Symbols currentSymbols_copy = currentSymbol.clone();

        if(!onlyBaseTypes) {

            ArrayInit newArray = parseArrayInit();
            if (newArray != null) return new Expressions(newArray);
            InOutFunction trying = parseInOutFunction();
            if(trying == null) {
                NewRecord newrecord = parseNewRecord();
                if (newrecord != null) return new Expressions(newrecord);
            } else {
                lexer = lexer_copy.clone();
                currentSymbol = currentSymbols_copy.clone();
            }

        }
        Operation identifier = parseOperations(onlyBaseTypes);
        if (identifier != null) return new Expressions(identifier);

        this.currentSymbol = currentSymbols_copy;
        this.lexer = lexer_copy;
        return null;
    }

    // Param -> (Expression | Array | AccessingData | NewRecord) | Type
    public CustomNode parseParam(boolean forinit) throws Exception {
        Lexer lexer_copy = this.lexer.clone();
        Symbols currentSymbol_copy = this.currentSymbol.clone();

        if(!forinit) {
            Expressions expr = parseExpression(false);
            if (expr != null) return expr;
        } else{
            Type type = parseType();
            if(type != null)return type;
        }

        this.lexer = lexer_copy;
        this.currentSymbol = currentSymbol_copy;
        return null;
    }

    // Parameters -> Param + Parameters | epsilon
    // reads from structure: "(param1, param2)"
    public ArrayList<Expressions> parseParameters() throws Exception {
        ArrayList<Expressions> list_parameters = new ArrayList<>();
        while (!Objects.equals(this.currentSymbol.getValue(), ")")) {
            Expressions param = parseExpression(false);
            if (param == null) {
                throw new Exception("There is a character missing for parameters at line: "+currentSymbol.getLine());
            }
            list_parameters.add(param);
            if (match(",") == null && !Objects.equals(this.currentSymbol.getValue(), ")"))
                throw new Exception("There is a character missing for parameters at line: "+currentSymbol.getLine());
        }
        return list_parameters;
    }

    // Numbers -> NaturalNumber | RealNumber
    public Numbers parseNumber() throws Exception {
        Lexer lexer_copy = this.lexer.clone();
        Symbols currentSymbol_copy = this.currentSymbol.clone();
        Symbols nn = match("NaturalNumber");
        if(nn == null){
            Symbols real = match("RealNumber");
            if(real == null){
                this.lexer = lexer_copy;
                this.currentSymbol = currentSymbol_copy;
                return null;
            }
            return new Numbers(real);
        }
        return new Numbers(nn);
    }

    //Operations -> ( | ) | AccessingData | Strings | Booleans | Number | Built-in function | Identifier
    // ATTENTION: unary -
    public Operation parseOperations(boolean onlyBaseType) throws Exception {
        Lexer lexer_copy = this.lexer.clone();
        Symbols currentSymbol_copy = this.currentSymbol.clone();

        ArrayList<CustomNode> list_operation = new ArrayList<>();
        ArrayList<CustomNode> list_factor = new ArrayList<>();
        ArrayList<CustomNode> list_operators = new ArrayList<>();

        CustomNode unary = match("-");
        if(unary != null) {
            list_operation.add(unary);
            list_operators.add(unary);
        }
        CustomNode lpar = match("(");
        CustomNode left = null;
        if(lpar != null) {
            list_operation.add(lpar);
            Operation new_op = parseOperations(onlyBaseType);
            if(new_op == null){
                this.lexer = lexer_copy;
                this.currentSymbol = currentSymbol_copy;
                return null;
            }
            list_operation.add(new_op);
            list_factor.add(new_op);
            lpar = match(")");
            if(lpar == null){
                throw new Exception("There is a closing windows missing for parameters at line: "+currentSymbol.getLine());
            }
            list_operation.add(lpar);
        } else {
            left = parseNumber();

            if (left == null && !onlyBaseType) {
                left = parseBuiltInFunction();
            }
            if (left == null && !onlyBaseType) {
                left = parseInOutFunction();
            }
            if (left == null && !onlyBaseType) {
                left = parseAccessingData();
            }
            if (left == null) {
                left = match("Strings");
            }
            if (left == null) {
                left = match("Booleans");
            }
            if (left == null) {
                left = match("Identifier");
            }
        }

        if(left != null || lpar != null) {
            if(left != null){
                list_operation.add(left);
                list_factor.add(left);
            }
            Symbols op = match("ArithmeticOperator");
            if(op == null){
                op = match("ComparingOperator");
            }
            while (op != null) {
                list_operation.add(op);
                list_operators.add(op);
                left = match("(");
                if (left != null) {
                    list_operation.add(left);
                    Operation new_op = parseOperations(onlyBaseType);
                    if(new_op == null){
                        this.lexer = lexer_copy;
                        this.currentSymbol = currentSymbol_copy;
                        return null;
                    }
                    list_operation.add(new_op);
                    list_factor.add(new_op);
                    left = match(")");
                    if(left == null){
                        throw new Exception("There is a closing windows missing for parameters at line: "+currentSymbol.getLine());
                    }
                    list_operation.add(left);
                } else {
                    CustomNode right = parseNumber();

                    if (right == null && !onlyBaseType) {
                        right = parseBuiltInFunction();
                    }
                    if (right == null && !onlyBaseType) {
                        right = parseInOutFunction();
                    }
                    if (right == null && !onlyBaseType) {
                        right = parseAccessingData();
                    }
                    if (right == null) {
                        right = match("Strings");
                    }
                    if (right == null) {
                        right = match("Booleans");
                    }
                    if (right == null) {
                        right = match("Identifier");
                    }

                    if (right == null) {
                        this.lexer = lexer_copy;
                        this.currentSymbol = currentSymbol_copy;
                        return null;
                    }
                    list_operation.add(right);
                    list_factor.add(right);
                }

                op = match("ArithmeticOperator");
                if(op == null){
                    op = match("ComparingOperator");
                    if(op != null && !Objects.equals(list_operators.get(0).getToken(), op.getToken())){
                        throw new Exception("There is an operator mismatching at line: "+currentSymbol.getLine());
                    }
                } else if(!Objects.equals(list_operators.get(0).getToken(), op.getToken())){
                    throw new Exception("There is an operator mismatching at line: "+currentSymbol.getLine());
                }
            }
            return new Operation(list_operation, list_factor, list_operators);
        }
        if(list_operation.size() != 0){
            return new Operation(list_operation, list_factor, list_operators);
        }
        this.lexer = lexer_copy;
        this.currentSymbol = currentSymbol_copy;
        return null;
    }

    // 3 types de data acces
    // 1. array: a[3]
    // 2. records: a.x
    // 3. records in an array: a[3].x
    // AccessingData -> identifier + "." + identifier | identifier + "[" + naturalNumber + "]" + "." + identifier |
    //                                                  identifier + "[" + naturalNumber + "]"
    public AccessingData parseAccessingData() throws Exception {
        Lexer lexer_copy = lexer.clone();
        Symbols currentSymbols_copy = currentSymbol.clone();
        Symbols identifier = match("Identifier");

        if(identifier != null) {
            if (match("[") != null) {
                Operation indexAccessed = parseOperations(false);
                if (indexAccessed.list_factor.size() != 0) {
                    if (match("]") != null) {
                        if (match(".") != null) {
                            Symbols records = match("Identifier");
                            if (records != null) {
                                return new AccessingData(identifier, indexAccessed, records);
                            }
                        } else {
                            return new AccessingData(identifier, indexAccessed, null);
                        }
                    } else {
                        throw new Exception("missing a closing bracket ");
                    }
                }
            } else if (match(".") != null) {
                Symbols recordsAccessed = match("Identifier");
                if (recordsAccessed != null) {
                    return new AccessingData(identifier, null, recordsAccessed);
                }
            }
        }

        this.lexer = lexer_copy;
        this.currentSymbol = currentSymbols_copy;
        return null;
    }

    // InOutFunction -> (readInt | readReal | readString | writeInt| writeReal| write| writeln) + ( + parameters + ) + ;
    public InOutFunction parseInOutFunction() throws Exception {
        Lexer lexer_copy = lexer.clone();
        Symbols currentSymbols_copy = currentSymbol.clone();

        ArrayList<String> list_name_function = new ArrayList<>(Arrays.asList("readInt", "readReal", "readString",
                "writeInt", "writeReal", "write", "writeln"));

        if(match("delete") != null){
            Expressions list_parameters = parseExpression(false);
            if(list_parameters != null && Objects.equals(this.currentSymbol.getValue(), ";"))
                return new InOutFunction("delete", List.of(list_parameters));
            throw new Exception("missing something at line: "+currentSymbol.getLine());
        }
        for(String nameOfFunction: list_name_function){
            if(match(nameOfFunction) != null){
                if(match("(") != null){
                    ArrayList<Expressions> list_parameters = parseParameters();
                    if(match(")") != null)
                        return new InOutFunction(nameOfFunction, list_parameters);
                    throw new Exception("missing something at line: "+currentSymbol.getLine());
                }
                throw new Exception("missing something at line: "+currentSymbol.getLine());
            }

        }
        this.lexer = lexer_copy;
        this.currentSymbol = currentSymbols_copy;
        return null;
    }

    /*
    not(bool) bool             negates a boolean value
    chr(int) string            turns the character (an int value) into a string
    len(string or array) int   gives the length of a string or array
    floor(real) int            returns the largest integer less than or equal the real value
     */
    public BuiltInFunction parseBuiltInFunction() throws Exception {
        Lexer lexer_copy = lexer.clone();
        Symbols currentSymbols_copy = currentSymbol.clone();

        if(match("not") != null){
            if(match("(") != null) {
                Operation bool = parseOperations(false);
                if (bool != null && match(")") != null)
                    return new BuiltInFunction("not", bool);
            }
            throw new Exception("There is a character missing at line: "+currentSymbol.getLine());
        } else if (match("chr") != null){
            if(match("(") != null) {
                Operation intop = parseOperations(false);
                if (intop != null && match(")") != null)
                    return new BuiltInFunction("chr", intop);
            }
            throw new Exception("There is a character missing at line: "+currentSymbol.getLine());
        } else if (match("len") != null) {
            if (match("(") != null) {
                Operation stringop = parseOperations(false);
                if (match(")") != null && stringop != null)
                    return new BuiltInFunction("len", stringop);
            }
            throw new Exception("There is a character missing at line: " + currentSymbol.getLine());
        }else if (match("floor") != null){
            if(match("(") != null) {
                Operation realop = parseOperations(false);
                if (realop != null && match(")") != null)
                    return new BuiltInFunction("floor", realop);
                throw new Exception("There is a character missing at line: "+currentSymbol.getLine());
            }
            throw new Exception("There is a character missing at line: "+currentSymbol.getLine());
        }

        this.lexer = lexer_copy;
        this.currentSymbol = currentSymbols_copy;
        return null;
    }

    public ArrayList<ControlStructure> parseControlStructure() throws Exception {
        Lexer lexer_copy = lexer.clone();
        Symbols currentSymbols_copy = currentSymbol.clone();

        ArrayList<ControlStructure> list_structures = new ArrayList<>();
        if(match("while") != null){
            Operation condition = parseOperations(false);
            if(condition != null && match("{") != null){
                BlockStatement body = parseBlockStatement(false);
                if(body.getSizeStatements() >0 && match("}") != null){
                    list_structures.add(new ControlStructure("while", condition, body, null));
                    return list_structures;
                } else if(body.getSizeStatements() == 0){
                    throw new Exception("You have an empty body for a while loop at line: "+currentSymbol.getLine());
                }
            }
            throw new Exception("There is a character missing at line: "+currentSymbol.getLine());
        } else if (match("if") != null){
            Operation condition = parseOperations(false);
            if(condition != null){
                if(match("{") != null){
                    BlockStatement body = parseBlockStatement(false);
                    if(body != null && match("}") != null){
                        list_structures.add(new ControlStructure("if", condition, body, null));
                        if(match("else")!=null){
                            if(match("{")!= null){
                                BlockStatement body2 = parseBlockStatement(false);
                                if(body2 != null && match("}") != null) {
                                    list_structures.add(new ControlStructure("else", null, body2, null));
                                    return list_structures;
                                }
                            }
                        }
                        return list_structures;
                    }
                }
            }
            throw new Exception("There is a something wrong at line: "+currentSymbol.getLine());
        } else if (match("for") != null){
            // QUE AVEC DES INT ? et on peut avoir une operation sur l initialisation?
                Symbols identifierCondition = match("Identifier");
                if(identifierCondition != null && match("=")!= null){
                    Operation initCondition = parseOperations(false);
                    if(initCondition != null && match("to") != null){
                        Operation finalCondition = parseOperations(false);
                        if(finalCondition != null && match("by") != null){
                            Operation increment = parseOperations(false);
                            if (increment != null && match("{") != null){
                                BlockStatement body = parseBlockStatement(false);
                                if(body != null && match("}") != null){
                                    list_structures.add(new ControlStructure("for", null,
                                            body, new ArrayList<>(Arrays.asList(identifierCondition,
                                            initCondition,
                                            finalCondition,
                                            increment))));
                                    return list_structures;
                                }
                            }
                        }
                    }
                }
            throw new Exception("There is a character missing at line: "+currentSymbol.getLine());
        }
        this.lexer = lexer_copy;
        this.currentSymbol = currentSymbols_copy;
        return null;
    }

    public BlockStatement parseBlockStatement(boolean acceptReturn) throws Exception {
        ArrayList<CustomNode> statements = new ArrayList<>();
        ArrayList<Expressions> returns = new ArrayList<>();
        while(currentSymbol != null && !Objects.equals(currentSymbol.getValue(), "}")){
            if(match("Comment") == null) {
                ArrayList<ControlStructure> controlStructure = parseControlStructure();
                if (controlStructure == null) {
                    GlobalVariable variable = parseGlobalVariable();
                    if (variable == null) {
                        Assignement newassignement = parseAssignement();
                        if (newassignement == null) {
                            // peut etre appele pour un appel de fonction aussi
                            NewRecord function = parseNewRecord();
                            if (function != null && match(";") != null) {
                                statements.add(function);
                            } else if (acceptReturn && match("return") != null) {
                                Expressions returnValue = parseExpression(false);
                                if (returnValue != null && match(";") != null) returns.add(returnValue);
                            } else {
                                InOutFunction inout = parseInOutFunction();
                                if (inout != null && match(";") != null) {
                                    statements.add(inout);
                                } else if (inout != null)
                                    throw new Exception("There is a character missing at line: " + currentSymbol.getLine());
                                else
                                    break;
                            }
                        } else {
                            statements.add(newassignement);
                        }
                    } else {
                        statements.add(variable);
                    }
                } else {
                    statements.addAll(controlStructure);
                    for(ControlStructure cs: controlStructure){
                        if(cs.body.list_return.size() > 0){
                            returns.addAll(cs.body.list_return);
                        }
                    }
                }
            }
        }
        return new BlockStatement(statements, returns);
    }

    // Records -> "record" + identifier + "{" + BlockRecord + "}"
    public Records parseRecordsInit() throws Exception {
        Lexer lexer_copy = lexer.clone();
        Symbols currentSymbols_copy = currentSymbol.clone();
        Symbols s =match("record");
        if(s != null) {
            Symbols identifier = match("Identifier");
            if(identifier != null && match("{") != null) {
                ArrayList<Type> record = parseInitParameter(false);
                if (match("}") != null)
                    return new Records(identifier, record);
            }
            throw new Exception("There is a character missing at line: "+currentSymbol.getLine());
        }
        this.lexer = lexer_copy;
        this.currentSymbol = currentSymbols_copy;
        return null;
    }

    // BlockRecord -> Type + Symbols + BlockRecord | epsilon
    private ArrayList<Type> parseInitParameter(boolean forProcedure) throws Exception {
        ArrayList<Type> types = new ArrayList<>();
        Type type = parseType();
        if(type != null){
            while(!((Objects.equals(currentSymbol.getValue(), "}")&&!forProcedure) ||
                    (Objects.equals(currentSymbol.getValue(), ")")&&forProcedure))){
                if(match("Comment") == null) {

                    if (type == null)
                        throw new Exception("There is a character missing at line: " + currentSymbol.getLine());

                    types.add(type);
                    if(forProcedure){
                        if (match(",") == null && !Objects.equals(currentSymbol.getValue(), ")"))
                            throw new Exception("There is a character missing at line: " + currentSymbol.getLine());
                    } else {
                        if (match(";") == null && !Objects.equals(currentSymbol.getValue(), "}"))
                            throw new Exception("There is a character missing at line: " + currentSymbol.getLine());
                    }
                    type = parseType();
                }
            }
        }
        return types;
    }

    // NewRecord -> Identifier + "("+Parameters+")"
    // PAS D'ERREUR?
    public NewRecord parseNewRecord() throws Exception {
        Lexer lexer_copy = lexer.clone();
        Symbols currentSymbol_copy = currentSymbol.clone();

        Symbols identifier = match("Identifier");
        if(identifier != null && match("(") != null){
            ArrayList<Expressions> list_new_values = parseParameters();
            if(match(")") != null)return new NewRecord(identifier, list_new_values);
        }

        this.lexer = lexer_copy;
        this.currentSymbol = currentSymbol_copy;
        return null;
    }

    // For constant: Assignement -> Type + "=" + Expressions
    // For other uses: Assignement -> Type + "[" + "]" + "=" + Array | Type + "=" + Expressions | Type + "=" + NewRecord
    public Assignement parseAssignementInit(boolean isForConstant) throws Exception {
        Lexer lexer_copy = lexer.clone();
        Symbols currentSymbol_copy = currentSymbol.clone();

        Type type = parseType();
        if(!isForConstant){
            if(type != null) {
                if(match("=") != null) {
                    ArrayInit a = parseArrayInit();
                    if (a != null) {
                        return new Assignement(type, a, type.getIdentifier());
                    }
                    Expressions expr = parseExpression(false);
                    if (expr != null) {
                        return new Assignement(type, expr, type.getIdentifier());
                    }
                    NewRecord newrecord = parseNewRecord();
                    if (newrecord != null) {
                        return new Assignement(type, newrecord, type.getIdentifier());
                    }

                    AccessingData accessData = parseAccessingData();
                    if (accessData != null) {
                        return new Assignement(type, accessData, type.getIdentifier());
                    }
                }else if (Objects.equals(this.currentSymbol.getValue(), ";"))
                    return new Assignement(type, null, type.getIdentifier());
            }
        } else {
            if(type != null && !type.isArrayType() && match("=") != null) {
                Expressions expr = parseExpression(true);
                if (expr != null)
                    return new Assignement(type, expr, type.getIdentifier());
            }
        }
        this.lexer = lexer_copy;
        this.currentSymbol = currentSymbol_copy;
        return null;
    }

    public Assignement parseAssignement() throws Exception {
        Lexer lexer_copy = lexer.clone();
        Symbols currentSymbol_copy = currentSymbol.clone();

        AccessingData data = parseAccessingData();
        if(data != null && match("=") != null){
            Expressions expr = parseExpression(false);
            if(expr != null && match(";") != null){
                return new Assignement(null, expr, data);
            }
        }

        Symbols variable = match("Identifier");
        if(variable != null){
            if(match("=") != null){
                Expressions expr = parseExpression(false);
                if(expr != null && match(";") != null){
                    return new Assignement(null, expr, variable);
                }
            }
        }

        this.lexer = lexer_copy;
        this.currentSymbol = currentSymbol_copy;
        return null;
    }

    //Constante -> "const" + Assignement
    public Constante parseConstante() throws Exception {
        Lexer lexer_copy = lexer.clone();
        Symbols currentSymbols_copy = currentSymbol.clone();

        Symbols cons = match("const");
        if(cons != null) {
            Assignement constanteAssignement = parseAssignementInit(true);
            if (constanteAssignement != null && match(";") != null)
                return new Constante(cons, constanteAssignement);
            throw new Exception("There is a character missing at line: "+currentSymbol.getLine());
        }
        this.lexer = lexer_copy;
        this.currentSymbol = currentSymbols_copy;
        return null;
    }

    public Procedure parseProcedure() throws Exception {
        Lexer lexer_copy = lexer.clone();
        Symbols currentSymbols_copy = currentSymbol.clone();

        if(match("proc") != null){
            Symbols name = match("Identifier");
            if(name != null && match("(") != null){
                ArrayList<Type> parameters = parseInitParameter(true);
                if(match(")") != null){
                    Symbols returnType = match("Identifier");
                    boolean arraytype = false;
                    if(match("[") != null) {
                        if (match("]") == null)
                            throw new Exception("Missing something at line: " + currentSymbol.getLine());
                        else
                            arraytype = true;
                    }
                    if(returnType != null && match("{") != null){
                        BlockStatement blockStatement = parseBlockStatement(true);
                        if(match("}") != null){
                            return new Procedure(name, parameters, new Type(name, returnType, arraytype), blockStatement, arraytype);
                        }
                    }
                }
            }
            throw new Exception("Missing something at line: "+currentSymbol.getLine());
        }

        this.lexer = lexer_copy;
        this.currentSymbol = currentSymbols_copy;
        return null;
    }

    //GlobalVariable -> "val" + Assignement | "var" + Assignement
    public GlobalVariable parseGlobalVariable() throws Exception {
        Lexer lexer_copy = lexer.clone();
        Symbols currentSymbols_copy = currentSymbol.clone();
        if(Objects.equals(currentSymbol.getValue(), "val") | Objects.equals(currentSymbol.getValue(), "var")) {
            match(currentSymbol.getToken());
            Assignement a = parseAssignementInit(false);
            if(a != null && match(";") != null)
                return new GlobalVariable(currentSymbols_copy.clone(), a);
            throw new Exception("There is a character missing at line: "+currentSymbol.getLine());
        }
        this.lexer = lexer_copy;
        this.currentSymbol = currentSymbols_copy;
        return null;
    }

    // Starting point -> Constante* + (GlobalVariable* | Records*)
    public StartingPoint parseStartingPoint() throws Exception {
        ArrayList<Constante> c = new ArrayList<>();
        ArrayList<Records> record_list = new ArrayList<>();
        ArrayList<Procedure> procedures_list = new ArrayList<>();
        ArrayList<BlockStatement> blockStatement_list = new ArrayList<>();

        if(match("Comment") != null){
            while(true){
                if(match("Comment") == null)break;
            }
        }
        if(this.currentSymbol == null)
            return new StartingPoint(c, record_list, procedures_list, blockStatement_list);
        // Starting by Constant definitions
        Constante constante = parseConstante();
        if(currentSymbol != null){
            while (constante != null) {
                if(match("Comment") == null){
                    while(true){
                        if(match("Comment") == null)break;
                    }
                }
                c.add(constante);
                if(currentSymbol == null)break;
                constante = parseConstante();
            }

        } else if(constante != null) c.add(constante);

        while(currentSymbol != null) {
            if(match("Comment") == null) {
                Records record = parseRecordsInit();
                if (record == null) {
                    Procedure procedure = parseProcedure();
                    if (procedure == null) {
                        BlockStatement blockStatement = parseBlockStatement(false);
                        if (blockStatement.list_statements.size() ==0) {
                            throw new Exception("There is an error at line: " + currentSymbol.getLine());
                        } else if (blockStatement.list_return.size() !=0) {
                            throw new Exception("There is an error at line: " + currentSymbol.getLine()+". no return accepted outside of a procedure.");
                        } else {
                            blockStatement_list.add(blockStatement);
                        }
                    } else {
                        procedures_list.add(procedure);
                    }
                } else {
                    record_list.add(record);
                }
            }
        }
        return new StartingPoint(c, record_list, procedures_list, blockStatement_list);
    }

    public String getAST() throws Exception {
        CustomNode currentNode = parseStartingPoint();
        if(currentNode==null) return null;
        while(currentNode.getChildren().size() == 1){
            currentNode = currentNode.getChildren().get(0);
        }
        if(this.currentSymbol == null){
            System.out.println("THIS ENDED RIGHT");
        }
        System.out.println(currentNode.getChildren());
        return currentNode.getToken();
    }

    public static void main(String[] args) throws Exception {
        String input ="""
                var value int = readInt();
                """;
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);

        System.out.println(parser.getAST());
    }
}

