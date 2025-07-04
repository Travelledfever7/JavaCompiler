/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analyzers;

import java.util.List;
import analyzers.lexical.*;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author lebus
 */
public class semantic {
    //Variables for the mangament of the analyzer
    public List<token> tokens;
    public int position = 0;
    //This map es a table of symbols where is saved the declared identifiers with their type
    public Map<String, String> symbolTable = new HashMap<>();
    
    public semantic(List<token> tokens) {
        this.tokens = tokens;
    }
    //Method that run the full analyzer
    public void analyze() {
        //A first validation of the start of the program
        if (!match("KEYWORD", "START")) {
            error("Program must start with 'START'");
        }
        //While the actual token doesnt be END, continue with instruction
        while (!isAtEnd() && !check("KEYWORD", "END")) {
            instruction();
        }
        //Final validation of the end of the program 
        if (!match("KEYWORD", "END")) {
            error("Program must end with 'END'");
        }

        System.out.println("Semantic analysis completed successfully.");
    }
    //Methos that analyze only one instruction
    private void instruction() {
        //Get the actual token
        token token = peek();
        //If the token is a keyword, then begin to compare the value
        if (token.type.equals("KEYWORD")) {
            switch (token.value) {
                case "let":
                    //Advance in one position the tokenlist to pass the keyword 'let' 
                    advance();
                    token identifier = expect("IDENTIFIER");
                    expect("OPERATOR", "=");
                    expression();

                    //Add the identifier value like an int to the symbol table
                    symbolTable.put(identifier.value, "int");
                    break;
                    
                case "print":
                    advance();
                    token var = expect("IDENTIFIER");
                    //Verify that the variable has been declarated perviously
                    if (!symbolTable.containsKey(var.value)) {
                        error("Variable '" + var.value + "' not declared");
                    }
                    break;

                case "if":
                case "while":
                    advance();
                    //Verify that the structure of the code is well written
                    expect("LPAREN");
                    expression();
                    expect("RPAREN");
                    expect("LBRACE");
                    //Only one instruction in the block
                    instruction();
                    expect("RBRACE");
                    break;

                default:
                    error("Unknown keyword: " + token.value);
            }
        } else {
            error("Instruction expected");
        }
        //If there isn't a valid keyword throws an error
    }
    //Method that analyzes full expressions
    private void expression() {
        // Validar que los operandos están declarados si son variables
        if (match("IDENTIFIER")) {
            //If the expression is a indetifier, verifies that already is declarated 
            token id = previous();
            if (!symbolTable.containsKey(id.value)) {
                error("Variable '" + id.value + "' not declared");
            }
            //If not be an identifier, try to catch a number, if neither, throws an error
        } else if (!match("NUMBER")) {
            error("Expected identifier or number");
        }
        //After all this process, verify if after the token is an operator 
        //to do a recursive expression()
        if (match("OPERATOR")) {
            expression();
        }
    }
    //Try to advance if the token is a expected type
    private boolean match(String type) {
        if (check(type)) {
            advance();
            return true;
        }
        return false;
    }
    //Another version of match to compare the value of the token
    private boolean match(String type, String value) {
        if (check(type, value)) {
            advance();
            return true;
        }
        return false;
    }
    //Method that verifies that the type of the actul token of the list is equal to the type 
    //passed in the argument
    private boolean check(String type) {
        return !isAtEnd() && peek().type.equals(type);
    }
    //Other check that verifies if the type and value are equal to the arguments
    private boolean check(String type, String value) {
        return !isAtEnd()
                && peek().type.equals(type)
                && peek().value.equals(value);
    }
    //Method that verifies if the type of the token is expected
    private token expect(String expectedType) {
        if (!check(expectedType)) {
            error("Expected " + expectedType + ", found: " + (isAtEnd() ? "EOF" : peek()));
        }
        return advance();
    }
    //Another method expect that adds the value
    private token expect(String expectedType, String expectedValue) {
        if (!check(expectedType, expectedValue)) {
            error("Expected " + expectedValue + ", found: " + (isAtEnd() ? "EOF" : peek()));
        }
        return advance();
    }
    //Advance in the list of tokens if the token isn't an 'END'
    private token advance() {
        if (!isAtEnd()) {
            return tokens.get(position++);
        }
        return null;
    }
    //Return the actual token in the list
    private token peek() {
        return tokens.get(position);
    }
    //Return the previous token in the list
    private token previous() {
        return tokens.get(position - 1);
    }
    //Verify if the position is in the final position of the list 
    private boolean isAtEnd() {
        return position >= tokens.size();
    }
    //Throws an error in case of semantic error
    private void error(String message) {
        throw new RuntimeException("Semantic error: " + message);
    }
}
