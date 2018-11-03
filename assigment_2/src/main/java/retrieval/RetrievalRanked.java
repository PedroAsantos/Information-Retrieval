/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrieval;

import components.ImprovedTokenizer;
import components.SimpleTokenizer;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author rute
 */
public class RetrievalRanked {
    
    
    
    public static Map<Integer,Double> cosineScore(String query,boolean tokenizeSimple,String fileName){
        Map<Integer,Double> score = new HashMap<>();
        
        List<String> queryTokens=null;
        
        if(tokenizeSimple){
            queryTokens = SimpleTokenizer.tokenize(query);
        }else{
            try {
                queryTokens = ImprovedTokenizer.personalizedTokenize(query);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(RetrievalRanked.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
            String[] postingList;
            double weightTD;
            int docId;
            
            //counting the number of each term that happens in the query
            Map<String, Long> counts = queryTokens.stream().collect(Collectors.groupingBy(e -> e,Collectors.counting()));

            //map to save logfreqs of each term in query
            Map<String, Double> logFreqs = new HashMap<>(); 

            //calculate log freq of each term
            counts.forEach((k,v)-> logFreqs.put(k,1+Math.log10(v)));
              

            //calculate normalization
            // double normalization = Math.sqrt(logFreqs.values().stream().map(lf-> lf*lf).collect(Collectors.summingDouble(i->i)));

            double normalization = Math.sqrt(logFreqs.values().stream().mapToDouble(lf-> lf*lf).sum());
            
            for(String token:queryTokens){
                raf.seek(binarySearch(raf, token));
                postingList =  raf.readLine().split(",");
                //TODO: calculate Wt,q
                for(int i = 1;i<postingList.length;i++){
                    weightTD = Double.parseDouble(postingList[i].split(":")[1].replaceAll("\\s+",""));
                    docId = Integer.parseInt(postingList[i].split(":")[0].replaceAll("\\s+",""));
                    if(score.containsKey(docId)){
                        score.put(docId, score.get(docId)+(weightTD*(logFreqs.get(token)/normalization)));
                    }else{
                        score.put(docId,weightTD*(weightTD*(logFreqs.get(token)/normalization)));
                    }
                }
                
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RetrievalRanked.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RetrievalRanked.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return score;
        
        
        
    }
    
    private static Long binarySearch(RandomAccessFile file, String target) throws IOException{
       
      
        file.seek(0);
        String termLine = file.readLine().split(",")[0];
        //first verification. the first line is skiped, so it is necessary to check before while loop.
        if (termLine == null || termLine.compareTo(target) >= 0) {
  
            return null;
        }

       
         //initial config to binary search
        long top = 0;
        long bottom = file.length();
        while (top <= bottom) {
            //find mid point
            long mid = top + (bottom - top) / 2;
            //go to mid poit
            file.seek(mid);
            //to advance to the beginning of the line
            file.readLine();
            //to read the line and save the term
            termLine = file.readLine().split(",")[0];

            if (termLine == null || termLine.compareTo(target) >= 0) {
               
                bottom = mid - 1;
            } else {
               
                top = mid + 1;
            }
        }

        file.seek(top);
        file.readLine();
        return file.getFilePointer();
    }
    
}
