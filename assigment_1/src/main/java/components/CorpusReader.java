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

/**
 *
 * @author rute
 */
public class CorpusReader {
     
    public CorpusReader(){
    
    
    }
       
    /**
     *
     * @param fileNames
     * @param columnsNumbers
     * @return 
     * @throws java.io.FileNotFoundException 
    */
    public static List<CorpusDocument> corpusReader(String[] fileNames, int[] columnsNumbers) throws FileNotFoundException, IOException{
        
        FileReader fileReader;
        BufferedReader bufferedReader;
        String line = null;
        String[] currentLineTokens;
        //each index of array corpus corresponds to a document 
        List<CorpusDocument> corpus = new ArrayList<>();
        String lineToSave = "";
        
        Runtime runtime = Runtime.getRuntime();
        NumberFormat format = NumberFormat.getInstance();
        StringBuilder sb = new StringBuilder();
        double percentageMemoryUsed = 0;
        double MAXIMUN_MEMORY_PERCENTAGE=80;
        int docId=0;
        int numberOfListSerialized=0;
        for (String fileName : fileNames) {
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);
            //skip first line that has the column names
            bufferedReader.readLine();
            //read each line
            while((line = bufferedReader.readLine()) != null) {
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
               
                //calculate the percentage of memory used - if memory used is bigger than the threhold, serialize object
                percentageMemoryUsed = ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())*100.0)/(Runtime.getRuntime().totalMemory());
                System.out.println("% = "+percentageMemoryUsed);
                if(percentageMemoryUsed>=MAXIMUN_MEMORY_PERCENTAGE){
                    try {
                        FileOutputStream fileOut = new FileOutputStream("/tmp/corpus_"+numberOfListSerialized+".ser");
                        ObjectOutputStream out = new ObjectOutputStream(fileOut);
                        out.writeObject(corpus);
                        out.close();
                        fileOut.close();
                        System.out.println("/tmp/corpus_"+numberOfListSerialized+".ser");
                    } catch (IOException i) {
                       i.printStackTrace();
                    }
                    corpus.clear();
                    numberOfListSerialized++;
                }
                

               // System.out.println("Total memory: " +  Runtime.getRuntime().totalMemory());
               // System.out.println("used memory: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
               // System.out.println("% "+((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())*100)/(Runtime.getRuntime().totalMemory()));
                System.out.println("corpus size: "+corpus.size());
                
                if(docId==20){
                    break;
                }
                docId++;
            }
        }
        
        /*
             The document should be processed to ignore any irrelevant sections and clean any existing tags
        */
        
        // returns the contents of each document in a collection (corpus)
        return corpus;
    }
    
    
}
