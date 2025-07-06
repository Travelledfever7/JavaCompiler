/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compiler;

import analyzers.lexical.lexical;
import analyzers.lexical.token;
import analyzers.semantic;
import analyzers.syntactic;
import java.util.List;
import view.DerivationBuilder;
import view.TreeNode;

/**
 *
 * @author lebus
 */
public class compiler {

    // Main method that orchestrates the compilation process and returns a derivation tree
    public TreeNode compile(String input) {
        // Instantiate the lexical analyzer to tokenize the input string
        lexical lexical = new lexical();

        // Convert the raw input code into a list of tokens using regular expressions
        List<token> tokens = lexical.tokenize(input);

        // Perform syntactic analysis to ensure the token sequence follows grammar rules
        new syntactic(tokens).parse();

        // Perform semantic analysis to check for logical errors (e.g., undeclared variables)
        new semantic(tokens).analyze();

        // Build the derivation tree from the validated token sequence
        DerivationBuilder builder = new DerivationBuilder(tokens);
        
        // Return the root node of the constructed derivation tree
        return builder.parse();
    }
}
