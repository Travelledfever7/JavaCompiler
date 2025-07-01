/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analyzers.lexical;

/**
 *
 * @author lebus
 */
public class token {
    public String type;
    public String value;
    
    public token(String type, String value){
        this.type = type;
        this.value = value;
    }
    
    @Override
    public String toString(){
        return "Token( " + type + ", \"" + value + "\")";
    }
}
