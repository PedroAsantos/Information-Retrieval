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
    private static final String[] arrayStopWords = {"qwe","qweqw","qweas"};
    private static final List<String> stopwords = new ArrayList<String>(Arrays.asList(arrayStopWords));
    
    public ImprovedTokenizer(){
    }
    
    /*
    public static List<String> stemming(String documentLine){
        SnowballStemmer stemmer = new englishStemmer();
        stemmer.setCurrent(documentLine);         
        
        String[] temp = documentLine.split("\\s");
        List<String> text = Arrays.asList(temp);
        
        List<String> stemmedList = new ArrayList<>();
        
        text.stream().forEach(s -> {
            stemmer.setCurrent(s);
            stemmer.stem();
            stemmedList.add(stemmer.getCurrent());
        });
        
        return stemmedList;
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
        List<String> result = Arrays.asList(filteredList.replaceAll("[^a-zA-Z 0-9]", "").replaceAll("\\s+"," ").split(" "));
        
        return result;
    }
    
    */
    public static List<String> personalizedTokenize(String documentLine) throws FileNotFoundException{
        //stop words
       
      
        String[] temp = documentLine.replaceAll("\\s+"," ").split(" ");
        List<String> text = Arrays.asList(temp);
        
        
        System.out.println(stopwords);
        List<String> filtered = text.stream()
                .filter(word -> !stopwords.contains(word)).collect(Collectors.toList());
        //stemming
        SnowballStemmer stemmer = new englishStemmer();         
        
        
        
        List<String> stemmedList = new ArrayList<>();
        
        
        filtered.stream().forEach(s -> {
            stemmer.setCurrent(s);
            stemmer.stem();
            stemmedList.add(stemmer.getCurrent());
        });
        
            
        
        //String filteredList = Arrays.toString(filtered);    
        String filteredList = String.join(", ", stemmedList);
        //deal with special characters
        
        List<String> result = Arrays.asList(filteredList.toLowerCase().replaceAll("[^a-zA-Z 0-9]", " ").split("\\s+"));
  
        
        
        return result;
        
    }
    
}