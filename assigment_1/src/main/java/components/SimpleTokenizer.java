/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rute
 */
public class SimpleTokenizer {
    
    public SimpleTokenizer(){
        
    }
    
    public static List<String> tokenize(String documentLine){
        List<String> tokensLine = new ArrayList<>();       
        System.out.print("upper->");
        System.out.println(documentLine);
        
        documentLine = documentLine.toLowerCase();
        
        System.out.print("lower->");
        System.out.println(documentLine);
        
        
        
        
        return tokensLine;
    }
    
    
    
}
