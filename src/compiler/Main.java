/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package compiler;
import analyzers.*;
import analyzers.lexical.*;
import java.util.List;
/**
 *
 * @author lebus
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        lexical lex = new lexical();
        String code = "START "
                + "let x = 10 "
                + "if (x > 5) { "
                + "print x "
                + "}"
                + "END";
        List<token> tokens = lex.tokenize(code);
        for (token tok: tokens){
            System.out.println(tok);
        }
        new sintactic(tokens).parse();
    }
    
}
