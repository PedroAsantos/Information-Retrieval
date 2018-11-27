/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assigment_3;

import indexer.Indexer;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import reader.CorpusDocument;
import reader.CorpusReader;
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
       

        
        
    } 
}
