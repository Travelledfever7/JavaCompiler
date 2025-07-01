/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analyzers.lexical;

import java.util.regex.Pattern;

/**
 *
 * @author lebus
 */
public class TokenDefinition {
    public String type;
    public Pattern pattern;
    
    public TokenDefinition(String type, String regex){
        this.type = type;
        this.pattern = Pattern.compile(regex);
    }
}
