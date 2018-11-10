/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assigment_2;

import components.AnaliseTokenize;
import obj.CorpusDocument;
import components.CorpusReader;
import components.ImprovedTokenizer;
import components.Indexer;
import components.SimpleTokenizer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import retrieval.RetrievalRanked;


/**
 *
 * @author Pedro Santos, 76532 /  Beatriz Coronha 92210     
 */
public class Assigment_2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String filename;
        filename = "/home/rute/Documents/cadeiras/5ano/ri/amazon_reviews_us_Watches_v1_00.tsv";  
        String[] indexName = filename.split("/");
        boolean simpleTokenize = false;
        File f = new File("indexer_"+indexName[indexName.length-1]+".txt");

        if(!(f.exists() && !f.isDirectory())){
    
          
            //filename = "/home/rute/Documents/cadeiras/5ano/ri/amazon_reviews_us_Wireless_v1_00.tsv";
            CorpusReader corpusReader = new CorpusReader(filename);

           
            
            List<CorpusDocument> corpus = null;
            
            Indexer invertedIndexer = new Indexer(indexName[indexName.length-1]);

            String[] fileNames = new String[1];
            int[] columnNumbers = new int[3];

            columnNumbers[0] = 5;
            columnNumbers[1] = 12;
            columnNumbers[2] = 13;

            do {
                try {
                    //read to memory the files
                    //each document in the posisiton of the arraylist;
                    corpus = corpusReader.corpusReader(columnNumbers);
                } catch (IOException e) {
                    System.out.print("Exception: ");
                    System.out.println(e.getMessage());
                }
                if (!corpus.isEmpty()) {
                    if (simpleTokenize) {
                        corpus.stream().forEach(x -> {
                            invertedIndexer.addToPostingList(SimpleTokenizer.tokenize(x.getCorpus()), x.getDocId());
                        });
                    } else {
                        corpus.stream().forEach(x -> {
                            try {
                                invertedIndexer.addToPostingList(ImprovedTokenizer.personalizedTokenize(x.getCorpus()), x.getDocId());
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(Assigment_2.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    }
                }
            } while (!corpus.isEmpty());
            //write on disk the rest of the index that it is in memory
            invertedIndexer.writeLastBlock();

            System.out.println("Merge Blocks");
            invertedIndexer.mergeBlocks();
        }   
        int cacheSize = 10;
        long timeToLive = 200;
        long timerInterval = 500;
        
        
        
        
        
        
        RetrievalRanked rr = new RetrievalRanked("indexer_"+indexName[indexName.length-1]+".txt",readFromFileTotalNumberOfDocuments(),cacheSize,timeToLive,timerInterval);
        //retrival information -- search
        long startTime = System.currentTimeMillis();
        rr.cosineScore("es266a2c1907935", simpleTokenize).forEach((k,v)->System.out.println("k: "+ k + "v: "+ v));
                    
        System.out.println("end");
        
        
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("ElapseTime->"+elapsedTime);
        
        
        
      //assigment_1 exercise
      //  System.out.println("Vocabulary Size: "+AnaliseTokenize.getVocabularySize("indexer.txt"));
      //  System.out.println("Ten Terms Freq One:"+ AnaliseTokenize.getTenTermsFreqOne("indexer.txt"));
      //  System.out.println("Most freq terms: "+ AnaliseTokenize.getTermsWithHigherFreq("indexer.txt"));
       
    }
    
    
    public static int readFromFileTotalNumberOfDocuments(){
         BufferedReader bufferedReader = null;
         String line=null;
        try {
            bufferedReader = new BufferedReader(new FileReader("indexer_number_of_docs.txt"));
            
            if((line = bufferedReader.readLine()) != null){
                line = line.split(":")[1];
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Assigment_2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Assigment_2.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
                Logger.getLogger(Assigment_2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
         return Integer.parseInt(line);
    }
}