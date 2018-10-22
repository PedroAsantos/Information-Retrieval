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
    
    public static List<String> tokenize2(String documentLine){
       
       // System.out.print("upper->");
       // System.out.println(documentLine);
        
        //documentLine = documentLine.toLowerCase();
        //documentLine.map(x->x.split("\\s+"));
       // stringToStream(documentLine).filter(x->x.length()>3).forEach(r);.forEach(System.out::println);
        //System.out.println(stringToStream("-->"+documentLine.toLowerCase()));
        
        List<String> tokensLine = new ArrayList<String>(Arrays.asList(stringReplace(documentLine.toLowerCase()).split(" ")));
       
        return tokensLine.stream().filter(x -> x.length()>3).collect(Collectors.toList());
            
        
        //return tokensLine;
    }
    
    public static List<String> tokenize(String documentLine){
        
         //documentLine.toLowerCase().replaceAll("[^a-zA-Z0-9]", " ").replaceAll("\\s+"," ");
         //Lower case all the letters, remove non alphabetics characters, 
         List<String> tokensLine = new ArrayList<String>(Arrays.asList(documentLine.toLowerCase().replaceAll("[^A-Za-z]", " ").replaceAll("\\s+"," ").split(" ")));
         
         return tokensLine.stream().filter(x -> x.length()>=3).collect(Collectors.toList());
    }
    
    private static String stringReplace(String stringConverting) {
        Pattern replace = Pattern.compile("[^a-zA-Z0-9]");
        Pattern replaceS = Pattern.compile("\\s+");
        
        Matcher regexMatcher = replace.matcher(stringConverting.trim());
        
        return replaceS.matcher(regexMatcher.replaceAll(" ")).replaceAll(" ");
         
         
         
//   return Pattern.compile("\\s+").splitAsStream(Pattern.compile("[+\\-!(){}\\[\\]^~:\\\\]|/\\*|\\*/|&&|\\|\\|").matcher(stringConverting).replaceAll(""));//.splitAsStream(stringConverting);
//        return Pattern.compile("\\s+").splitAsStream(stringConverting).filter(s -> !toRemove.contains(s));//.splitAsStream(stringConverting);

    }
    
}
