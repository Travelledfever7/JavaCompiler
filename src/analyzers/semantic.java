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

    public List<token> tokens;
    public int position = 0;
    public Map<String, String> symbolTable = new HashMap<>();

    public semantic(List<token> tokens) {
        this.tokens = tokens;
    }

    public void analyze() {
        if (!match("KEYWORD", "START")) {
            error("Program must start with 'START'");
        }

        while (!isAtEnd() && !check("KEYWORD", "END")) {
            instruction();
        }

        if (!match("KEYWORD", "END")) {
            error("Program must end with 'END'");
        }

        System.out.println("Semantic analysis completed successfully.");
    }

    private void instruction() {
        token token = peek();

        if (token.type.equals("KEYWORD")) {
            switch (token.value) {
                case "let":
                    advance(); // consume 'let'
                    token identifier = expect("IDENTIFIER");
                    expect("OPERATOR", "=");
                    expression();

                    // Declaración: añadir a tabla de símbolos
                    symbolTable.put(identifier.value, "int");
                    break;

                case "print":
                    advance(); // consume 'print'
                    token var = expect("IDENTIFIER");
                    if (!symbolTable.containsKey(var.value)) {
                        error("Variable '" + var.value + "' not declared");
                    }
                    break;

                case "if":
                case "while":
                    advance(); // consume if/while
                    expect("LPAREN");
                    expression();
                    expect("RPAREN");
                    expect("LBRACE");
                    instruction(); // por ahora solo 1 instrucción dentro del bloque
                    expect("RBRACE");
                    break;

                default:
                    error("Unknown keyword: " + token.value);
            }
        } else {
            error("Instruction expected");
        }
    }

    private void expression() {
        // Validar que los operandos están declarados si son variables
        if (match("IDENTIFIER")) {
            token id = previous();
            if (!symbolTable.containsKey(id.value)) {
                error("Variable '" + id.value + "' not declared");
            }
        } else if (!match("NUMBER")) {
            error("Expected identifier or number");
        }

        if (match("OPERATOR")) {
            expression(); // soporta expresiones como x + 2
        }
    }

    private boolean match(String type) {
        if (check(type)) {
            advance();
            return true;
        }
        return false;
    }

    private boolean match(String type, String value) {
        if (check(type, value)) {
            advance();
            return true;
        }
        return false;
    }

    private boolean check(String type) {
        return !isAtEnd() && peek().type.equals(type);
    }

    private boolean check(String type, String value) {
        return !isAtEnd()
                && peek().type.equals(type)
                && peek().value.equals(value);
    }

    private token expect(String expectedType) {
        if (!check(expectedType)) {
            error("Expected " + expectedType + ", found: " + (isAtEnd() ? "EOF" : peek()));
        }
        return advance();
    }

    private token expect(String expectedType, String expectedValue) {
        if (!check(expectedType, expectedValue)) {
            error("Expected " + expectedValue + ", found: " + (isAtEnd() ? "EOF" : peek()));
        }
        return advance();
    }

    private token advance() {
        if (!isAtEnd()) {
            return tokens.get(position++);
        }
        return null;
    }

    private token peek() {
        return tokens.get(position);
    }

    private token previous() {
        return tokens.get(position - 1);
    }

    private boolean isAtEnd() {
        return position >= tokens.size();
    }

    private void error(String message) {
        throw new RuntimeException("Semantic error: " + message);
    }

    public Map<String, String> getSymbolTable() {
        return symbolTable;
    }
}
