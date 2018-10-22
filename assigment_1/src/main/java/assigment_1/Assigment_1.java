/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assigment_1;

import components.CorpusDocument;
import components.CorpusReader;
import components.Indexer;
import components.SimpleTokenizer;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author rute
 */
public class Assigment_1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
               
        
        String filename;
        filename = "/home/rute/Documents/cadeiras/5ano/ri/amazon_reviews_us_Watches_v1_00.tsv";
        CorpusReader corpusReader = new CorpusReader(filename);
        
        List<CorpusDocument> corpus = null;
        Indexer invertedIndexer = new Indexer();
        
        String[] fileNames = new String[1];
        int[] columnNumbers = new int[3];
        
        columnNumbers[0]=5;
        columnNumbers[1]=12;
        columnNumbers[2]=13;
                
 
        
        do{
            try {
            //read to memory the files
            //each document in the posisiton of the arraylist;
            corpus = corpusReader.corpusReader(columnNumbers);
        } catch (Exception e) {
            System.out.print("Exception: ");
            System.out.println(e.getMessage());
        }
            if(corpus.size()!=0){
               corpus.stream().forEach(x->{invertedIndexer.addToPostingList(SimpleTokenizer.tokenize(x.getCorpus()),x.getDocId());});       
            }
            System.out.println("Corpus size: "+ corpus.size());
        }while(corpus.size()!=0);
        
        
        //Put each token's document in the index.
         
     /*   List<String> tokens;
        for(int docId=0;docId<corpus.size();docId++){
            tokens = SimpleTokenizer.tokenize(corpus.get(docId));
            invertedIndexer.addToPostingList(tokens,docId);
        }*/
        
     
        //it is necessary to save the last block that it is onlyin 
        System.out.println("Merge Blocks");
        invertedIndexer.mergeBlocks();
      //  invertedIndexer.printIndex();

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("ElapseTime->"+elapsedTime);
       
       /* 
        
        Indexer invertedIndex1 = new Indexer();
       // CorpusReader.reader();
       //transform-se em tokens, coloca-se em index loop. 
        long startTime = System.currentTimeMillis();
      // for(int i = 0;i<10000;i++){
        List<String> teste = SimpleTokenizer.tokenize("O pedro e a mariana pedro melhores manos");
      // }
        List<String> teste1 = SimpleTokenizer.tokenize("pedro tenho de ser forte e vontade de aprender");

        invertedIndex1.addToPostingList(teste,0);
        invertedIndex1.addToPostingList(teste1,1);

        invertedIndex1.printIndex();
        
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
      //  System.out.println("##");
      //  teste.forEach(System.out::println);       
       
       
       System.out.println("Hello");
    }
    
}
