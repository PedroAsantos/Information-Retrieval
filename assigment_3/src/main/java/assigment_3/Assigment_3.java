/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assigment_3;

import indexer.Indexer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import reader.CorpusDocument;
import reader.CorpusReader;
import retrieval.RetrievalRanked;
import tokenizers.ImprovedTokenizer;
import tokenizers.SimpleTokenizer;

/**
 *
 * @author rute
 */
public class Assigment_3 {
    public static void main(String[] args) {
        CorpusReader corpusReader = new CorpusReader("/home/rute/Documents/cadeiras/5ano/ri/cranfield/");
        List<CorpusDocument> corpus = null;
        boolean simpleTokenize = true;
        String indexerName = "indexer.txt";
        
        File f = new File(indexerName);
        
        
        
       
        
        if(!(f.exists() && !f.isDirectory())){
            Indexer invertedIndexer = new Indexer(indexerName);
            
            do {
                corpus = corpusReader.corpusReader();
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
                                Logger.getLogger(Assigment_3.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    }
                }
               // System.out.println(corpus);
            } while (!corpus.isEmpty());
            invertedIndexer.writeLastBlock();

            System.out.println("Merge Blocks");
            invertedIndexer.mergeBlocks();
            
        }
        
        
                  
        int cacheSize = 30;
        long timeToLive = 200;
        long timerInterval = 500;
        String nameFileQueries = "cranfield.queries.txt";
        RetrievalRanked rr = new RetrievalRanked(indexerName,readFromFileTotalNumberOfDocuments(),cacheSize,timeToLive,timerInterval);
        //generate block index
        rr.generateBlocks();
        
        
        runQueriesFile(rr, nameFileQueries,simpleTokenize);
        
        
        
        
    } 
    
    public static void runQueriesFile(RetrievalRanked rr, String nameFile,boolean simpleTokenize){
        
        BufferedReader bufferedReader = null;
        String line=null;
        try {
            bufferedReader = new BufferedReader(new FileReader(nameFile));
            
            while((line = bufferedReader.readLine()) != null){
                System.out.println(rr.retrievalTop(line, simpleTokenize,10));                
                
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Assigment_3.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Assigment_3.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
                Logger.getLogger(Assigment_3.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
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
            Logger.getLogger(Assigment_3.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Assigment_3.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
                Logger.getLogger(Assigment_3.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
         return Integer.parseInt(line);
    }
}
