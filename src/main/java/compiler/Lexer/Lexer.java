package compiler.Lexer;

import compiler.Lexer.Symbols.ArithmeticOperator;
import compiler.Lexer.Symbols.Symbols;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lexer {
    List<Symbols> list_symbols = new ArrayList<>();
    int index = 0;
    boolean end = false;

    public Lexer(Reader input) {
        try {
            int theCharNum = input.read();
            StringBuilder s = new StringBuilder();
            Symbol symbol = new Symbol(s.toString(), 1);
            int line_number = 1;
            while (theCharNum != -1) {
                char theChar = (char) theCharNum;

                if(symbol.check_validity(s.toString()+theChar)){
                    s.append(theChar);
                } else{
                    if (Objects.equals(symbol.symbol.getToken(), "Strings")
                            && (!(symbol.get_value().charAt(0) == '\"')
                            || !(symbol.get_value().charAt(symbol.get_value().length()-1) == '\"'))){
                        throw new Exception("wrong format for a String");
                    } else if(Objects.equals(symbol.symbol.getToken(), "Comment") && ((s.length()>1
                            && (!Character.toString(s.charAt(0)).equals("/")
                            || !Character.toString(s.charAt(1)).equals("/"))) || s.length() ==1)){
                        symbol.symbol = new ArithmeticOperator();
                        symbol.symbol.setValue("/");
                        list_symbols.add(symbol.clone().get_symbol());
                    } else {
                        list_symbols.add(symbol.clone().get_symbol());
                    }
                    s = new StringBuilder("" + theChar);
                    symbol.reset(s.toString());
                    if(symbol.get_symbol() == null){
                        throw new Exception("unknow character at line: "+line_number);
                    }
                }
                if(theChar == '\n'){
                    line_number += 1;
                    symbol.setLineNumber(line_number);
                }
                theCharNum = input.read();
            }
            if(symbol.check_validity(s.toString())){
                if (Objects.equals(symbol.symbol.getToken(), "Strings")
                        && (!(symbol.get_value().charAt(0) == '\"')
                        || !(symbol.get_value().charAt(symbol.get_value().length()-1) == '\"'))){
                    throw new Exception("wrong format for a String");
                } else if (Objects.equals(symbol.symbol.getToken(), "Comment") && s.length()>1
                        && (!(symbol.get_value().charAt(0) == '/')
                        || !(symbol.get_value().charAt(1) == '/'))){
                    throw new Exception("wrong format");
                } else {
                    list_symbols.add(symbol.clone().get_symbol());
                }
            } else {
                throw new Exception();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private Lexer(int index, boolean end, List<Symbols> symbols){
        this.index = index;
        this.end = end;
        this.list_symbols = symbols;
    }

    public Symbols getNextSymbol() {
        if(!end) {
            if (index == this.list_symbols.size() - 1) {
                end = true;
            }
            index = index +1;
            if (Objects.equals(list_symbols.get(index - 1).getToken(), "WS")){
                return getNextSymbol();
            }
            return list_symbols.get(index-1);
        }
        return null;
    }

    public Lexer clone(){
        return new Lexer(this.index, this.end, this.list_symbols);
    }

    public static void main(String[] args) {
        String input = "var value int = x - 0;// this is a comment\n";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbols s = lexer.getNextSymbol();
        while (s != null){
            System.out.println(s);
            System.out.println("line: "+s.getLine());
            s = lexer.getNextSymbol();
        }
    }
}