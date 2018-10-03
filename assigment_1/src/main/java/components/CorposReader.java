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

/**
 *
 * @author rute
 */
public class CorposReader {
     
    public CorposReader(){
    
    
    }
       
    /**
     *
     * @param fileNames
     * @param columnsNumbers
     * @return 
     * @throws java.io.FileNotFoundException 
    */
    public static List<String> corpusReader(String[] fileNames, int[] columnsNumbers) throws FileNotFoundException, IOException{
        
        FileReader fileReader;
        BufferedReader bufferedReader;
        String line = null;
        String[] currentLineTokens;
        //each index of array corpus corresponds to a document 
        List<String> corpus = new ArrayList<>();
        String lineToSave = "";
        
        for (String fileName : fileNames) {
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);
            //read line
            while((line = bufferedReader.readLine()) != null) {
                //split line
                currentLineTokens = line.split("\t");
                
                for(int columnNumber=0;columnNumber<columnsNumbers.length;columnNumber++){
                    //add column to the corpos array list
                    lineToSave+=currentLineTokens[columnNumber];
                    //add tab for each column of document
                    if(columnNumber!=columnsNumbers.length-1){
                        lineToSave+="\t";
                    }
                }
                // if(!lineToSave.equals("")){
                corpus.add(lineToSave);
                //  }
                lineToSave="";
            }
        }
        
        /*
             The document should be processed to ignore any irrelevant sections and clean any existing tags
        */
        
        System.out.println("Reader!!");
        // returns the contents of each document in a collection (corpus)
        return corpus;
    }
    
    
}
