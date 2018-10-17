/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

/**
 *
 * @author beatriz
 */

public class ImprovedTokenizer {
    
    public ImprovedTokenizer(){
    }
    
    /*
    public static String stemming(String documentLine){
        SnowballStemmer stemmer = new englishStemmer();
        stemmer.setCurrent(documentLine); //set string you need to stem
        stemmer.stem();  //stem the word
        String result = stemmer.getCurrent();//get the stemmed word
        return result;
    }
    */
    
    /*
    public static List<String> stopwordFilter(String documentLine) throws FileNotFoundException{
        Scanner scanner = new Scanner(new FileReader("stop.txt"));
        List<String> stopwords = new ArrayList<String>();   
        
        
        while(scanner.hasNextLine()){
            String word = scanner.nextLine();
            stopwords.add(word);
        }

        String[] words = stopwords.toArray(new String[0]);
        
        List<String> wordList = new ArrayList<>(Arrays.asList(words));
        List<String> list = new ArrayList<>(Arrays.asList(documentLine));
        
        String[] filtered = list.stream().map(statement -> Arrays.asList(statement.split(" ")))
                .map(listOfWords -> listOfWords.stream().filter(word -> !wordList.contains(word)).collect(Collectors.joining(" ")))
                    .toArray(String[]::new);
        
        String filteredList = String.join(" ", filtered);
        List<String> result = Arrays.asList(filteredList.split(" "));
        
        return result;
    }
    
  
    
    public static List<String> specialCharacters(String documentLine){
        //Apostrophes, periods and @ ignored, "-" replaced by space
        List<String> inicial = new ArrayList<String>(Arrays.asList(documentLine.toLowerCase().replaceAll("'", "")
            .replaceAll("\\.", "").replaceAll("@", "").replaceAll("-", " ").replaceAll("\\s+"," ").split(" ")));
        
        return inicial;
    }
    
    */
    public static List<String> personalizedTokenize(String documentLine) throws FileNotFoundException{
       
       //stop words
       Scanner scanner = new Scanner(new FileReader("stop.txt"));
        List<String> stopwords = new ArrayList<String>();   
        
        
        while(scanner.hasNextLine()){
            String word = scanner.nextLine();
            stopwords.add(word);
        }

        String[] words = stopwords.toArray(new String[0]);
        List<String> wordList = new ArrayList<>(Arrays.asList(words));
         List<String> list = new ArrayList<>(Arrays.asList(documentLine));
        String[] filtered = list.stream().map(statement -> Arrays.asList(statement.split(" ")))
                .map(listOfWords -> listOfWords.stream().filter(word -> !wordList.contains(word)).collect(Collectors.joining(" ")))
                    .toArray(String[]::new);
        
        String filteredList = String.join(" ", filtered);
        
        //deal with special characters
        List<String> result = Arrays.asList(filteredList.replaceAll("'", "").replaceAll("\\.", "").replaceAll("@", "")
                .replaceAll("-", " ").replaceAll("\\s+"," ").split(" "));
        
        return result;
        
    }
    
}
