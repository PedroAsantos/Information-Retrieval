/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.NumberFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rute
 */
public class CorpusReader {
    private FileReader fileReader;
    private BufferedReader bufferedReader;
    private int docId=0;
    private static final int MAXIMUN_DOCS=50;
    
    public CorpusReader(String fileName){
        try {
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CorpusReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
    /**
     *
     * @param fileNames
     * @param columnsNumbers
     * @return 
     * @throws java.io.FileNotFoundException 
    */
    public List<CorpusDocument> corpusReader(int[] columnsNumbers) throws FileNotFoundException, IOException{
        
        
        String line = null;
        String[] currentLineTokens;
        //each index of array corpus corresponds to a document 
        List<CorpusDocument> corpus = new ArrayList<>();
        String lineToSave = "";
        int countBlockDocs =0; 
        
           
        //skip first line that has the column names
        if(docId==0){
            bufferedReader.readLine();
        }
        //read each line
        while((line = bufferedReader.readLine()) != null) {
            if(countBlockDocs==MAXIMUN_DOCS){
               return corpus;
            }
            //temp 
            if(docId==100){
               corpus.clear();
               return corpus;
            }
                
            //split line
            currentLineTokens = line.split("\t");
                
            for(int columnNumber=0;columnNumber<columnsNumbers.length;columnNumber++){
              //add column to the corpos array list
              lineToSave+=currentLineTokens[columnsNumbers[columnNumber]];
              //add tab for each column of document
              if(columnNumber!=columnsNumbers.length-1){
                lineToSave+="\t";
              }
            }
                
            // System.out.println("linetosave: "+lineToSave);
            // if(!lineToSave.equals("")){
            corpus.add(new CorpusDocument(lineToSave,docId));
            //corpus.add(lineToSave);
            //  }
            lineToSave="";
               
           

            // System.out.println("Total memory: " +  Runtime.getRuntime().totalMemory());
            // System.out.println("used memory: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
            // System.out.println("% "+((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())*100)/(Runtime.getRuntime().totalMemory()));
               
            
            docId++;
            countBlockDocs++;
        }
        
        
         
            /*
                 The document should be processed to ignore any irrelevant sections and clean any existing tags
            */

            // returns the contents of each document in a collection (corpus)
        return corpus;
    }
       
}
    
    
