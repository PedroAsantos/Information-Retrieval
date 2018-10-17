/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assigment_1;

import components.CorpusReader;
import components.ImprovedTokenizer;
import components.Indexer;
import components.SimpleTokenizer;
import java.io.FileNotFoundException;
import java.util.List;


/**
 *
 * @author rute
 */
public class Assigment_1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
       /*
        List<String> corpus = null;
        Indexer invertedIndexer = new Indexer();
        
        String[] fileNames = new String[1];
        int[] columnNumbers = new int[1];
        
        
        
        try {
            //read to memory the files
            //each document in the posisiton of the arraylist;
            corpus = CorpusReader.corpusReader(fileNames,columnNumbers);
        } catch (Exception e) {
            System.out.print("Exception: ");
            System.out.println(e.getMessage());
        }
        
        //for each doc tokenize and index in the index
        List<String> tokens;
        for(int docId=0;docId<corpus.size();docId++){
            tokens = SimpleTokenizer.tokenize(corpus.get(docId));
            invertedIndexer.addToPostingList(tokens,docId);
        }
        */
        
        
        
       // CorpusReader.reader();
       //transform-se em tokens, coloca-se em index loop. 
        long startTime = System.currentTimeMillis();
      // for(int i = 0;i<10000;i++){
        List<String> teste = SimpleTokenizer.tokenize("Pedro $$$$ aàaa à qwes alcatrõo antónio ---- ..... pe-ss 123123 qwe santos    asasd");
      // }
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("ElapseTime->"+elapsedTime);
         
        
     /*startTime = System.currentTimeMillis();
       for(int i = 0;i<10000;i++){
             List<String> teste = SimpleTokenizer.tokenize2("Pedro $$$$ a 123123 qwe santos    asasd");
       }
        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;
        System.out.println("ElapseTime2->"+elapsedTime);
        */
        System.out.println("##");
        teste.forEach(System.out::println);       
       
       
       System.out.println("Hello");
       
       /*
       String testeStemmer = ImprovedTokenizer.stemming("running");
       System.out.println(testeStemmer);
       */
       
       //List<String> testeStop = ImprovedTokenizer.stopwordFilter("this is a list of words");
       //testeStop.forEach(System.out::println);
       
       
      //List<String> testeSpecial = ImprovedTokenizer.specialCharacters("bob's b.e.a.t.r.i.z. black-vultures bia@coronha");
      //testeSpecial.forEach(System.out::println);
      
      List<String> testando = ImprovedTokenizer.personalizedTokenize("this is a list of words bob's b.e.a.t.r.i.z. black-vultures biacoronha@gmail.com");
      testando.forEach(System.out::println);
       
    }
    
}
