/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author rute
 */
public class SimpleTokenizer {
    
    public SimpleTokenizer(){
        
    }
 
    public static List<String> tokenize(String documentLine){
        
         //Lower case all the letters, remove non alphabetics characters, 
        // List<String> tokensLine = new ArrayList<String>(Arrays.asList(documentLine.toLowerCase().replaceAll("[^A-Za-z]", " ").replaceAll("\\s+"," ").split(" ")));
         List<String> tokensLine = new ArrayList<String>(Arrays.asList(documentLine.toLowerCase().replaceAll("\\s+"," ").split(" ")));
         
         return tokensLine.stream().map(x -> x.replaceAll("[^A-Za-z]", "")).filter(x -> x.length()>=3).collect(Collectors.toList());
    }
       
}
