/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assigment_2;

import components.AnaliseTokenize;
import obj_indexer.CorpusDocument;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        long startTimeIndexing = System.currentTimeMillis();
        String filename;
        filename = "/home/rute/Documents/cadeiras/5ano/ri/amazon_reviews_us_Wireless_v1_00.tsv";  
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
        
        long stopTimeIndexing = System.currentTimeMillis();
        long elapsedTimeIndexing = stopTimeIndexing - startTimeIndexing;
        System.out.println("ElapseTime Indexing->"+elapsedTimeIndexing);
        
        int cacheSize = 30;
        long timeToLive = 200;
        long timerInterval = 500;
        
        
        
        
        
        
        RetrievalRanked rr = new RetrievalRanked("indexer_"+indexName[indexName.length-1]+".txt",readFromFileTotalNumberOfDocuments(),cacheSize,timeToLive,timerInterval);
        
        //generate block index
        rr.generateBlocks();
        
        //testeRetrieval(indexName,cacheSize,timeToLive,timerInterval);
        
        long startTimeSearch = System.currentTimeMillis();
        //search to receive in a hashmap the results   
        rr.cosineScore("Wirelle", simpleTokenize).forEach((k,v)->System.out.println("k: "+ k + "v: "+ v));
        
        long stopTimeSearch = System.currentTimeMillis();
        long elapsedTimeSearch = stopTimeSearch - startTimeSearch;
        System.out.println("ElapseTime Search->"+elapsedTimeSearch);
       
       //search to receive the 10 elements with a higher score in a list 
        System.out.println(rr.retrievalTop("offo", simpleTokenize,10));

   
       
    }
    /*
    *
    * Function to read from file the total number of documents to be used in the instanciation of Retrieval Ranked Object.
    *
    *
    */
    
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
    //function to test retrival with cache empty and then the second time with all the terms in cache
    public static void testeRetrieval(String[] indexName,int cacheSize,long timeToLive,long timerInterval){
         RetrievalRanked rr = new RetrievalRanked("indexer_"+indexName[indexName.length-1]+".txt",readFromFileTotalNumberOfDocuments(),cacheSize,timeToLive,timerInterval);
        rr.generateBlocks();
        
        String[] arrayToTest= new String[] {"offmedhigh","offmusicpow","offnow","optimize","oterhwis","othercool","otherc","otheral","otte","ottercaa","es266a2c1907935","butera","butelsoftwar","comgpproductb00nfyjc7kref","zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz","zz1","x693","oramazon","x6xxx","x710s","xm84hc"};
        List<Long> results = new ArrayList<>();
       
        for (int i = 0; i < arrayToTest.length; i++) {
            long startTimeSearch = System.currentTimeMillis();
            
            rr.cosineScore(arrayToTest[i], false).forEach((k,v)->System.out.println("k: "+ k + "v: "+ v));
            
            long stopTimeSearch = System.currentTimeMillis();
            long elapsedTimeSearch = stopTimeSearch - startTimeSearch;
            results.add(elapsedTimeSearch);
            System.out.println("ElapseTime Search->"+elapsedTimeSearch);
        }
        
        System.out.println("media:"+results.stream().mapToLong(Long::longValue).sum()/results.size());
        results.clear();
        
          for (int i = 0; i < arrayToTest.length; i++) {
            long startTimeSearch = System.currentTimeMillis();
            
            rr.cosineScore(arrayToTest[i], false).forEach((k,v)->System.out.println("k: "+ k + "v: "+ v));
            
            long stopTimeSearch = System.currentTimeMillis();
            long elapsedTimeSearch = stopTimeSearch - startTimeSearch;
            results.add(elapsedTimeSearch);
            System.out.println("ElapseTime Search->"+elapsedTimeSearch);
        }
        
        System.out.println("AVARAGE TIME:"+results.stream().mapToLong(Long::longValue).sum()/results.size());
        results.clear();
    }
    
}