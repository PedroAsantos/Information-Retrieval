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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rute
 */
public class AnaliseTokenize {
 
    public static int getVocabularySize(String fileName){
            int countVocabulary = 0;
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            
            while((line = bufferedReader.readLine()) != null) {
                countVocabulary++;
            }
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AnaliseTokenize.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AnaliseTokenize.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
       return countVocabulary;
           
    }
    
    public static List<String> getTenTermsFreqOne(String fileName){
        
        List<String> result = new ArrayList<>();
        
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            String[] postingLine;
            String[] lineArray;
            while((line = bufferedReader.readLine()) != null) {
                lineArray= line.split(",");
                if(lineArray.length==2){
                    postingLine = lineArray[1].split(":");
                    if(Integer.parseInt(postingLine[1])==1){
                        result.add(lineArray[0].replaceAll(",",""));    
                    }   
                }
                if(result.size()==10){
                    break;
                }
            }
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AnaliseTokenize.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AnaliseTokenize.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
    public static PriorityQueue<TermFreqHigher> getTermsWithHigherFreq(String fileName){
        List <String> result = new ArrayList<>();
        PriorityQueue<TermFreqHigher> pq = new PriorityQueue<TermFreqHigher>();
        
         try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            String[] lineArray;
            int totalFreq;
            while(pq.size()<10 && (line = bufferedReader.readLine()) != null) {
                 lineArray= line.split(",");
                 totalFreq=0;
                 for(int i =1;i<lineArray.length;i++){
                     totalFreq+=Integer.parseInt(lineArray[i].split(":")[1]);
                 }
                 
                  if(pq.size()<10){
                    pq.add(new TermFreqHigher(lineArray[0],totalFreq));
                  }else if(totalFreq>pq.peek().getFreq()){
                    pq.poll();
                    pq.add(new TermFreqHigher(lineArray[0],totalFreq));
                  }
            }
            
            
            while((line = bufferedReader.readLine()) != null) {
                lineArray= line.split(",");
                totalFreq=0;
                for(int i =1;i<lineArray.length;i++){
                   totalFreq+=Integer.parseInt(lineArray[i].split(":")[1]);
                }
                 if(totalFreq>pq.peek().getFreq()){
                        pq.poll();
                        pq.add(new TermFreqHigher(lineArray[0],totalFreq));
                 }
            }
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AnaliseTokenize.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AnaliseTokenize.class.getName()).log(Level.SEVERE, null, ex);
        }   
        return pq;
    }
    public static void testeQueu(){
        
        PriorityQueue<TermFreqHigher> pq = new PriorityQueue<TermFreqHigher>();
        pq.add(new TermFreqHigher("pedro", 2));
        pq.add(new TermFreqHigher("asd", 10));
        pq.add(new TermFreqHigher("pro", 3));
        pq.add(new TermFreqHigher("dro", 1));
        pq.add(new TermFreqHigher("sdasfro", 21));
        
        while(!pq.isEmpty()){
            System.out.println(pq.poll());
        }
        
    }
}
