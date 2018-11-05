/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrieval;

import components.ImprovedTokenizer;
import components.SimpleTokenizer;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
    private RandomAccessFile raf;
    private String fileName;
    private long inicialBottom;
    private HashMap<Character,CharacterInf> charBotTop;
    
    public RetrievalRanked(String fileName){
        this.fileName=fileName;
        try {
            this.raf = new RandomAccessFile(fileName, "rw");
            try {
                this.inicialBottom = raf.length();
            } catch (IOException ex) {
                Logger.getLogger(RetrievalRanked.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RetrievalRanked.class.getName()).log(Level.SEVERE, null, ex);
        }
        findBottomTop();
    }
    
    private void findBottomTop(){
        charBotTop = new HashMap<>();
        
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;
            int lineNumber=0;
            char currentChar='.';
            CharacterInf infTemp;
            while((line = bufferedReader.readLine()) != null){
                if(lineNumber!=0 && currentChar!=line.charAt(0)){
                    infTemp = charBotTop.get(currentChar);
                    infTemp.setBottom(lineNumber-1);
                    charBotTop.put(currentChar,infTemp);
                }
                if(!charBotTop.containsKey(line.charAt(0))){
                    currentChar = line.charAt(0);
                    charBotTop.put(currentChar,new CharacterInf(lineNumber));
                }
                lineNumber++;
                
            }
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RetrievalRanked.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RetrievalRanked.class.getName()).log(Level.SEVERE, null, ex);
        }
        charBotTop.forEach((k,v)->System.out.println("k: "+k+" v: "+v));
        
        
    }
    
    public Map<Integer,Double> cosineScore(String query,boolean tokenizeSimple){
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
                raf.seek(binarySearch(token));
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
    
    private Long binarySearch(String target) throws IOException{
       long startTime = System.currentTimeMillis();
       
       long top=charBotTop.get(target.charAt(0)).getTop();
       long bottom=charBotTop.get(target.charAt(0)).getBottom();
        raf.seek(bottom);
        String termLine = raf.readLine().split(",")[0];
         //String termLine = raf.readLine();
        //first verification. the first line is skiped, so it is necessary to check before while loop.
        if (termLine == null || termLine.compareTo(target) >= 0) {
  
            return null;
        }

       
         //initial config to binary search
        //long top = 0;
        //long bottom = inicialBottom;
        
        
        while (top <= bottom) {
            //find mid point
            long mid = top + (bottom - top) / 2;
            //go to mid poit
            raf.seek(mid);
            //to advance to the beginning of the line
            raf.readLine();
            //to read the line and save the term
            termLine = raf.readLine().split(",")[0];
            //termLine = raf.readLine();
           
            if (termLine == null || termLine.compareTo(target) >= 0) {
               
                bottom = mid - 1;
            } else {
               
                top = mid + 1;
            }
        }

        raf.seek(top);
        raf.readLine();
        
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("ElapseTime->"+elapsedTime+" target: "+target);
        
        
        return raf.getFilePointer();
    }
    
}