/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import analyzers.lexical.token;
import java.util.List;

/**
 *
 * @author m-cam
 */
public class DerivationBuilder {

    private final List<token> tokens; // List of tokens produced by the lexer
    private int position; // Current token index being processed

    // Constructor initializes token list and position
    public DerivationBuilder(List<token> tokens) {
        this.tokens = tokens;
        this.position = 0;
    }

    // Builds the root of the derivation tree and parses all instructions
    public TreeNode parse() {
        TreeNode root = new TreeNode("<program>");
        root.addChild(new TreeNode(advance().value)); // Add START token

        // Parse all instructions until END token is found
        while (!peek().value.equals("END")) {
            root.addChild(instruction());
        }

        root.addChild(new TreeNode(advance().value)); // Add END token
        return root;
    }

    // Builds a subtree for each supported instruction type (let, print, if, while)
    private TreeNode instruction() {
        TreeNode node = new TreeNode("<instruction>");
        String keyword = peek().value;
        System.out.println("MK" + advance().type);

        if (keyword.equals("let")) {
            node.addChild(new TreeNode(advance().value)); // let keyword
            node.addChild(identifier()); // IDENTIFIER node
            node.addChild(operator()); // Operator (=)
            node.addChild(expression()); // Expression node

        } else if (keyword.equals("print")) {
            node.addChild(new TreeNode(advance().value)); // print keyword
            node.addChild(identifier()); // IDENTIFIER node

        } else if (keyword.equals("if") || keyword.equals("while")) {
            node.addChild(new TreeNode(advance().value)); // if or while
            node.addChild(new TreeNode(advance().value)); // (
            node.addChild(expression()); // Conditional expression
            node.addChild(new TreeNode(advance().value)); // )
            node.addChild(new TreeNode(advance().value)); // {
            node.addChild(instruction()); // Nested instruction
            node.addChild(new TreeNode(advance().value)); // }
        }

        return node;
    }

    // Recursively parses expressions composed of identifiers, numbers, and operators
    private TreeNode expression() {
        TreeNode node = new TreeNode("<expression>");
        String current = peek().type;

        if (current.equals("IDENTIFIER")) {
            node.addChild(identifier());
        } else if (current.equals("NUMBER")) {
            node.addChild(number());
        } else {
            if (!isAtEnd()) {
                node.addChild(operator()); // OPERATOR
                node.addChild(expression()); // Right side expression
            }
        }

        return node;
    }

    // Creates a <identifier> node with its corresponding value
    private TreeNode identifier() {
        TreeNode node = new TreeNode("<identifier>");
        node.addChild(new TreeNode(advance().value));
        return node;
    }

    // Creates a <number> node with its value
    private TreeNode number() {
        TreeNode node = new TreeNode("<number>");
        node.addChild(new TreeNode(advance().value));
        return node;
    }

    // Creates a <operator> node with its value (e.g., =, +, <)
    private TreeNode operator() {
        TreeNode node = new TreeNode("<operator>");
        node.addChild(new TreeNode(advance().value));
        return node;
    }

    // Advances and returns the next token
    private token advance() {
        return tokens.get(position++);
    }

    // Returns the current token without advancing
    private token peek() {
        return tokens.get(position);
    }

    // Checks if the end of the token list has been reached
    private boolean isAtEnd() {
        return position >= tokens.size();
    }

}

