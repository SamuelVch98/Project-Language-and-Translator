package compiler.Lexer;

import compiler.Lexer.Symbols.*;

import java.lang.reflect.Constructor;
import java.util.*;

public class Symbol {
    List<Symbols> symbol_list;
    String value;
    Symbols symbol;
    int line_nbr;

    public Symbol(String s, int line_nbr) {
        // Initializing the variables of the symbol class
        this.value = s;
        this.line_nbr = line_nbr;
        this.symbol_list = Arrays.asList(new WS(), new NaturalNumber(), new RealNumber(), new Strings(),
                new AssignementOperator(), new Booleans(),new LParenthesis(), new RParenthesis(),
                new ComparingOperator(), new LBoxBrackets(), new RBoxBrackets(), new LBraceBrackets(),
                new RBraceBrackets(), new ArithmeticOperator(), new SpecialCharacter(), new Comment(),
                new Keyword(), new Identifier());

        // TODO: Check if we are accurate with the file they gave us as an example of the language, see if we take
        //  everything into account and if we do not overexpand + throw error when a symbol is not recognised
        for (Symbols key: symbol_list){
            if(key.matches(s)){
                this.symbol = key;
                this.symbol.setValue(s);
                this.symbol.setLineNbr(this.line_nbr);
            }
        }
    }

    private Symbol(String value, Symbols symbol, List<Symbols> symbol_list, int line_nbr){
        this.value = value;
        this.symbol = symbol;
        this.symbol_list = symbol_list;
        this.line_nbr = line_nbr;
    }

    public boolean check_validity(String futur_string){
        if(this.symbol != null){
            for (Symbols key: symbol_list){
                if(key.matches(futur_string)){
                    this.value = futur_string;
                    symbol = key;
                    this.symbol.setValue(futur_string);
                    this.symbol.setLineNbr(this.line_nbr);
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public void setLineNumber(int nbr){
        this.line_nbr = nbr;
        this.symbol.setLineNbr(nbr);
    }

    public void reset(String s){
        this.value = s;
        symbol = null;
        for (Symbols key: symbol_list){
            if(key.matches(s)){
                symbol = key;
                this.symbol.setValue(s);
                this.symbol.setLineNbr(this.line_nbr);
            }
        }
    }

    public Symbol clone(){
        return new Symbol(this.value, this.symbol.clone(), this.symbol_list, this.line_nbr);
    }

    public Symbols get_symbol(){
        return this.symbol;
    }

    public String get_value(){
        return this.value;
    }

    public String toString(){
        return "["+this.symbol.getToken()+": "+this.value+"]";
    }
}