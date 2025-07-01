/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analyzers.lexical;

import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;

/**
 *
 * @author lebus
 */
public class lexical {

    public ArrayList<TokenDefinition> tokenDefinitions = new ArrayList<>(Arrays.asList(
    new TokenDefinition("KEYWORD", "\\b(if|else|while|print|let)\\b"),
    new TokenDefinition("NUMBER", "\\b\\d+\\b"),
    new TokenDefinition("IDENTIFIER", "\\b[a-zA-Z][a-zA-Z0-9]*\\b"),
    new TokenDefinition("OPERATOR", "[+\\-*/=<>]"),
    new TokenDefinition("LBRACE", "\\{"),
    new TokenDefinition("RBRACE", "\\}"),
    new TokenDefinition("LPAREN", "\\("),
    new TokenDefinition("RPAREN", "\\)"),
    new TokenDefinition("WHITESPACE", "[ \\t\\r\\n]+"),
    new TokenDefinition("UNKNOWN", ".")
    ));
    public List<token> tokenize(String input){
        List<token> tokens = new ArrayList<>();
        int position = 0;
        
        while (position < input.length()){
            boolean matchFound = false;
            
            for(TokenDefinition def : tokenDefinitions){
                Matcher matcher = def.pattern.matcher(input);
                matcher.region(position, input.length());
                
                if(matcher.lookingAt()){
                    String value = matcher.group();
                    
                    if(!def.type.equals("WHITESPACE")){
                        if(def.type.equals("UNKOWN")){
                            throw new RuntimeException("Unexpectet character: '" + value + "'");
                        }
                        tokens.add(new token(def.type, value));
                    }
                    position = matcher.end();
                    matchFound = true;
                    break;
                }
            }
            if(!matchFound) {
                throw new RuntimeException("No valid token found at position" + position);
            }
        }
        
        return tokens;
    }
}
