/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analyzers;

import analyzers.lexical.*;
import java.util.List;

/**
 *
 * @author lebus
 */

/*
<S>             -> "START" | <Instruction> | "END"
<Instructions>  ->  <Instruction> <Instructions> | lambda
<Instruction>   -> "let" <Identifier> "=" <Expression> | "print" <Identifier> | "if" "(" <Condition> ")" <Block> | "while" "(" <Condition> ")" <Block>
<Block>         -> "{" <Instructions> "}"
<Expression>    -> <Identifier> <Operator> <Expression> | <Identifier> | <Numeber>
<Condition>     -> <Expression>
<Identifier>    -> [a-zA-Z][a-zA-Z0-9]*
<Numeber>       -> [0-9]+
<Operator>      -> "+" | "-" | "*" | "/" | "=" | "<" | ">"
 */
public class sintactic {

    public List<token> tokens;
    public int position;

    public sintactic(List<token> tokens) {
        this.tokens = tokens;
        this.position = 0;
    }

    public void parse() {
        //Matcher that valid if thte first token is START
        match("KEYWORD", "START");
        
        //Iterate with the method instruction to find the token end
        while (!isAtEnd() && !check("KEYWORD", "END")) {
            instruction();            
        }
        //New validation to confirm that arent more tokens
        match("KEYWORD", "END");       // nueva validación de cierre
        //Throws an error if are more tokens after the end
        if (!isAtEnd()) {
            error("No tokens expected after 'END'");
        }
        //If all the parsing are good print succesfully
        System.out.println("Parsing successful.");
    }

    public void instruction() {
        //Receive the actual token in the list tokens
        token token = peek();
        
        switch (token.type) {
            //Search to find the keywords and it complements
            case "KEYWORD":
                switch (token.value) {
                    //The first case is with let, to determinate variables
                    //and end with an expression
                    case "let":
                        match("KEYWORD", "let");
                        match("IDENTIFIER");
                        match("OPERATOR", "=");
                        expression();
                        break;

                    case "if":
                        match("KEYWORD", "if");
                        match("LPAREN");
                        expression();
                        match("RPAREN");
                        match("LBRACE");
                        instruction();
                        match("RBRACE");
                        break;

                    case "while":
                        match("KEYWORD", "while");
                        match("LPAREN");
                        expression();
                        match("RPAREN");
                        match("LBRACE");
                        instruction();
                        match("RBRACE");
                        break;

                    case "print":
                        match("KEYWORD", "print");
                        match("IDENTIFIER");
                        break;

                    default:
                        error("unexpected keyword '" + token.value + "'");
                }
                break;

            default:
                error("instruction expected");
        }
    }
    //This metod analyses expressions
    public void expression() {
        //Try to find and identifier or a number
        if (matchIf("IDENTIFIER") || matchIf("NUMBER")) {
            if (matchIf("OPERATOR")) {
                //If finds an operator makes a callback to do the process again
                expression();   
            }
        } else {
            error("expression expected");
        }
    }
    //Function that return the actual token in the list
    public token peek() {
        return tokens.get(position);
    }
    //Function that verify if there are no more tokens in the list
    public boolean isAtEnd() {
        return position >= tokens.size();
    }
    //Function that valid if the actual token matches exactly with a type and a value
    //for START and END
    public boolean check(String type, String value) {
        return !isAtEnd()
                && peek().type.equals(type)
                && peek().value.equals(value);
    }
    //Function that advance a token if the type is match
    public void match(String expectedType) {
        if (isAtEnd() || !peek().type.equals(expectedType)) {
            error(expectedType);
        }
        position++;
    }
    //Extended Function of match that also verify the value of the token
    public void match(String expectedType, String expectedValue) {
        if (isAtEnd()) {
            error(expectedType + " with value '" + expectedValue + "'");
        }
        token token = peek();
        if (!token.type.equals(expectedType) || !token.value.equals(expectedValue)) {
            error(expectedType + " with value '" + expectedValue + "'");
        }
        position++;
    }
    //Advance if the type matches and return a boolean, is used to optional expressions
    public boolean matchIf(String expectedType) {
        if (!isAtEnd() && peek().type.equals(expectedType)) {
            position++;
            return true;
        }
        return false;
    }
    //Print a error message if finds a unexpected token
    public void error(String message) {
        throw new RuntimeException("Syntax error at token " + (isAtEnd() ? "EOF" : peek()) + ": expected " + message);
    }

}
