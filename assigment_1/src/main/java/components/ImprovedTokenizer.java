/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

/**
 *
 * @author beatriz
 */

public class ImprovedTokenizer {
    private static final String[] arrayStopWords = {"i","me","my","myself","we","us","our","ours","ourselves","you","your","yours","yourself","yourselves","he","him","his","himself","she","her","hers","herself","it","its","itself","they","them","their","theirs","themselves","what","which","who","whom","this","that","these","those","am","is","are","was","were","be","been","being","have","has","had","having","do","does","did","doing","would","could","should","ought","might","however","be","included","will","would","shall","should","can","could","may","might","must","ought","i'm","you're","he's","she's","it's","we're","they're","i've","you've","we've","they've","i'd","you'd","he'd","she'd","we'd","they'd","i'll","you'll","he'll","she'll","we'll","they'll","isn't","aren't","wasn't","weren't","hasn't","haven't","hadn't","doesn't","don't","didn't","won't","wouldn't","shan't","shouldn't","can't","cannot","couldn't","mustn't","let's","that's","who's","what's","here's","there's","when's","where's","why's","how's","daren't","needn't","oughtn't","mightn't","a","an","the","and","but","if","or","because","as","until","while","of","at","by","for","with","about","against","between","into","through","during","before","after","above","below","to","from","up","down","in","out","on","off","over","under","again","further","then","once","here","there","when","where","why","how","all","any","both","each","few","more","most","other","some","such","no","nor","not","only","own","same","so","than","too","very","one","every","least","less","many","now","ever","never","say","says","said","also","get","go","goes","just","made","make","put","see","seen","whether","like","well","back","even","still","way","take","since","another","however","two","three","four","five","first","second","new","old","high","long"};
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
       
        //deal with the text with special characters, some simple mistakes like "five,stars" -> missing space ater comma, etc...
        String[] temp = documentLine.toLowerCase().replaceAll("[-+.:,_<>*=&%#;@?!\\p{Ps}\\p{Pe}]"," ").replaceAll("\\s+"," ").split(" ");
        List<String> text = Arrays.asList(temp);
        //removing stop words
        List<String> filtered = text.stream()
                .filter(word -> !stopwords.contains(word)).collect(Collectors.toList());
        //stemming
        SnowballStemmer stemmer = new englishStemmer();         
      
        
        List<String> stemmedList = new ArrayList<>();
        
        //stemming
        filtered.stream().forEach(s -> {
            stemmer.setCurrent(s);
            stemmer.stem();
            stemmedList.add(stemmer.getCurrent());
        });
                     
        //removing all special characters.
        return stemmedList.stream().map(x -> x.replaceAll("[^a-zA-Z 0-9]", "")).filter(x-> x.length()>0).collect(Collectors.toList());
        
    }
    
}