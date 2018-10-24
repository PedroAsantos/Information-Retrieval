/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
    private static final int MAXIMUN_DOCS=10000;
    
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
               
         
            corpus.add(new CorpusDocument(lineToSave,docId));
           
            lineToSave="";
               
           

           
            
            docId++;
            countBlockDocs++;
        }      
        if(corpus.isEmpty()){
            bufferedReader.close();
        }
           
        // returns the contents of each document in a collection (corpus)
        return corpus;
    }
       
}
    
    
