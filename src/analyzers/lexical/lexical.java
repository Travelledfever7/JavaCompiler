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
    new TokenDefinition("KEYWORD", "\\b(if|else|while|print|let|START|END)\\b"),
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
                //Object that try to find coincidences of the regular 
                //expression in the input
                Matcher matcher = def.pattern.matcher(input);
                //Limits the search from the actual position to 
                //dont analyze twice or more the input
                matcher.region(position, input.length());
                //Verify that the pattern matches in the start
                //of the defined region
                if(matcher.lookingAt()){
                    //Catch de text that matches with the regular expresion
                    String value = matcher.group();
                    //To ignore de whitespaces in the input 
                    if(!def.type.equals("WHITESPACE")){
                        //Throws an error of unknown character 
                        if(def.type.equals("UNKNOWN")){
                            throw new RuntimeException("Unexpectet character: '" + value + "'");
                        }
                        //Create a new token and add to the list
                        tokens.add(new token(def.type, value));
                    }
                    //Update the actual position to the final of the text
                    position = matcher.end();
                    //Puts in true to note that is a valid token
                    matchFound = true;
                    break;
                }
            }
            //Not found valid tokens
            if(!matchFound) {
                throw new RuntimeException("No valid token found at position" + position);
            }
        }
        
        return tokens;
    }
}
