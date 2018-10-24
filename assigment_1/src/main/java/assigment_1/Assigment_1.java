/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assigment_1;

import components.AnaliseTokenize;
import components.CorpusDocument;
import components.CorpusReader;
import components.ImprovedTokenizer;
import components.Indexer;
import components.SimpleTokenizer;
import java.io.FileNotFoundException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Pedro Santos, 76532 /  Beatriz Coronha 92210     
 */
public class Assigment_1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
         
        String filename;
        filename = "/home/rute/Documents/cadeiras/5ano/ri/amazon_reviews_us_Watches_v1_00.tsv";
        //filename = "/home/rute/Documents/cadeiras/5ano/ri/amazon_reviews_us_Wireless_v1_00.tsv";
        CorpusReader corpusReader = new CorpusReader(filename);
        
        boolean SimpleTokenize=true;
        
        
        List<CorpusDocument> corpus = null;
        Indexer invertedIndexer = new Indexer();
       
        String[] fileNames = new String[1];
        int[] columnNumbers = new int[3];
        
        columnNumbers[0]=5;
        columnNumbers[1]=12;
        columnNumbers[2]=13;
        
        /*try {
            System.out.println("impro: "+ ImprovedTokenizer.personalizedTokenize("@This is a improving very complex tokleniser for myself, right? car's pedro will be very good pedro@gmail.com(my mail [familiar])"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Assigment_1.class.getName()).log(Level.SEVERE, null, ex);
        }*/
       
        do{
            try {
            //read to memory the files
            //each document in the posisiton of the arraylist;
                corpus = corpusReader.corpusReader(columnNumbers);
            } catch (Exception e) {
                 System.out.print("Exception: ");
                 System.out.println(e.getMessage());
            }
            if(!corpus.isEmpty()){
                if(SimpleTokenize){
                 corpus.stream().forEach(x->{invertedIndexer.addToPostingList(SimpleTokenizer.tokenize(x.getCorpus()),x.getDocId());});       
                }else{
                 corpus.stream().forEach(x->{try {
                     invertedIndexer.addToPostingList(ImprovedTokenizer.personalizedTokenize(x.getCorpus()),x.getDocId());
                     } catch (FileNotFoundException ex) {
                         Logger.getLogger(Assigment_1.class.getName()).log(Level.SEVERE, null, ex);
                     }
});       
                }
            }
        }while(!corpus.isEmpty());
        //write on disk the rest of the index that it is in memory
        invertedIndexer.writeLastBlock();
       
        
        //Put each token's document in the index.
         
     /*   List<String> tokens;
        for(int docId=0;docId<corpus.size();docId++){
            tokens = SimpleTokenizer.tokenize(corpus.get(docId));
            invertedIndexer.addToPostingList(tokens,docId);
        }*/
        System.out.println("Merge Blocks");
        invertedIndexer.mergeBlocks();
        
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("ElapseTime->"+elapsedTime);
        
      
        MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();

        //System.out.println("Max_heap: "+memBean.getNonHeapMemoryUsage(). / (1024 * 1024) + "mb");
        System.out.println("Vocabulary Size: "+AnaliseTokenize.getVocabularySize("indexer.txt"));
        System.out.println("Ten Terms Freq One:"+ AnaliseTokenize.getTenTermsFreqOne("indexer.txt"));
        System.out.println("Most freq terms: "+ AnaliseTokenize.getTermsWithHigherFreq("indexer.txt"));
       
    }
    
}
